package com.poweredbypace.pace.print;

import java.io.File;
import java.util.List;

import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.domain.Product;

public interface BinderyFormGenerator {
	File generate(List<Product> products, JobProgressInfo job);
	File generate(Product product, JobProgressInfo job);
}
