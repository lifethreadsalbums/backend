package com.poweredbypace.pace.domain.layout;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.poweredbypace.pace.domain.BaseEntity;

@Entity
@Table(name="P_FILM_STRIP")
@JsonIgnoreProperties(ignoreUnknown=true)
public class FilmStrip extends BaseEntity {

	private static final long serialVersionUID = 8939393646934833602L;
	
	private Layout layout;
	private List<FilmStripItem> items = new ArrayList<FilmStripItem>();
	private Boolean hasCoverZone;

	@JsonBackReference
	@OneToOne(mappedBy="filmStrip", fetch=FetchType.LAZY,
		cascade = CascadeType.ALL, orphanRemoval = true)
	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "filmStrip",
		cascade = CascadeType.ALL,
		orphanRemoval = true)
	public List<FilmStripItem> getItems() {
		return items;
	}

	public void setItems(List<FilmStripItem> items) {
		this.items = items;
	}

	@Column(name = "HAS_COVER_ZONE", columnDefinition = "TINYINT(1)")
	public Boolean getHasCoverZone() {
		return hasCoverZone;
	}

	public void setHasCoverZone(Boolean hasCoverZone) {
		this.hasCoverZone = hasCoverZone;
	}
	
}
