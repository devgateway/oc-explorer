package org.devgateway.toolkit.persistence.mongo.reader;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.devgateway.toolkit.persistence.mongo.dao.DBConstants;
import org.devgateway.toolkit.persistence.mongo.spring.VNImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.MongoRepository;

public abstract class RowImporter<T, R extends MongoRepository<T, String>> {

	private final Logger logger = LoggerFactory.getLogger(VNImportService.class);

	protected R repository;

	protected DecimalFormat df;
	protected int skipRows;
	protected int cursorRowNo = 0;
	protected int importedRows = 0;
	protected List<T> documents;

	public RowImporter(R repository, int skipRows) {
		this.repository = repository;
		df = new DecimalFormat();
		df.setParseBigDecimal(true);
		documents = new ArrayList<>(DBConstants.IMPORT_ROW_BATCH);
		this.skipRows = skipRows;
	}

	public boolean importRows(List<String[]> rows) throws ParseException {
		documents.clear();

		for (String[] row : rows) {
			if (cursorRowNo++ < skipRows)
				continue;

			try {
				importRow(row);
				importedRows++;
			} catch (Exception e) {
				logger.error("Error importing row " + cursorRowNo + ". "+ e);
				// throw e; we do not stop
			}
		}

		repository.save(documents);
		logger.info("Imported " + importedRows);
		return true;
	}

	public abstract boolean importRow(String[] row) throws ParseException;

}
