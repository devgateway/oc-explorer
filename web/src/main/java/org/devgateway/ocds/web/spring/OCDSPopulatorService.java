package org.devgateway.ocds.web.spring;

import com.mongodb.DBObject;
import org.apache.log4j.Logger;
import org.devgateway.ocds.persistence.mongo.Classification;
import org.devgateway.ocds.persistence.mongo.FlaggedRelease;
import org.devgateway.ocds.persistence.mongo.Identifiable;
import org.devgateway.ocds.persistence.mongo.Organization;
import org.devgateway.ocds.persistence.mongo.repository.main.ClassificationRepository;
import org.devgateway.ocds.persistence.mongo.repository.main.FlaggedReleaseRepository;
import org.devgateway.ocds.persistence.mongo.repository.main.OrganizationRepository;
import org.devgateway.toolkit.persistence.mongo.spring.MongoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Created by mpostelnicu on 10-May-17.
 */

@Service
public class OCDSPopulatorService {

    private ConcurrentHashMap<String, AtomicInteger> idxGen;

    private ConcurrentHashMap<String, String> orgNameId;

    protected static Logger logger = Logger.getLogger(OCDSPopulatorService.class);
    @Autowired
    private FlaggedReleaseRepository releaseRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private ClassificationRepository classificationRepository;

    @Autowired
    protected MongoTemplate mongoTemplate;

    public void logMessage(String message) {
        logger.info(message);
    }

    public void randomizeOrganizations(Consumer<String> logMessage) {
        logMessage.accept("<b>RANDOMIZE ORGS.</b>");


        mongoTemplate.createCollection("tmporg");

        MongoUtil.processRepositoryItemsPaginated(organizationRepository, this::randomizeOrganization,
                this::logMessage
        );

        mongoTemplate.dropCollection("organization");
        mongoTemplate.aggregate(Aggregation.newAggregation(Aggregation.out("organization")),
                "tmporg", DBObject.class
        );
        mongoTemplate.dropCollection("tmporg");

        logMessage.accept("<b>RANDOMIZE ORGS COMPLETED.</b>");
    }


    public void randomizeReleases(Consumer<String> logMessage) {
        logMessage.accept("<b>RANDOMIZE RELEASES.</b>");

        MongoUtil.processRepositoryItemsPaginated(releaseRepository, this::randomizeRelease,
                this::logMessage
        );

        logMessage.accept("<b>RANDOMIZE RELEASES COMPLETED.</b>");
    }

    public void randomizeClassifications(Consumer<String> logMessage) {
        logMessage.accept("<b>RANDOMIZE CLASSIFICATIONS.</b>");

        MongoUtil.processRepositoryItemsPaginated(classificationRepository, this::randomizeClassification,
                this::logMessage
        );

        logMessage.accept("<b>RANDOMIZE CLASSIFICATIONS COMPLETED.</b>");
    }


    public void randomizeOrganization(Organization o) {
        o.setName(getIdxName("Organization"));
        String oldId = o.getId();
        orgNameId.put(oldId, o.getName());
        o.setId(o.getName());
        o.getIdentifier().setId(o.getId());
        o.getAdditionalIdentifiers().stream().filter(i -> i.getId().equals(oldId)).findFirst().get().setId(o.getId());

        if (o.getAddress() != null) {
            o.getAddress().setCountryName(getIdxName("Country"));
            o.getAddress().setLocality(getIdxName("Locality"));
            o.getAddress().setPostalCode(getIdxName("Postal Code"));
            o.getAddress().setRegion(getIdxName("Region"));
            o.getAddress().setStreetAddress(getIdxName("Street"));
        }
        if (o.getContactPoint() != null) {
            o.getContactPoint().setEmail("sample@sample.abc");
            o.getContactPoint().setFaxNumber("Fax");
            o.getContactPoint().setName(getIdxName("Contact"));
            o.getContactPoint().setTelephone("Phone");
            try {
                o.getContactPoint().setUrl(new URI("http://sample.abc"));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        mongoTemplate.save(o, "tmporg");
    }

    public void randomizeClassification(Classification c) {
        c.setDescription(getIdxName("Classification"));
        classificationRepository.save(c);
    }

    public <T extends Identifiable, ID extends Serializable> T getSavedOrgEntityFromEntity(T t,
                                                                                           MongoRepository<T, ID>
                                                                                                   repository) {
        T newOrg = repository.findOne((ID) orgNameId.get((String) t.getIdProperty()));
        if (newOrg == null) {
            throw new RuntimeException("An unidentified element was used inline");
        }
        return newOrg;
    }

    public <T extends Identifiable, ID extends Serializable> T getSavedEntityFromEntity(T t,
                                                                                        MongoRepository<T, ID>
                                                                                                repository) {
        T newOrg = repository.findOne((ID) t.getIdProperty());
        if (newOrg == null) {
            throw new RuntimeException("An unidentified element was used inline");
        }
        return newOrg;
    }


    public <T extends Identifiable, ID extends Serializable>
    void replaceOrgEntitiesWithSavedEntities(Collection<T> c,
                                             MongoRepository<T, ID> repository) {
        Iterator<T> i = c.iterator();
        while (i.hasNext()) {
            T o = i.next();
            i.remove();
            c.add(getSavedOrgEntityFromEntity(o, repository));
        }
    }

    public void randomizeRelease(FlaggedRelease r) {
        r.setOcid(getIdxName("ocid-release"));
        if (r.getBids() != null && r.getBids().getDetails() != null) {
            r.getBids().getDetails().forEach(d ->
                    replaceOrgEntitiesWithSavedEntities(d.getTenderers(), organizationRepository));
        }
        if (r.getAwards() != null) {
            r.getAwards().forEach(award -> {
                if (award.getSuppliers() != null) {
                    replaceOrgEntitiesWithSavedEntities(
                            award.getSuppliers(),
                            organizationRepository
                    );
                }
                award.setDescription(getIdxName("Award Description"));
                award.setTitle(getIdxName("Award Title"));
            });
            if (r.getBuyer() != null) {
                r.setBuyer(getSavedOrgEntityFromEntity(r.getBuyer(), organizationRepository));
            }

            if (r.getPlanning() != null && r.getPlanning().getBudget() != null) {
                r.getPlanning().getBudget().setProject(getIdxName("Project"));
                r.getPlanning().getBudget().setProjectID(getIdxName("Project ID"));
            }

            if (r.getTender() != null) {
                if (r.getTender().getProcuringEntity() != null) {
                    r.getTender().setProcuringEntity(getSavedOrgEntityFromEntity(
                            r.getTender().getProcuringEntity(),
                            organizationRepository
                    ));
                }

                if (r.getTender().getItems() != null) {
                    r.getTender().getItems().forEach(i -> {
                        i.setDescription(getIdxName("Tender Item Description"));
                        if (i.getClassification() != null) {
                            i.setClassification(getSavedEntityFromEntity(
                                    i.getClassification(),
                                    classificationRepository
                            ));
                        }
                    });
                }
            }
        }
        releaseRepository.save(r);
    }


    public String getIdxName(String category) {
        if (!idxGen.containsKey(category)) {
            idxGen.put(category, new AtomicInteger());
        }
        return category + "-" + idxGen.get(category).incrementAndGet();
    }

//    @PostConstruct
//    public void setProcessors() {
//        idxGen = new ConcurrentHashMap<>();
//        orgNameId = new ConcurrentHashMap<>();
//
//        randomizeOrganizations(this::logMessage);
//        randomizeClassifications(this::logMessage);
//        randomizeReleases(this::logMessage);
//    }

}
