package org.devgateway.toolkit.persistence.mongo.reader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

import org.devgateway.ocvn.persistence.mongo.ocds.Budget;
import org.devgateway.ocvn.persistence.mongo.ocds.Release;
import org.devgateway.ocvn.persistence.mongo.ocds.Value;
import org.devgateway.toolkit.persistence.mongo.dao.Location;
import org.devgateway.toolkit.persistence.mongo.dao.VNPlanning;
import org.devgateway.toolkit.persistence.mongo.repository.LocationRepository;
import org.devgateway.toolkit.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.toolkit.persistence.mongo.spring.VNImportService;

/**
 * 
 * @author mihai Specific {@link RowImporter} for Procurement Plans, in the
 *         custom Excel format provided by Vietnam
 * @see VNPlanning
 */
public class ProcurementPlansRowImporter extends RowImporter<Release, ReleaseRepository> {
	private LocationRepository locationRepository;

	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", new Locale("en"));

	public ProcurementPlansRowImporter(final ReleaseRepository releaseRepository, final VNImportService importService,
			final LocationRepository locationRepository, final int skipRows) {
		super(releaseRepository, importService, skipRows);
		this.locationRepository = locationRepository;
	}

	@Override
	public boolean importRow(final String[] row) throws ParseException {

		String projectID = row[0];
		Release oldRelease = repository.findByBudgetProjectId(projectID);
		if (oldRelease != null) {
			throw new RuntimeException("Duplicate planning.budget.projectID");
		}

		Release release = new Release();
		release.setOcid("ocvn-prjid-" + projectID);
		release.getTag().add("planning");
		documents.add(release);
		VNPlanning planning = new VNPlanning();
		Budget budget = new Budget();
		release.setPlanning(planning);
		planning.setBudget(budget);

		// search for loations

		String[] locations = row[3].split(",");
		for (int i = 0; i < locations.length; i++) {
			Location location = locationRepository.findByName(locations[i].trim());
			if (location == null) {
				location = new Location();
				location.setName(locations[i]);
			}

			planning.getLocations().add(location);
		}

		planning.setBidPlanProjectDateIssue(row[4].isEmpty() ? null : getDateFromString(sdf, row[4]));
		planning.setBidPlanProjectStyle(row[5]);
		planning.setBidPlanProjectCompanyIssue(row[6]);
		planning.setBidPlanProjectType(row[7]);
		planning.setBidPlanProjectFund(getInteger(row[8]));
		if (!row[9].trim().isEmpty()) {
			planning.setBidPlanProjectClassify(Arrays.asList(row[9].trim().split(", ")));
		}
		planning.setBidPlanProjectDateApprove(row[10].isEmpty() ? null : getDateFromString(sdf, row[10]));
		planning.setBidPlanNm(row[11]);
		planning.setBidPlanProjectStdClsCd(row[12]);
		if (row.length > 13) {
			planning.setBidNo(row[13]);
		}

		budget.setProjectID(row[0]);
		budget.setProject(row[1]);

		Value value = new Value();
		budget.setAmount(value);
		value.setCurrency("VND");
		value.setAmount(getDecimal(row[2]));
		return true;
	}
}
