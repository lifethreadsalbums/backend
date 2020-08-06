package com.poweredbypace.pace.irisbook;

import java.util.Date;

import org.apache.commons.lang.BooleanUtils;

import com.poweredbypace.pace.binderyform.impl.BinderyFormHelper;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.ImageStampElement;
import com.poweredbypace.pace.domain.layout.LayoutSize;

public class IrisProduct {

	private Product product;
	
	public IrisProduct(Product p) {
		this.product = p;
	}
	
	public Product getProduct() {
		return product;
	}
	
	public String getPageStyleLabel() {
		String style =  product.getProductOptionDisplayValue("pageStyle");
		style = style.replaceAll("\\(2mm Substrate\\)", "").replaceAll("\\(1mm Substrate\\)", "");
		return style;
	}
	
	public String getPageStyleCode() {
		return product.getProductOptionCode("pageStyle");
	}
	
	public String getPrintTypeCode() {
		return product.getProductOptionCode("printType");
	}
	
	public String getPrintTypeLabel() {
		return product.getProductOptionDisplayValue("printType");
	}
	
	public String getCoreColourLabel() {
		return product.getProductOptionDisplayValue("coreColour");
	}
	
	public String getBoxLinersWallsMaterialLabel() {
		return product.getProductOptionDisplayValue("boxLinersWallsMaterial");
	}
	
	public String getBoxLinersWallsColourLabel() {
		return product.getProductOptionDisplayValue("boxLinersWallsColour");
	}
	
	public String getSpineMaterialLabel() {
		return product.getProductOptionDisplayValue("spineMaterial");
	}
	
	public String getSpineColourLabel() {
		return product.getProductOptionDisplayValue("spineColour");
	}
	
	public String getLinerMaterialLabel() {
		return product.getProductOptionDisplayValue("linerMaterial");
	}
	
	public String getLinerColourLabel() {
		return product.getProductOptionDisplayValue("linerColour");
	}
	
	public String getBookMaterialLabel() {
		return product.getProductOptionDisplayValue("bookMaterial");
	}
	
	public String getBookColourLabel() {
		return product.getProductOptionDisplayValue("bookColour");
	}
	
	public String getBookEndPapersLabel() {
		return product.getProductOptionDisplayValue("endPapersColour")
			+ " " + product.getProductOptionDisplayValue("endPapersType");
	}
	
	public String getBookMaterialCode() {
		return product.getProductOptionCode("bookMaterial");
	}
	
	public String getBookColourCode() {
		return product.getProductOptionCode("bookColour");
	}
	
	public String getBookEndPapersCode() {
		return product.getProductOptionCode("endPapersColour");
	}
	
	public String getPaperTypeCode() {
		return product.getProductOptionCode("paperType");
	}
	
	public String getBoxTypeCode() {
		return product.getProductOptionCode("boxType");
	}
	
	public String getBoxTypeLabel() {
		return product.getProductOptionDisplayValue("boxType");
	}
	
	public String getBoxMaterialLabel() {
		return product.getProductOptionDisplayValue("boxMaterial");
	}
	
	public String getBoxMaterialCode() {
		return product.getProductOptionCode("boxMaterial");
	}
	
	public String getBoxColourLabel() {
		return product.getProductOptionDisplayValue("boxColour");
	}
	
	public String getBoxColourCode() {
		return product.getProductOptionCode("boxColour");
	}
	
	public String getBoxRibbonLabel() {
		return product.getProductOptionDisplayValue("boxRibbon");
	}
	
	public String getPaperTypeLabel() {
		return product.getProductOptionDisplayValue("paperType");
	}
	
	public String getSpineStyleCode() {
		return product.getProductOptionCode("spineStyle");
	}
	
	public Integer getQuantity() {
		return product.getQuantity();
	}
	
	public Integer getPageCount() {
		return product.getPageCount();
	}
	
	public String getProductType() {
		return product.getProductOptionCode("productType");
	}
	
	public String getProductLine() {
		return product.getProductOptionCode("_productPrototype");
	}
	
	public boolean isPortfolio() {
		return "port_portfolio".equals(getProductLine()) ||
			"port_pages".equals(getProductLine());
	}
	
	public String getProductLineLabel() {
		return product.getProductOptionDisplayValue("_productPrototype");
	}
	
	public boolean hasBox() {
//		String type = this.getProductLine();
//		return "luxe".equals(type) || "fm_luxe".equals(type);
		return product.getProductOptionCode("boxType")!=null;
	}
	
	public String getShapeCode() {
		return product.getProductOptionCode("size");
	}
	
	public String getCoverType() {
		LayoutSize layoutSize = product.getLayoutSize();
		if (layoutSize!=null && layoutSize.getCoverType()!=null)
			return layoutSize.getCoverType().getCode();
		return null;
	}
	
	public Date getDueDate() {
		return product.getProductOptionValue("dueDate", Date.class);
	}
	
	public boolean getPriority() {
		return BooleanUtils.isTrue(product.getRush());
	}
	
	public boolean getCustomLogo() {
		return getCustomLogoUrl()!=null;
	}
	
	public String getCustomLogoUrl() {
		try {
			ImageStampElement el = product.getProductOptionValue("companyLogo", ImageStampElement.class);
			if (el!=null) 
				return el.getImageFile().getUrl();
		} catch (Exception ex) {}
		return null;
	}
	
	public boolean getCustomDie() {
		return getCustomDieUrl()!=null;
	}
	
	public String getCustomDieUrl() {
		try {
			ImageStampElement el = product.getProductOptionValue("bookStampText", ImageStampElement.class);
			if (el!=null) 
				return el.getImageFile().getUrl();
		} catch (Exception ex) {}
		return null;
	}
	
	public boolean hasStamp() {
		return BinderyFormHelper.getStamps(product).size()>0 || getCustomDieUrl()!=null;
	}
	
	public boolean getStudioSample() {
		return BooleanUtils.isTrue(product.getStudioSample());
	}
	
	public String getJobId() {
		return product.getProductNumber();
	}
	
	public String getAdminNotes() {
		return product.getProductOptionValue("adminNotes", String.class);
	}
	
	public String getCustomerNotes() {
		return product.getProductOptionValue("_notes", String.class);
	}
	
	public Date getProductionDate() {
		if (product.getOrderItem()!=null) {
			return product.getOrderItem().getOrder().getDateCreated();
		}
		
		return product.getProductOptionValue("_dateCreated", Date.class);
	}
	
//	public Date getOrderDate() {
//		return product.getProductOptionValue("_dateCreated", Date.class);
//	}
	
	public boolean isPrintingOnly() {
		String code = product.getPrototypeProduct().getCode();
		return "port_pages".equals(code);
	}
	
	public boolean isNoPrinting() {
		Boolean portPrinting = product.getProductOptionValue("portPrinting", Boolean.class);
		return portPrinting!=null && portPrinting.booleanValue()==false;
	}
	
	public boolean isReprint() {
		return product.isReprint();
	}
	
	public boolean isTS() {
		String code = product.getPrototypeProduct().getCode();
		return "port_test_sheet fm_test_sheet pb_test_sheet".indexOf(code)>=0;
	}
	
	public boolean isDuplicate() {
		return product.getParent()!=null;
	}
}
