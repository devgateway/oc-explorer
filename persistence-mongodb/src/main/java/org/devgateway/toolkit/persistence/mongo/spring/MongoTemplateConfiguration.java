package org.devgateway.toolkit.persistence.mongo.spring;

import javax.annotation.PostConstruct;

import org.devgateway.ocvn.persistence.mongo.ocds.Organization;
import org.devgateway.ocvn.persistence.mongo.ocds.Release;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

@Configuration
public class MongoTemplateConfiguration {

	private final Logger logger = LoggerFactory.getLogger(MongoTemplateConfiguration.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@PostConstruct
	public void addMongoIndex() {
		mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("planning.bidNo", Direction.ASC));
		mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("awards.status", Direction.ASC));
		mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("tender.tenderPeriod.startDate", Direction.ASC));
		mongoTemplate.indexOps(Organization.class).ensureIndex(new Index().on("identifier._id", Direction.ASC));
		mongoTemplate.indexOps(Organization.class).ensureIndex(new Index().on("additionalIdentifiers._id", Direction.ASC));		
		logger.info("Added extra Mongo indexes");
	}

}
