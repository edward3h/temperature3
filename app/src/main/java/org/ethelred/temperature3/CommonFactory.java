package org.ethelred.temperature3;

import io.avaje.http.client.HttpClient;
import io.avaje.http.client.JsonbBodyAdapter;
import io.avaje.inject.Bean;
import io.avaje.inject.Factory;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import jakarta.inject.Singleton;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.ethelred.temperature3.templates.StaticTemplates;
import org.ethelred.temperature3.templates.Templates;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.mariadb.jdbc.MariaDbDataSource;
import org.pkl.config.java.ConfigEvaluatorBuilder;
import org.pkl.core.ModuleSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Factory
public class CommonFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonFactory.class);

    @Singleton
    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Singleton
    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure().ignoreIfMissing().load();
    }

    @Singleton
    @Bean
    public Configuration configuration(Dotenv dotenv) {
        var dotenvMap = dotenv.entries().stream().collect(Collectors.toMap(DotenvEntry::getKey, DotenvEntry::getValue));
        var profile = dotenvMap.getOrDefault("PROFILE", "dev");
        try (var eval = ConfigEvaluatorBuilder.preconfigured()
                .addEnvironmentVariables(dotenvMap)
                .build()) {
            return eval.evaluate(ModuleSource.modulePath(profile + ".pkl")).as(Configuration.class);
        }
    }

    @Singleton
    @Bean
    public SensorsAPI sensorsAPI(Configuration configuration) {
        return HttpClient.builder()
                .baseUrl(configuration.getSensors().getUrl().toString())
                .bodyAdapter(new JsonbBodyAdapter())
                .build()
                .create(SensorsAPI.class);
    }

    @Singleton
    @Bean
    public KumoJsAPI kumoJsAPI(Configuration configuration) {
        return HttpClient.builder()
                .baseUrl(configuration.getKumojs().getUrl().toString())
                .bodyAdapter(new JsonbBodyAdapter())
                .build()
                .create(KumoJsAPI.class);
    }

    @Bean
    public DataSource getDataSource(Configuration configuration) {
        var url = configuration.getDatasource().getUrl();
        MariaDbDataSource dataSource;
        try {
            dataSource = new MariaDbDataSource(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataSource;
    }

    private void runLiquibaseUpdate(DataSource dataSource) {
        try (var connection = dataSource.getConnection()) {
            var lc = new JdbcConnection(connection);
            var ldb = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(lc);
            var liquibase = new Liquibase("db/liquibase-changelog.yaml", new ClassLoaderResourceAccessor(), ldb);
            //noinspection deprecation
            liquibase.update();
        } catch (SQLException | LiquibaseException e) {
            LOGGER.error("Failed to run Liquibase update", e);
        }
    }

    @Bean
    public Jdbi getJdbi(DataSource dataSource) {
        runLiquibaseUpdate(dataSource);
        var jdbi = Jdbi.create(dataSource);
        jdbi.installPlugin(new SqlObjectPlugin());
        return jdbi;
    }

    @Bean
    public ReadingsDao readingsDao(Jdbi jdbi) {
        return jdbi.onDemand(ReadingsDao.class);
    }

    @Bean
    public Templates templates() {
        return new StaticTemplates();
    }
}
