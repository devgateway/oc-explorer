package org.devgateway.ocds.web.spring;

import org.apache.log4j.Logger;
import org.devgateway.ocds.persistence.mongo.Classification;
import org.devgateway.ocds.persistence.mongo.Identifiable;
import org.devgateway.ocds.persistence.mongo.Organization;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.repository.main.ClassificationRepository;
import org.devgateway.ocds.persistence.mongo.repository.main.OrganizationRepository;
import org.devgateway.ocds.persistence.mongo.repository.main.ReleaseRepository;
import org.devgateway.toolkit.persistence.mongo.spring.MongoUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    protected static Logger logger = Logger.getLogger(OCDSPopulatorService.class);
    @Autowired
    private ReleaseRepository releaseRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private ClassificationRepository classificationRepository;

    //private String getRandomTxt() {
//        return RandomStringUtils.randomAlphabetic(10, 15);
//    }

    public void logMessage(String message) {
        logger.info(message);
    }

    public void randomizeOrganizations(Consumer<String> logMessage) {
        logMessage.accept("<b>RANDOMIZE ORGS.</b>");

        MongoUtil.processRepositoryItemsPaginated(organizationRepository, this::randomizeOrganization,
                this::logMessage);

        logMessage.accept("<b>RANDOMIZE ORGS COMPLETED.</b>");
    }


    public void randomizeReleases(Consumer<String> logMessage) {
        logMessage.accept("<b>RANDOMIZE RELEASES.</b>");

        MongoUtil.processRepositoryItemsPaginated(releaseRepository, this::randomizeRelease,
                this::logMessage);

        logMessage.accept("<b>RANDOMIZE RELEASES COMPLETED.</b>");
    }

    public void randomizeClassifications(Consumer<String> logMessage) {
        logMessage.accept("<b>RANDOMIZE CLASSIFICATIONS.</b>");

        MongoUtil.processRepositoryItemsPaginated(classificationRepository, this::randomizeClassification,
                this::logMessage);

        logMessage.accept("<b>RANDOMIZE CLASSIFICATIONS COMPLETED.</b>");
    }




    public void randomizeOrganization(Organization o)  {
        o.setName(getIdxName("Organization"));
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
        organizationRepository.save(o);
    }

    public void randomizeClassification(Classification c) {
        c.setDescription(getIdxName("Classification"));
        classificationRepository.save(c);
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
    void replaceEntitiesWithSavedEntities(Collection<T> c,
                                          MongoRepository<T, ID> repository) {
        Iterator<T> i = c.iterator();
        while (i.hasNext()) {
            T o = i.next();
            i.remove();
            c.add(getSavedEntityFromEntity(o, repository));
        }
    }

    public void randomizeRelease(Release r) {
        if (r.getBids() != null && r.getBids().getDetails() != null) {
            r.getBids().getDetails().forEach(d ->
                    replaceEntitiesWithSavedEntities(d.getTenderers(), organizationRepository));
        }
        if (r.getAwards() != null) {
            r.getAwards().forEach(award -> {
                if (award.getSuppliers() != null) {
                    replaceEntitiesWithSavedEntities(award.getSuppliers(),
                            organizationRepository);
                }
                award.setDescription(getIdxName("Award Description"));
                award.setTitle(getIdxName("Award Title"));
            });
            if (r.getBuyer() != null) {
                r.setBuyer(getSavedEntityFromEntity(r.getBuyer(), organizationRepository));
            }

            if (r.getPlanning() != null && r.getPlanning().getBudget() != null) {
                r.getPlanning().getBudget().setProject(getIdxName("Project"));
                r.getPlanning().getBudget().setProjectID(getIdxName("Project ID"));
            }

            if (r.getTender() != null) {
                if (r.getTender().getProcuringEntity() != null) {
                    r.getTender().setProcuringEntity(getSavedEntityFromEntity(r.getTender().getProcuringEntity(),
                            organizationRepository));
                }

                if (r.getTender().getItems() != null) {
                    r.getTender().getItems().forEach(i -> {
                        i.setDescription(getIdxName("Tender Item Description"));
                        if (i.getClassification() != null) {
                            i.setClassification(getSavedEntityFromEntity(i.getClassification(),
                                    classificationRepository));
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
        return category + " " + idxGen.get(category).incrementAndGet();
    }

    //@PostConstruct
//    public void setProcessors() {
//        idxGen = new ConcurrentHashMap<>();
//
//        randomizeOrganizations(this::logMessage);
//        randomizeClassifications(this::logMessage);
//        randomizeReleases(this::logMessage);
//    }

}
