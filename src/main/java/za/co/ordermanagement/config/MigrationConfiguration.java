package za.co.ordermanagement.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MigrationConfiguration {
    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String hostUrl;

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        return new Flyway(Flyway
                .configure()
                .baselineOnMigrate(true)
                .dataSource(hostUrl, username, password));
    }
}
