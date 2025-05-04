package com.cdc.debezium.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Gaurav Regmi
 */
@Getter
@Setter
@Entity
@Table(name = "BANKXP_CHANGE_DATA")
public class BankxpChangeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TABLE_NAME")
    private String tableName;
    @Column(name = "OPERATION")
    private String operation;
    @Column(name = "CHANGE_CAPTURE")
    private String changeCapture;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private  Date createdDate;
}
