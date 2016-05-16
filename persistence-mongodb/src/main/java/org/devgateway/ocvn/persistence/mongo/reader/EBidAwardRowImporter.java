package org.devgateway.ocvn.persistence.mongo.reader;

import java.text.ParseException;

import org.devgateway.ocds.persistence.mongo.Identifier;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.Value;
import org.devgateway.ocds.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.ocds.persistence.mongo.spring.VNImportService;
import org.devgateway.ocvn.persistence.mongo.dao.MongoConstants;
import org.devgateway.ocvn.persistence.mongo.dao.OCDSConst;
import org.devgateway.ocvn.persistence.mongo.dao.VNAward;
import org.devgateway.ocvn.persistence.mongo.dao.VNOrganization;
import org.devgateway.ocvn.persistence.mongo.dao.VNPlanning;
import org.devgateway.ocvn.persistence.mongo.repository.VNOrganizationRepository;

/**
 * Specific {@link RowImporter} for eBid Awards {@link VNAward} in the custom
 * Excel format provided by Vietnam
 * 
 * @author mihai
 * @see VNAward
 *
 */
public class EBidAwardRowImporter extends RowImporter<Release, ReleaseRepository> {

	protected VNOrganizationRepository organizationRepository;

	public EBidAwardRowImporter(final ReleaseRepository releaseRepository, final VNImportService importService,
			final VNOrganizationRepository organizationRepository, final int skipRows) {
		super(releaseRepository, importService, skipRows);
		this.organizationRepository = organizationRepository;
	}

	@Override
	public boolean importRow(final String[] row) throws ParseException {

		Release release = repository.findByPlanningBidNo(row[0]);

		if (release == null) {
			release = new Release();
			release.setOcid(MongoConstants.OCDS_PREFIX + "bidno-" + row[0]);
			release.getTag().add("award");
			VNPlanning planning = new VNPlanning();
			release.setPlanning(planning);
			planning.setBidNo(row[0]);
		}

		VNAward award = new VNAward();
		award.setId(release.getOcid() + "-award-" + release.getAwards().size());
		release.getAwards().add(award);

		Value value = new Value();
		value.setCurrency("VND");
		value.setAmount(getDecimal(row[1]));
		award.setValue(value);

		VNOrganization supplier = organizationRepository.findOne(row[2]);

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

		if (row.length > 5) {
			award.setStatus(row[5].equals("Y") ? OCDSConst.Awards.STATUS_ACTIVE : OCDSConst.Awards.STATUS_UNSUCCESSFUL);
		}

		if (row.length > 6) {
			award.setInelibigleYN(row[6]);
		}

		if (row.length > 7) {
			award.setIneligibleRson(row[7]);
		}

		if (row.length > 8) {
			award.setDate(row[8].isEmpty() ? null : getExcelDate(row[8]));
		}

		// For unsuccessful awards (in both eBid and Offline bid tabs), map the
		// information on the bidder (supplier) to tender.tenderers for that
		// BID_NO
		// we ignore the fields if there are no tenders found
		if (release.getTender() != null) {
			if (award.getStatus().equals(OCDSConst.Awards.STATUS_UNSUCCESSFUL)) {
				release.getTender().getTenderers().add(supplier);
			}
			release.getTender().setNumberOfTenderers(release.getTender().getTenderers().size());
		}

		if (release.getId() == null) {
			release = repository.save(release);
		} else {
			documents.add(release);
		}

		return true;
	}
}
