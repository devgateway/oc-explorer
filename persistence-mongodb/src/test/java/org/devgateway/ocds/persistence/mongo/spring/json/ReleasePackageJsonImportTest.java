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

        Assert.assertEquals(1, releases.size());
        Assert.assertNotNull(importedRelease);
        Assert.assertEquals("GBP", importedRelease.getPlanning().getBudget().getAmount().getCurrency());
    }

    @Test
    public void importObjectsTender() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();

        File file = new File(classLoader.getResource("json/fictional-example/ocds-213czf-000-00001-02-tender.json").getFile());
        ReleasePackageJsonImport releasePackageJsonImport = new ReleasePackageJsonImport(releaseRepository, file);
        Collection<Release> releases = releasePackageJsonImport.importObjects();
        Release importedRelease = releaseRepository.findById("ocds-213czf-000-00001-02-tender");

        Assert.assertEquals(1, releases.size());
        Assert.assertNotNull(importedRelease);
        Assert.assertEquals(Tender.ProcurementMethod.open, importedRelease.getTender().getProcurementMethod());
    }

    @Test
    public void importObjectsTenderAmendment() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();

        File file = new File(classLoader.getResource("json/fictional-example/ocds-213czf-000-00001-03-tenderAmendment.json").getFile());
        ReleasePackageJsonImport releasePackageJsonImport = new ReleasePackageJsonImport(releaseRepository, file);
        Collection<Release> releases = releasePackageJsonImport.importObjects();
        Release importedRelease = releaseRepository.findById("ocds-213czf-000-00001-03-tenderAmendment");

        Assert.assertEquals(1, releases.size());
        Assert.assertNotNull(importedRelease);
        Assert.assertArrayEquals(new String[] {"electronicSubmission"}, importedRelease.getTender().getSubmissionMethod().toArray());
    }

    @Test
    public void importObjectsAward() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();

        File file = new File(classLoader.getResource("json/fictional-example/ocds-213czf-000-00001-04-award.json").getFile());
        ReleasePackageJsonImport releasePackageJsonImport = new ReleasePackageJsonImport(releaseRepository, file);
        Collection<Release> releases = releasePackageJsonImport.importObjects();
        Release importedRelease = releaseRepository.findById("ocds-213czf-000-00001-04-award");

        Assert.assertEquals(1, releases.size());
        Assert.assertNotNull(importedRelease);
        Set<Award> awards = importedRelease.getAwards();
        Assert.assertEquals(Award.Status.pending, awards.iterator().next().getStatus());
    }

    @Test
    public void importObjectsContract() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();

        File file = new File(classLoader.getResource("json/fictional-example/ocds-213czf-000-00001-05-contract.json").getFile());
        ReleasePackageJsonImport releasePackageJsonImport = new ReleasePackageJsonImport(releaseRepository, file);
        Collection<Release> releases = releasePackageJsonImport.importObjects();
        Release importedRelease = releaseRepository.findById("ocds-213czf-000-00001-05-contract");

        Assert.assertEquals(1, releases.size());
        Assert.assertNotNull(importedRelease);
        Set<Contract> contracts = importedRelease.getContracts();
        Assert.assertEquals(Contract.Status.active, contracts.iterator().next().getStatus());
    }

    @Test
    public void importObjectsImplementation() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();

        File file = new File(classLoader.getResource("json/fictional-example/ocds-213czf-000-00001-06-implementation.json").getFile());
        ReleasePackageJsonImport releasePackageJsonImport = new ReleasePackageJsonImport(releaseRepository, file);
        Collection<Release> releases = releasePackageJsonImport.importObjects();
        Release importedRelease = releaseRepository.findById("ocds-213czf-000-00001-06-implementation");

        Assert.assertEquals(1, releases.size());
        Assert.assertNotNull(importedRelease);
        Assert.assertArrayEquals(new Tag[] {Tag.implementation}, importedRelease.getTag().toArray());
        Set<Contract> contracts = importedRelease.getContracts();
        Set<Transaction> transactions = contracts.iterator().next().getImplementation().getTransactions();
        Assert.assertEquals("https://openspending.org/uk-barnet-spending/", transactions.iterator().next().getSource());
    }
}
