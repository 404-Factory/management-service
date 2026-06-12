package com.factory.management.infrastructure.repository;

import com.factory.management.infrastructure.entity.Defect;
import com.factory.management.infrastructure.repository.support.DefectRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefectRepository extends JpaRepository<Defect, Long>, DefectRepositorySupport {

}