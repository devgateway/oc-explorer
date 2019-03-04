package org.devgateway.toolkit.persistence.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.devgateway.toolkit.persistence.excel.service.TranslateService;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Persistable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 13/11/2017
 */
public class ExcelFileDefaultTest {
    private static final Logger logger = LoggerFactory.getLogger(ExcelFileDefaultTest.class);

    public class TestTranslateService implements TranslateService {
        @Override
        public String getTranslation(Class clazz, Field field) {
            return null;
        }
    }

    @Test
    public void createWorkbook() throws Exception {
        final ExcelFile excelFile = new ExcelFileDefault(createObjects(), new TestTranslateService());
        final Workbook workbook = excelFile.createWorkbook();

        // try (FileOutputStream outputStream = new FileOutputStream("/Users/ionut/Downloads/file-export.xlsx")) {
        //     workbook.write(outputStream);
        // }

        Assert.assertEquals("Number of sheets", 3, workbook.getNumberOfSheets());

        Assert.assertNotNull("buyer sheet", workbook.getSheet("testbuyer"));
        Assert.assertNotNull("contract sheet", workbook.getSheet("testcontract"));
        Assert.assertNotNull("document sheet", workbook.getSheet("testdocument"));

        Assert.assertEquals("buyer name", "buyer 1",
                workbook.getSheet("testbuyer").getRow(1).getCell(0).toString());
        Assert.assertEquals("buyer classification", "TP-1 - alii aliquam",
                workbook.getSheet("testbuyer").getRow(2).getCell(8).toString());

        Assert.assertEquals("contract parent", "testbuyer - 1",
                workbook.getSheet("testcontract").getRow(1).getCell(0).toString());
        Assert.assertEquals("contract amount", 1000,
                workbook.getSheet("testcontract").getRow(1).getCell(2).getNumericCellValue(), 0.0);


        Assert.assertEquals("document number of rows", 2, workbook.getSheet("testdocument").getLastRowNum());

        Assert.assertEquals("buyer address flatten", "Street 1 | Street 2",
                workbook.getSheet("testbuyer").getRow(1).getCell(4).toString());
    }

    private List createObjects() {
        final List objects = new ArrayList();

        final List<TestAddress> addresses1 = new ArrayList<>();
        final List<TestAddress> addresses2 = new ArrayList<>();
        final TestAddress address1 = new TestAddress(Long.valueOf(1), "Street 1", "Romania");
        final TestAddress address2 = new TestAddress(Long.valueOf(2), "Street 2", "France");
        final TestAddress address3 = new TestAddress(Long.valueOf(1), "Street 3", "UK");
        final TestAddress address4 = new TestAddress(Long.valueOf(2), "Street 4", "Moldova");
        addresses1.add(address1);
        addresses1.add(address2);
        addresses2.add(address3);
        addresses2.add(address4);

        final TestOrganization organization1 = new TestOrganization(Long.valueOf(1), "organization 1", addresses1);
        final TestOrganization organization2 = new TestOrganization(Long.valueOf(2), "organization 2", addresses1);

        final List<TestContract> contracts1 = new ArrayList<>();
        final List<TestContract> contracts2 = new ArrayList<>();
        final TestContract contract1 = new TestContract(Long.valueOf(1), "ABC-100", 1000);
        final TestContract contract2 = new TestContract(Long.valueOf(2), "ABC-101", 2000);
        final TestContract contract3 = new TestContract(Long.valueOf(3), "ABC-102", 100);
        final TestContract contract4 = new TestContract(Long.valueOf(4), "ABC-103", 7000);
        contracts1.add(contract1);
        contracts1.add(contract2);
        contracts2.add(contract3);
        contracts2.add(contract4);

        final TestDocument document1 = new TestDocument(Long.valueOf(1), "document 1", "PDF");
        final TestDocument document2 = new TestDocument(Long.valueOf(2), "document 2", "TXT");

        final TestClassification classification1 = new TestClassification(Long.valueOf(1), "XZ-1", "dolor sit amet");
        final TestClassification classification2 = new TestClassification(Long.valueOf(2), "TP-1", "alii aliquam");

        final TestBuyer buyer1 = new TestBuyer(Long.valueOf(1), "buyer 1", organization1,
                addresses1, contracts1, document1, classification1);
        final TestBuyer buyer2 = new TestBuyer(Long.valueOf(2), "buyer 2", organization2,
                addresses2, contracts2, document2, classification2);

        objects.add(buyer1);
        objects.add(buyer2);

        return objects;
    }


    /** ******************************************************************************
     *  Test Entities
     ******************************************************************************* */
    public class TestBuyer implements Persistable<Long> {
        private final Long id;

        @ExcelExport
        private final String name;

        @ExcelExport(name = "organization")
        private final TestOrganization org;

        @ExcelExport
        private final List<TestAddress> addresses;

        @ExcelExport(separateSheet = true)
        private final List<TestContract> contracts;

        @ExcelExport(separateSheet = true)
        private final TestDocument document;

        @ExcelExport(justExport = true)
        private final TestClassification classification;

        private TestBuyer(final Long id, final String name, final TestOrganization org,
                          final List<TestAddress> addresses, final List<TestContract> contracts,
                          final TestDocument document, final TestClassification classification) {
            this.id = id;
            this.name = name;
            this.org = org;
            this.addresses = addresses;
            this.contracts = contracts;
            this.document = document;
            this.classification = classification;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public boolean isNew() {
            return false;
        }

        public String getName() {
            return name;
        }

        public TestOrganization getOrg() {
            return org;
        }

        public List<TestAddress> getAddresses() {
            return addresses;
        }

        public List<TestContract> getContracts() {
            return contracts;
        }

        public TestDocument getDocument() {
            return document;
        }

        public TestClassification getClassification() {
            return classification;
        }
    }

    public class TestOrganization implements Persistable<Long> {
        private final Long id;

        @ExcelExport
        private final List<TestAddress> addresses;

        @ExcelExport
        private final String name;

        private TestOrganization(final Long id, final String name, final List<TestAddress> addresses) {
            this.id = id;
            this.name = name;
            this.addresses = addresses;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public boolean isNew() {
            return false;
        }

        public List<TestAddress> getAddresses() {
            return addresses;
        }

        public String getName() {
            return name;
        }
    }

    public class TestAddress implements Persistable<Long> {
        private final Long id;

        @ExcelExport
        private final String street;

        @ExcelExport
        private final String country;

        public TestAddress(final Long id, final String street, final String country) {
            this.id = id;
            this.street = street;
            this.country = country;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public boolean isNew() {
            return false;
        }

        public String getStreet() {
            return street;
        }

        public String getCountry() {
            return country;
        }
    }


    public class TestContract implements Persistable<Long> {
        private final Long id;

        @ExcelExport
        private final String identifier;

        @ExcelExport
        private final int amount;

        public TestContract(final Long id, final String identifier, final int amount) {
            this.id = id;
            this.identifier = identifier;
            this.amount = amount;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public boolean isNew() {
            return false;
        }

        public String getIdentifier() {
            return identifier;
        }

        public int getAmount() {
            return amount;
        }
    }

    public class TestDocument implements Persistable<Long> {
        private final Long id;

        @ExcelExport
        private final String fileName;

        @ExcelExport
        private final String type;

        public TestDocument(final Long id, final String fileName, final String type) {
            this.id = id;
            this.fileName = fileName;
            this.type = type;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public boolean isNew() {
            return false;
        }

        public String getFileName() {
            return fileName;
        }

        public String getType() {
            return type;
        }
    }

    public class TestClassification implements Persistable<Long> {
        private final Long id;

        @ExcelExport
        private final String schema;

        @ExcelExport
        private final String description;

        public TestClassification(final Long id, final String schema, final String description) {
            this.id = id;
            this.schema = schema;
            this.description = description;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public boolean isNew() {
            return false;
        }


        @Override
        public String toString() {
            return schema + " - " + description;
        }

        public String getSchema() {
            return schema;
        }

        public String getDescription() {
            return description;
        }
    }
}
