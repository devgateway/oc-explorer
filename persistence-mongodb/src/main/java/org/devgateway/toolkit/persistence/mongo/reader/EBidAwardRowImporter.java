package org.devgateway.toolkit.persistence.mongo.reader;

import java.text.ParseException;

import org.devgateway.ocvn.persistence.mongo.ocds.Identifier;
import org.devgateway.ocvn.persistence.mongo.ocds.Release;
import org.devgateway.ocvn.persistence.mongo.ocds.Value;
import org.devgateway.toolkit.persistence.mongo.dao.VNAward;
import org.devgateway.toolkit.persistence.mongo.dao.VNOrganization;
import org.devgateway.toolkit.persistence.mongo.dao.VNPlanning;
import org.devgateway.toolkit.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.toolkit.persistence.mongo.repository.VNOrganizationRepository;
import org.devgateway.toolkit.persistence.mongo.spring.VNImportService;

/**
 * Specific {@link RowImporter} for eBid Awards {@link VNAward} in the custom Excel format provided by Vietnam
 * @author mihai
 * @see VNAward
 *
 */
public class EBidAwardRowImporter extends RowImporter<Release, ReleaseRepository> {

	protected VNOrganizationRepository organizationRepository;

	public EBidAwardRowImporter(ReleaseRepository releaseRepository, VNImportService importService, VNOrganizationRepository organizationRepository,
			int skipRows) {
		super(releaseRepository, importService, skipRows);
		this.organizationRepository = organizationRepository;
	}

	@Override
	public boolean importRow(String[] row) throws ParseException {

		Release release = repository.findByPlanningBidNo(row[0]);

		if (release == null) {
			release = new Release();
			release.setOcid("ocvn-bidno-"+row[0]);
			release.getTag().add("award");
			VNPlanning planning = new VNPlanning();
			release.setPlanning(planning);
			planning.setBidNo(row[0]);			
		}
		

		VNAward award = new VNAward();
		award.setId(release.getOcid()+"-award-"+release.getAwards().size());
		release.getAwards().add(award);

		Value value = new Value();
		value.setCurrency("VND");
		value.setAmount(getDecimal(row[1]));
		award.setValue(value);

		VNOrganization supplier = organizationRepository.findById(row[2]);

		if (supplier == null) {
			supplier = new VNOrganization();
			Identifier supplierId = new Identifier();
			supplierId.setId(row[2]);
			supplier.setIdentifier(supplierId);
			supplier = organizationRepository.save(supplier);
		}
		
		award.getSuppliers().add(supplier);

		award.setContractTime(row[3]);

		award.setBidOpenRank(row[4].isEmpty() ? null : getInteger(row[4]));

		if (row.length > 5)
			award.setStatus(row[5].equals("Y") ? "active" : "unsuccessful");

		if (row.length > 6)
			award.setInelibigleYN(row[6]);

		if (row.length > 7)
			award.setIneligibleRson(row[7]);

		if (row.length > 8)
			award.setDate(row[8].isEmpty() ? null : getExcelDate(row[8]));

		if(release.getId()==null) 
			release=repository.save(release);
		else
			documents.add(release);
		
		return true;
	}
}
