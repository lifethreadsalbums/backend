package com.poweredbypace.pace.binderyform.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.poweredbypace.pace.binderyform.BinderyFormRenderer;
import com.poweredbypace.pace.binderyform.BookMeasurement;
import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductPageType;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.domain.layout.PageRangeValue;
import com.poweredbypace.pace.domain.layout.PageRangeValue.PageRangeValueCollection;
import com.poweredbypace.pace.domain.layout.Spread;
import com.poweredbypace.pace.exception.ImageProcessingException;
import com.poweredbypace.pace.irisbook.IrisConstants.BoxStyle;
import com.poweredbypace.pace.irisbook.IrisConstants.Material;
import com.poweredbypace.pace.irisbook.IrisConstants.ProductType;
import com.poweredbypace.pace.irisbook.IrisConstants.SpineStyle;
import com.poweredbypace.pace.irisbook.IrisProduct;
import com.poweredbypace.pace.irisbook.IrisUtils;
import com.poweredbypace.pace.service.GenericRuleService;

public class BfMainContentRenderer implements BinderyFormRenderer {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private static final String PORT_INSIDE_SPINE_WIDTH = "PORT_INSIDE_SPINE_WIDTH";
	private static final String PORT_INSIDE_HINGE_GAP = "PORT_INSIDE_HINGE_GAP";
	
	private float[] cellWidths;
	
	@Autowired
	private BookMeasurement bookMeasurement;
	
	@Autowired
	private GenericRuleService ruleService;
	
	@Override
	public void render(Document document, PdfWriter writer,
			Product product, LayoutSize layoutSize,
			LayoutSize coverLayoutSize, Spread firstPage, Spread lastPage,
			Spread coverPage, JobProgressInfo job, int minProgress,
			int maxProgress) throws IOException, DocumentException,
			ImageProcessingException {
		
		IrisProduct p = new IrisProduct(product);
		document.add(getHeadTable(document, p, layoutSize));
		document.add(getMainTable(writer, p, layoutSize));
	}

	protected PdfPTable getHeadTable(Document doc, IrisProduct product, LayoutSize layoutSize) throws DocumentException
	{
		PdfPTable headTable = new PdfPTable(3);	
		
		headTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		String s = layoutSize.getCode().replace("-", "");
		
		String shape = IrisUtils.getShapePaperCode(s, product.getProductType(), product.getPaperTypeCode());
		
		PdfPCell shapeCell = new PdfPCell(
				new Phrase(shape, 
						BinderyFormHelper.getFont(30, true)));
		shapeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		shapeCell.setBorder(0);
		shapeCell.setVerticalAlignment(Element.ALIGN_TOP);
		shapeCell.setPadding(0);
		shapeCell.setPaddingTop(-8);
		shapeCell.setFixedHeight(30f);
		
		PdfPCell priorityCell = new PdfPCell(); 
		priorityCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		priorityCell.setVerticalAlignment(Element.ALIGN_TOP);
		priorityCell.setBorder(0);
		priorityCell.setPadding(0);
		priorityCell.setPaddingTop(-20f);
		priorityCell.setFixedHeight(39f);
		Paragraph p1 = new Paragraph(product.getPriority() ? "!RUSH!" : "",
						BinderyFormHelper.getFont(26, true, true, BaseColor.RED));
		p1.setAlignment(Element.ALIGN_CENTER);
		p1.setSpacingAfter(0f);
		p1.setSpacingBefore(-5f);
		
		priorityCell.addElement(p1);
		if (product.getPriority()) {
			Date dd = product.getDueDate();
			if (dd!=null) {
				SimpleDateFormat formatter = new SimpleDateFormat("MMM/dd/yy");
				String dueDate = "MUST SHIP ON " + formatter.format(dd);
				Paragraph p2 = new Paragraph(dueDate,
						BinderyFormHelper.getFont(17, true, false, BaseColor.RED));
				p2.setSpacingBefore(-6f);
				p2.setAlignment(Element.ALIGN_CENTER);
				priorityCell.addElement(p2);
			}
		}
		
		PdfPCell jobIdCell = new PdfPCell(new Phrase(product.getJobId(), BinderyFormHelper.getFont(30, true)));
		jobIdCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		jobIdCell.setVerticalAlignment(Element.ALIGN_TOP);
		jobIdCell.setBorder(0);
		jobIdCell.setPadding(0);
		jobIdCell.setPaddingTop(-8);
		
		headTable.addCell(shapeCell);
		headTable.addCell(priorityCell);
		headTable.addCell(jobIdCell);
		headTable.setSpacingAfter(0f);
		
		float [] widths = {0f, 0f, 0f};
		widths[0] = measureCellWidth(shapeCell) + 6f;
		widths[2] = measureCellWidth(jobIdCell) + 6f;
		widths[1] = (doc.right() - doc.left()) - (widths[0] + widths[2]);
		headTable.setLockedWidth(true);
		headTable.setTotalWidth(widths);
		
		return headTable;
	}

	protected PdfPTable getMainTable(PdfWriter writer, IrisProduct product, LayoutSize layoutSize) 
			throws DocumentException, IOException, ImageProcessingException
	{
		boolean hasBookStamps = false;
		boolean hasBoxStamps = false;
		for(StampInfo si:BinderyFormHelper.getStamps(product.getProduct()))
		{
			if (si.getBookStamp())
				hasBookStamps = true;
			if (si.getBoxStamp())
				hasBoxStamps = true;
		}		
		
		boolean isFM = ProductType.FLUSHMOUNT.equals(product.getProductType());
		boolean isPort = product.isPortfolio();
		
		int numCols = isFM || (isPort && product.hasBox()) ? 3 : 2;
		cellWidths = numCols==3 ?
				new float[] {2.4f * 72f, 1.5f *72f, 2.56f * 72f} : 
				new float[] {2.9f * 72f, 2.56f * 72f};
		PdfPTable table = new PdfPTable(numCols);
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.setWidthPercentage(100f);
		table.setLockedWidth(true);
		
		if (isPort)
		{
			table.addCell(getPortCell(writer, product, layoutSize, hasBookStamps));
			table.addCell(getSpineCell(writer, product, layoutSize));
		} else if (isFM) {
			table.addCell(getBookCell(writer, product, layoutSize, hasBookStamps));
			table.addCell(getPageStyleCell(writer, product, layoutSize));
		} else
			table.addCell(getBookCell(writer, product, layoutSize, hasBookStamps));
		
		if (product.hasBox() || (!product.hasBox() && !product.isPortfolio()))
		{
			table.addCell(getBoxCell(product, layoutSize, hasBoxStamps));
		}
		
		if (cellWidths.length==3)
		{
			float totalWidth = 0f;
			for(float w:cellWidths)
				totalWidth += w;
			
			float dist = Math.max(0, 400f - totalWidth) / 3f;
			cellWidths[0] += dist;
			cellWidths[1] += dist;
		}
		
		table.setTotalWidth(cellWidths);
		
		return table;
	}
	

	protected PdfPCell getBookCell(PdfWriter writer, IrisProduct product, LayoutSize layoutSize,
			boolean hasBookStamps) throws BadElementException, IOException
	{
		DecimalFormat format = new DecimalFormat("#0.0");
		format.setDecimalSeparatorAlwaysShown(false);
		
		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		
		boolean isPrintingOnly = product.isPrintingOnly();
		
		if (isPrintingOnly)
		{
			Font font = BinderyFormHelper.getFont(17, true);
			font.setColor(BaseColor.RED);
			Paragraph header = new Paragraph("PRINTING ONLY", font);
			header.setSpacingAfter(6f);
			cell.addElement(header);
		} else {
			Paragraph header = new Paragraph("BOOK ", BinderyFormHelper.getFont(17, true));
			header.setSpacingAfter(6f);
			header.add(new Chunk(BinderyFormHelper.renderCheckBox(writer), 0f, 0f));
			cell.addElement(header);
		}  
		
		String bookMaterial = product.getBookMaterialCode();
		
		if (!StringUtils.equals(bookMaterial, Material.FIC) && 
			!StringUtils.equals(bookMaterial, Material.QBIC))
		{
			cell.setFixedHeight(2.80f * 72);
		} else {
			cell.setFixedHeight(2.50f * 72);
		}
		
		cell.setPaddingRight(0f);
		cell.setPaddingBottom(0f);
		
		int numPages = product.getPageCount();
		if (!isPrintingOnly)
		{
			double spineWidth = getPageRangeValue(product.getProduct(), GenericRule.SPINE_WIDTH);
			cell.addElement(BinderyFormHelper.createTitleValueParagraph("Spine", format.format(spineWidth)));
		
			try {
				if (!StringUtils.equals(bookMaterial, Material.FIC) && 
					!StringUtils.equals(bookMaterial, Material.QBIC))
				{
					bookMeasurement.init(product.getProduct());
						
					float clothWidth = bookMeasurement.getValue("ClothWidth");
					float clothHeight = bookMeasurement.getValue("ClothHeight");
					String val = format.format(clothWidth)+" x "+format.format(clothHeight);
					cell.addElement(BinderyFormHelper.createTitleValueParagraph("Cloth", val));
				} 
			} catch (Exception ex) {
				log.warn("Cannot calculate book cloth", ex);
			}
			cell.addElement(BinderyFormHelper.createTitleValueParagraph("Material", shorten(product.getBookMaterialLabel())));
			cell.addElement(BinderyFormHelper.createTitleValueParagraph("Colour", shorten(product.getBookColourLabel())));
			
			if (Material.QBIC.equals(bookMaterial)) {
				cell.addElement(BinderyFormHelper.createTitleValueParagraph("Spine Material", 
						shorten(product.getProduct().getProductOptionDisplayValue("qbicMaterial"))));
				cell.addElement(BinderyFormHelper.createTitleValueParagraph("Spine Colour", 
						shorten(product.getProduct().getProductOptionDisplayValue("qbicColour"))));
			}
			
			cell.addElement(BinderyFormHelper.createTitleValueParagraph("Endpapers", shorten(product.getBookEndPapersLabel())));
		
			cell.addElement(BinderyFormHelper.createTitleValueParagraph("Stamp", hasBookStamps ? "yes" : "no"));
		}
		if (product.getProduct().getPrototypeProduct().getProductPageType()==ProductPageType.PageBased)
			cell.addElement(BinderyFormHelper.createTitleValueParagraph("Pages", Integer.toString(numPages)));
		else 
			cell.addElement(BinderyFormHelper.createTitleValueParagraph("Spreads", Integer.toString(numPages)));
		
		BaseColor qColor = null;
		if (product.getQuantity()>1)
			qColor = new BaseColor(0xfff101);
		
		cell.addElement(BinderyFormHelper.createTitleValueParagraph("Job Quantity", 
				Integer.toString(product.getQuantity()),
				qColor));
		
		
		String[] whiteBoardsColors = { 
			"ivory",
			"light_grey",
			"oat",
			"wheat",
			
			"baby_blue",
			"baby_pink",
			"sunflower",
			"cream",
			
			"pearl",
			"nat_lin_canvas",
        };
		
		boolean useWhiteBoards = false;
		if (StringUtils.equals(bookMaterial, Material.FIC) || 
			StringUtils.equals(bookMaterial, Material.QBIC)) {
			useWhiteBoards = true;
		} else {
			for(String color:whiteBoardsColors)
			{
				if (StringUtils.equals(product.getBookColourCode(), color))
					useWhiteBoards = true;
			}
		}
		
		if (useWhiteBoards)
		{
			Paragraph p = new Paragraph();
			p.add(new Chunk("USE WHITE BOARDS", BinderyFormHelper.getFont(14, true)));
			cell.addElement(p);
		}
		
		
		if (cellWidths.length==3)
		{
			float width = measureCellWidth(cell);
			cellWidths[0] = width + 3f;
		}
			
		return cell;
	}
	
	protected PdfPCell getBoxCell(IrisProduct product, LayoutSize layoutSize, boolean hasBoxStamps)
	{
		boolean hasBox = product.hasBox();
		boolean hasRibbon = hasBox && product.getBoxTypeCode().equals(BoxStyle.PRESENTATION_BOX);
		
		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setPaddingLeft(0f);
		
		if (!hasBox)
			return cell;
		
		Paragraph header = new Paragraph("BOX", BinderyFormHelper.getFont(17, true));
		header.setSpacingAfter(6f);
		cell.addElement(header);
		
		String boxStyle = product.getBoxTypeLabel();
		if (BoxStyle.PRESENTATION_BOX.equals(product.getBoxTypeCode()))
			boxStyle = "Pres. Box";
		
		cell.addElement(BinderyFormHelper.createTitleValueParagraph("Style", boxStyle));
		cell.addElement(BinderyFormHelper.createTitleValueParagraph("Material", shorten(product.getBoxMaterialLabel())));
		cell.addElement(BinderyFormHelper.createTitleValueParagraph(
				cellWidths.length==3 ? "Col" : "Colour", shorten(product.getBoxColourLabel())));
		cell.addElement(BinderyFormHelper.createTitleValueParagraph("Ribbon", hasRibbon ? product.getBoxRibbonLabel() : null));
		
		if ( ("leather".equals(product.getBoxMaterialCode()) || 
			 "vintage_leather".equals(product.getBoxMaterialCode())) && 
			 	!(BoxStyle.SLIP_CASE.equals(product.getBoxTypeCode())) ) {
			cell.addElement(BinderyFormHelper.createTitleValueParagraph("T&L Mat.", shorten(product.getBoxLinersWallsMaterialLabel())));
			cell.addElement(BinderyFormHelper.createTitleValueParagraph("T&L Col.", shorten(product.getBoxLinersWallsColourLabel())));
		}
		
		cell.addElement(BinderyFormHelper.createTitleValueParagraph("Stamp", hasBoxStamps ? "yes" : "no"));
		
		if (cellWidths.length==3)
		{
			float width = measureCellWidth(cell);
			cellWidths[2] = width + 3f;
		}
		
		return cell;
	}
	
	protected PdfPCell getPortCell(PdfWriter writer, IrisProduct product, LayoutSize layoutSize,
			boolean hasBookStamps) throws BadElementException, IOException
	{
		boolean isPrintingOnly = product.isPrintingOnly();
		
		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		
		Paragraph header = new Paragraph(isPrintingOnly ? "PORTFOLIO PAGES " : "PORTFOLIO ", BinderyFormHelper.getFont(17, true));
		
		header.setSpacingAfter(6f);
		header.add(new Chunk(BinderyFormHelper.renderCheckBox(writer), 0f, 0f));
		cell.addElement(header);
		cell.setFixedHeight(2.30f * 72);
		cell.setPaddingRight(0f);
		cell.setPaddingBottom(0f);
		
		cell.addElement(
			BinderyFormHelper.createTitleValueParagraph("Material", shorten(product.getBookMaterialLabel())));
		
		if (!isPrintingOnly)
		{
			cell.addElement(
				BinderyFormHelper.createTitleValueParagraph(cellWidths.length==3 ? "Col" : "Colour", product.getBookColourLabel()));
			cell.addElement(
				BinderyFormHelper.createTitleValueParagraph("Liner Mat", shorten(product.getLinerMaterialLabel())));
			cell.addElement(
				BinderyFormHelper.createTitleValueParagraph("Liner Col", shorten(product.getLinerColourLabel())));
			cell.addElement(
				BinderyFormHelper.createTitleValueParagraph("Stamp", hasBookStamps ? "yes" : "no"));
		}
		cell.addElement( 
			BinderyFormHelper.createTitleValueParagraph("Pages", Integer.toString(product.getPageCount())));
		
		BaseColor qColor = null;
		if (product.getQuantity()>1)
			qColor = new BaseColor(0xfff101);
		
		cell.addElement(BinderyFormHelper.createTitleValueParagraph("Job Quantity", 
			Integer.toString(product.getQuantity()), qColor));
			
		if (cellWidths.length==3) {
			float width = measureCellWidth(cell);
			cellWidths[0] = width + 3f;
		}
		return cell;
	}
	
	
	protected PdfPCell getPageStyleCell(PdfWriter writer, IrisProduct product, LayoutSize layoutSize) throws BadElementException, IOException
	{
		boolean isFM = ProductType.FLUSHMOUNT.equals(product.getProductType());
		
		boolean isPrintingOnly = product.isPrintingOnly();
		DecimalFormat format = new DecimalFormat("#0.0");
		format.setDecimalSeparatorAlwaysShown(false);
		
		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setPaddingLeft(0f);
		cell.setPaddingRight(0f);
		
		if (isPrintingOnly)
			return cell;
		
		Paragraph header = new Paragraph("PAGE ", BinderyFormHelper.getFont(17, true));
		header.setSpacingAfter(6f);
		cell.addElement(header);
		
		@SuppressWarnings("serial")
		final Map<String,String> pageStyles = new HashMap<String, String>()
		{
		    {
		        put("thick", "Thick");
		        put("medium", "Medium");
		        
		    }
		};
		String pageStyle = product.getPageStyleLabel();
		String pageStyleCode = product.getPageStyleCode();
		if (pageStyles.containsKey(pageStyleCode)) {
			pageStyle = pageStyles.get(pageStyleCode);
		}
		
		cell.addElement(BinderyFormHelper.createTitleValueParagraph("Style", pageStyle));
		if (isFM) {
			cell.addElement(BinderyFormHelper.createTitleValueParagraph("Core", product.getCoreColourLabel()));
		}
		
		cell.addElement(BinderyFormHelper.createTitleValueParagraph("Hinge", 
				format.format( getPageRangeValue(product.getProduct(), GenericRule.HINGE_GAP) )));
		
		if (isFM) {
			
//			ID = 27, Enhanced Velvet 255GSM use: GV
//			ID = 25, Lasal Luster 300GSM use: GL
//			ID = 26, Lasal Matte 235GSM use: GM
//			ID = 28, Fuji Luster use: FL
//			ID = 29, Fuji Deeo Matte use: FDM
			
			@SuppressWarnings("serial")
			final Map<String,String> papers = new HashMap<String, String>()
			{
			    {
			        put("enhanced_velvet", "GV");
			        put("lasal_luster", "GL");
			        put("lassal_matte", "GM");
			        put("fuji_luster", "FL");
			        put("fuji_matte", "FSM");
			    }
			};
			
			String paperName = product.getPaperTypeLabel();
			String paperCode = product.getPaperTypeCode();
			
			if (papers.containsKey(paperCode)) {
				paperName = papers.get(paperCode);
			}
			
			cell.addElement(BinderyFormHelper.createTitleValueParagraph("Paper", paperName));
		}
		
		if (cellWidths.length==3)
		{
			float width = measureCellWidth(cell);
			cellWidths[1] = width + 3f;
		}
		return cell;
	}
	
	protected PdfPCell getSpineCell(PdfWriter writer, IrisProduct product, LayoutSize layoutSize) throws BadElementException, IOException
	{
		DecimalFormat format = new DecimalFormat("#0.0");
		format.setDecimalSeparatorAlwaysShown(false);
		
		boolean isPrintingOnly = product.isPrintingOnly();
		
		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setPaddingLeft(0f);
		cell.setPaddingRight(0f);
		
		if (isPrintingOnly)
			return cell;
		
		Paragraph header = new Paragraph("SPINE ", BinderyFormHelper.getFont(18, true));
		header.setSpacingAfter(6f);
		if (product.getSpineStyleCode().equals(SpineStyle.REMOVABLE))
			header.add(new Chunk(BinderyFormHelper.renderCheckBox(writer), 0f, 0f));
		cell.addElement(header);
		
		double spineWidthInCm = getPageRangeValue(product.getProduct(), GenericRule.SPINE_WIDTH); 
		
		cell.addElement(BinderyFormHelper.createTitleValueParagraph("Style", 
				SpineStyle.REMOVABLE.equals(product.getSpineStyleCode()) ? "Removable" : "Fixed" ));
		
		
		if (product.getSpineStyleCode().equals(SpineStyle.REMOVABLE))
		{
			cell.addElement(BinderyFormHelper.createTitleValueParagraph("Spine", 
					format.format(spineWidthInCm)));
			
			cell.addElement(BinderyFormHelper.createTitleValueParagraph("Material", 
					shorten(product.getSpineMaterialLabel())));
			cell.addElement(BinderyFormHelper.createTitleValueParagraph("Col", 
					shorten(product.getSpineColourLabel())));
		} else {
			cell.addElement(BinderyFormHelper.createTitleValueParagraph("Outside Sp", 
					format.format(spineWidthInCm)));
			
			cell.addElement(BinderyFormHelper.createTitleValueParagraph("Inside Sp", 
					format.format( getPageRangeValue(product.getProduct(), PORT_INSIDE_SPINE_WIDTH) )));

			cell.addElement(BinderyFormHelper.createTitleValueParagraph("Ins. Hinge", 
					format.format( getPageRangeValue(product.getProduct(), PORT_INSIDE_HINGE_GAP) )));
		}
		
		if (cellWidths.length==3)
		{
			float width = measureCellWidth(cell);
			cellWidths[1] = width + 6f;
		}
		return cell;
	}
	
	private float measureCellWidth(PdfPCell cell)
	{
		float maxWidth = 0f;
		List<Element> elements = cell.getCompositeElements();
		if (elements==null && cell.getPhrase()!=null)
		{
			elements = new ArrayList<Element>();
			elements.add(cell.getPhrase());
		}
		for(Element element:elements)
		{
			float width = 0f;
			for(Chunk chunk:element.getChunks())
			{
				if (chunk.getImage()!=null)
				{
					width += chunk.getImage().getWidth();
				} else {
					String text = chunk.getContent();
					BaseFont font = chunk.getFont().getCalculatedBaseFont(false);
					width += font.getWidthPoint(text, chunk.getFont().getSize());
				}
			}
			if (width>maxWidth)
				maxWidth = width;
		}
		return maxWidth;
	}
	
	private double getPageRangeValue(Product product, String code) {
		List<PageRangeValue> values = ruleService.getRuleCollectionValue(product, code, PageRangeValue.class);
		if (values==null) return 0d;
		PageRangeValueCollection valCol = new PageRangeValueCollection(values);
		return valCol.getValue(product.getPageCount());
	}
	
	private String shorten(String colour) {
		
		if (colour==null)
			return null;
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("Chocolate Brown 309", "Choc. Brn. 309");
		map.put("Cafe au Lait 215", "Cafe Lait 215");
		map.put("Robin's Egg 268", "R. Egg 268");
		map.put("Burnt Orange 823",	"Bt. Orange 823");
		map.put("Medium Grey 863", "Med. Grey 863");
		
		map.put("Blue Whale 273", "Blu Whale 273");
		map.put("Chartreuse 263", "Chart. 263");
		map.put("Light Blue 272", "Lt. Blue 272");
		map.put("Light Pink 237", "Lt. Pink 237");
		map.put("Cornflower 865", "Crnflower 865");
		map.put("Deep Ocean 857", "Dp. Ocean 857");
		map.put("Light Grey 846", "Lt. Grey 846");
		map.put("Medium Grey 863", "Med. Grey 863");
		map.put("Vintage Leather", "Leather");
		map.put("Natural Linen", "Nat. Linen");
		map.put("Buttercup 249", "But.Cup 249"); 
		map.put("Antique Lace 253", "Ant. Lce 253");
		map.put("Full Printed Cover", "FIC");
		map.put("3/4 Printed Cover", "1/4 IC");
		
		map.put("Black Fine Art", "Black");
		map.put("Cream Fine Art", "Cream");
		
		for(String key:map.keySet()) {
			String val = map.get(key);
			colour = colour.replaceAll(key, val);
		}
		return colour;
	}
	
}
