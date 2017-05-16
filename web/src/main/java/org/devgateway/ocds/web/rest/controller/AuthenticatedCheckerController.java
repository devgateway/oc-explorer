package org.devgateway.ocds.web.rest.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by mpostelnicu on 16-May-17.
 */
@RestController
public class AuthenticatedCheckerController {

    @ApiOperation(value = "Returns true if the user is authenticated, false otherwise")
    @RequestMapping(value = "/isAuthenticated", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Boolean isAuthenticated(final HttpServletResponse response) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return true;
        }

        return false;

    }

}
