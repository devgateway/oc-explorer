/**
 * 
 */
package org.devgateway.toolkit.persistence.mongo.reader;

import java.text.ParseException;

import org.devgateway.ocvn.persistence.mongo.ocds.Address;
import org.devgateway.ocvn.persistence.mongo.ocds.ContactPoint;
import org.devgateway.ocvn.persistence.mongo.ocds.Identifier;
import org.devgateway.ocvn.persistence.mongo.ocds.Organization;
import org.devgateway.toolkit.persistence.mongo.repository.OrganizationRepository;

/**
 * @author mihai
 *
 */
public class SupplierRowImporter extends RowImporter<Organization, OrganizationRepository> {

	public SupplierRowImporter(OrganizationRepository repository, int skipRows) {
		super(repository, skipRows);
	}

	@Override
	public boolean importRow(String[] row) throws ParseException {
		if (row[0] == null || row[0].isEmpty())
			throw new RuntimeException("Main identifier empty!");
		Organization organization = repository.findById(row[0]);
		if (organization != null)
			throw new RuntimeException("Duplicate identifer for organization " + organization);
		organization = new Organization();
		Identifier identifier = new Identifier();

		identifier.setId(row[0]);
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

		documents.add(organization);

		return true;
	}

}
