package com.multidb.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        // If @Transactional(readOnly = true), use the "READ" datasource
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        System.out.println("Current Thread: " + Thread.currentThread().getName() +
                " | Is ReadOnly? " + isReadOnly +
                " | Routing to: " + (isReadOnly ? "READ" : "WRITE"));
        return isReadOnly ? "READ" : "WRITE";
    }
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        // 'dataSource' here must be the @Primary LazyConnectionDataSourceProxy bean
        return new DataSourceTransactionManager(dataSource);
    }
}