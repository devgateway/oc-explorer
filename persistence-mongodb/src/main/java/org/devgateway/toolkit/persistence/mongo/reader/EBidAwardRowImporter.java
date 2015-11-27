package org.devgateway.toolkit.persistence.mongo.reader;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.poi.ss.usermodel.DateUtil;
import org.devgateway.ocvn.persistence.mongo.ocds.Identifier;
import org.devgateway.ocvn.persistence.mongo.ocds.Organization;
import org.devgateway.ocvn.persistence.mongo.ocds.Release;
import org.devgateway.ocvn.persistence.mongo.ocds.Value;
import org.devgateway.toolkit.persistence.mongo.dao.VNAward;
import org.devgateway.toolkit.persistence.mongo.dao.VNPlanning;
import org.devgateway.toolkit.persistence.mongo.repository.ReleaseRepository;

public class EBidAwardRowImporter extends RowImporter {

	SimpleDateFormat sdf = new SimpleDateFormat("dd.MMM.yy", new Locale("en"));

	public EBidAwardRowImporter(ReleaseRepository releaseRepository, int skipRows) {
		super(releaseRepository, skipRows);
	}

	@Override
	public boolean importRow(String[] row) throws ParseException {

		Release release = releaseRepository.findByPlanningBidNo(row[0]);

		if (release == null) {
			release = new Release();
			VNPlanning planning = new VNPlanning();
			release.setPlanning(planning);
			planning.setBidNo(row[0]);
		}
		releases.add(release);

		VNAward award = new VNAward();
		release.getAwards().add(award);

		Value value = new Value();
		value.setCurrency("VND");
		value.setAmount(new BigDecimal(row[1]));
		award.setValue(value);

		Organization supplier = new Organization();
		Identifier supplierId = new Identifier();
		supplierId.setId(row[2]);
		supplier.setIdentifier(supplierId);
		award.getSuppliers().add(supplier);

		award.setContractTime(row[3]);

		award.setBidOpenRank(row[4].isEmpty() ? null : Integer.parseInt(row[4]));

		if (row.length > 5)
			award.setStatus(row[5].equals("Y")?"active":"");

		if (row.length > 6)
			award.setInelibigleYN(row[6]);

		if (row.length > 7)
			award.setIneligibleRson(row[7]);

		if (row.length > 8)
			award.setDate(row[8].isEmpty() ? null : DateUtil.getJavaCalendar(Double.parseDouble(row[8])).getTime());

		return true;
	}
}
