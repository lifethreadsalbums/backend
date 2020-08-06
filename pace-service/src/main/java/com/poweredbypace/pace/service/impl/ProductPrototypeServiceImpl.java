package com.poweredbypace.pace.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.domain.ProductOptionGroup;
import com.poweredbypace.pace.domain.PrototypeProduct;
import com.poweredbypace.pace.domain.PrototypeProductOption;
import com.poweredbypace.pace.domain.PrototypeProductOptionValue;
import com.poweredbypace.pace.domain.TResource;
import com.poweredbypace.pace.domain.layout.Element;
import com.poweredbypace.pace.domain.layout.ImageElement;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.repository.PrototypeProductRepository;
import com.poweredbypace.pace.service.ProductPrototypeService;

@Service
public class ProductPrototypeServiceImpl implements ProductPrototypeService, ApplicationListener<ContextRefreshedEvent> {
	
	private final Log log = LogFactory.getLog(getClass());

	@Autowired
	private PrototypeProductRepository repo;
	
	@Autowired
	@PersistenceContext(unitName="paceUnit")
	private EntityManager em;
	
	@Autowired
    private CacheManager cacheManager;
	
	private boolean initialized = false;
	
	@Override
	@Cacheable(value="prototypeProduct", key="#id")
	public PrototypeProduct getById(long id) {
		PrototypeProduct prototype = repo.findOne(id);
		lazyLoad(prototype);
		return prototype;
	}

	@Override
	@Cacheable(value="prototypeProduct", key="#code")
	public PrototypeProduct getByCode(String code) {
		PrototypeProduct prototype = repo.findByCode(code);
		lazyLoad(prototype);
		return prototype;
	}

	@Override
	@Cacheable(value="prototypeProduct", key="#store")
	public PrototypeProduct getDefault(Store store) {
		PrototypeProduct prototype = repo.findDefault(store);
		lazyLoad(prototype);
		return prototype;
	}

	
	private void lazyLoad(PrototypeProduct prototype) {
		log.info("Caching prototype "+prototype.getCode());
		lazyLoad(prototype.getDuplicateLabel());
		lazyLoad(prototype.getSingularLabel());
		lazyLoad(prototype.getPluralLabel());
		for(PrototypeProductOption o:prototype.getPrototypeProductOptions()) {
			lazyLoad(o.getGroup());
			lazyLoad(o.getProductOptionType().getGroup());
			lazyLoad(o.getLabel());
			lazyLoad(o.getPrompt());
			lazyLoad(o.getProductOptionType().getResource());
			lazyLoad(o.getProductOptionType().getPrompt());
				
			for (PrototypeProductOptionValue v:o.getPrototypeProductOptionValues()) {
				lazyLoad(v);
			}
			o.getIsRequired();
		}
		for(Store store:prototype.getStores()) {
			lazyLoad(store);
		}
		em.detach(prototype);
	}
	
	private void lazyLoad(PrototypeProductOptionValue v) {
		if (v.getChildren()!=null) {
			for (PrototypeProductOptionValue v2:v.getChildren()) {
				lazyLoad(v2);
			}
		}
		lazyLoad(v.getProductOptionValue().getResource());
		if (v.getCoverType()!=null) {
			v.getCoverType().getCode();
		}
		if (v.getElementPosition()!=null) {
			v.getElementPosition().getVariants().size();
		}
		if (v.getFoil()!=null) {
			v.getFoil().getCode();
		}
		if (v.getLayoutSize()!=null) {
			v.getLayoutSize().getCoverType();
			if (v.getLayoutSize().getTemplateSpread()!=null) {
				for (Element el: v.getLayoutSize().getTemplateSpread().getElements()) {
					if (el instanceof ImageElement) {
						((ImageElement) el).getImageFile();
					}
				}
			};
		}
	}
	
	private void lazyLoad(ProductOptionGroup group) {
		if (group!=null) {
			lazyLoad(group.getLabel());
			lazyLoad(group.getPrompt());
		}
	}
	
	private void lazyLoad(TResource res) {
		if (res!=null) {
			res.getTranslations().size();
		}
	}
	
	private void lazyLoad(Store store) {
		if (store!=null) {
			//store.getCurrencyRules().size();
			store.getAddress();
		}
	}
	
	public void prepopulateCache() {
		log.debug("Prepopulating product prototype cache");
		for(PrototypeProduct p:repo.findAll()) {	
			lazyLoad(p);
			
			cacheManager.getCache("prototypeProduct").put(p.getCode(), p);
			cacheManager.getCache("prototypeProduct").put(p.getId(), p);
			
			if (BooleanUtils.isTrue(p.getIsDefault())) {
				for(Store store:p.getStores()) {
					cacheManager.getCache("prototypeProduct").put(store, p);
				}
			}
		}
		cacheManager.getCache("prototypeProductOptionValue").clear();
		cacheManager.getCache("prototypeProductOption").clear();
		cacheManager.getCache("tresource").clear();
	}
	

	@Override
	@org.springframework.transaction.annotation.Transactional(readOnly=true)
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (initialized) return;
		prepopulateCache();
		initialized = true;
	}
}
