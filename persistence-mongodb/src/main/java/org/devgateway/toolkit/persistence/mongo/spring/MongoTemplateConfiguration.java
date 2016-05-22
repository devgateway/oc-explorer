package org.devgateway.toolkit.persistence.mongo.spring;

import org.apache.commons.io.IOUtils;
import org.devgateway.ocds.persistence.mongo.Location;
import org.devgateway.ocds.persistence.mongo.Release;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ScriptOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.script.ExecutableMongoScript;
import org.springframework.data.mongodb.core.script.NamedMongoScript;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;

@Configuration
public class MongoTemplateConfiguration {

    private final Logger logger = LoggerFactory.getLogger(MongoTemplateConfiguration.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void createMandatoryImportIndexes() {
        mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("planning.budget.projectID", Direction.ASC));
        mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("planning.bidNo", Direction.ASC));
        mongoTemplate.indexOps(Location.class).ensureIndex(new Index().on("description", Direction.ASC));

    }

    @PostConstruct
    public void mongoPostInit() {
        createMandatoryImportIndexes();
        createPostImportStructures();
    }

    public void createPostImportStructures() {

        // initialize some extra indexes
        mongoTemplate.indexOps(Release.class)
                .ensureIndex(new Index().on("planning.bidPlanProjectDateApprove", Direction.ASC));
        mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("ocid", Direction.ASC));

        mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("tender.procurementMethod", Direction.ASC));
        mongoTemplate.indexOps(Release.class)
                .ensureIndex(new Index().on("tender.procurementMethodRationale", Direction.ASC));
        mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("tender.status", Direction.ASC));
        mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("awards.status", Direction.ASC));
        mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("awards.date", Direction.ASC));
        mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("awards.value.amount", Direction.ASC));
        mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("tender.value.amount", Direction.ASC));
        mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("tender.contrMethod._id", Direction.ASC));
        mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("tender.contrMethod.details", Direction.ASC));
        mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("tender.numberOfTenderers", Direction.ASC));
        mongoTemplate.indexOps(Release.class)
                .ensureIndex(new Index().on("tender.tenderPeriod.startDate", Direction.ASC));
        mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("tender.tenderPeriod.endDate", Direction.ASC));
        mongoTemplate.indexOps(Release.class)
                .ensureIndex(new Index().on("tender.items.classification._id", Direction.ASC));

        logger.info("Added extra Mongo indexes");

        ScriptOperations scriptOps = mongoTemplate.scriptOps();

        // add script to calculate the percentiles endpoint
        URL scriptFile = getClass().getResource("/tenderBidPeriodPercentilesMongo.js");
        try {
            String scriptText = IOUtils.toString(scriptFile);
            ExecutableMongoScript script = new ExecutableMongoScript(scriptText);
            scriptOps.register(new NamedMongoScript("tenderBidPeriodPercentiles", script));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // add general mongo system helper methods
        URL systemScriptFile = getClass().getResource("/mongoSystemScripts.js");
        try {
            String systemScriptFileText = IOUtils.toString(systemScriptFile);
            ExecutableMongoScript script = new ExecutableMongoScript(systemScriptFileText);
            scriptOps.execute(script);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
