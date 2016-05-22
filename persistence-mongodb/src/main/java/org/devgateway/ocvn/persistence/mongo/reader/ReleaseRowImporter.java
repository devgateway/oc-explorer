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
 * @author mihai
 */
public abstract class ReleaseRowImporter extends RowImporter<Release, ReleaseRepository> {

	public ReleaseRowImporter(final ReleaseRepository releaseRepository, final VNImportService importService,
			final int skipRows) {
		super(releaseRepository, importService, skipRows);
	}

	@Override
	public void importRow(final String[] row) throws ParseException {
		Release release = createReleaseFromReleaseRow(row);
		if (release.getId() == null) {
			repository.insert(release);
		} else {
			repository.save(release);
		}
	}

	public abstract Release createReleaseFromReleaseRow(final String[] row) throws ParseException;

}
