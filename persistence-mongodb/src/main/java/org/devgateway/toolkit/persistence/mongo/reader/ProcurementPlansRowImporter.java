package org.devgateway.toolkit.persistence.mongo.reader;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

import org.devgateway.ocvn.persistence.mongo.ocds.Budget;
import org.devgateway.ocvn.persistence.mongo.ocds.Release;
import org.devgateway.ocvn.persistence.mongo.ocds.Value;
import org.devgateway.toolkit.persistence.mongo.dao.VNPlanning;
import org.devgateway.toolkit.persistence.mongo.repository.ReleaseRepository;

public class ProcurementPlansRowImporter extends RowImporter<Release, ReleaseRepository> {

	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", new Locale("en"));

	public ProcurementPlansRowImporter(ReleaseRepository releaseRepository, int skipRows) {
		super(releaseRepository, skipRows);
	}

	@Override
	public boolean importRow(String[] row) throws ParseException {

		String projectID = row[0];
		Release oldRelease = repository.findByBudgetProjectId(projectID);
		if (oldRelease != null)
			throw new RuntimeException("Duplicate planning.budget.projectID");

		Release release = new Release();
		documents.add(release);
		VNPlanning planning = new VNPlanning();
		Budget budget = new Budget();
		release.setPlanning(planning);
		planning.setBudget(budget);

		planning.setBidPlanProjectPlace(row[3]);
		planning.setBidPlanProjectDateIssue(row[4].isEmpty() ? null : sdf.parse(row[4]));
		planning.setBidPlanProjectStyle(row[5]);
		planning.setBidPlanProjectCompanyIssue(row[6]);
		planning.setBidPlanProjectType(row[7]);
		planning.setBidPlanProjectFund(Integer.parseInt(row[8]));
		planning.setBidPlanProjectClassify(Arrays.asList(row[9].split(", ")));
		planning.setBidPlanProjectDateApprove(row[10].isEmpty() ? null : sdf.parse(row[10]));
		planning.setBidPlanNm(row[11]);
		planning.setBidPlanProjectStdClsCd(row[12]);
		if (row.length > 13)
			planning.setBidNo(row[13]);

		budget.setProjectID(row[0]);
		budget.setProject(row[1]);

		Value value = new Value();
		budget.setAmount(value);
		value.setCurrency("VND");
		value.setAmount(new BigDecimal(row[2]));
		return true;
	}
}
