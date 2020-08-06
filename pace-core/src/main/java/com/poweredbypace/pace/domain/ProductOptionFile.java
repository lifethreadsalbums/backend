package com.poweredbypace.pace.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "P_PRODUCT_OPTION_FILE")
public class ProductOptionFile extends ProductOption<File> {

	private static final long serialVersionUID = 7374235254914614850L;

	private File file;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="FILE_ID", nullable=true)
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Transient
	public File getValue() {
		return getFile();
	}

	public void setValue(File value) {
		setFile(value);
		
	}
}
