package org.devgateway.toolkit.persistence.mongo.spring;

import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.devgateway.ocvn.persistence.mongo.ocds.Release;
import org.devgateway.toolkit.persistence.mongo.dao.VNOrganization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ScriptOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.TextIndexDefinition.TextIndexDefinitionBuilder;
import org.springframework.data.mongodb.core.script.ExecutableMongoScript;
import org.springframework.data.mongodb.core.script.NamedMongoScript;

@Configuration
public class MongoTemplateConfiguration {

	private final Logger logger = LoggerFactory.getLogger(MongoTemplateConfiguration.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@PostConstruct
	public void mongoPostInit() {
		mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("planning.bidNo", Direction.ASC));
		mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("awards.status", Direction.ASC));
		mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("tender.tenderPeriod.startDate", Direction.ASC));
		mongoTemplate.indexOps(VNOrganization.class).ensureIndex(new Index().on("identifier._id", Direction.ASC));
		mongoTemplate.indexOps(VNOrganization.class).ensureIndex(new Index().on("additionalIdentifiers._id", Direction.ASC));
		mongoTemplate.indexOps(VNOrganization.class)
				.ensureIndex(new TextIndexDefinitionBuilder().onField("name").onField("id").build());
		logger.info("Added extra Mongo indexes");
		
		
		ScriptOperations scriptOps = mongoTemplate.scriptOps();

		URL scriptFile = getClass().getResource("/tenderBidPeriodPercentilesMongo.js");
		try {
			String scriptText = IOUtils.toString(scriptFile);
			ExecutableMongoScript script = new ExecutableMongoScript(scriptText);
			scriptOps.register(new NamedMongoScript("tenderBidPeriodPercentiles", script));
		} catch (IOException e) {
			e.printStackTrace();
		}

		
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
