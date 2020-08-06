package com.poweredbypace.pace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.settings.Settings;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.user.User;

public interface SettingsRepository extends JpaRepository<Settings, Long> {
	
	Settings findByUser(User user);
	Settings findByStore(Store store);
	Settings findByProduct(Product product);

}
