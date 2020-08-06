package com.poweredbypace.pace.manager;

import java.math.BigInteger;
import java.util.List;

import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.PrototypeProduct;
import com.poweredbypace.pace.domain.PrototypeProductOption;
import com.poweredbypace.pace.domain.PrototypeProductOptionValue;
import com.poweredbypace.pace.domain.TProductOptionValue;
import com.poweredbypace.pace.domain.layout.Layout;

public interface ProductManager {
	
	PrototypeProduct getPrototype(long id);
	Product createProductFromPrototype(PrototypeProduct prototypeProduct);
	Product createProductFromPrototype(long prototypeId);
	Product getProduct(Long id);
	Layout getLayout(Long id);
	int countImageStampElements(ImageFile imageFile);
	List<BigInteger> findByImageFileId(long id);
	PrototypeProductOptionValue getPrototypeProductOptionValue(long id);
	PrototypeProductOption getPrototypeProductOption(long id);
	TProductOptionValue getProductOptionValueByCode(String code);
}
