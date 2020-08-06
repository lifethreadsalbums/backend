package com.poweredbypace.pace.domain.widget;

import com.poweredbypace.pace.domain.PrototypeProductOption.SortType;

@SuppressWarnings("serial")
public class BuildSectionWidget extends Widget {
	
	private SortType sortType;

	public SortType getSortType() {
		return sortType;
	}

	public void setSortType(SortType sortType) {
		this.sortType = sortType;
	}
	
}
