/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo.reader;

import java.text.ParseException;

import org.devgateway.ocds.persistence.mongo.Address;
import org.devgateway.ocds.persistence.mongo.ContactPoint;
import org.devgateway.ocds.persistence.mongo.Identifier;
import org.devgateway.ocds.persistence.mongo.spring.VNImportService;
import org.devgateway.ocvn.persistence.mongo.dao.VNOrganization;
import org.devgateway.ocvn.persistence.mongo.repository.VNOrganizationRepository;

/**
 * @author mihai Specific {@link RowImporter} for Public Institutions, in the
 *         custom Excel format provided by Vietnam
 * @see VNOrganization
 */
public class PublicInstitutionRowImporter extends RowImporter<VNOrganization, VNOrganizationRepository> {

	public PublicInstitutionRowImporter(final VNOrganizationRepository repository, final VNImportService importService,
			final int skipRows) {
		super(repository, importService, skipRows);
	}

	@Override
	public void importRow(final String[] row) throws ParseException {
		if (row[0] == null || row[0].isEmpty()) {
			throw new RuntimeException("Main identifier empty!");
		}
		VNOrganization organization = repository.findOne(row[0]);
		if (organization != null) {
			throw new RuntimeException("Duplicate identifer for organization " + organization);
		}
		organization = new VNOrganization();
		Identifier identifier = new Identifier();

		identifier.setId(row[0]);
		organization.setId(row[0]);
		organization.setIdentifier(identifier);
		organization.setName(row[1]);

		if (row[44] != null && !row[44].isEmpty()) {
			Identifier additionalIdentifier = new Identifier();
			additionalIdentifier.setId(row[44]);
			organization.getAdditionalIdentifiers().add(additionalIdentifier);
		}

		Address address = new Address();
		address.setStreetAddress(row[14]);
		address.setPostalCode(row[13]);

		organization.setAddress(address);

		ContactPoint cp = new ContactPoint();
		cp.setName(row[5]);
		cp.setTelephone(row[7]);
		cp.setFaxNumber(row[8]);
		cp.setEmail(row[9]);
		cp.setUrl(row[18]);

		organization.setContactPoint(cp);

		repository.insert(organization);
		
	}

}
