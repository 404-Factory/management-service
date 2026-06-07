package com.factory.management.infrastructure.repository;

import com.factory.management.infrastructure.entity.Defect;
import com.factory.management.infrastructure.repository.support.DefectRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DefectRepository extends JpaRepository<Defect, Long>, DefectRepositorySupport {

}