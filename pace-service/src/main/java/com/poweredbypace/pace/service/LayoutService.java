package com.poweredbypace.pace.service;

import java.util.List;

import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.Element;
import com.poweredbypace.pace.domain.layout.FilmStrip;
import com.poweredbypace.pace.domain.layout.ImageElement;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.layout.LayoutSize;


public interface LayoutService {
	
	void syncWithParentLayout(Layout layout, Product product);
	Layout save(Layout layout);
	Layout publishLayout(Layout l);
	void createLayout(Product p);
	Layout resize(Product p, Layout layout, LayoutSize newSize);
	Layout copy(Layout layout);
	Layout getCoverLayout(Product product);
	Layout getEffectiveLayout(Product product);
	Layout getEffectiveCoverLayout(Product product);
	LayoutSize getEffectiveLayoutSize(Product product, Layout layout);
	float getSpineWidth(Product product);	
	float getHingeGap(Product product);
	ImageFile duplicateAndConvert(Layout layout, ImageElement backgroundFrame, Element emptyFrame);
	List<ImageFile> splitImages(List<ImageFile> images);
	
	void checkLayout(Product product);
	void deleteUnusedImages(Layout layout);
	void deleteUnusedImages(FilmStrip filmstrip, List<Layout> layouts);

}
