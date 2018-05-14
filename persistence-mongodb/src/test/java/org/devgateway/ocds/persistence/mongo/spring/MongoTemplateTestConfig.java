package org.devgateway.ocds.persistence.mongo.spring;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientOptions;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongoCmdOptionsBuilder;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Feature;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import org.devgateway.toolkit.persistence.mongo.spring.MongoTemplateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.EnumSet;

/**
 * Created by mpostelnicu on 6/12/17.
 */
@Configuration
@Profile("integration")
public class MongoTemplateTestConfig {

    public enum OCEMongoVersion implements IFeatureAwareVersion {

        V3_4_11("3.4.11", Feature.SYNC_DELAY, Feature.STORAGE_ENGINE);

        private final String specificVersion;
        private EnumSet<Feature> features;

        OCEMongoVersion(String vName, Feature... features) {
            this.specificVersion = vName;
            this.features = Feature.asSet(features);
        }

        @Override
        public String asInDownloadPath() {
            return specificVersion;
        }

        @Override
        public boolean enabled(Feature feature) {
            return features.contains(feature);
        }

        @Override
        public String toString() {
            return "Version{" + specificVersion + '}';
        }

    }


    @Autowired
    private MongoProperties properties;

    @Autowired
    private CustomConversions customConversions;

    @Autowired
    private Environment environment;

    @Autowired(required = false)
    private MongoClientOptions options;

    private String originalUri;

    @Bean(destroyMethod = "stop")
    public MongodProcess mongodProcess(MongodExecutable embeddedMongoServer) throws IOException {
        return embeddedMongoServer.start();
    }

    @Bean(destroyMethod = "stop")
    public MongodExecutable embeddedMongoServer(MongodStarter mongodStarter, IMongodConfig iMongodConfig)
            throws IOException {
        return mongodStarter.prepare(iMongodConfig);
    }

    @Bean
    public IMongodConfig mongodConfig() throws IOException {
        return new MongodConfigBuilder().version(OCEMongoVersion.V3_4_11)
                .cmdOptions(new MongoCmdOptionsBuilder().useNoJournal(true)
                        .build())
                .build();
    }

    @Bean
    public MongodStarter mongodStarter() {
        return MongodStarter.getDefaultInstance();
    }


    @Bean(name = "mongoTemplate")
    public MongoTemplate mongoTemplate(MongodProcess mongodProcess) throws Exception {
        Net net = mongodProcess.getConfig().net();
        properties.setHost(net.getServerAddress().getHostName());
        properties.setPort(net.getPort());
        properties.setDatabase(originalUri);
        properties.setUri(null);

        MongoTemplate template = new MongoTemplate(
                new SimpleMongoDbFactory(properties.createMongoClient(this.options, environment),
                        properties.getDatabase()));
        ((MappingMongoConverter) template.getConverter()).setCustomConversions(customConversions);
        return template;
    }

    @PostConstruct
    public void postConstruct() {
        //set uri string
        originalUri = new ConnectionString(properties.getUri()).getDatabase();
    }

    /**
     * Creates a shadow template configuration by adding "-shadow" as postfix of database name.
     * This is used to replicate the entire database structure in a shadow/temporary database location
     *
     * @return
     * @throws Exception
     */
    @Bean(name = "shadowMongoTemplate")
    public MongoTemplate shadowMongoTemplate(MongodProcess mongodProcess) throws Exception {
        Net net = mongodProcess.getConfig().net();
        properties.setHost(net.getServerAddress().getHostName());
        properties.setPort(net.getPort());
        properties.setDatabase(originalUri + MongoTemplateConfig.SHADOW_POSTFIX);
        properties.setUri(null);
        MongoTemplate template = new MongoTemplate(
                new SimpleMongoDbFactory(properties.createMongoClient(this.options, environment),
                        properties.getDatabase()));
        ((MappingMongoConverter) template.getConverter()).setCustomConversions(customConversions);
        return template;
    }
}
