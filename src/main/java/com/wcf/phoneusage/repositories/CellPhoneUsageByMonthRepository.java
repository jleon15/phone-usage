package com.wcf.phoneusage.repositories;

import com.wcf.phoneusage.entities.CellPhoneUsageByMonth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CellPhoneUsageByMonthRepository extends JpaRepository<CellPhoneUsageByMonth, Integer> {
}
