package com.poweredbypace.pace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.layout.ImageStampElement;

public interface ImageStampElementRepository extends JpaRepository<ImageStampElement, Long> {
	
	int countByImageFile(ImageFile imageFile);

}
