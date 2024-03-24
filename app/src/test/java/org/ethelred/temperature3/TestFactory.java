package org.ethelred.temperature3;

import io.avaje.inject.Bean;
import io.avaje.inject.Factory;
import io.avaje.inject.test.TestScope;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;

@TestScope
@Factory
public class TestFactory {
    @Bean
    public DataSource getTestDataSource() {
        var ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test;MODE=MariaDB;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1");
        return ds;
    }
}
