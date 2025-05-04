package com.cdc.debezium.config;

import com.cdc.debezium.constant.DbziumConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

/**
 * @author Gaurav Regmi
 */
@Configuration
public class DebeziumConnectorConfig {

    @Bean
    public io.debezium.config.Configuration studentConnector() throws IOException {
        File offsetStorageTempFile = File.createTempFile(DbziumConstant.OffsetStorageFile.PREFIX,
                DbziumConstant.OffsetStorageFile.SUFFIX);
        File dbHistoryTempFile = File.createTempFile(DbziumConstant.DbHistoryFile.PREFIX,
                DbziumConstant.DbHistoryFile.SUFFIX);

        return io.debezium.config.Configuration.create()
                .with("connector.class", "io.debezium.connector.mysql.MySqlConnector")
                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", offsetStorageTempFile)
                .with("offset.flush.interval.ms", DbziumConstant.Configuration.OFFSET_FLUSH_INTERVAL)
                .with("plugin.name",DbziumConstant.Configuration.PLUGIN_NAME)
                .with("name", DbziumConstant.Configuration.CONNECTOR_NAME)
                .with("database.server.name", DbziumConstant.Configuration.DB_SERVER_NAME)
                .with("database.server.id", DbziumConstant.Configuration.DB_SERVER_ID)
                .with("database.hostname", DbziumConstant.Configuration.DB_HOSTNAME)
                .with("database.port", DbziumConstant.Configuration.DB_PORT)
                .with("database.user", DbziumConstant.Configuration.DB_USER)
                .with("database.password", DbziumConstant.Configuration.DB_PASSWORD)
                .with("database.dbname", DbziumConstant.Configuration.DB_NAME)
                .with("table.whitelist", DbziumConstant.WHITELIST_TABLES)
                .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
                .with("database.history.file.filename", dbHistoryTempFile)
                .build();
    }
}
