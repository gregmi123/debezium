package com.cdc.debezium.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

/**
 * @author Gaurav Regmi
 */
@Getter
@Setter
@ToString
public class ChangeDataDTO {
    private String table;
    private String operation;
    private HashMap<String, Object> message;
}
