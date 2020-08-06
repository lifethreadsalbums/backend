package com.poweredbypace.pace;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.poweredbypace.pace.domain.shipping.Shipment;
import com.poweredbypace.pace.shipping.ShipmentExporter;

public class EstExporter implements ShipmentExporter {

	private EstShipmentConverter converter;
	
	public void export(List<Shipment> shipments, Writer writer) throws IOException {


		ICsvBeanWriter beanWriter = null;
		try {
			beanWriter = new CsvBeanWriter(writer,
					CsvPreference.STANDARD_PREFERENCE);

			final Map<String, CellProcessor> processors = new LinkedHashMap<String, CellProcessor>();
			
			processors.put("recordType", new NotNull());
			processors.put("importedOrderId", new NotNull());
			processors.put("clientId", new Optional());
			processors.put("titleName", new Optional());
			processors.put("firstName", new Optional());
			processors.put("lastName", new Optional());
			processors.put("titleOrDepartament", new Optional());
			processors.put("companyName", new Optional());
			processors.put("additionalAddressInformation", new Optional());
			processors.put("addressLine1", new Optional());
			processors.put("addressLine2", new Optional());
			processors.put("city", new Optional());
			processors.put("provinceorState", new Optional());
			processors.put("postalCodeorZipCode", new Optional());
			processors.put("countryCode", new NotNull());
			processors.put("clientVoicePhone", new Optional());
			processors.put("clientFAXNumber", new Optional());
			processors.put("clientEmailAddress", new Optional());
			processors.put("weight", new Optional());
			processors.put("service", new NotNull());
			processors.put("length", new Optional());
			processors.put("width", new Optional());
			processors.put("height", new Optional());
			processors.put("documentIndicator", new Optional());
			processors.put("oversizeIndicator", new Optional());
			processors.put("deliveryConfirmationIndicator", new Optional());
			processors.put("signatureIndicator", new Optional());
			processors.put("usPostalBoxIndicator", new Optional());
			processors.put("doNotSafeDropIndicator", new Optional());
			processors.put("cardforPickupIndicator", new Optional());
			processors.put("proofOfAgeRequired18", new Optional());
			processors.put("proofOfAgeRequired19", new Optional());
			processors.put("leaveatDoorIndicator", new Optional());
			processors.put("registeredIndicator", new Optional());
			processors.put("specialDeliveryIndicator", new Optional());
			processors.put("adviceOfReceiptIndicator", new Optional());
			processors.put("pickupAtThePostOffice", new Optional());
			processors.put("notificationMethod", new Optional());
			processors.put("eveningIndicator", new Optional());
			processors.put("saturdayIndicator", new Optional());
			processors.put("notifyRecipient", new Optional());
			processors.put("insuredAmount", new Optional());
			processors.put("codValue", new Optional());
			processors.put("methodOfCollection", new Optional());

			for (final Shipment shipment : shipments) {
				EstExportBean bean = converter.convert(shipment);
				beanWriter.write(bean, processors.keySet().toArray(new String[0]), processors.values().toArray(new CellProcessor[0]));
			}

		} finally {
			if (beanWriter != null) {
				beanWriter.close();
			}
		}
	}

	public EstShipmentConverter getConverter() {
		return converter;
	}

	public void setConverter(EstShipmentConverter converter) {
		this.converter = converter;
	}

	@Override
	public String getFileType() {
		return "csv";
	}
}
