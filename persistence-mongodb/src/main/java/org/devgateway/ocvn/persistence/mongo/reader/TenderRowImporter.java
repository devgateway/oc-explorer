package org.devgateway.ocvn.persistence.mongo.reader;

import java.text.ParseException;

import org.devgateway.ocds.persistence.mongo.Amount;
import org.devgateway.ocds.persistence.mongo.Classification;
import org.devgateway.ocds.persistence.mongo.Identifier;
import org.devgateway.ocds.persistence.mongo.Item;
import org.devgateway.ocds.persistence.mongo.Period;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.Tag;
import org.devgateway.ocds.persistence.mongo.Tender;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.persistence.mongo.reader.ReleaseRowImporter;
import org.devgateway.ocds.persistence.mongo.reader.RowImporter;
import org.devgateway.ocds.persistence.mongo.repository.ClassificationRepository;
import org.devgateway.ocds.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.ocds.persistence.mongo.spring.ImportService;
import org.devgateway.ocvn.persistence.mongo.dao.ContrMethod;
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
public class TenderRowImporter extends ReleaseRowImporter {

	private VNOrganizationRepository organizationRepository;
	private ClassificationRepository classificationRepository;
	private ContrMethodRepository contrMethodRepository;

	public TenderRowImporter(final ReleaseRepository releaseRepository, final ImportService importService,
			final VNOrganizationRepository organizationRepository,
			final ClassificationRepository classificationRepository, final ContrMethodRepository contrMethodRepository,
			final int skipRows) {
		super(releaseRepository, importService, skipRows);
		this.organizationRepository = organizationRepository;
		this.classificationRepository = classificationRepository;
		this.contrMethodRepository = contrMethodRepository;
	}

	@Override
	public Release createReleaseFromReleaseRow(final String[] row) throws ParseException {

		Release release = repository.findByPlanningBidNo(getRowCell(row, 0));

		if (release == null) {
			release = new Release();
			release.setOcid(MongoConstants.OCDS_PREFIX + "bidno-" + getRowCell(row, 0));
			release.getTag().add(Tag.tender);
			VNPlanning planning = new VNPlanning();
			release.setPlanning(planning);
			planning.setBidNo(getRowCell(row, 0));
		}

		VNTender tender = (VNTender) release.getTender();
		if (tender == null) {
			tender = new VNTender();
			tender.setId(release.getOcid());
			release.setTender(tender);
		}

		Tender.Status status = null;
		if (getRowCell(row, 1).equals("Y") && (getRowCell(row, 2) == null || getRowCell(row, 2).equals("N"))
				&& (getRowCell(row, 3) == null || getRowCell(row, 3).equals("N"))) {
			status = Tender.Status.active;
		}

		if (getRowCell(row, 1) == null && getRowCell(row, 2) == null && getRowCell(row, 3) == null) {
			status = Tender.Status.planned;
		}

		if (getRowCell(row, 1) == null && (getRowCell(row, 2).equals("N"))
				&& (getRowCell(row, 3) == null || getRowCell(row, 3).equals("N"))) {
			status = Tender.Status.planned;
		}

		if (getRowCell(row, 1).equals("Y") && (getRowCell(row, 2).equals("Y"))
				&& (getRowCell(row, 3) == null || getRowCell(row, 3).equals("N"))) {
			status = Tender.Status.cancelled;
		}

		if (getRowCell(row, 1) == null && (getRowCell(row, 2).equals("Y"))
				&& (getRowCell(row, 3) == null || getRowCell(row, 3).equals("N"))) {
			status = Tender.Status.cancelled;
		}
		tender.setStatus(status);
		tender.setApproveState(getRowCell(row, 1));
		tender.setCancelYN(getRowCell(row, 2));
		tender.setModYn(getRowCell(row, 3));
		tender.setBidMethod(getInteger(getRowCell(row, 4)));

		Tender.ProcurementMethod procurementMethod = null;
		String procurementMethodDetails = null;
		switch (getInteger(getRowCell(row, 5))) {
		case 1:
			procurementMethod = Tender.ProcurementMethod.open;
			procurementMethodDetails = "Đấu thầu rộng rãi";
			break;
		case 2:
			procurementMethod = Tender.ProcurementMethod.selective;
			procurementMethodDetails = "Đấu thầu hạn chế";
			break;
		case 3:
			procurementMethod = Tender.ProcurementMethod.limited;
			procurementMethodDetails = "Chỉ định thầu";
			break;
		case 4:
			procurementMethod = Tender.ProcurementMethod.limited;
			procurementMethodDetails = "Mua sắm trực tiếp";
			break;
		case 5:
			procurementMethod = Tender.ProcurementMethod.open;
			procurementMethodDetails = "Chào hàng cạnh tranh";
			break;
		case 6:
			procurementMethod = Tender.ProcurementMethod.limited;
			procurementMethodDetails = "Tự thực hiện";
			break;
		case 7:
			procurementMethod = Tender.ProcurementMethod.selective;
			procurementMethodDetails = "Trong trường hợp đặc biệt";
			break;
		default:
			procurementMethod = null;
			procurementMethodDetails = null;
			break;
		}
		tender.setProcurementMethodDetails(procurementMethodDetails);
		tender.setProcurementMethod(procurementMethod);

		Integer contrMethodId = getInteger(getRowCell(row, 6));
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
				contrMethod = contrMethodRepository.insert(contrMethod);
			}
			tender.setContrMethod(contrMethod);
		}

		Period period = new Period();

		period.setStartDate(getExcelDate(getRowCell(row, 7)));
		period.setEndDate(getExcelDate(getRowCell(row, 8)));
		tender.setTenderPeriod(period);
		tender.setBidOpenDt(getExcelDate(getRowCell(row, 9)));

		VNOrganization procuringEntity = organizationRepository.findOne(getRowCell(row, 10));

		if (procuringEntity == null) {
			procuringEntity = new VNOrganization();
			procuringEntity.setProcuringEntity(true);
			Identifier procuringEntityIdentifier = new Identifier();
			procuringEntityIdentifier.setId(getRowCell(row, 10));
			procuringEntity.setIdentifier(procuringEntityIdentifier);
			procuringEntity = organizationRepository.insert(procuringEntity);
		} else {
			if (procuringEntity.getProcuringEntity() == null || !procuringEntity.getProcuringEntity()) {
				procuringEntity.setProcuringEntity(true);
				procuringEntity = organizationRepository.save(procuringEntity);
			}
		}

		tender.setProcuringEntity(procuringEntity);

		VNOrganization orderInstituCd = organizationRepository.findOne(getRowCell(row, 11));

		if (orderInstituCd == null) {
			orderInstituCd = new VNOrganization();
			Identifier orderInstituCdIdentifier = new Identifier();
			orderInstituCdIdentifier.setId(getRowCell(row, 11));
			orderInstituCd.setIdentifier(orderInstituCdIdentifier);
			orderInstituCd = organizationRepository.insert(orderInstituCd);
		}
		release.setBuyer(orderInstituCd);

		if (getRowCell(row, 12) != null) {
			Amount value = new Amount();
			value.setCurrency("VND");
			value.setAmount(getDecimal(getRowCell(row, 12)));
			tender.setValue(value);
		}

		tender.setPublicationMethod(getRowCell(row, 14));

		tender.setCancellationRationale(getRowCell(row, 15));

		if (getRowCell(row, 21) != null) {
			if (tender.getItems().isEmpty()) {
				Item item = new Item();
				item.setId(Integer.toString(tender.getItems().size()));
				tender.getItems().add(item);
			}

			// we set classification for all items within this tender. If none
			// are found, we create a fake item and add only this classification
			for (Item item : tender.getItems()) {
				String classificationId = getRowCell(row, 21);
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
					classification = classificationRepository.insert(classification);

				}
				item.setClassification(classification);
			}

		}

		return release;
	}
}
