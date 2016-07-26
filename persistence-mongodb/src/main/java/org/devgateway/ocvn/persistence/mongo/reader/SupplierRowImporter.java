package org.devgateway.ocvn.persistence.mongo.reader;

import org.devgateway.ocds.persistence.mongo.Address;
import org.devgateway.ocds.persistence.mongo.ContactPoint;
import org.devgateway.ocds.persistence.mongo.Identifier;
import org.devgateway.ocds.persistence.mongo.reader.RowImporter;
import org.devgateway.ocds.persistence.mongo.spring.ImportService;
import org.devgateway.ocvn.persistence.mongo.dao.VNOrganization;
import org.devgateway.ocvn.persistence.mongo.repository.VNOrganizationRepository;

import java.text.ParseException;

/**
 * @author mihai Specific {@link RowImporter} for Suppliers, in the custom Excel
 *         format provided by Vietnam
 * @see VNOrganization
 */
public class SupplierRowImporter extends RowImporter<VNOrganization, VNOrganizationRepository> {

	public SupplierRowImporter(final VNOrganizationRepository repository, final ImportService importService,
			final int skipRows) {
		super(repository, importService, skipRows);
	}

	@Override
	public void importRow(final String[] row) throws ParseException {
		if (getRowCell(row, 0) == null) {
			throw new RuntimeException("Main identifier empty!");
		}
		VNOrganization organization = repository.findOne(getRowCell(row, 0));
		if (organization != null) {
			throw new RuntimeException("Duplicate identifer for organization " + organization);
		}
		organization = new VNOrganization();
		Identifier identifier = new Identifier();

		identifier.setId(getRowCell(row, 0));
		organization.setId(getRowCell(row, 0));
		organization.setIdentifier(identifier);
		organization.setName(getRowCell(row, 2));

		Address address = new Address();
		address.setStreetAddress(getRowCell(row, 18));
		address.setPostalCode(getRowCell(row, 17));

		organization.setAddress(address);

		ContactPoint cp = new ContactPoint();
		cp.setTelephone(getRowCell(row, 20));
		cp.setFaxNumber(getRowCell(row, 21));
		cp.setUrl(getRowCell(row, 22));

		organization.setContactPoint(cp);

		repository.insert(organization);

	}

}
