package com.poweredbypace.pace.domain.layout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.json.BaseEntitySerializer;
import com.poweredbypace.pace.json.SimpleUserSerializer;

@Entity
@Table(name="P_SPREAD_COMMENT")
public class SpreadComment extends BaseEntity {

	private static final long serialVersionUID = -411553595023722415L;
	
	private Layout layout;
	private Integer revision;
	private String spreadId;
	private String elementId;
	private Boolean completed;
	private Boolean isArchived;
	private User user;
	private String text;
	private Date dateCreated;
	private Date dateCompleted;
	private SpreadComment parent;
	private List<SpreadComment> replies = new ArrayList<SpreadComment>();
	private Float positionX;
	private Float positionY;
	private Boolean isRead;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAYOUT_ID")
	@JsonSerialize(using=BaseEntitySerializer.class)
	public Layout getLayout() {
		return layout;
	}
	public void setLayout(Layout layout) {
		this.layout = layout;
	}
	
	@Column(name = "REVISION")
	public Integer getRevision() {
		return revision;
	}
	public void setRevision(Integer revision) {
		this.revision = revision;
	}
	
	@Column(name = "SPREAD_ID")
	public String getSpreadId() {
		return spreadId;
	}
	public void setSpreadId(String spreadId) {
		this.spreadId = spreadId;
	}
	
	public String getElementId() {
		return elementId;
	}
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	@Column(name = "DATE_CREATED")
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	@Column(name = "DATE_COMPLETED")
	public Date getDateCompleted() {
		return dateCompleted;
	}
	public void setDateCompleted(Date dateCompleted) {
		this.dateCompleted = dateCompleted;
	}
	
	@Column(name = "POS_X")
	public Float getPositionX() {
		return positionX;
	}
	public void setPositionX(Float positionX) {
		this.positionX = positionX;
	}
	
	@Column(name = "POS_Y")
	public Float getPositionY() {
		return positionY;
	}
	public void setPositionY(Float positionY) {
		this.positionY = positionY;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	@JsonSerialize(using=SimpleUserSerializer.class)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(name = "COMPLETED", columnDefinition = "TINYINT(1)")
	public Boolean getCompleted() {
		return completed;
	}
	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}
	
	@Column(name = "IS_READ", columnDefinition = "TINYINT(1)")
	public Boolean getIsRead() {
		return isRead;
	}
	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}
	
	@Column(name = "IS_ARCHIVED", columnDefinition = "TINYINT(1)")
	public Boolean getIsArchived() {
		return isArchived;
	}
	public void setIsArchived(Boolean isArchived) {
		this.isArchived = isArchived;
	}
	@Column(name = "COMMENT_TEXT", columnDefinition = "TEXT")
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@JoinColumn(name = "PARENT_ID")
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonSerialize(using=BaseEntitySerializer.class)
	public SpreadComment getParent() {
		return parent;
	}
	public void setParent(SpreadComment parent) {
		this.parent = parent;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent",
		cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SpreadComment> getReplies() {
		return replies;
	}
	public void setReplies(List<SpreadComment> replies) {
		this.replies = replies;
	}
	
}
