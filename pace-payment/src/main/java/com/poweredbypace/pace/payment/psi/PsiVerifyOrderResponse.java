package com.poweredbypace.pace.payment.psi;

import java.util.Map;

public class PsiVerifyOrderResponse {

	public String ResponseCode;
	public String ResponseMsg;
	public String OrderStatus;
	public String OrderID;
	public String ProcessTime;
	
	public Map<String,String> OrderClientInfo;
	public Map<String,String> OrderBillingInfo;
	public Map<String,String> OrderShippingInfo;
	public Map<String,String> OrderPaymentCardInfo;
	public Map<String,String> OrderAmountInfo;
	
	public String PaymentType;
	public String CardRefNumber;
	public String TransactionAuth;
	public String TransactionRefNumber;
	
}
