package com.cdc.debezium.listener;

import com.cdc.debezium.dto.ChangeDataDTO;
import com.cdc.debezium.model.BankxpChangeData;
import com.cdc.debezium.repository.BankxpChangeDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Gaurav Regmi
 */
@Slf4j
@Component
public class JmsListener {

    @Autowired
    private BankxpChangeDataRepository bankxpChangeDataRepository;

    @org.springframework.jms.annotation.JmsListener(destination = "dbzium.bankxp.queue", containerFactory = "messageFactory")
    public void consume(ChangeDataDTO changeDataDTO) {
        try {
            log.info("Data Changed: {} with Operation: {} in table {}", changeDataDTO.getMessage(),
                    changeDataDTO.getOperation(), changeDataDTO.getTable());

            BankxpChangeData bankxpChangeData = new BankxpChangeData();
            bankxpChangeData.setTableName(changeDataDTO.getTable());
            bankxpChangeData.setOperation(changeDataDTO.getOperation());
            bankxpChangeData.setChangeCapture(changeDataDTO.getMessage().toString());
            bankxpChangeData.setCreatedDate(new Date());

            bankxpChangeDataRepository.save(bankxpChangeData);
        } catch (Exception e) {
            log.error("Error : ", e);
        }
    }
}
