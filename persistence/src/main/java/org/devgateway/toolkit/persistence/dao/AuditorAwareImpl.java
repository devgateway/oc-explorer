package org.devgateway.toolkit.persistence.dao;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
    	if(SecurityContextHolder.getContext().getAuthentication()==null)
			return null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication==null) return null;
        final Object principal = authentication.getPrincipal();
        if (principal instanceof Person)
            return  ((Person) principal).getUsername();
        return null;
    	
    }


}
