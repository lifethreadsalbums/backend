package com.poweredbypace.pace.service;

import java.io.File;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.exception.PrintGenerationException;

public interface DieService {

	File generateDie(Product product, String optionCode) throws PrintGenerationException;
	File generateDieScreenshot(Product product, String optionCode) throws PrintGenerationException;
	
}
