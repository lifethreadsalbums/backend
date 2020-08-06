package com.poweredbypace.pace.print;

import java.io.File;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.TextStampElement;
import com.poweredbypace.pace.event.ProgressListener;
import com.poweredbypace.pace.exception.PrintGenerationException;

public interface LayoutPrintGenerator {

	File generateDie(Product product, TextStampElement element, ProgressListener progressListener) throws InterruptedException, PrintGenerationException; 
	File generateCameos(Product product, ProgressListener progressListener) throws InterruptedException, PrintGenerationException;
	File generateAlbum(Product product, ProgressListener progressListener) throws InterruptedException, PrintGenerationException;
	File generateAlbumPreview(Product product, ProgressListener progressListener) throws InterruptedException, PrintGenerationException;
	File generateCover(Product product, ProgressListener progressListener) throws InterruptedException, PrintGenerationException;;
	
}
