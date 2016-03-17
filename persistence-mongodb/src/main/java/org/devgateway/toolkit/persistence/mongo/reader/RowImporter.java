package org.devgateway.toolkit.persistence.mongo.reader;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.DateUtil;
import org.devgateway.toolkit.persistence.mongo.spring.VNImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.MongoRepository;

public abstract class RowImporter<T, R extends MongoRepository<T, String>> {

	private final Logger logger = LoggerFactory.getLogger(VNImportService.class);

	protected R repository;
	
	protected VNImportService importService;

	protected int skipRows;
	protected int cursorRowNo = 0;
	protected int importedRows = 0;
	protected List<T> documents;

	public RowImporter(R repository, VNImportService importService, int skipRows) {
		this.repository = repository;
		this.importService=importService;
		documents = new ArrayList<>();
		this.skipRows = skipRows;
	}

	/**
	 * Returns a double number, checking the {@link NumberFormatException} and
	 * wrapping the error into a {@link RuntimeException} that can be thrown
	 * later
	 * 
	 * @param string
	 * @return
	 */
	public Double getDouble(String string) {
		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException e) {
			throw new RuntimeException("Cell value " + string + " is not a valid number.");
		}
	}
	
	
	public BigDecimal getDecimal(String string) {
		try {
			return new BigDecimal(string);
		} catch (NumberFormatException e) {
			throw new RuntimeException("Cell value " + string + " is not a valid decimal.");
		}
	}
	
	
	public Integer getInteger(String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			throw new RuntimeException("Cell value " + string + " is not a valid integer.");
		}
	}
	
	
	public Date getDateFromString(SimpleDateFormat sdf,String string) {
		try {
			return sdf.parse(string);
		} catch (ParseException e) {
			throw new RuntimeException(
					"Cell value " + string + " is not a valid date. Use format " + sdf.getNumberFormat().toString());
		}
	}

	public Date getExcelDate(String string) {
		try {
			return DateUtil.getJavaCalendar(Double.parseDouble(string)).getTime();
		} catch (NumberFormatException e) {
			throw new RuntimeException("Cell value " + string + " is not a valid Excel date.");
		}
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
				importService.logMessage("	<font style='color:red'>Error importing row " + cursorRowNo + ". "+ e+"</font>");
				// throw e; we do not stop
			}
		}

		repository.save(documents);
		logger.debug("Finished importing " + importedRows+" rows.");
		return true;
	}

	public abstract boolean importRow(String[] row) throws ParseException;

}
