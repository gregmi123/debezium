package com.cdc.debezium.constant;

/**
 * @author Gaurav Regmi
 */
public class DbziumConstant {
    /**
     * Format "dbname1.tablename1,dbname2.tablename2"
     * To exclude the changes of database tables
     */
    public static final String BLACKLIST_TABLES = "";// e.g "customer.abc,customer.info"
    /**
     * Format "dbname1.tablename1,dbname2.tablename2"
     * To include only the changes of these database tables
     */
    public static final String WHITELIST_TABLES = "bankxp.SYSTEM_CONFIG";// e.g "customer.abc,customer.info"

    public interface DbHistoryFile {
        String PREFIX = "dbhistory_";
        String SUFFIX = ".dat";
    }

    public interface OffsetStorageFile {
        String PREFIX = "offsets_";
        String SUFFIX = ".dat";
    }

    public interface Configuration {
        String DB_NAME = "bankxp";
        String DB_HOSTNAME = "10.13.194.238";
        String DB_PORT = "3307";
        String DB_USER = "root";
        String DB_PASSWORD = "f1s0ft@123";

        String DB_SERVER_ID = "184756";
        String DB_SERVER_NAME = "bankxp-cdc";

        String CONNECTOR_NAME = "mysqlConnector";
        String PLUGIN_NAME = "mysqlOutput";
        int OFFSET_FLUSH_INTERVAL = 60000;
    }
}
