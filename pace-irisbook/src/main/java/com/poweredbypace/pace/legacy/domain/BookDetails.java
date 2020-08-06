package com.poweredbypace.pace.legacy.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.poweredbypace.pace.irisbook.IrisUtils;
import com.poweredbypace.pace.legacy.domain.Order.OrderType;



@Entity
@Table(name = "book_details")
@SuppressWarnings("serial")
public class BookDetails implements Serializable {
	
	public static class SpineStyle {
		public static final String OPEN = "Open";
		public static final String CLOSED = "Closed";
	}
	
	public static class FirstPageType {
		public static final String LPS = "LPS";
		public static final String RPS = "RPS";
	}
	

	/**
	 * Attribute bookDetailsId.
	 */
	private Long bookDetailsId;

	/**
	 * Attribute bookColour.
	 */
	private String bookColour;

	/**
	 * Attribute bookMaterial.
	 */
	private String bookMaterial;

	/**
	 * Attribute bookEndPapers.
	 */
	private String bookEndPapers;

	/**
	 * Attribute boxStyle.
	 */
	private String boxStyle;

	/**
	 * Attribute boxColour.
	 */
	private String boxColour;

	/**
	 * Attribute boxMaterial.
	 */
	private String boxMaterial;

	/**
	 * Attribute boxRibbon.
	 */
	private String boxRibbon;

	private Boolean customLogo;
	
	private Boolean studioSample;

	private String jobId;
	
	private List<StampLine> stampLines = null;
	
	private String notes;
	
	private String scale;

	private Integer quantity;
	
	private Date productionDate;
	
	private String comments;
	
	private String type;
	
	private Boolean priority;
	
	private String jobName;
	
	private String jobComments;
	
	private String spineStyle;
	
	private String spineMaterial;
	
	private String spineColour;
	
	private String linerMaterial;
	
	private String linerColour;
	
	private Integer paperTypeId;
	
	private Boolean customDie;
	
	private String customDieUrl;
	
	private Date coverPrintDate;
	
	private String printPages;
	
	private Integer numPrintPages;
	
	private String shape;
	
	private String coverType;
	
	private Double rate;
	
	private String category;
	
	private Integer priorityInterval;
	
	private Date dueDate;
	
	private Date shipDate;
	
	private String bookEndPapers2;
	
	private String firstPageType;
	
	private String field1;
	
	private String field2;
	
	private String field3;
	
	private String field4;
	
	private String field5;
	
	private String field6;
	
	private String field7;
	
	private String field8;
	
	private String field9;
	
	private String field10;
	
	private String field11;
	
	private String field12;
	
	private String field13;
	
	private String field14;
	
	private String field15;
	
	private String field16;
	
	private String field17;
	
	private String field18;
	
	private String field19;
	
	private String field20;
	
	private List<Attachment> attachments;
	
	private Boolean customBoxDie;
	
	private String customBoxDieUrl;
	/**
	 * <p> 
	 * </p>
	 * @return bookDetailsId
	 */
	@Basic
	@Id
	@GeneratedValue
	@Column(name = "book_details_id")
	public Long getBookDetailsId() {
		return bookDetailsId;
	}

	/**
	 * @param bookDetailsId new value for bookDetailsId 
	 */
	public void setBookDetailsId(Long bookDetailsId) {
		if (bookDetailsId!=null && bookDetailsId==0)
			bookDetailsId = null;
		this.bookDetailsId = bookDetailsId;
	}

	/**
	 * <p> 
	 * </p>
	 * @return bookColour
	 */
	@Basic
	@Column(name = "book_colour", length = 200)
	public String getBookColour() {
		return bookColour;
	}

	/**
	 * @param bookColour new value for bookColour 
	 */
	public void setBookColour(String bookColour) {
		this.bookColour = bookColour;
	}

	/**
	 * <p> 
	 * </p>
	 * @return bookMaterial
	 */
	@Basic
	@Column(name = "book_material", length = 200)
	public String getBookMaterial() {
		return bookMaterial;
	}

	/**
	 * @param bookMaterial new value for bookMaterial 
	 */
	public void setBookMaterial(String bookMaterial) {
		this.bookMaterial = bookMaterial;
	}

	/**
	 * <p> 
	 * </p>
	 * @return bookEndPapers
	 */
	@Basic
	@Column(name = "book_end_papers", length = 200)
	public String getBookEndPapers() {
		return bookEndPapers;
	}

	/**
	 * @param bookEndPapers new value for bookEndPapers 
	 */
	public void setBookEndPapers(String bookEndPapers) {
		this.bookEndPapers = bookEndPapers;
	}

	
	@Basic
	@Column(name = "book_end_papers2", length = 200)
	public String getBookEndPapers2() {
		return bookEndPapers2;
	}

	/**
	 * @param bookEndPapers2 new value for bookEndPapers2
	 */
	public void setBookEndPapers2(String bookEndPapers2) {
		this.bookEndPapers2 = bookEndPapers2;
	}
	/**
	 * <p> 
	 * </p>
	 * @return boxStyle
	 */
	@Basic
	@Column(name = "box_style", length = 200)
	public String getBoxStyle() {
		return boxStyle;
	}

	/**
	 * @param boxStyle new value for boxStyle 
	 */
	public void setBoxStyle(String boxStyle) {
		this.boxStyle = boxStyle;
	}

	/**
	 * <p> 
	 * </p>
	 * @return boxColour
	 */
	@Basic
	@Column(name = "box_colour", length = 200)
	public String getBoxColour() {
		return boxColour;
	}

	/**
	 * @param boxColour new value for boxColour 
	 */
	public void setBoxColour(String boxColour) {
		this.boxColour = boxColour;
	}

	/**
	 * <p> 
	 * </p>
	 * @return boxMaterial
	 */
	@Basic
	@Column(name = "box_material", length = 200)
	public String getBoxMaterial() {
		return boxMaterial;
	}

	/**
	 * @param boxMaterial new value for boxMaterial 
	 */
	public void setBoxMaterial(String boxMaterial) {
		this.boxMaterial = boxMaterial;
	}

	/**
	 * <p> 
	 * </p>
	 * @return boxRibbon
	 */
	@Basic
	@Column(name = "box_ribbon", length = 200)
	public String getBoxRibbon() {
		return boxRibbon;
	}

	/**
	 * @param boxRibbon new value for boxRibbon 
	 */
	public void setBoxRibbon(String boxRibbon) {
		this.boxRibbon = boxRibbon;
	}

	

	@Basic
	@Column(name = "custom_logo")
	public Boolean getCustomLogo() {
		return customLogo;
	}

	public void setCustomLogo(Boolean customLogo) {
		this.customLogo = customLogo;
	}

	@Basic
	@Column(name = "studio_sample")
	public Boolean getStudioSample() {
		return studioSample;
	}

	public void setStudioSample(Boolean studioSample) {
		this.studioSample = studioSample;
	}

	@Basic
	@Column(name = "job_id", length = 200)
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	@OneToMany(mappedBy="bookDetails", fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@Fetch(value=FetchMode.SUBSELECT)
	public List<StampLine> getStampLines() {
		return stampLines;
	}

	public void setStampLines(List<StampLine> stampLines) {
		this.stampLines = stampLines;
	}

	@Basic
	@Column(name = "notes")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@Basic
	@Column(name = "scale")
	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}
	
	@Column(name = "quantity")
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Column(name = "production_date")
	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	@Basic
	@Column(name = "comments")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	@Basic
	@Column(name = "job_name", length = 200)
	public String getJobName() {
		return jobName;
	}

	/**
	 * @param jobName new value for jobName 
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	@Column(name = "type", length=200)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "priority")
	public Boolean getPriority() {
		return priority;
	}

	public void setPriority(Boolean priority) {
		this.priority = priority;
	}
	
	@Column(name = "job_comments")
	public String getJobComments() {
		return jobComments;
	}

	public void setJobComments(String jobComments) {
		this.jobComments = jobComments;
	}

	@Transient
	public Boolean hasBox() 
	{
		String type = this.getType();
		boolean hasBox = type!=null && 
			!StringUtils.isEmpty(this.getBoxStyle()) && 
			!type.equals(OrderType.PURE) && !type.equals(OrderType.FM_PURE) &&  
			!type.equals(OrderType.SOUL) && !type.equals(OrderType.FM_SOUL) &&
			!"Printing Only".equals(this.getBookMaterial());
		return hasBox;
	}
	
	@Transient
	public Boolean hasStamp() 
	{
		String type = this.getType();
		boolean hasStamp = type!=null && 
				!type.equals(OrderType.PURE) && !type.equals(OrderType.FM_PURE) && 
				!"Printing Only".equals(this.getBookMaterial());
		return hasStamp;
	}
	

	@Column(name = "spine_style", length=200)
	public String getSpineStyle() {
		if (spineStyle==null)
			return SpineStyle.OPEN;
		return spineStyle;
	}

	public void setSpineStyle(String spineStyle) {
		this.spineStyle = spineStyle;
	}

	@Column(name = "spine_material", length=200)
	public String getSpineMaterial() {
		return spineMaterial;
	}

	public void setSpineMaterial(String spineMaterial) {
		this.spineMaterial = spineMaterial;
	}

	@Column(name = "spine_colour", length=200)
	public String getSpineColour() {
		return spineColour;
	}

	public void setSpineColour(String spineColour) {
		this.spineColour = spineColour;
	}

	@Column(name = "liner_material", length=200)
	public String getLinerMaterial() {
		return linerMaterial;
	}

	public void setLinerMaterial(String linerMaterial) {
		this.linerMaterial = linerMaterial;
	}

	@Column(name = "liner_colour", length=200)
	public String getLinerColour() {
		return linerColour;
	}

	public void setLinerColour(String linerColour) {
		this.linerColour = linerColour;
	}

	@Column(name = "paper_type_id")
	public Integer getPaperTypeId() {
		return paperTypeId;
	}

	public void setPaperTypeId(Integer paperTypeId) {
		this.paperTypeId = paperTypeId;
	}

	@Column(name = "custom_die")
	public Boolean getCustomDie() {
		return customDie;
	}

	public void setCustomDie(Boolean customDie) {
		this.customDie = customDie;
	}

	@Column(name = "custom_die_url")
	public String getCustomDieUrl() {
		return customDieUrl;
	}

	public void setCustomDieUrl(String customDieUrl) {
		this.customDieUrl = customDieUrl;
	}

	@Column(name = "cover_print_date")
	public Date getCoverPrintDate() {
		return coverPrintDate;
	}

	public void setCoverPrintDate(Date coverPrintDate) {
		this.coverPrintDate = coverPrintDate;
	}

	@Column(name = "print_pages")
	public String getPrintPages() {
		return printPages;
	}

	public void setPrintPages(String printPages) {
		this.printPages = printPages;
	}

	@Column(name = "num_print_pages")
	public Integer getNumPrintPages() {
		return numPrintPages;
	}

	public void setNumPrintPages(Integer numPrintPages) {
		this.numPrintPages = numPrintPages;
	}

	@Column(name = "shape")
	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	@Column(name = "cover_type")
	public String getCoverType() {
		return coverType;
	}

	public void setCoverType(String coverType) {
		this.coverType = coverType;
	}
	
	@Column(name = "rate")
	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	@Transient
	public String getShapeCode()
	{
		return shape!=null ? shape.replaceAll("PRF BK", "").trim() : null;
	}
	
	@Column(name = "category")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	@Column(name = "priority_interval")
	public Integer getPriorityInterval() {
		return priorityInterval;
	}

	public void setPriorityInterval(Integer priorityInterval) {
		this.priorityInterval = priorityInterval;
	}
	
	@Column(name = "due_date")
	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Column(name = "ship_date")
	public Date getShipDate() {
		return shipDate;
	}

	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}
	
	
	@Column(name = "first_page_type")
	public String getFirstPageType() {
		return firstPageType;
	}

	public void setFirstPageType(String firstPageType) {
		this.firstPageType = firstPageType;
	}
	
	@Column(name = "field1", length=200)
	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	@Column(name = "field2", length=200)
	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	@Column(name = "field3", length=200)
	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

	@Column(name = "field4", length=200)
	public String getField4() {
		return field4;
	}

	public void setField4(String field4) {
		this.field4 = field4;
	}

	@Column(name = "field5", length=200)
	public String getField5() {
		return field5;
	}

	public void setField5(String field5) {
		this.field5 = field5;
	}

	@Column(name = "field6", length=200)
	public String getField6() {
		return field6;
	}

	public void setField6(String field6) {
		this.field6 = field6;
	}

	@Column(name = "field7", length=200)
	public String getField7() {
		return field7;
	}

	public void setField7(String field7) {
		this.field7 = field7;
	}

	@Column(name = "field8", length=200)
	public String getField8() {
		return field8;
	}

	public void setField8(String field8) {
		this.field8 = field8;
	}

	@Column(name = "field9", length=200)
	public String getField9() {
		return field9;
	}

	public void setField9(String field9) {
		this.field9 = field9;
	}

	@Column(name = "field10", length=200)
	public String getField10() {
		return field10;
	}

	public void setField10(String field10) {
		this.field10 = field10;
	}
	
	
	@Column(name = "field11", length=200)
	public String getField11() {
		return field11;
	}

	public void setField11(String field11) {
		this.field11 = field11;
	}

	@Column(name = "field12", length=200)
	public String getField12() {
		return field12;
	}

	public void setField12(String field12) {
		this.field12 = field12;
	}

	@Column(name = "field13", length=200)
	public String getField13() {
		return field13;
	}

	public void setField13(String field13) {
		this.field13 = field13;
	}

	@Column(name = "field14", length=200)
	public String getField14() {
		return field14;
	}

	public void setField14(String field14) {
		this.field14 = field14;
	}

	@Column(name = "field15", length=200)
	public String getField15() {
		return field15;
	}

	public void setField15(String field15) {
		this.field15 = field15;
	}

	@Column(name = "field16", length=200)
	public String getField16() {
		return field16;
	}

	public void setField16(String field16) {
		this.field16 = field16;
	}

	@Column(name = "field17", length=200)
	public String getField17() {
		return field17;
	}

	public void setField17(String field17) {
		this.field17 = field17;
	}

	@Column(name = "field18", length=200)
	public String getField18() {
		return field18;
	}

	public void setField18(String field18) {
		this.field18 = field18;
	}

	@Column(name = "field19", length=200)
	public String getField19() {
		return field19;
	}

	public void setField19(String field19) {
		this.field19 = field19;
	}

	@Column(name = "field20", length=200)
	public String getField20() {
		return field20;
	}

	public void setField20(String field20) {
		this.field20 = field20;
	}

	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="book_details_id", updatable=false)
	@Fetch(value=FetchMode.SUBSELECT)
	@NotFound(action=NotFoundAction.IGNORE) 
	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	@Transient
	public String getUniqueId()
	{
		if (jobId!=null && bookDetailsId!=null)
			return String.format("%s_%d", jobId, bookDetailsId);
		else if (bookDetailsId!=null)
			return bookDetailsId.toString();
		else
			return UUID.randomUUID().toString();
	}
	
	
	@Column(name = "custom_box_die")
	public Boolean getCustomBoxDie() {
		return customBoxDie;
	}

	public void setCustomBoxDie(Boolean customBoxDie) {
		this.customBoxDie = customBoxDie;
	}

	@Column(name = "custom_box_die_url", length=1000)
	public String getCustomBoxDieUrl() {
		return customBoxDieUrl;
	}

	public void setCustomBoxDieUrl(String customBoxDieUrl) {
		this.customBoxDieUrl = customBoxDieUrl;
	}
	
	
	@Transient
	public String getEffectiveShape()
	{
		if (scale!=null && !"100%".equals(scale))
            return IrisUtils.getReducedShapeCode(shape, scale);
		
		return shape;
	}
	
	@Transient
	public String getPrintType() { return this.getField2(); };
	public void setPrintType(String value) { this.setField2(value); };
	
	
	
	public BookDetails() {
		super();
	}

	public BookDetails(BookDetails bd)
	{
		this.bookColour = bd.bookColour;
		this.bookMaterial = bd.bookMaterial;
		this.bookEndPapers = bd.bookEndPapers;
		this.boxStyle = bd.boxStyle;
		this.boxColour = bd.boxColour;
		this.boxMaterial = bd.boxMaterial;
		this.boxRibbon = bd.boxRibbon;
		this.customLogo = bd.customLogo;
		this.studioSample = bd.studioSample;
		this.jobId = bd.jobId;
		this.notes = bd.notes;
		this.scale = bd.scale;
		this.quantity = bd.quantity;
		this.productionDate = bd.productionDate;
		this.comments = bd.comments;
		this.type = bd.type;
		this.priority = bd.priority;
		this.priorityInterval = bd.priorityInterval;
		this.dueDate = bd.dueDate!=null ? new Date( bd.dueDate.getTime() ) : null;
		this.shipDate = bd.shipDate!=null ? new Date( bd.shipDate.getTime() ) : null;
		this.jobName = bd.jobName;
		this.jobComments = bd.jobComments;
		this.spineStyle = bd.spineStyle;
		this.spineMaterial = bd.spineMaterial;
		this.spineColour = bd.spineColour;
		this.linerMaterial = bd.linerMaterial;
		this.linerColour = bd.linerColour;
		this.paperTypeId = bd.paperTypeId;
		this.customDie = bd.customDie;
		this.customDieUrl = bd.customDieUrl;
		this.coverPrintDate = bd.coverPrintDate;
		this.printPages = bd.printPages;
		this.numPrintPages = bd.numPrintPages;
		this.shape = bd.shape;
		this.coverType = bd.coverType;
		this.rate = bd.rate;
		this.category = bd.category;
		this.firstPageType = bd.firstPageType;
		this.field1 = bd.field1;
		this.field2 = bd.field2;
		this.field3 = bd.field3;
		this.field4 = bd.field4;
		this.field5 = bd.field5;
		this.field6 = bd.field6;
		this.field7 = bd.field7;
		this.field8 = bd.field8;
		this.field9 = bd.field9;
		this.field10 = bd.field10;
		this.field11 = bd.field11;
		this.field12 = bd.field12;
		this.field13 = bd.field13;
		this.field14 = bd.field14;
		this.field15 = bd.field15;
		this.field16 = bd.field16;
		this.field17 = bd.field17;
		this.field18 = bd.field18;
		this.field19 = bd.field19;
		this.field20 = bd.field20;
		this.customBoxDie = bd.customBoxDie;
		this.customBoxDieUrl = bd.customBoxDieUrl;
		
		this.stampLines = new ArrayList<StampLine>();
		if (bd.stampLines!=null)
		{
			for(StampLine sl:bd.stampLines)
			{
				this.stampLines.add(new StampLine(sl, this));
			}
		}
		this.attachments = new ArrayList<Attachment>();
		if (bd.attachments!=null)
		{
			for(Attachment a:bd.attachments)
			{
				this.attachments.add(new Attachment(a));
			}
		}
	}
	
}