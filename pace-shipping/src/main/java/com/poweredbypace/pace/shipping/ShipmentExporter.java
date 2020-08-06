package com.poweredbypace.pace.shipping;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.poweredbypace.pace.domain.shipping.Shipment;

public interface ShipmentExporter {
	
	public void export(List<Shipment> shipments, Writer writer) throws IOException;
	
	public String getFileType();
}
