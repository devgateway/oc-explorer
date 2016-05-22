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
 * @author mihai Specific {@link RowImporter} for Suppliers, in the custom Excel
 *         format provided by Vietnam
 * @see VNOrganization
 */
public class SupplierRowImporter extends RowImporter<VNOrganization, VNOrganizationRepository> {

	public SupplierRowImporter(final VNOrganizationRepository repository, final VNImportService importService,
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
		organization.setName(row[2]);

		Address address = new Address();
		address.setStreetAddress(row[18]);
		address.setPostalCode(row[17]);

		organization.setAddress(address);

		ContactPoint cp = new ContactPoint();
		cp.setTelephone(row[20]);
		cp.setFaxNumber(row[21]);
		cp.setUrl(row[22]);

		organization.setContactPoint(cp);

		repository.insert(organization);

	}

}
