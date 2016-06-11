package org.devgateway.ocvn.persistence.mongo.reader;

import org.devgateway.ocds.persistence.mongo.Amount;
import org.devgateway.ocds.persistence.mongo.Award;
import org.devgateway.ocds.persistence.mongo.Identifier;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.Tag;
import org.devgateway.ocds.persistence.mongo.Tender;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.persistence.mongo.reader.ReleaseRowImporter;
import org.devgateway.ocds.persistence.mongo.reader.RowImporter;
import org.devgateway.ocds.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.ocds.persistence.mongo.spring.ImportService;
import org.devgateway.ocvn.persistence.mongo.dao.VNAward;
import org.devgateway.ocvn.persistence.mongo.dao.VNOrganization;
import org.devgateway.ocvn.persistence.mongo.dao.VNPlanning;
import org.devgateway.ocvn.persistence.mongo.dao.VNTender;
import org.devgateway.ocvn.persistence.mongo.repository.VNOrganizationRepository;

import java.text.ParseException;

/**
 * Specific {@link RowImporter} for Offline Awards, in the custom Excel format
 * provided by Vietnam
 *
 * @author mihai
 * @see VNAward
 */
public class OfflineAwardRowImporter extends ReleaseRowImporter {

	private VNOrganizationRepository organizationRepository;

	public OfflineAwardRowImporter(final ReleaseRepository releaseRepository, final ImportService importService,
			final VNOrganizationRepository organizationRepository, final int skipRows) {
		super(releaseRepository, importService, skipRows);
		this.organizationRepository = organizationRepository;
	}

	@Override
	public Release createReleaseFromReleaseRow(final String[] row) throws ParseException {

		Release release = repository.findByPlanningBidNo(getRowCell(row, 0));

		if (release == null) {
			release = new Release();
			release.getTag().add(Tag.award);
			release.setOcid(MongoConstants.OCDS_PREFIX + "bidno-" + getRowCell(row, 0));
			VNPlanning planning = new VNPlanning();
			release.setPlanning(planning);
			planning.setBidNo(getRowCell(row, 0));
		}

		if (release.getTender() == null) {
			VNTender tender = new VNTender();
			tender.setId(release.getOcid());
			release.setTender(tender);
		}

		release.getTender().getSubmissionMethod().add(Tender.SubmissionMethod.written.toString());

		VNAward award = new VNAward();
		award.setId(release.getOcid() + "-award-" + release.getAwards().size());

		release.getAwards().add(award);

		award.setTitle(getRowCell(row, 1));

		if (getRowCell(row, 2) != null) {
			Amount value = new Amount();
			value.setCurrency("VND");
			value.setAmount(getDecimal(getRowCell(row, 2)));
			award.setValue(value);
		}

		VNOrganization supplier = organizationRepository.findOne(getRowCell(row, 3));

		if (supplier == null) {
			supplier = new VNOrganization();
			Identifier supplierId = new Identifier();
			supplierId.setId(getRowCell(row, 3));
			supplier.setIdentifier(supplierId);
			supplier = organizationRepository.insert(supplier);
		}

		award.getSuppliers().add(supplier);

		award.setContractTime(getRowCell(row, 4));

		award.setStatus("Y".equals(getRowCell(row, 5)) ? Award.Status.active : Award.Status.unsuccesful);

		award.setInelibigleYN(getRowCell(row, 6));
	
		award.setIneligibleRson(getRowCell(row, 7));
	
		award.setBidType(getInteger(getRowCell(row, 8)));

		award.setBidSuccMethod(getInteger(getRowCell(row, 9)));
		
		if (getRowCell(row, 10) != null) {
			Amount value2 = new Amount();
			value2.setCurrency("VND");
			value2.setAmount(getDecimal(getRowCell(row, 10)));
			award.setValue(value2);
		}

		award.setDate(getExcelDate(getRowCell(row, 11)));

		// For unsuccessful awards (in both eBid and Offline bid tabs), map the
		// information on the bidder (supplier) to tender.tenderers for that
		// BID_NO
		// we ignore the fields if there are no tenders found
		if (award.getStatus().equals(Award.Status.unsuccesful)) {
			release.getTender().getTenderers().add(supplier);
		}
		release.getTender().setNumberOfTenderers(release.getTender().getTenderers().size());

		return release;
	}
}
