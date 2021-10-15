package com.wcf.phoneusage.repositories;

import com.wcf.phoneusage.entities.CellPhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CellPhoneRepository extends JpaRepository<CellPhone, Integer> {
}
