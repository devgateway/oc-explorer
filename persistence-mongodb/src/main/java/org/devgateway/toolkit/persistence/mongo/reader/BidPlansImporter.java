package org.devgateway.toolkit.persistence.mongo.reader;

import java.text.ParseException;

import org.devgateway.ocvn.persistence.mongo.ocds.BigDecimal2;
import org.devgateway.ocvn.persistence.mongo.ocds.Budget;
import org.devgateway.ocvn.persistence.mongo.ocds.Item;
import org.devgateway.ocvn.persistence.mongo.ocds.ItemUnit;
import org.devgateway.ocvn.persistence.mongo.ocds.Planning;
import org.devgateway.ocvn.persistence.mongo.ocds.Release;
import org.devgateway.ocvn.persistence.mongo.ocds.Tender;
import org.devgateway.ocvn.persistence.mongo.ocds.Value2;
import org.devgateway.toolkit.persistence.mongo.repository.ReleaseRepository;

public class BidPlansImporter extends RowImporter {

	public BidPlansImporter(ReleaseRepository releaseRepository, int skipRows) {
		super(releaseRepository, skipRows);
	}

	@Override
	public boolean importRow(String[] row) throws ParseException {

		String projectID = row[0];
		Release release = releaseRepository.findByBudgetProjectId(projectID);
		if (release == null) {
			release = new Release();
			Planning planning = new Planning();
			Budget budget = new Budget();
			release.setPlanning(planning);
			planning.setBudget(budget);
			budget.setProjectID(row[0]);
		}
		releases.add(release);

		Tender tender = release.getTender();
		if (tender == null) {
			tender = new Tender();
			release.setTender(tender);
		}

		// create Items
		Item item = new Item();
		tender.getItems().add(item);

		ItemUnit unit = new ItemUnit();
		Value2 value = new Value2();
		value.setCurrency("VND");
		
		
		value.setAmount(new BigDecimal2(row[5]));
		unit.setValue(value);
		item.setUnit(unit);
		item.setDescription(row[1]);
		item.setId(row[8]);

		return true;
	}
}
