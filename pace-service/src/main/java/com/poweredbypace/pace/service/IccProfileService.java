package com.poweredbypace.pace.service;

import java.io.File;

import com.poweredbypace.pace.domain.IccProfile;
import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.Product;


public interface IccProfileService {
	
	String getProfilesPath();
	File getImage(ImageFile image, IccProfile iccProfile);
	File getImage(ImageFile image, Product product);
	File convert(ImageFile image, IccProfile iccProfile);
	File convert(File rgbFile, IccProfile profile);
	IccProfile getIccProfile(ImageFile image, Product product);
	IccProfile getIccProfile(Product product);
	
}
