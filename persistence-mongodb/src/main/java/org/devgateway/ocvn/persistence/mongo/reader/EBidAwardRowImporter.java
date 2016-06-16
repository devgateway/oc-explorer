package org.devgateway.ocvn.persistence.mongo.reader;

import java.text.ParseException;

import org.devgateway.ocds.persistence.mongo.Amount;
import org.devgateway.ocds.persistence.mongo.Award;
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

/**
 * Specific {@link RowImporter} for eBid Awards {@link VNAward} in the custom
 * Excel format provided by Vietnam
 *
 * @author mihai
 * @see VNAward
 *
 */
public class EBidAwardRowImporter extends ReleaseRowImporter {

	protected VNOrganizationRepository organizationRepository;

	public EBidAwardRowImporter(final ReleaseRepository releaseRepository, final ImportService importService,
			final VNOrganizationRepository organizationRepository, final int skipRows) {
		super(releaseRepository, importService, skipRows);
		this.organizationRepository = organizationRepository;
	}

	@Override
	public Release createReleaseFromReleaseRow(final String[] row) throws ParseException {

		Release release = repository.findByPlanningBidNo(getRowCell(row, 0));

		if (release == null) {
			release = new Release();
			release.setOcid(MongoConstants.OCDS_PREFIX + "bidno-" + getRowCell(row, 0));
			release.getTag().add(Tag.award);
			VNPlanning planning = new VNPlanning();
			release.setPlanning(planning);
			planning.setBidNo(getRowCell(row, 0));
		}

		if (release.getTender() == null) {
			VNTender tender = new VNTender();
			tender.setId(release.getOcid());
			release.setTender(tender);
		}

		release.getTender().getSubmissionMethod().add(Tender.SubmissionMethod.electronicSubmission.toString());

		VNAward award = new VNAward();
		award.setId(release.getOcid() + "-award-" + release.getAwards().size());
		release.getAwards().add(award);

		Amount value = new Amount();
		value.setCurrency("VND");
		value.setAmount(getDecimal(getRowCell(row, 1)));
		award.setValue(value);

		VNOrganization supplier = organizationRepository.findOne(getRowCell(row, 2));
		
		if (supplier == null) {
			supplier = new VNOrganization();
			supplier.setName(getRowCell(row, 2));
			supplier = organizationRepository.insert(supplier);
		}		

		award.setStatus("Y".equals(getRowCell(row, 5)) ? Award.Status.active : Award.Status.unsuccesful);
		
		// active=successful awards have suppliers
		if (Award.Status.active.equals(award.getStatus())) {
			award.getSuppliers().add(supplier);
		}

		award.setContractTime(getRowCell(row, 3));

		award.setBidOpenRank(getInteger(getRowCell(row, 4)));


		award.setInelibigleYN(getRowCell(row, 6));

		award.setIneligibleRson(getRowCell(row, 7));

		award.setDate(getExcelDate(getRowCell(row, 9)));

		//regardless if the award is active or not, we add the supplier to tenderers
		release.getTender().getTenderers().add(supplier);

		release.getTender().setNumberOfTenderers(release.getTender().getTenderers().size());

		return release;
	}
}
