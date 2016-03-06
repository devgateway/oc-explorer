package org.devgateway.toolkit.persistence.mongo.reader;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.poi.ss.usermodel.DateUtil;
import org.devgateway.ocvn.persistence.mongo.ocds.Identifier;
import org.devgateway.ocvn.persistence.mongo.ocds.Release;
import org.devgateway.ocvn.persistence.mongo.ocds.Value;
import org.devgateway.toolkit.persistence.mongo.dao.VNAward;
import org.devgateway.toolkit.persistence.mongo.dao.VNOrganization;
import org.devgateway.toolkit.persistence.mongo.dao.VNPlanning;
import org.devgateway.toolkit.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.toolkit.persistence.mongo.repository.VNOrganizationRepository;

public class OfflineAwardRowImporter extends RowImporter<Release, ReleaseRepository> {

	SimpleDateFormat sdf = new SimpleDateFormat("dd.MMM.yy", new Locale("en"));
	private VNOrganizationRepository organizationRepository;

	public OfflineAwardRowImporter(ReleaseRepository releaseRepository, VNOrganizationRepository organizationRepository,
			int skipRows) {
		super(releaseRepository, skipRows);
		this.organizationRepository = organizationRepository;
	}

	@Override
	public boolean importRow(String[] row) throws ParseException {

		Release release = repository.findByPlanningBidNo(row[0]);

		if (release == null) {
			release = new Release();
			release.getTag().add("award");
			release.setOcid("ocvn-bidno-"+row[0]);
			VNPlanning planning = new VNPlanning();
			release.setPlanning(planning);
			planning.setBidNo(row[0]);
		}
		documents.add(release);

		VNAward award = new VNAward();
		award.setId(release.getOcid()+"-award-"+release.getAwards().size());
		
		release.getAwards().add(award);

		award.setTitle(row[1]);

		if (!row[2].isEmpty()) {
			Value value = new Value();
			value.setCurrency("VND");
			value.setAmount(new BigDecimal(row[2]));
			award.setValue(value);
		}

		VNOrganization supplier = organizationRepository.findById(row[3]);

		if (supplier == null) {
			supplier = new VNOrganization();
			Identifier supplierId = new Identifier();
			supplierId.setId(row[3]);
			supplier.setIdentifier(supplierId);		
			supplier = organizationRepository.save(supplier);
		}

		award.getSuppliers().add(supplier);

		award.setContractTime(row[4]);

		// award.setBidOpenRank(row[4].isEmpty() ? null :
		// Integer.parseInt(row[4]));

		if (row.length > 5)
			award.setStatus(row[5].equals("Y") ? "active" : "unsuccessful");

		if (row.length > 6)
			award.setInelibigleYN(row[6]);

		if (row.length > 7)
			award.setIneligibleRson(row[7]);

		if (row.length > 8)
			award.setBidType(row[8].isEmpty() ? null : Integer.parseInt(row[8]));

		if (row.length > 9)
			award.setBidSuccMethod(row[9].isEmpty() ? null : Integer.parseInt(row[9]));

		if (row.length > 10 && row[10]!=null && !row[10].isEmpty()) {
			Value value2 = new Value();
			value2.setCurrency("VND");
			value2.setAmount(new BigDecimal(row[10]));
			award.setValue(value2);
		}
		
		if (row.length > 11)
			award.setDate(row[11].isEmpty() ? null : DateUtil.getJavaCalendar(Double.parseDouble(row[11])).getTime());

		return true;
	}
}
