package com.cdc.debezium.listener;

import com.cdc.debezium.dto.ChangeDataDTO;
import com.cdc.debezium.util.Operation;
import io.debezium.config.Configuration;
import io.debezium.embedded.EmbeddedEngine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.Queue;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static io.debezium.data.Envelope.FieldName.*;
import static java.util.stream.Collectors.toMap;

/**
 * @author Gaurav Regmi
 */
@Slf4j
@Component
public class CDCListener {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Queue queue;

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final EmbeddedEngine engine;

    private CDCListener(Configuration studentConnector) {
        this.engine = EmbeddedEngine
                .create()
                .using(studentConnector)
                .notifying(this::handleEvent).build();
    }

    @PostConstruct
    private void start() throws InterruptedException {
        this.executor.execute(engine);
    }

    private void handleEvent(SourceRecord sourceRecord) {
        Struct sourceRecordValue = (Struct) sourceRecord.value();

        try {
            if (sourceRecordValue != null) {
                Operation operation = Operation.forCode((String) sourceRecordValue.get(OPERATION));

                //Only if this is a transactional operation.
                if (operation != Operation.READ) {

                    Map<String, Object> message;
                    String record = AFTER; //For Update & Insert operations.

                    if (operation == Operation.DELETE) {
                        record = BEFORE; //For Delete operations.
                    }

                    //Build a map with all row data received.
                    Struct struct = (Struct) sourceRecordValue.get(record);
                    String tableName = Arrays.asList(struct.schema().name().split("\\.")).get(2);

                    message = struct.schema().fields().stream()
                            .map(Field::name)
                            .filter(fieldName -> struct.get(fieldName) != null)
                            .map(fieldName -> Pair.of(fieldName, struct.get(fieldName)))
                            .collect(toMap(Pair::getKey, Pair::getValue));

                    ChangeDataDTO changeDataDTO = new ChangeDataDTO();
                    changeDataDTO.setMessage((HashMap<String, Object>) message);
                    changeDataDTO.setOperation(operation.name());
                    changeDataDTO.setTable(tableName);
                    //Call the service to handle the data change.

                    jmsTemplate.convertAndSend(queue, changeDataDTO);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
