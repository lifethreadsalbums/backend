package com.poweredbypace.pace.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.poweredbypace.pace.domain.shipping.Shipment;
import com.poweredbypace.pace.repository.ShipmentRepository;
import com.poweredbypace.pace.service.impl.ShippingManager;

@Controller
public class ShipmentController {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private ShippingManager shippingManager;
	
	@Autowired
	private ShipmentRepository shipmentService;

	@RequestMapping(value = "/api/shipment/{id}/export", method = RequestMethod.GET)
	public void getFile(@PathVariable("id") Long id, HttpServletResponse response) {
		try {
			Shipment shipment = shipmentService.findOne(id);
			String shippingProviderId = shipment.getShippingProviderId();
			String ext = shippingManager.getExportFileExtension(shippingProviderId);
			response.setContentType("text/" + ext + ";charset=utf-8");
			response.setHeader("Content-Disposition","attachment; filename=\"" + shippingProviderId + "." + ext  + "\"");
			shippingManager.exportShipments(shippingProviderId,
					shipmentService.findAll(),
					new OutputStreamWriter(response.getOutputStream()));
			response.flushBuffer();
		} catch (IOException e) {
			log.error(e.getStackTrace(), e);
		}
	}
	
}
