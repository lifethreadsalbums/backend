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
import com.poweredbypace.pace.repository.IccProfileRepository;
import com.poweredbypace.pace.repository.ImageFileRepository;
import com.poweredbypace.pace.repository.ProductRepository;
import com.poweredbypace.pace.service.IccProfileService;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RegenerateImagesTaskImpl extends AbstractTask implements RegenerateImagesTask {
	
	private  final Log log = LogFactory.getLog(getClass());
	
	public static class Params {
		public Long[] ids;
		public Long iccProfileId;
		public Long productId;
	}
	
	@Autowired
	private IccProfileService iccProfileService;
	
	@Autowired
	private IccProfileRepository iccProfileRepo;
	
	@Autowired
	private ImageFileRepository imageFileRepo;
	
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
		if (params.iccProfileId!=null) {
			IccProfile iccProfile = iccProfileRepo.getOne(params.iccProfileId);
			log.info("Regenerating images using " + iccProfile.getProfile());
			for(Long id: params.ids) {
				ImageFile imageFile = imageFileRepo.getOne(id);
				imageFile.setCustomIccProfile(iccProfile);
				log.info("Regenerating image " + imageFile.getFilename());
				File file = iccProfileService.convert(imageFile, iccProfile);
				file.delete();
			}
		} else if (params.productId!=null) {
			Product product = productRepo.getOne(params.productId);
			log.info("Regenerating images for " + product.getName());
			
			for(Long id: params.ids) {
				ImageFile imageFile = imageFileRepo.getOne(id);
				imageFile.setCustomIccProfile(null);
				IccProfile iccProfile = iccProfileService.getIccProfile(imageFile, product);
				log.info("Regenerating image " + imageFile.getFilename() + " using "+iccProfile.getLabel());
				File file = iccProfileService.convert(imageFile, iccProfile);
				file.delete();
			}
		}
		log.info("Done");
	}

}
