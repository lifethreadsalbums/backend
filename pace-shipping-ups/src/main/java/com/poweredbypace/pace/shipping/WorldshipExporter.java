package com.poweredbypace.pace.shipping;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poweredbypace.pace.domain.shipping.Shipment;
import com.ups.xmlschema.ct.worldship.impexp.shipmentimport.v1_0_0.ObjectFactory;
import com.ups.xmlschema.ct.worldship.impexp.shipmentimport.v1_0_0.Shipments;

public class WorldshipExporter implements ShipmentExporter {

	private Log log = LogFactory.getLog(WorldshipExporter.class);
	
	private WorldshipShipmentConverter converter;
	private Map<String, Object> params;
	
	public void export(List<Shipment> shipments, Writer writer) throws IOException {
		try {
			JAXBContext context = JAXBContext.newInstance("com.ups.xmlschema.ct.worldship.impexp.shipmentimport.v1_0_0");
			Shipments upsShipments = new ObjectFactory().createShipments();

			for(Shipment shipment : shipments) {
				upsShipments.getShipment().add(getConverter().convert(shipment, getParams()));
			}
			
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
			marshaller.marshal(upsShipments, writer);
		} catch (JAXBException e) {
			log.error(e.getStackTrace(), e);
		}
		
	}

	public WorldshipShipmentConverter getConverter() {
		return converter;
	}

	public void setConverter(WorldshipShipmentConverter converter) {
		this.converter = converter;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	@Override
	public String getFileType() {
		return "xml";
	}
}
