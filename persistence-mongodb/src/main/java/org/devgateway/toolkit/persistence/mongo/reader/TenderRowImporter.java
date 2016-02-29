package org.devgateway.toolkit.persistence.mongo.reader;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.poi.ss.usermodel.DateUtil;
import org.devgateway.ocvn.persistence.mongo.ocds.Classification;
import org.devgateway.ocvn.persistence.mongo.ocds.Identifier;
import org.devgateway.ocvn.persistence.mongo.ocds.Item;
import org.devgateway.ocvn.persistence.mongo.ocds.Period;
import org.devgateway.ocvn.persistence.mongo.ocds.Release;
import org.devgateway.ocvn.persistence.mongo.ocds.Value;
import org.devgateway.toolkit.persistence.mongo.dao.VNOrganization;
import org.devgateway.toolkit.persistence.mongo.dao.VNPlanning;
import org.devgateway.toolkit.persistence.mongo.dao.VNTender;
import org.devgateway.toolkit.persistence.mongo.repository.ClassificationRepository;
import org.devgateway.toolkit.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.toolkit.persistence.mongo.repository.VNOrganizationRepository;

public class TenderRowImporter extends RowImporter<Release, ReleaseRepository> {

	SimpleDateFormat sdf = new SimpleDateFormat("dd.MMM.yy", new Locale("en"));
	private VNOrganizationRepository organizationRepository;
	private ClassificationRepository classificationRepository;

	public TenderRowImporter(ReleaseRepository releaseRepository, VNOrganizationRepository organizationRepository,
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
		String procurementMethodDetails = null;
		switch (Integer.parseInt(row[5])) {
		case 1:
			procurementMethod = "open";
			procurementMethodDetails = "Đấu thầu rộng rãi";
			break;
		case 2:
			procurementMethod = "selective";
			procurementMethodDetails = "Đấu thầu hạn chế";
			break;
		case 3:
			procurementMethod = "limited";
			procurementMethodDetails = "Chỉ định thầu";
			break;
		case 4:
			procurementMethod = "limited";
			procurementMethodDetails = "Mua sắm trực tiếp";
			break;
		case 5:
			procurementMethod = "open";
			procurementMethodDetails = "Chào hàng cạnh tranh";
			break;
		case 6: 
			procurementMethod = "limited";
			procurementMethodDetails = "Tự thực hiện";
			break;
		case 7:
			procurementMethod = "selective";
			procurementMethodDetails = "Trong trường hợp đặc biệt";
			break;

		}
		tender.setProcurementMethodDetails(procurementMethodDetails);
		tender.setProcurementMethod(procurementMethod);
		tender.setContrMethod(Integer.parseInt(row[6]));

		Period period = new Period();

		period.setStartDate(row[7].isEmpty() ? null : DateUtil.getJavaCalendar(Double.parseDouble(row[7])).getTime());
		period.setEndDate(row[8].isEmpty() ? null : DateUtil.getJavaCalendar(Double.parseDouble(row[8])).getTime());
		tender.setTenderPeriod(period);
		tender.setBidOpenDt(row[9].isEmpty() ? null : DateUtil.getJavaCalendar(Double.parseDouble(row[9])).getTime());

		VNOrganization procuringEntity = organizationRepository.findById(row[10]);

		if (procuringEntity == null) {
			procuringEntity = new VNOrganization();
			procuringEntity.setProcuringEntity(true);
			Identifier procuringEntityIdentifier = new Identifier();
			procuringEntityIdentifier.setId(row[10]);
			procuringEntity.setIdentifier(procuringEntityIdentifier);
			procuringEntity = organizationRepository.save(procuringEntity);
		} else {
			if (procuringEntity.getProcuringEntity() == null || procuringEntity.getProcuringEntity()==false) {
				procuringEntity.setProcuringEntity(true);
				procuringEntity = organizationRepository.save(procuringEntity);
			}
		}

		tender.setProcuringEntity(procuringEntity);

		VNOrganization orderInstituCd = organizationRepository.findById(row[11]);

		if (orderInstituCd == null) {
			orderInstituCd = new VNOrganization();
			Identifier orderInstituCdIdentifier = new Identifier();
			orderInstituCdIdentifier.setId(row[11]);
			orderInstituCd.setIdentifier(orderInstituCdIdentifier);
			orderInstituCd = organizationRepository.save(orderInstituCd);
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
				String classificationId=row[21].trim();
				Classification classification = classificationRepository.findById(classificationId);
				if (classification == null) {
					classification = new Classification();
					classification.setId(classificationId);

					switch (classificationId) {
					case "1":
						classification.setDescription("Hàng hóa");
						break;
					case "3":
						classification.setDescription("Xây lắp");
						break;
					case "5":
						classification.setDescription("Tư vấn");
						break;
					case "10":
						classification.setDescription("EPC");
						break;
					default:
						classification.setDescription("Undefined");
						break;
					}
					classification = classificationRepository.save(classification);

				}
				item.setClassification(classification);
			}

		}

		return true;
	}
}
