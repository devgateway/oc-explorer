/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.forms.wicket.page;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.resource.JQueryResourceReference;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.security.SecurityUtil;
import org.devgateway.toolkit.forms.wicket.page.lists.ListGroupPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListTestFormPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListVietnamImportSourceFiles;
import org.devgateway.toolkit.forms.wicket.page.user.EditUserPage;
import org.devgateway.toolkit.forms.wicket.page.user.LogoutPage;
import org.devgateway.toolkit.forms.wicket.styles.MainCss;
import org.devgateway.toolkit.persistence.dao.Person;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.HtmlTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.core.markup.html.references.RespondJavaScriptReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;

/**
 * Base wicket-bootstrap {@link org.apache.wicket.Page}
 *
 * @author miha
 */
public abstract class BasePage extends GenericWebPage<Void> {
    private static final long serialVersionUID = -4179591658828697452L;

    protected static Logger logger = Logger.getLogger(BasePage.class);

    protected NotificationPanel feedbackPanel;
    protected Header header;
    protected Footer footer;

    protected Label pageTitle;


	private Navbar navbar;
	
	
	public static class HALRedirectPage extends RedirectPage {
		private static final long serialVersionUID = -750983217518258464L;
		
		public HALRedirectPage() {
			super(WebApplication.get().getServletContext().getContextPath()+"/api/browser/");
		}

	}
	
	public static class UIRedirectPage extends RedirectPage {
		private static final long serialVersionUID = -750983217518258464L;
		
		public UIRedirectPage() {
			super(WebApplication.get().getServletContext().getContextPath()+"/ui/index.html");
		}

	}
	
	

    /**
     * Construct.
     *
     * @param parameters current page parameters
     */
    public BasePage(final PageParameters parameters) {
        super(parameters);

        add(new HtmlTag("html"));
        // add javascript files at the bottom of the page
        add(new HeaderResponseContainer("scripts-container", "scripts-bucket"));

        feedbackPanel = createFeedbackPanel();
        add(feedbackPanel);

        header = new Header("header", parameters);

        navbar = newNavbar("navbar");
        header.add(navbar);

        footer = new Footer("footer");

        add(header);
        add(footer);

        pageTitle = new Label("pageTitle", new ResourceModel("page.title"));
        add(pageTitle);
    }

    protected NotificationPanel createFeedbackPanel() {
        NotificationPanel notificationPanel = new NotificationPanel("feedback");
        notificationPanel.setOutputMarkupId(true);
        return notificationPanel;
    }

    /**
     * creates a new {@link Navbar} instance
     *
     * @param markupId The components markup id.
     * @return a new {@link Navbar} instance
     */
    protected Navbar newNavbar(String markupId) {

    	PageParameters pageParametersForAccountPage = new PageParameters();
        Navbar navbar = new Navbar(markupId);

        // logout menu
        NavbarButton<LogoutPage> logoutMenu = new NavbarButton<LogoutPage>(LogoutPage.class,new StringResourceModel("navbar.logout", this, null));
        logoutMenu.setIconType(GlyphIconType.logout);
        MetaDataRoleAuthorizationStrategy.authorize(logoutMenu, Component.RENDER, SecurityConstants.Roles.ROLE_EDITOR);

        navbar.setPosition(Navbar.Position.TOP);
        navbar.setInverted(true);

       
        Person person = SecurityUtil.getCurrentAuthenticatedPerson();
        // account menu
        Model<String> account = null;
        if(person!=null){
            account = Model.of(person.getFirstName());
        }

        NavbarButton<EditUserPage> accountMenu = new NavbarButton<>(EditUserPage.class, pageParametersForAccountPage, account);
        accountMenu.setIconType(GlyphIconType.user);
        MetaDataRoleAuthorizationStrategy.authorize(accountMenu, Component.RENDER, SecurityConstants.Roles.ROLE_EDITOR);

        //home
        NavbarButton<Homepage> homeMenu = new NavbarButton<>(Homepage.class, pageParametersForAccountPage, Model.of("Home"));
        homeMenu.setIconType(GlyphIconType.home);
        MetaDataRoleAuthorizationStrategy.authorize(homeMenu, Component.RENDER, SecurityConstants.Roles.ROLE_EDITOR);

        
		// admin menu
		NavbarDropDownButton adminMenu = new NavbarDropDownButton(new StringResourceModel("navbar.admin", this, null)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<AbstractLink> newSubMenuButtons(String arg0) {
				List<AbstractLink> list = new ArrayList<>();
				list.add(new MenuBookmarkablePageLink<ListGroupPage>(ListGroupPage.class, null,
						new StringResourceModel("navbar.groups", this, null))
								.setIconType(FontAwesomeIconType.tags));
				
				list.add(new MenuBookmarkablePageLink<ListTestFormPage>(ListTestFormPage.class, null,
						new StringResourceModel("navbar.testcomponents", this, null))
								.setIconType(FontAwesomeIconType.android));

				list.add(new MenuBookmarkablePageLink<ListVietnamImportSourceFiles>(ListVietnamImportSourceFiles.class, null,
						new StringResourceModel("navbar.importfiles", this, null))
								.setIconType(FontAwesomeIconType.upload));
				
				
				list.add(new MenuBookmarkablePageLink<SpringEndpointsPage>(SpringEndpointsPage.class, null,
						new StringResourceModel("navbar.springendpoints", this, null))
								.setIconType(FontAwesomeIconType.anchor));
				
				MenuBookmarkablePageLink<HALRedirectPage> halBrowserLink = new MenuBookmarkablePageLink<HALRedirectPage>(
						HALRedirectPage.class, null, new StringResourceModel(
								"navbar.halbrowser", this, null)) {
									private static final long serialVersionUID = 1L;

									@Override 
						            protected void onComponentTag(ComponentTag tag) { 
						                super.onComponentTag(tag); 
						                tag.put("target", "_blank"); 
						            } 
						        };
			    halBrowserLink.setIconType(FontAwesomeIconType.rss).setEnabled(true);
				
				list.add(halBrowserLink);
				
				MenuBookmarkablePageLink<UIRedirectPage> uiBrowserLink = new MenuBookmarkablePageLink<UIRedirectPage>(
						UIRedirectPage.class, null, new StringResourceModel(
								"navbar.ui", this, null)) {
									private static final long serialVersionUID = 1L;

									@Override 
						            protected void onComponentTag(ComponentTag tag) { 
						                super.onComponentTag(tag); 
						                tag.put("target", "_blank"); 
						            } 
						        };
		        uiBrowserLink.setIconType(FontAwesomeIconType.rocket).setEnabled(true);
				
				list.add(uiBrowserLink);


				return list;
			}
		};
        
        adminMenu.setIconType(GlyphIconType.cog);
        MetaDataRoleAuthorizationStrategy.authorize(adminMenu, Component.RENDER, SecurityConstants.Roles.ROLE_ADMIN);
        
        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.RIGHT,homeMenu,adminMenu,accountMenu,logoutMenu));

        
        return navbar;
    }



   
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(MainCss.INSTANCE));

        response.render(RespondJavaScriptReference.headerItem());

        response.render(CssHeaderItem.forReference(FontAwesomeCssReference.instance()));
       
            response.render(JavaScriptHeaderItem.forReference(JQueryResourceReference.get()));
            response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(MainCss.class,
                    "/assets/js/fileupload.js")));
       
    }
}

