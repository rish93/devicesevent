//package com.health.devicesevent.configuration;
//
//import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.SQLException;
//
//@Configuration
//class TenantConnectionProvider implements MultiTenantConnectionProvider {
//
//    private static Logger log = LoggerFactory.getLogger(TenantConnectionProvider.class);
//    private String DEFAULT_TENANT = "public";
//    private DataSource datasource;
//
//    public TenantConnectionProvider(DataSource dataSource) {
//        this.datasource = dataSource;
//    }
//
//    @Override
//    public boolean isUnwrappableAs(Class unwrapType) {
//        return false;
//    }
//
//    @Override
//    public <T> T unwrap(Class<T> unwrapType) {
//        return null;
//    }
//
//    @Override
//    public Connection getAnyConnection() throws SQLException {
//        return datasource.getConnection();
//    }
//
//    @Override
//    public void releaseAnyConnection(Connection connection) throws SQLException {
//        connection.close();
//    }
//
//    @Override
//    public Connection getConnection(Object tenantIdentifier) throws SQLException {
//        log.info("Get connection for tenant {}", tenantIdentifier);
//        final Connection connection = getAnyConnection();
//        connection.setSchema(tenantIdentifier.toString());
//        return connection;
//    }
//
//    @Override
//    public void releaseConnection(Object tenantIdentifier, Connection connection) throws SQLException {
//        log.info("Release connection for tenant {}", tenantIdentifier);
//        connection.setSchema(DEFAULT_TENANT);
//        releaseAnyConnection(connection);
//    }
//
//    @Override
//    public boolean supportsAggressiveRelease() {
//        return false;
//    }
//
//}