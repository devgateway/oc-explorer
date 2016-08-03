/**
 * 
 */
package org.devgateway.toolkit.persistence.mongo.spring;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.devgateway.ocds.persistence.mongo.Record;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mihai
 * 
 *         AOP Monitor that saves new {@link Release}S in {@link Record}S
 *         for archiving purposes.
 *         http://standard.open-contracting.org/latest/en/getting_started/releases_and_records/#records
 * 
 *         Whenever a new release is identified, the {@link Release} save action
 *         will trigger a {@link Release} archiving process as well.
 * 
 *         This will - search for a {@link Record} with the
 *         {@link Record#getOcid()} - if none can be found, create a new record
 *         - if existing record found, append to the record -
 * 
 * 
 */
@Aspect
@Component
public class ReleaseRecordMonitor {

	protected static Logger logger = Logger.getLogger(ReleaseRecordMonitor.class);

	@Autowired
	private RecordRepository recordRepository;

	public List<Record> saveRecordsForReleases(Iterable<Release> releases) {
		return StreamSupport.stream(releases.spliterator(), false).map(this::saveRecordForRelease)
				.collect(Collectors.toList());
	}

	@AfterReturning(value = "execution(*"
			+ " org.devgateway.ocds.persistence.mongo.repository.ReleaseRepository+.insert(..))", returning = "release")
	public Record saveRecordForRelease(JoinPoint jp, Release release) {
		logger.debug("Release archival triggered by" + jp);
		return saveRecordForRelease(release);
	}
	
	public Record saveRecordForRelease(Release release) {
		Record record = recordRepository.findByOcid(release.getOcid());
		if (record == null) {
			record = new Record();
			record.setOcid(release.getOcid());
		}
		record.getReleases().add(release);
		return recordRepository.save(record);
	}

}
