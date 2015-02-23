package io.highway.to.urhell.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "io.highway.to.urhell.dao", repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
//@EnableMongoRepositories("io.highway.to.thunder.mongo")
public class SpringDataConfiguration {

    /*@Value("${mongo.hosts}")
    private List<ServerAddress> mongoHost;
    @Value("${mongo.database}")
    private String mongoDatabase;
    @Value("${mongo.username}")
    private String mongoUsername;
    @Value("${mongo.password}")
    private String mongoPassword;

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        UserCredentials userCredentials = new UserCredentials(mongoUsername, mongoPassword);
        MongoClient client = new MongoClient(mongoHost);
        return new SimpleMongoDbFactory(client, mongoDatabase, userCredentials);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }*/

}
