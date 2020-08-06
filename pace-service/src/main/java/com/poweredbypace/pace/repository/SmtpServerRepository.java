package com.poweredbypace.pace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.mail.SmtpServer;

public interface SmtpServerRepository extends JpaRepository<SmtpServer,Long>{

}
