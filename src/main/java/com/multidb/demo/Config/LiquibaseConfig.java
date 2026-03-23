package com.multidb.demo.Config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
@Configuration
public class LiquibaseConfig
{
    @Bean
    public CommandLineRunner runLiquibase(
            @Qualifier("writeDataSource") DataSource writeDataSource,
            @Qualifier("readDataSource") DataSource readDataSource) {
        return args -> {
            // 1. Run on Primary
            runUpdate(writeDataSource, "Primary");

            // 2. Run on Replica (To ensure tables exist for reading)
            runUpdate(readDataSource, "Replica");
        };
    }

    private void runUpdate(DataSource dataSource, String dbName) {
        try (java.sql.Connection connection = dataSource.getConnection()) {
            liquibase.database.Database database = liquibase.database.DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new liquibase.database.jvm.JdbcConnection(connection));

            liquibase.Liquibase liquibase = new liquibase.Liquibase(
                    "db/changelog/db.changelog-master.xml",
                    new liquibase.resource.ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update(new liquibase.Contexts());
            System.out.println("✅ Liquibase: Migration successful on " + dbName);
        } catch (Exception e) {
            System.err.println("❌ Liquibase: Migration failed on " + dbName + ": " + e.getMessage());
        }
    }
}
