/**
 * 
 */
package org.devgateway.ocds.forms.wicket.providers;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.devgateway.ocds.persistence.dao.UserDashboard;
import org.devgateway.ocds.persistence.repository.UserDashboardRepository;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.security.SecurityUtil;
import org.devgateway.toolkit.forms.wicket.providers.SortableJpaRepositoryDataProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * @author mpost
 *
 */
public class PersonDashboardJpaRepositoryProvider extends SortableJpaRepositoryDataProvider<UserDashboard> {

    private static final long serialVersionUID = -490237568464403107L;

    private UserDashboardRepository userDashboardRepository;

    public PersonDashboardJpaRepositoryProvider(UserDashboardRepository jpaRepository) {
        super(jpaRepository);
        userDashboardRepository = (UserDashboardRepository) jpaRepository;
    }

    /**
     * @see SortableDataProvider#iterator(long, long)
     */
    @Override
    public Iterator<UserDashboard> iterator(final long first, final long count) {
        int page = (int) ((double) first / WebConstants.PAGE_SIZE);
        Page<UserDashboard> findAll =
                userDashboardRepository.findDashboardsForPersonId(SecurityUtil.getCurrentAuthenticatedPerson().getId(),
                        new PageRequest(page, WebConstants.PAGE_SIZE, translateSort()));
        return findAll.iterator();
    }

    @Override
    public long size() {
        return userDashboardRepository.countDashboardsForPersonId(SecurityUtil.getCurrentAuthenticatedPerson().getId());
    }

}
