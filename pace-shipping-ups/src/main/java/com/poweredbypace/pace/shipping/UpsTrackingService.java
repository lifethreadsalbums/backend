package com.poweredbypace.pace.shipping;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poweredbypace.pace.shipping.TrackingResponse.DeliveryStatus;
import com.ups.www.wsdl.xoltws.track.v2_0.TrackServiceStub;
import com.ups.www.wsdl.xoltws.track.v2_0.TrackServiceStub.ActivityType;
import com.ups.www.wsdl.xoltws.track.v2_0.TrackServiceStub.PackageType;
import com.ups.www.wsdl.xoltws.track.v2_0.TrackServiceStub.RequestType;
import com.ups.www.wsdl.xoltws.track.v2_0.TrackServiceStub.ServiceAccessToken_type0;
import com.ups.www.wsdl.xoltws.track.v2_0.TrackServiceStub.ShipmentType;
import com.ups.www.wsdl.xoltws.track.v2_0.TrackServiceStub.TrackRequest;
import com.ups.www.wsdl.xoltws.track.v2_0.TrackServiceStub.TrackResponse;
import com.ups.www.wsdl.xoltws.track.v2_0.TrackServiceStub.UPSSecurity;
import com.ups.www.wsdl.xoltws.track.v2_0.TrackServiceStub.UsernameToken_type0;

public class UpsTrackingService {
	
	private final Log log = LogFactory.getLog(getClass());
	private UpsShippingProvider provider;
	
	private static ConfigurationContext configContext;

	static {
	     try {
			configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
		} catch (AxisFault e) { }
	}
	
	public UpsTrackingService(UpsShippingProvider provider) {
		super();
		this.provider = provider;
	}

	public TrackingResponse track(String trackingId) {
		try {
			TrackServiceStub trackServiceStub = new TrackServiceStub(configContext, provider.getTrackEndpoint());
			TrackResponse tr = trackServiceStub.processTrack(populateTrackRequest(trackingId), populateUPSSecurity());			
			
			return populateTrackingResponse(tr);
		} catch (Exception e) {
			log.error("Error calling UPS tracking service. " + e.getMessage());
			return null;
		}
		
	}
	
	private TrackingResponse populateTrackingResponse(TrackResponse res) {
		TrackingResponse r = null;
		ObjectMapper om = new ObjectMapper();
		
		if (res.getShipment()!=null && res.getShipment().length>0) {
			ShipmentType st = res.getShipment()[0];
			PackageType pt = st.getPackage()[0];
			if (pt.getActivity()!=null && pt.getActivity().length>0) {
				ActivityType a = pt.getActivity()[0];
				if (a.getStatus()!=null && "D".equals(a.getStatus().getType())) {
					r = new TrackingResponse();
					r.setDeliveryStatus(DeliveryStatus.Delivered);
					
					DateFormat df = new SimpleDateFormat("yyyyMMdd");
					try {
						Date result = df.parse(a.getDate());
						r.setDeliveryDate(result);
					} catch (ParseException e) {
						log.error("Unable to parse delivery date ");
					}
					
				}
			}
			
		}
		
		
		try {
			log.debug("Track Response:\r" + om.writerWithDefaultPrettyPrinter().writeValueAsString(res));
		} catch (JsonProcessingException e) {	}
		return r;
	}
	
	private UPSSecurity populateUPSSecurity(){

		UPSSecurity upss = new UPSSecurity();
		ServiceAccessToken_type0 upsSvcToken = new ServiceAccessToken_type0();
		upsSvcToken.setAccessLicenseNumber(provider.getAccesskey());
		upss.setServiceAccessToken(upsSvcToken);
		UsernameToken_type0 upsSecUsrnameToken = new UsernameToken_type0();
		upsSecUsrnameToken.setUsername(provider.getUsername());
		upsSecUsrnameToken.setPassword(provider.getPassword());
		upss.setUsernameToken(upsSecUsrnameToken);

		return upss;
	}
	
	private static TrackRequest populateTrackRequest(String trackingId) {
		
		TrackRequest trackRequest = new TrackRequest();
		RequestType request = new RequestType();
		String[] requestOption = { "0" };
		request.setRequestOption(requestOption);
		trackRequest.setRequest(request);			
		trackRequest.setInquiryNumber(trackingId);
		trackRequest.setTrackingOption("02");
		
		return trackRequest;
	}

}
