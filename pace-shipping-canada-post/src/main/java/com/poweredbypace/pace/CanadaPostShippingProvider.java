package com.poweredbypace.pace;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poweredbypace.pace.domain.Money;
import com.poweredbypace.pace.domain.shipping.Shipment;
import com.poweredbypace.pace.domain.shipping.ShippingOption;
import com.poweredbypace.pace.domain.shipping.ShippingPackage;
import com.poweredbypace.pace.shipping.RateShippingResponse;
import com.poweredbypace.pace.shipping.RateShippingResponseEntry;
import com.poweredbypace.pace.shipping.ShippingProvider;
import com.poweredbypace.pace.shipping.ShippingResponse;
import com.poweredbypace.pace.shipping.ShippingResponse.ResponseEnum;
import com.poweredbypace.pace.shipping.TrackingResponse;
import com.poweredbypace.pace.shipping.TrackingResponse.DeliveryStatus;
import com.poweredbypace.pace.util.UnitConverterUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import ca.canadapost.cpcdp.rating.generated.messages.Messages;
import ca.canadapost.cpcdp.rating.generated.rating.MailingScenario;
import ca.canadapost.cpcdp.rating.generated.rating.MailingScenario.Destination;
import ca.canadapost.cpcdp.rating.generated.rating.MailingScenario.Destination.Domestic;
import ca.canadapost.cpcdp.rating.generated.rating.MailingScenario.Destination.International;
import ca.canadapost.cpcdp.rating.generated.rating.MailingScenario.Destination.UnitedStates;
import ca.canadapost.cpcdp.rating.generated.rating.MailingScenario.ParcelCharacteristics.Dimensions;
import ca.canadapost.cpcdp.rating.generated.rating.PriceQuotes;
import ca.canadapost.cpcdp.tracking.generated.track.PinSummary;
import ca.canadapost.cpcdp.tracking.generated.track.TrackingSummary;

public class CanadaPostShippingProvider extends ShippingProvider {

	private final Log log = LogFactory.getLog(CanadaPostShippingProvider.class);
	
	private String username;
	private String password;
	private String mailedBy;
	private String contract;
	private String labelFormat;
	private String methodOfPayment;
	
	public CanadaPostShippingProvider(String providerId) {
		super(providerId);
	}

	private String getRatingLink() {
		return "https://ct.soa-gw.canadapost.ca/rs/ship/price";
	}
	
	private String getShippingLink() {
		return "https://ct.soa-gw.canadapost.ca/rs/" + getMailedBy() + "/" + getMailedBy() + "/shipment";
	}
	
	private boolean isDomestic(Shipment shipment) {
		return "CA".equals(shipment.getToAddress().getCountry());
	}
	
	private boolean isUnitedStates(Shipment shipment) {
		return "US".equals(shipment.getToAddress().getCountry());
	}
	
	@Override
	public RateShippingResponse rate(Shipment shipment) {

		if (isUnitedStates(shipment) || isDomestic(shipment))
			throw new IllegalStateException("Not supported");
		
		
		MailingScenario mailingScenario = new MailingScenario();
		
		mailingScenario.setCustomerNumber(mailedBy);

		MailingScenario.ParcelCharacteristics parcelCharacteristics = new MailingScenario.ParcelCharacteristics();

		parcelCharacteristics.setWeight(new BigDecimal(UnitConverterUtil.convertInchToCm(shipment.getTotalWeight())).setScale(2, BigDecimal.ROUND_DOWN));
		
		Dimensions dimensions = new Dimensions();
		ShippingPackage shippingPackage = shipment.getPackages().get(0);
		if(shippingPackage.getWidth() != null
			&& shippingPackage.getHeight() != null
			&& shippingPackage.getLength() != null) {
			dimensions.setWidth(BigDecimal.valueOf(UnitConverterUtil.convertInchToCm(shippingPackage.getWidth())).setScale(0, BigDecimal.ROUND_DOWN));
			dimensions.setHeight(BigDecimal.valueOf(UnitConverterUtil.convertInchToCm(shippingPackage.getHeight())).setScale(0, BigDecimal.ROUND_DOWN));
			dimensions.setLength(BigDecimal.valueOf(UnitConverterUtil.convertInchToCm(shippingPackage.getLength())).setScale(0, BigDecimal.ROUND_DOWN));
			parcelCharacteristics.setDimensions(dimensions);
		}
		
		mailingScenario.setParcelCharacteristics(parcelCharacteristics);
		
		mailingScenario.setOriginPostalCode(shipment.getFromAddress().getZipCode().replaceAll(" ", ""));

		Destination destination = new Destination();

		if(isDomestic(shipment)) {
			Domestic domestic = new Domestic();
			domestic.setPostalCode(shipment.getToAddress().getZipCode().replaceAll(" ", ""));		
			destination.setDomestic(domestic);
		} else if(isUnitedStates(shipment)) {
			UnitedStates unitedStates = new UnitedStates();
			unitedStates.setZipCode(shipment.getToAddress().getZipCode());
			destination.setUnitedStates(unitedStates);
		} else {
			International international = new International();
			international.setCountryCode(shipment.getToAddress().getCountry());
			destination.setInternational(international);
		}
		
		mailingScenario.setDestination(destination);
		
        Client client = getJerseyClient();
		ClientResponse resp = createRateMailingScenario(client, mailingScenario);
        InputStream respIS = resp.getEntityInputStream();
        
        log.debug("HTTP Response Status: " + resp.getStatus() + " " + resp.getClientResponseStatus());
        
        // Example of using JAXB to parse xml response
        JAXBContext jc;
        RateShippingResponse response = new RateShippingResponse();
        response.setShipperName("CANADA POST");
        response.setShipment(shipment);
        try {
        	jc = JAXBContext.newInstance(PriceQuotes.class, Messages.class);
            Object entity = jc.createUnmarshaller().unmarshal(respIS);
            // Determine whether response data matches GetRatesInfo schema.
            if (entity instanceof PriceQuotes) {
            	PriceQuotes priceQuotes = (PriceQuotes) entity;
                for (Iterator<PriceQuotes.PriceQuote> iter = priceQuotes.getPriceQuotes().iterator(); iter.hasNext();) { 
                	PriceQuotes.PriceQuote aPriceQuote = iter.next();                	
	                log.debug("Service Name: " + aPriceQuote.getServiceName());
	                log.debug("Price: $" + aPriceQuote.getPriceDetails().getDue() + "\n");
	                RateShippingResponseEntry entry = new RateShippingResponseEntry();
	                Money money = new Money(aPriceQuote.getPriceDetails().getDue(), "CAD");
	                entry.setMoney(money);
	                ShippingOption shippingOption = new ShippingOption(getProviderId(), aPriceQuote.getServiceName(), aPriceQuote.getServiceCode());
	                shippingOption.setName("Canada Post "+aPriceQuote.getServiceName());
	                entry.setShippingOption(shippingOption);
	                response.getEntries().add(entry);
                }
                response.setResponseEnum(ResponseEnum.OK);
            } else {
                // Assume Error Schema
                Messages messageData = (Messages) entity;
                for (Iterator<Messages.Message> iter = messageData.getMessage().iterator(); iter.hasNext();) {
                    Messages.Message aMessage = iter.next();
                    log.debug("Error Code: " + aMessage.getCode());
                    log.debug("Error Msg: " + aMessage.getDescription());
                    response.setResponseEnum(ResponseEnum.UNCATEGORIZED);
                    response.setMessage("Error Msg: " + aMessage.getDescription() + " (code: " + aMessage.getCode());
                }
            }
        } catch (Exception e) {
        	log.error(e);
        }

        closeJerseyClient(client);
		return response;
	}

	@Override
	public ShippingResponse ship(Shipment shipment, ShippingOption shippingOption) {
		return null;
	}

	private Client getJerseyClient() {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        client.addFilter(new com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(username, password));
        return client;
	}
	
	private void closeJerseyClient(Client client) {
		client.destroy();
	}

	public ClientResponse createRateMailingScenario(Client client, Object xml) throws UniformInterfaceException {
        WebResource aWebResource = client.resource(getRatingLink());
        return aWebResource.accept("application/vnd.cpc.ship.rate-v2+xml").header("Content-Type", "application/vnd.cpc.ship.rate-v2+xml").acceptLanguage("en-CA").post(ClientResponse.class, xml);
    }

    public ClientResponse createShipment(Client client, Object xml) throws UniformInterfaceException {
        WebResource aWebResource = client.resource(getShippingLink());
        return aWebResource.accept("application/vnd.cpc.shipment-v4+xml").header("Content-Type", "application/vnd.cpc.shipment-v4+xml").acceptLanguage("en-CA").post(ClientResponse.class, xml);
    }
	
    public ClientResponse getTrackingSummary(Client client, String pin) throws UniformInterfaceException {   
    	String url = "https://soa-gw.canadapost.ca/vis/track/pin/" + pin + "/summary";
    	WebResource aWebResource = client.resource(url);
        return aWebResource.accept("application/vnd.cpc.track+xml").acceptLanguage("en-CA").get(ClientResponse.class);
    }
    
	@Override
	protected boolean checkAvailability(Shipment shipment) {
		if(shipment.isDropShipment()) {
			return false;
		}
		return true;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMailedBy() {
		return mailedBy;
	}

	public void setMailedBy(String mailedBy) {
		this.mailedBy = mailedBy;
	}

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

	public String getLabelFormat() {
		return labelFormat;
	}

	public void setLabelFormat(String labelFormat) {
		this.labelFormat = labelFormat;
	}

	public String getMethodOfPayment() {
		return methodOfPayment;
	}

	public void setMethodOfPayment(String methodOfPayment) {
		this.methodOfPayment = methodOfPayment;
	}

	@Override
	public TrackingResponse track(String trackingId) {
		Client myClient = getJerseyClient();
        ClientResponse resp = getTrackingSummary(myClient, trackingId);
        InputStream respIS = resp.getEntityInputStream();
  
//      try {
//			String result = IOUtils.toString(respIS, StandardCharsets.UTF_8);
//			log.debug(result);
//		} catch (IOException e1) {
//			
//		}
        
        JAXBContext jc;
        try {
        	jc = JAXBContext.newInstance(TrackingSummary.class, ca.canadapost.cpcdp.tracking.generated.messages.Messages.class);
            Object entity = jc.createUnmarshaller().unmarshal(respIS);
            // Determine whether response data matches TrackingSummary schema.
            if (entity instanceof TrackingSummary) {            	
            	TrackingSummary trackingSummary = (TrackingSummary) entity;           	
                for (Iterator<PinSummary> iter = trackingSummary.getPinSummaries().iterator(); iter.hasNext();) {
                	PinSummary pinSummary = (PinSummary) iter.next();

                	log.info("PIN Number: " + pinSummary.getPin());
                	log.info("Mailed On Date: " + pinSummary.getMailedOnDate());
                	log.info("Event Description: " + pinSummary.getEventDescription());
	                if (StringUtils.isNotEmpty(pinSummary.getActualDeliveryDate()) ) {
	                	TrackingResponse res = new TrackingResponse();
	                	res.setDeliveryStatus(DeliveryStatus.Delivered);
	                	//2010-01-05
	                	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						Date result = df.parse(pinSummary.getActualDeliveryDate());
						res.setDeliveryDate(result);
						result = df.parse(pinSummary.getMailedOnDate());
						res.setShippingDate(result);
						log.info("Delivery date: "+result);
						return res;
	                }
                }
                
            } else {
                // Assume Error Schema
                ca.canadapost.cpcdp.tracking.generated.messages.Messages messageData = (ca.canadapost.cpcdp.tracking.generated.messages.Messages) entity;
                for (Iterator<ca.canadapost.cpcdp.tracking.generated.messages.Messages.Message> iter = messageData.getMessages().iterator(); iter.hasNext();) {
                	ca.canadapost.cpcdp.tracking.generated.messages.Messages.Message aMessage = (ca.canadapost.cpcdp.tracking.generated.messages.Messages.Message) iter.next();
                    log.error("Error Code: " + aMessage.getCode() + ", Error Msg: " + aMessage.getDescription());
                }
            }
        } catch (Exception e) {
        	log.error("Error while attempting to track with ID="+trackingId + "." + e.getMessage());
        } finally {
			myClient.destroy();
		}
        
        return null;
	}

}
