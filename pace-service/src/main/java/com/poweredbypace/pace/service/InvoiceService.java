package com.poweredbypace.pace.service;

import java.io.File;

import com.poweredbypace.pace.domain.order.Invoice;
import com.poweredbypace.pace.domain.order.Order;

public interface InvoiceService {
	Invoice create(Order order);
	File generateInvoice(Order order);
	void emailInvoice(Invoice invoice);
	void emailInvoiceToAdmin(Invoice invoice);
}
