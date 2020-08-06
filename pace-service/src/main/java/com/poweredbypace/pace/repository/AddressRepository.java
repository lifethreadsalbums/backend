package com.poweredbypace.pace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
