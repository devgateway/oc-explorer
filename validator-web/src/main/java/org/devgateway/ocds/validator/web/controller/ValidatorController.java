package org.devgateway.ocds.validator.web.controller;

import com.github.fge.jsonschema.core.report.ProcessingReport;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.devgateway.ocds.validator.OcdsValidatorApiRequest;
import org.devgateway.ocds.validator.OcdsValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mpostelnicu on 16-May-17.
 */
@RestController
public class ValidatorController {

    @Autowired
    private OcdsValidatorService ocdsValidatorService;

    @ApiOperation(value = "Validates data against Open Contracting Data Standard using x-www-form-urlencoded "
            + "media type")
    @RequestMapping(value = "/validateForm", method = {RequestMethod.GET, RequestMethod.POST})
    public ProcessingReport validateForm(@ModelAttribute @Valid OcdsValidatorApiRequest request,
                                         final HttpServletResponse response)
            throws IOException {
        return ocdsValidatorService.validate(request);
    }

    @ApiOperation(value = "Validates data against Open Contracting Data Standard using application/json "
            + "media type")
    @RequestMapping(value = "/validateJson", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessingReport> validateJson(@ModelAttribute @Valid @RequestBody
                                                                     OcdsValidatorApiRequest request,
                                                         final HttpServletResponse response)
            throws IOException {
        return new ResponseEntity<ProcessingReport>(ocdsValidatorService.validate(request), HttpStatus.OK);
    }

}
