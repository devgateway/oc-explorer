package org.devgateway.ocds.web.rest.controller;

import java.io.IOException;
import java.util.List;

import org.devgateway.ocds.persistence.mongo.Address;
import org.devgateway.ocds.persistence.mongo.ContactPoint;
import org.devgateway.ocds.persistence.mongo.Identifier;
import org.devgateway.ocds.persistence.mongo.Organization;
import org.devgateway.ocds.persistence.mongo.repository.OrganizationRepository;
import org.devgateway.ocds.web.rest.controller.request.OrganizationSearchRequest;
import org.devgateway.ocds.web.rest.controller.selector.BuyerSearchController;
import org.devgateway.ocds.web.rest.controller.selector.OrganizationSearchController;
import org.devgateway.ocds.web.rest.controller.selector.ProcuringEntitySearchController;
import org.devgateway.ocds.web.rest.controller.selector.SupplierSearchController;
import org.devgateway.toolkit.persistence.mongo.spring.MongoTemplateConfiguration;
import org.devgateway.toolkit.persistence.mongo.test.AbstractMongoTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
public class OrganizationEndpointsTest extends AbstractMongoTest {

	@Autowired
	private OrganizationSearchController organizationSearchController;

	@Autowired
	private ProcuringEntitySearchController procuringEntitySearchController;

	@Autowired
	private BuyerSearchController buyerSearchController;
	
	@Autowired
	private SupplierSearchController supplierSearchController; 

	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Autowired
    private MongoTemplateConfiguration mongoTemplateConfiguration;

	
	private String orgId = "1234";

	@Before
	public void importTestData() throws IOException, InterruptedException {

		if (testDataInitialized) {
			return;
		}

		Organization organization = new Organization();
		organization.setName("Development Gateway");
		organization.setId(orgId);

		Address address = new Address();
		address.setCountryName("Romania");
		address.setLocality("Bucuresti");
		address.setPostalCode("022671");
		address.setRegion("Bucuresti");
		address.setStreetAddress("7 Sos. Iancului");
		organization.setAddress(address);

		ContactPoint contactPoint = new ContactPoint();
		contactPoint.setEmail("mpostelnicu@developmentgateway.org");
		contactPoint.setFaxNumber("01234567");
		contactPoint.setTelephone("01234567");
		contactPoint.setUrl("http://developmentgateway.org");
		organization.setContactPoint(contactPoint);

		Identifier identifier = new Identifier();
		organization.getAdditionalIdentifiers().add(identifier);
		organization.getTypes().add(Organization.OrganizationType.procuringEntity);
		organization.getTypes().add(Organization.OrganizationType.buyer);

		Organization savedOrganization = organizationRepository.save(organization);

		Assert.assertNotNull(savedOrganization);
		Assert.assertEquals(orgId, savedOrganization.getId());

		testDataInitialized = true;
	}

	@Test
	public void testOrganizationIdEndpoint() {
		Organization organizationId = organizationSearchController.byId(orgId);
		Assert.assertNotNull(organizationId);
	}

	@Test
	public void testOrganizationSearchText() {
		OrganizationSearchRequest osr = new OrganizationSearchRequest();
		osr.setText("Development");
		List<Organization> organizations = organizationSearchController.searchText(osr);
		Assert.assertEquals(1, organizations.size(), 0);
	}

	@Test
	public void testProcuringEntityIdEndpoint() {
		Organization organizationId = procuringEntitySearchController.byId(orgId);
		Assert.assertNotNull(organizationId);
	}

	@Test
	public void testProcuringEntitySearchText() {
		OrganizationSearchRequest osr = new OrganizationSearchRequest();
		osr.setText("Development");
		List<Organization> organizations = procuringEntitySearchController.searchText(osr);
		Assert.assertEquals(1, organizations.size(), 0);
	}

	@Test
	public void testBuyerIdEndpoint() {
		Organization organizationId = buyerSearchController.byId(orgId);
		Assert.assertNotNull(organizationId);
	}

	@Test
	public void testBuyerSearchText() {
		OrganizationSearchRequest osr = new OrganizationSearchRequest();
		osr.setText("Development");
		List<Organization> organizations = buyerSearchController.searchText(osr);
		Assert.assertEquals(1, organizations.size(), 0);
	}

	@Test
	public void testSupplierIdEndpoint() {
		Organization organizationId = supplierSearchController.byId(orgId);
		Assert.assertNull(organizationId);
	}

	@Test
	public void testSupplierSaerchText() {
		OrganizationSearchRequest osr = new OrganizationSearchRequest();
		osr.setText("Development");
		List<Organization> organizations = supplierSearchController.searchText(osr);
		Assert.assertEquals(0, organizations.size(), 0);
	}
	
}