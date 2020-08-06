package com.poweredbypace.pace.service;

import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.exception.PrintGenerationException;
import com.poweredbypace.pace.print.OutputType;

public interface PrintProductionService {
	
	void generateDies(long productId, OutputType outputType, JobProgressInfo jobInfo) throws PrintGenerationException;
	void generateDies(Product product, OutputType outputType, JobProgressInfo jobInfo) throws PrintGenerationException;
	
	void generateCameos(long productId, OutputType outputType, JobProgressInfo jobInfo) throws InterruptedException, PrintGenerationException;
	void generateAlbum(long productId, OutputType outputType, JobProgressInfo jobInfo) throws InterruptedException, PrintGenerationException;
	void generateCover(long productId, OutputType outputType, JobProgressInfo jobInfo) throws InterruptedException, PrintGenerationException;
	void generateAlbumPreview(long productId, OutputType outputType, JobProgressInfo jobInfo) throws InterruptedException, PrintGenerationException;
	
	void generateCameos(Product product, OutputType outputType, JobProgressInfo jobInfo) throws InterruptedException, PrintGenerationException;
	void generateAlbum(Product product, OutputType outputType, JobProgressInfo jobInfo) throws InterruptedException, PrintGenerationException;
	void generateAlbumPreview(Product product, OutputType outputType, JobProgressInfo jobInfo) throws InterruptedException, PrintGenerationException;
	void generateCover(Product product, OutputType outputType, JobProgressInfo jobInfo) throws InterruptedException, PrintGenerationException;
	
}
