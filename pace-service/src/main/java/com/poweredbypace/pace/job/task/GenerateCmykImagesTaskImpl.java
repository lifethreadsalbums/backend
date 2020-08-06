package com.poweredbypace.pace.job.task;

import java.io.File;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.domain.IccProfile;
import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.FilmStripImageItem;
import com.poweredbypace.pace.domain.layout.FilmStripItem;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.repository.ProductRepository;
import com.poweredbypace.pace.service.IccProfileService;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GenerateCmykImagesTaskImpl extends AbstractTask implements GenerateCmykImagesTask {
	
	private  final Log log = LogFactory.getLog(getClass());
	
	public static class Params {
		public Long productId;
	}
	
	@Autowired
	private IccProfileService iccProfileService;
	
	@Autowired
	private ProductRepository productRepo;
	
	
	@Override
	public int getTimeout() {
		return 60 * 60 * 2; //2h
	}

	@Override
	@Transactional(value=TxType.REQUIRES_NEW)
	public void run() {
		Params params = (Params) job.getParams();
		
		Product product = productRepo.getOne(params.productId);
		Layout layout = product.getLayout();
		if (layout!=null) {
			log.info("Regenerating images for " + product.getName());
			for(FilmStripItem item:layout.getFilmStrip().getItems()) {
				if (item instanceof FilmStripImageItem) {
				 	ImageFile imageFile = ((FilmStripImageItem)item).getImage();
				 	IccProfile iccProfile = iccProfileService.getIccProfile(imageFile, product);
					log.info("Regenerating image " + imageFile.getFilename() + " using "+iccProfile.getLabel());
					try {
						File file = iccProfileService.convert(imageFile, iccProfile);
						file.delete();
					} catch(Exception ex) {
						log.error("Cannot convert image " + imageFile.getFilename() + ", ID="+imageFile.getId());
					}
				}
			}
			log.info("Regenerating images for " + product.getName()+ " completed.");
		}
	}

}
