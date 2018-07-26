package com.security.data.audit.dao;

import com.security.data.audit.model.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<UserFile, UUID> {
}
