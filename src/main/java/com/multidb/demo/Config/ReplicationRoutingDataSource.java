package com.multidb.demo.Config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        // Return "MASTER" for writes, "SLAVE" for read-only
        return TransactionSynchronizationManager.isCurrentTransactionReadOnly()
                ? "SLAVE" : "MASTER";
    }
}
