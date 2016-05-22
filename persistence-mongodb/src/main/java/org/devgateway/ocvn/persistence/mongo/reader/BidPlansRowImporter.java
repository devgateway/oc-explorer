package org.devgateway.ocvn.persistence.mongo.reader;

import java.text.ParseException;

import org.devgateway.ocds.persistence.mongo.Budget;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.Tender;
import org.devgateway.ocds.persistence.mongo.Value;
import org.devgateway.ocds.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.ocds.persistence.mongo.spring.VNImportService;
import org.devgateway.ocvn.persistence.mongo.dao.MongoConstants;
import org.devgateway.ocvn.persistence.mongo.dao.VNItem;
import org.devgateway.ocvn.persistence.mongo.dao.VNPlanning;
import org.devgateway.ocvn.persistence.mongo.dao.VNTender;

/**
 * @author mihai Specific {@link RowImporter} for Bid Plans in the custom Excel
 *         format provided by Vietnam
 * @see VNPlanning
 */
public class BidPlansRowImporter extends RowImporter<Release, ReleaseRepository> {

	public BidPlansRowImporter(final ReleaseRepository releaseRepository, final VNImportService importService,
			final int skipRows) {
		super(releaseRepository, importService, skipRows);
	}

	@Override
	public boolean importRow(final String[] row) throws ParseException {

		String projectID = row[0];
		Release release = repository.findByBudgetProjectId(projectID);

		if (release == null) {
			release = new Release();
			release.getTag().add("planning");
			release.setOcid(MongoConstants.OCDS_PREFIX + "prjid-" + projectID);
			VNPlanning planning = new VNPlanning();
			release.setPlanning(planning);
		}
		documents.add(release);

		Budget budget = new Budget();
		budget.setProjectID(row[0]);
		release.getPlanning().setBudget(budget);
		Value value = new Value();
		value.setCurrency("VND");
		budget.setAmount(value);

		// decimal2
		value.setAmount(getDecimal(row[5]));

		Tender tender = release.getTender();
		if (tender == null) {
			tender = new VNTender();
			tender.setId(release.getOcid());
			release.setTender(tender);
		}

		// create Items
		VNItem item = new VNItem();
		item.setId(Integer.toString(tender.getItems().size()));
		tender.getItems().add(item);

		// decimal2
		value.setAmount(getDecimal(row[5]));
		item.setDescription(row[1]);
		item.setBidPlanItemRefNum(row[2]);
		item.setBidPlanItemStyle(row[3]);
		item.setBidPlanItemFund(row[4]);
		item.setBidPlanItemMethodSelect(row[6]);
		item.setBidPlanItemMethod(row[7]);
		item.setId(row[8]);

		return true;
	}
}
