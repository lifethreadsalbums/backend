package com.poweredbypace.pace.expression.impl;

import org.mozilla.javascript.ScriptableObject;

import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.Product;

public class ImageFileContext extends ProductContext {

	public ImageFileContext(Product product, ImageFile image) {
		super(product);
		this.put("image", new ImageFileObject(image));
	}

	private static final long serialVersionUID = 8426430352049897283L;
	
	private static class ImageFileObject extends ScriptableObject {

		private static final long serialVersionUID = -7392272277023982478L;

		@Override
		public String getClassName() {
			return "ImageFile";
		}
		
		public ImageFileObject(ImageFile image) {
			this.defineProperty("filename", image.getFilename(), READONLY);
			this.defineProperty("isBlackAndWhite", image.getIsBlackAndWhite(), READONLY);
		}
		
	}

}
