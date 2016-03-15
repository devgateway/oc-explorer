/**
 * 
 */
package org.devgateway.toolkit.forms.wicket.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.ocvn.forms.wicket.components.LogLabel;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
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
	
	@SpringBean(name="getAsyncExecutor")
	private Executor asyncExecutor;
	
	@SpringBean
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	private static final long serialVersionUID = 1L;
	private BootstrapForm<VietnamImportBean> importForm;

	private Select2ChoiceBootstrapFormComponent<VietnamImportSourceFiles> sourceFiles;

	private LaddaAjaxButton importButton;

	private Label logText;

	private LaddaAjaxButton doneButton;

	private Select2MultiChoiceBootstrapFormComponent<String> fileTypes;

	private TransparentWebMarkupContainer importContainer;
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
		
		importContainer = new TransparentWebMarkupContainer("importContainer");
		importContainer.setOutputMarkupId(true);
		importForm.add(importContainer);
		
		AbstractReadOnlyModel<String> logTextModel=new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return vnImportService.getMsgBuffer().toString();
			}
		};
		
		logText = new LogLabel("logText",logTextModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onPostProcessTarget(AjaxRequestTarget target) {
			}
		};				
		importContainer.add(logText);		
	}
	
	protected void addFileTypesSelect() {
		fileTypes = new Select2MultiChoiceBootstrapFormComponent<String>(
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
				importContainer.setVisibilityAllowed(true);
				target.add(importContainer);				
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
		
		switchFieldsBasedOnExecutorAvailability(null);

	}
	
	private void switchFieldsBasedOnExecutorAvailability(AjaxRequestTarget target) {
		
		boolean enabled=threadPoolTaskExecutor.getActiveCount()==0;
		
		importContainer.setVisibilityAllowed(!enabled);
		sourceFiles.setEnabled(enabled);
		fileTypes.setEnabled(enabled);
		importButton.setEnabled(enabled);
		
		if(target!=null) {
			target.add(sourceFiles);
			target.add(fileTypes);
			target.add(importButton);;
		}
			
	}

}
