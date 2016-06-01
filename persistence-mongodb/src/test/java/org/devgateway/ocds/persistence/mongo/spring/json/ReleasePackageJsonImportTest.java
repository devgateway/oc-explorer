package org.devgateway.ocds.persistence.mongo.spring.json;

import org.devgateway.ocds.persistence.mongo.Award;
import org.devgateway.ocds.persistence.mongo.Contract;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.Tag;
import org.devgateway.ocds.persistence.mongo.Tender;
import org.devgateway.ocds.persistence.mongo.Transaction;
import org.devgateway.ocds.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.ocds.persistence.mongo.test.AbstractMongoTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Collection;
import java.util.Set;

/**
 * @author idobre
 * @since 6/1/16
 */
public class ReleasePackageJsonImportTest extends AbstractMongoTest {
    @Autowired
    private ReleaseRepository releaseRepository;

    @Test
    public void importObjectsPlanning() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();

        File file = new File(classLoader.getResource("json/fictional-example/ocds-213czf-000-00001-01-planning.json").getFile());
        JsonImportPackage releasePackageJsonImport = new ReleasePackageJsonImport(releaseRepository, file);
        Collection<Release> releases = releasePackageJsonImport.importObjects();
        Release importedRelease = releaseRepository.findById("ocds-213czf-000-00001-01-planning");

        Assert.assertEquals(releases.size(), 1);
        Assert.assertNotNull(importedRelease);
        Assert.assertEquals(importedRelease.getPlanning().getBudget().getAmount().getCurrency(), "GBP");
    }

    @Test
    public void importObjectsTender() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();

        File file = new File(classLoader.getResource("json/fictional-example/ocds-213czf-000-00001-02-tender.json").getFile());
        ReleasePackageJsonImport releasePackageJsonImport = new ReleasePackageJsonImport(releaseRepository, file);
        Collection<Release> releases = releasePackageJsonImport.importObjects();
        Release importedRelease = releaseRepository.findById("ocds-213czf-000-00001-02-tender");

        Assert.assertEquals(releases.size(), 1);
        Assert.assertNotNull(importedRelease);
        Assert.assertEquals(importedRelease.getTender().getProcurementMethod(), Tender.ProcurementMethod.open);
    }

    @Test
    public void importObjectsTenderAmendment() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();

        File file = new File(classLoader.getResource("json/fictional-example/ocds-213czf-000-00001-03-tenderAmendment.json").getFile());
        ReleasePackageJsonImport releasePackageJsonImport = new ReleasePackageJsonImport(releaseRepository, file);
        Collection<Release> releases = releasePackageJsonImport.importObjects();
        Release importedRelease = releaseRepository.findById("ocds-213czf-000-00001-03-tenderAmendment");

        Assert.assertEquals(releases.size(), 1);
        Assert.assertNotNull(importedRelease);
        Assert.assertArrayEquals(importedRelease.getTender().getSubmissionMethod().toArray(), new String[] {"electronicSubmission"});
    }

    @Test
    public void importObjectsAward() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();

        File file = new File(classLoader.getResource("json/fictional-example/ocds-213czf-000-00001-04-award.json").getFile());
        ReleasePackageJsonImport releasePackageJsonImport = new ReleasePackageJsonImport(releaseRepository, file);
        Collection<Release> releases = releasePackageJsonImport.importObjects();
        Release importedRelease = releaseRepository.findById("ocds-213czf-000-00001-04-award");

        Assert.assertEquals(releases.size(), 1);
        Assert.assertNotNull(importedRelease);
        Set<Award> awards = importedRelease.getAwards();
        Assert.assertEquals(awards.iterator().next().getStatus(), Award.Status.pending);
    }

    @Test
    public void importObjectsContract() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();

        File file = new File(classLoader.getResource("json/fictional-example/ocds-213czf-000-00001-05-contract.json").getFile());
        ReleasePackageJsonImport releasePackageJsonImport = new ReleasePackageJsonImport(releaseRepository, file);
        Collection<Release> releases = releasePackageJsonImport.importObjects();
        Release importedRelease = releaseRepository.findById("ocds-213czf-000-00001-05-contract");

        Assert.assertEquals(releases.size(), 1);
        Assert.assertNotNull(importedRelease);
        Set<Contract> contracts = importedRelease.getContracts();
        Assert.assertEquals(contracts.iterator().next().getStatus(), Contract.Status.active);
    }

    @Test
    public void importObjectsImplementation() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();

        File file = new File(classLoader.getResource("json/fictional-example/ocds-213czf-000-00001-06-implementation.json").getFile());
        ReleasePackageJsonImport releasePackageJsonImport = new ReleasePackageJsonImport(releaseRepository, file);
        Collection<Release> releases = releasePackageJsonImport.importObjects();
        Release importedRelease = releaseRepository.findById("ocds-213czf-000-00001-06-implementation");

        Assert.assertEquals(releases.size(), 1);
        Assert.assertNotNull(importedRelease);
        Assert.assertArrayEquals(importedRelease.getTag().toArray(), new Tag[] {Tag.implementation});
        Set<Contract> contracts = importedRelease.getContracts();
        Set<Transaction> transactions = contracts.iterator().next().getImplementation().getTransactions();
        Assert.assertEquals(transactions.iterator().next().getSource(), "https://openspending.org/uk-barnet-spending/");
    }
}
