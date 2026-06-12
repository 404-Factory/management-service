package com.factory.management.infrastructure.repository;

import com.factory.management.infrastructure.entity.Process;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessRepository extends JpaRepository<Process, Long> {

}
