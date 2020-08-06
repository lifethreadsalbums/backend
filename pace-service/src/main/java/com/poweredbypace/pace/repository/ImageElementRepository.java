package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.layout.ImageElement;

public interface ImageElementRepository extends JpaRepository<ImageElement, Long> {
	
	List<ImageElement> findByImageFile(ImageFile imageFile);

}
