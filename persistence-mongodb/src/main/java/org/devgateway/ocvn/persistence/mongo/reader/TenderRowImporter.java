package org.devgateway.ocvn.persistence.mongo.reader;

import java.text.ParseException;

import org.devgateway.ocds.persistence.mongo.Classification;
import org.devgateway.ocds.persistence.mongo.Identifier;
import org.devgateway.ocds.persistence.mongo.Item;
import org.devgateway.ocds.persistence.mongo.Period;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.Value;
import org.devgateway.ocds.persistence.mongo.repository.ClassificationRepository;
import org.devgateway.ocds.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.ocds.persistence.mongo.spring.VNImportService;
import org.devgateway.ocvn.persistence.mongo.dao.ContrMethod;
import org.devgateway.ocvn.persistence.mongo.dao.MongoConstants;
import org.devgateway.ocvn.persistence.mongo.dao.VNOrganization;
import org.devgateway.ocvn.persistence.mongo.dao.VNPlanning;
import org.devgateway.ocvn.persistence.mongo.dao.VNTender;
import org.devgateway.ocvn.persistence.mongo.repository.ContrMethodRepository;
import org.devgateway.ocvn.persistence.mongo.repository.VNOrganizationRepository;

/**
 * Specific {@link RowImporter} for Tenders, in the custom Excel format provided
 * by Vietnam
 * 
 * @author mihai
 * @see VNTender
 */
public class TenderRowImporter extends RowImporter<Release, ReleaseRepository> {

	private VNOrganizationRepository organizationRepository;
	private ClassificationRepository classificationRepository;
	private ContrMethodRepository contrMethodRepository;

	public TenderRowImporter(final ReleaseRepository releaseRepository, final VNImportService importService,
			final VNOrganizationRepository organizationRepository,
			final ClassificationRepository classificationRepository, ContrMethodRepository contrMethodRepository,
			final int skipRows) {
		super(releaseRepository, importService, skipRows);
		this.organizationRepository = organizationRepository;
		this.classificationRepository = classificationRepository;
		this.contrMethodRepository = contrMethodRepository;
	}

	@Override
	public boolean importRow(final String[] row) throws ParseException {

		Release release = repository.findByPlanningBidNo(row[0]);

		if (release == null) {
			release = new Release();
			release.setOcid(MongoConstants.OCDS_PREFIX + "bidno-" + row[0]);
			release.getTag().add("tender");
			VNPlanning planning = new VNPlanning();
			release.setPlanning(planning);
			planning.setBidNo(row[0]);
		}

		VNTender tender = (VNTender) release.getTender();
		if (tender == null) {
			tender = new VNTender();
			tender.setId(release.getOcid());
			release.setTender(tender);
		}

		String status = null;
		if (row[1].equals("Y") && (row[2].equals("N") || row[2].isEmpty())
				&& (row[3].equals("N") || row[3].isEmpty())) {
			status = "active";
		}

		if (row[1].isEmpty() && (row[2].isEmpty()) && (row[3].isEmpty())) {
			status = "planned";
		}

		if (row[1].isEmpty() && (row[2].equals("N")) && (row[3].equals("N") || row[3].isEmpty())) {
			status = "planned";
		}

		if (row[1].equals("Y") && (row[2].equals("Y")) && (row[3].equals("N") || row[3].isEmpty())) {
			status = "cancelled";
		}

		if (row[1].isEmpty() && (row[2].equals("Y")) && (row[3].equals("N") || row[3].isEmpty())) {
			status = "cancelled";
		}
		tender.setStatus(status);
		tender.setApproveState(row[1]);
		tender.setCancelYN(row[2]);
		tender.setModYn(row[3]);
		tender.setBidMethod(getInteger(row[4]));

		String procurementMethod = null;
		String procurementMethodDetails = null;
		switch (getInteger(row[5])) {
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
		default:
			procurementMethod = null;
			procurementMethodDetails = null;
			break;
		}
		tender.setProcurementMethodDetails(procurementMethodDetails);
		tender.setProcurementMethod(procurementMethod);
		
		
		Integer contrMethodId = getInteger(row[6]);
		if (contrMethodId != null) {
			ContrMethod contrMethod = contrMethodRepository.findOne(contrMethodId);
			if (contrMethod == null) {
				contrMethod = new ContrMethod();
				contrMethod.setId(contrMethodId);
				switch (contrMethodId) {
				case 1:
					contrMethod.setDetails("Trọn gói");
					break;
				case 2:
					contrMethod.setDetails("Theo đơn giá");
					break;
				case 3:
					contrMethod.setDetails("Theo thời gian");
					break;
				case 4:
					contrMethod.setDetails("Theo tỷ lệ phần trăm");
					break;
				case 5:
					contrMethod.setDetails("Hỗn hợp");
					break;
				default:
					contrMethod.setDetails("Undefined");
					break;
				}
				contrMethod = contrMethodRepository.save(contrMethod);
			}
			tender.setContrMethod(contrMethod);
		}
		
		Period period = new Period();

		period.setStartDate(row[7].isEmpty() ? null : getExcelDate(row[7]));
		period.setEndDate(row[8].isEmpty() ? null : getExcelDate(row[8]));
		tender.setTenderPeriod(period);
		tender.setBidOpenDt(row[9].isEmpty() ? null : getExcelDate(row[9]));

		VNOrganization procuringEntity = organizationRepository.findOne(row[10]);

		if (procuringEntity == null) {
			procuringEntity = new VNOrganization();
			procuringEntity.setProcuringEntity(true);
			Identifier procuringEntityIdentifier = new Identifier();
			procuringEntityIdentifier.setId(row[10]);
			procuringEntity.setIdentifier(procuringEntityIdentifier);
			procuringEntity = organizationRepository.save(procuringEntity);
		} else {
			if (procuringEntity.getProcuringEntity() == null || !procuringEntity.getProcuringEntity()) {
				procuringEntity.setProcuringEntity(true);
				procuringEntity = organizationRepository.save(procuringEntity);
			}
		}

		tender.setProcuringEntity(procuringEntity);

		VNOrganization orderInstituCd = organizationRepository.findOne(row[11]);

		if (orderInstituCd == null) {
			orderInstituCd = new VNOrganization();
			Identifier orderInstituCdIdentifier = new Identifier();
			orderInstituCdIdentifier.setId(row[11]);
			orderInstituCd.setIdentifier(orderInstituCdIdentifier);
			orderInstituCd = organizationRepository.save(orderInstituCd);
		}
		release.setBuyer(orderInstituCd);

		if (row.length > 12 && !row[12].isEmpty()) {
			Value value = new Value();
			value.setCurrency("VND");
			value.setAmount(getDecimal(row[12]));
			tender.setValue(value);
		}

		if (row.length > 21 && !row[21].isEmpty()) {
			if (tender.getItems().isEmpty()) {
				Item item = new Item();
				item.setId(Integer.toString(tender.getItems().size()));
				tender.getItems().add(item);
			}

			// we set classification for all items within this tender. If none
			// are found, we create a fake item and add only this classification
			for (Item item : tender.getItems()) {
				String classificationId = row[21].trim();
				Classification classification = classificationRepository.findOne(classificationId);
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

		if (release.getId() == null) {
			release = repository.save(release);
		} else {
			documents.add(release);
		}

		return true;
	}
}
