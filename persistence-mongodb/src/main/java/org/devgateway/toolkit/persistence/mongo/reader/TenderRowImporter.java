package org.devgateway.toolkit.persistence.mongo.reader;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.poi.ss.usermodel.DateUtil;
import org.devgateway.ocvn.persistence.mongo.ocds.Classification;
import org.devgateway.ocvn.persistence.mongo.ocds.Identifier;
import org.devgateway.ocvn.persistence.mongo.ocds.Item;
import org.devgateway.ocvn.persistence.mongo.ocds.Organization;
import org.devgateway.ocvn.persistence.mongo.ocds.Period;
import org.devgateway.ocvn.persistence.mongo.ocds.Release;
import org.devgateway.ocvn.persistence.mongo.ocds.Value;
import org.devgateway.toolkit.persistence.mongo.dao.VNPlanning;
import org.devgateway.toolkit.persistence.mongo.dao.VNTender;
import org.devgateway.toolkit.persistence.mongo.repository.ClassificationRepository;
import org.devgateway.toolkit.persistence.mongo.repository.OrganizationRepository;
import org.devgateway.toolkit.persistence.mongo.repository.ReleaseRepository;

public class TenderRowImporter extends RowImporter<Release, ReleaseRepository> {

	SimpleDateFormat sdf = new SimpleDateFormat("dd.MMM.yy", new Locale("en"));
	private OrganizationRepository organizationRepository;
	private ClassificationRepository classificationRepository;

	public TenderRowImporter(ReleaseRepository releaseRepository, OrganizationRepository organizationRepository,
			ClassificationRepository classificationRepository, int skipRows) {
		super(releaseRepository, skipRows);
		this.organizationRepository = organizationRepository;
		this.classificationRepository = classificationRepository;
	}

	@Override
	public boolean importRow(String[] row) throws ParseException {

		Release release = repository.findByPlanningBidNo(row[0]);

		if (release == null) {
			release = new Release();
			VNPlanning planning = new VNPlanning();
			release.setPlanning(planning);
			planning.setBidNo(row[0]);
		}
		documents.add(release);

		VNTender tender = (VNTender) release.getTender();
		if (tender == null) {
			tender = new VNTender();
			tender.setId(row[0]);
			release.setTender(tender);
		}

		String status = null;
		if (row[1].equals("Y") && (row[2].equals("N") || row[2].isEmpty()) && (row[3].equals("N") || row[3].isEmpty()))
			status = "active";

		if (row[1].isEmpty() && (row[2].isEmpty()) && (row[3].isEmpty()))
			status = "planned";

		if (row[1].isEmpty() && (row[2].equals("N")) && (row[3].equals("N") || row[3].isEmpty()))
			status = "planned";

		if (row[1].equals("Y") && (row[2].equals("Y")) && (row[3].equals("N") || row[3].isEmpty()))
			status = "cancelled";

		if (row[1].isEmpty() && (row[2].equals("Y")) && (row[3].equals("N") || row[3].isEmpty()))
			status = "cancelled";
		tender.setStatus(status);
		tender.setApproveState(row[1]);
		tender.setCancelYN(row[2]);
		tender.setModYn(row[3]);
		tender.setBidMethod(Integer.parseInt(row[4]));
		

		String procurementMethod = null;
		String succBidderMethodName = null;
		switch (Integer.parseInt(row[5])) {
		case 1:
			procurementMethod = "open";
			succBidderMethodName = "Đấu thầu rộng rãi";
			break;
		case 2:
			procurementMethod = "selective";
			succBidderMethodName = "Đấu thầu hạn chế";
			break;
		case 3:
			procurementMethod = "limited";
			succBidderMethodName = "Chỉ định thầu";
			break;
		case 4:
			procurementMethod = "limited";
			succBidderMethodName = "Mua sắm trực tiếp";
			break;
		case 5:
			procurementMethod = "open";
			succBidderMethodName = "Chào hàng cạnh tranh";
			break;
		case 6: 
			procurementMethod = "limited";
			succBidderMethodName = "Tự thực hiện";
			break;
		case 7:
			procurementMethod = "selective";
			succBidderMethodName = "Trong trường hợp đặc biệt";
			break;

		}
		tender.setSuccBidderMethodName(succBidderMethodName);
		tender.setProcurementMethod(procurementMethod);
		tender.setContrMethod(Integer.parseInt(row[6]));

		Period period = new Period();

		period.setStartDate(row[7].isEmpty() ? null : DateUtil.getJavaCalendar(Double.parseDouble(row[7])).getTime());
		period.setEndDate(row[8].isEmpty() ? null : DateUtil.getJavaCalendar(Double.parseDouble(row[8])).getTime());
		tender.setTenderPeriod(period);
		tender.setBidOpenDt(row[9].isEmpty() ? null : DateUtil.getJavaCalendar(Double.parseDouble(row[9])).getTime());

		Organization procuringEntity = organizationRepository.findById(row[10]);

		if (procuringEntity == null) {
			procuringEntity = new Organization();
			Identifier procuringEntityIdentifier = new Identifier();
			procuringEntityIdentifier.setId(row[10]);
			procuringEntity.setIdentifier(procuringEntityIdentifier);
		}

		tender.setProcuringEntity(procuringEntity);

		Organization orderInstituCd = organizationRepository.findById(row[11]);

		if (orderInstituCd == null) {
			orderInstituCd = new Organization();
			Identifier orderInstituCdIdentifier = new Identifier();
			orderInstituCdIdentifier.setId(row[11]);
			orderInstituCd.setIdentifier(orderInstituCdIdentifier);
		}
		tender.setOrderIntituCd(orderInstituCd);

		if (row.length > 12 && !row[12].isEmpty()) {
			Value value = new Value();
			value.setCurrency("VND");
			value.setAmount(new BigDecimal(row[12]));
			tender.setValue(value);
		}

		if (row.length > 21 && !row[21].isEmpty()) {
			if (tender.getItems().isEmpty()) {
				Item item = new Item();
				tender.getItems().add(item);
			}

			// we set classification for all items within this tender. If none
			// are found, we create a fake item and add only this classification
			for (Item item : tender.getItems()) {
				Classification classification = classificationRepository.findById(row[21]);
				if (classification == null) {
					classification = new Classification();
					classification.setId(row[21]);
				}
				item.setClassification(classification);
			}

		}

		return true;
	}
}
