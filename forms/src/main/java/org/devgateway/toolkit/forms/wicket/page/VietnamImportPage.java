/**
 * 
 */
package org.devgateway.toolkit.forms.wicket.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2MultiChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.events.EditingDisabledEvent;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.forms.wicket.providers.GenericPersistableJpaRepositoryTextChoiceProvider;
import org.devgateway.toolkit.persistence.dao.VietnamImportSourceFiles;
import org.devgateway.toolkit.persistence.mongo.dao.ImportFileTypes;
import org.devgateway.toolkit.persistence.mongo.spring.VNImportService;
import org.devgateway.toolkit.persistence.repository.VietnamImportSourceFilesRepository;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import nl.dries.wicket.hibernate.dozer.DozerModel;

/**
 * @author mpostelnicu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/import")
public class VietnamImportPage extends BasePage {

	@SpringBean
	private VietnamImportSourceFilesRepository sourceFilesRepository;
	
	@SpringBean
	private VNImportService vnImportService;

	private static final long serialVersionUID = 1L;
	private BootstrapForm<VietnamImportBean> importForm;

	private Select2ChoiceBootstrapFormComponent<VietnamImportSourceFiles> sourceFiles;

	private LaddaAjaxButton importButton;

	private Label logText;

	private LaddaAjaxButton doneButton;
	/**
	 * @param parameters
	 */
	public VietnamImportPage(PageParameters parameters) {
		super(parameters);
	}

	class VietnamImportBean implements Serializable {
		private static final long serialVersionUID = 1L;

		private VietnamImportSourceFiles sourceFiles;

		private List<String> fileTypes=new ArrayList<>(ImportFileTypes.allFileTypes);

		public List<String> getFileTypes() {
			return fileTypes;
		}

		public void setFileTypes(List<String> fileTypes) {
			this.fileTypes = fileTypes;
		}

		public VietnamImportSourceFiles getSourceFiles() {
			return sourceFiles;
		}

		public void setSourceFiles(VietnamImportSourceFiles sourceFiles) {
			this.sourceFiles = sourceFiles;
		}

	}

	public class EditForm extends BootstrapForm<VietnamImportBean> {
		public EditForm(String componentId) {
			super(componentId);
			// TODO Auto-generated constructor stub
		}

		private static final long serialVersionUID = -9127043819229346784L;

	}

	protected void addForm() {
		importForm = new BootstrapForm<VietnamImportBean>("form", new CompoundPropertyModel<>(new DozerModel<>(new VietnamImportBean())));
		importForm.setOutputMarkupId(true);
		add(importForm);
	}

	protected void addSourceFilesSelect() {
		sourceFiles = new Select2ChoiceBootstrapFormComponent<VietnamImportSourceFiles>(
				"sourceFiles",
				new GenericPersistableJpaRepositoryTextChoiceProvider<VietnamImportSourceFiles>(sourceFilesRepository));
		sourceFiles.required();
		importForm.add(sourceFiles);

	}

	protected void addLogText() {
		
		AbstractReadOnlyModel<String> logTextModel=new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return vnImportService.getMsgBuffer().toString();
			}
		};
		
		logText = new Label("logText",logTextModel);
		logText.add(new AjaxSelfUpdatingTimerBehavior(Duration.ONE_SECOND));
		logText.setOutputMarkupId(true);
		logText.setOutputMarkupPlaceholderTag(true);
		//logText.setVisibilityAllowed(false);
		importForm.add(logText);		
	}
	
	protected void addFileTypesSelect() {
		Select2MultiChoiceBootstrapFormComponent<String> fileTypes= new Select2MultiChoiceBootstrapFormComponent<String>(
				"fileTypes", new GenericChoiceProvider<String>(ImportFileTypes.allFileTypes));
		fileTypes.required();
		importForm.add(fileTypes);
	}
	
	protected void addDoneButton() {
		doneButton = new LaddaAjaxButton("done", Type.Default) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				setResponsePage(Homepage.class);
			}
		};
		doneButton.setDefaultFormProcessing(false);
		doneButton.setLabel(Model.of("Done"));
		doneButton.setDefaultFormProcessing(false);
		doneButton.setIconType(FontAwesomeIconType.thumbs_up);
		importForm.add(doneButton);		
	}
	
	protected void addImportButton() {
		importButton = new LaddaAjaxButton("import", Type.Danger) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				send(getPage(), Broadcast.BREADTH, new EditingDisabledEvent());	
				//logText.setVisibilityAllowed(true);
				target.add(logText);				
				target.add(form);		
				
				try {
					vnImportService.importAllSheets(importForm.getModelObject().getFileTypes(),importForm.getModelObject().getSourceFiles(),true);
				} catch (Exception e) {
					logger.error(e);
					e.printStackTrace();
				} finally {
					target.add(logText);
					target.add(feedbackPanel);
					this.setEnabled(false);
					target.add(this);
				}
				
				
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				ValidationError error = new ValidationError();
				error.addKey("formHasErrors");
				error(error);
				
				target.add(form);
				target.add(feedbackPanel);
			}
		};
		importButton.setLabel(Model.of("Start import process"));
		importButton.setIconType(FontAwesomeIconType.hourglass_start);
		importForm.add(importButton);		
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		vnImportService.newMsgBuffer();

		addForm();
		addSourceFilesSelect();
		addFileTypesSelect();
		addImportButton();		
		addLogText();
		addDoneButton();
		

	}

}
