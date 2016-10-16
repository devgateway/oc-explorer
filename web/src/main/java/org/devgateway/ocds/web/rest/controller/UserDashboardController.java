package org.devgateway.ocds.web.rest.controller;

import javax.validation.Valid;

import org.devgateway.ocds.persistence.dao.UserDashboard;
import org.devgateway.ocds.persistence.repository.UserDashboardRepository;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
public class UserDashboardController {

    private UserDashboardRepository repository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    public UserDashboardController(UserDashboardRepository repo) {
        repository = repo;
    }

    @RequestMapping(method = { RequestMethod.POST, RequestMethod.GET },
            value = "/userDashboards/search/getDefaultDashboardForCurrentUser")
    @PreAuthorize("hasRole('ROLE_PROCURING_ENTITY')")
    public @ResponseBody ResponseEntity<?> getDefaultDashboardForCurrentUser() {
        UserDashboard dashboard = repository.getDefaultDashboardForPersonId(getCurrentAuthenticatedPerson().getId());
        Resource<UserDashboard> resource = new Resource<>(dashboard);
        return ResponseEntity.ok(resource);
    }

    @RequestMapping(method = { RequestMethod.POST, RequestMethod.GET },
            value = "/userDashboards/saveDashboardForCurrentUser")
    @PreAuthorize("hasRole('ROLE_PROCURING_ENTITY')")
    public ResponseEntity<Void> saveDashboardForCurrentUser(@ModelAttribute @Valid UserDashboard userDashboard) {
        Person person = personRepository.getOne(getCurrentAuthenticatedPerson().getId());
        userDashboard.getUsers().add(person);
        person.getDashboards().add(userDashboard);
        repository.save(userDashboard);
        return ResponseEntity.ok().build();
    }

    private static Person getCurrentAuthenticatedPerson() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        final Object principal = authentication.getPrincipal();
        if (principal instanceof Person) {
            return (Person) principal;
        }
        return null;
    }

}