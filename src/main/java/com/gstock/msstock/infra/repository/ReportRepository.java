package com.gstock.msstock.infra.repository;

import com.gstock.msstock.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
