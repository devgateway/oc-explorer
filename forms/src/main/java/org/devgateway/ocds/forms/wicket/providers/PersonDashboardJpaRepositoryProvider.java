/**
 * 
 */
package org.devgateway.ocds.forms.wicket.providers;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.devgateway.ocds.persistence.dao.UserDashboard;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.providers.SortableJpaServiceDataProvider;
import org.devgateway.toolkit.persistence.service.PersonService;
import org.devgateway.toolkit.persistence.service.UserDashboardService;
import org.devgateway.toolkit.web.security.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Iterator;

/**
 * @author mpost
 *
 */
public class PersonDashboardJpaRepositoryProvider extends SortableJpaServiceDataProvider<UserDashboard> {

    private static final long serialVersionUID = -490237568464403107L;

    private UserDashboardService userDashboardService;

    private PersonService personService;

    public PersonDashboardJpaRepositoryProvider(UserDashboardService userDashboardService,
                                                PersonService personService) {
        super(userDashboardService);
        this.personService = personService;
        this.userDashboardService = userDashboardService;
    }

    /**
     * @see SortableDataProvider#iterator(long, long)
     */
    @Override
    public Iterator<UserDashboard> iterator(final long first, final long count) {
        int page = (int) ((double) first / WebConstants.PAGE_SIZE);
        Page<UserDashboard> findAll =
                userDashboardService.findDashboardsForPersonId(SecurityUtil.getCurrentAuthenticatedPerson().getId(),
                        new PageRequest(page, WebConstants.PAGE_SIZE, translateSort()));
        return findAll.iterator();
    }

    @Override
    public long size() {
        return personService.findById(SecurityUtil.getCurrentAuthenticatedPerson().getId()).get()
                .getDashboards().size();
    }

}
