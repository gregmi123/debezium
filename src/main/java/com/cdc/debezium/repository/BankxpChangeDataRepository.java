package com.cdc.debezium.repository;

import com.cdc.debezium.model.BankxpChangeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Gaurav Regmi
 */
@Repository
public interface BankxpChangeDataRepository extends JpaRepository<BankxpChangeData, Long> {
}
