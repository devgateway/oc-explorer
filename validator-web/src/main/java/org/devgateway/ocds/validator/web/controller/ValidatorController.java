package org.devgateway.ocds.validator.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.fge.jsonschema.core.report.ListProcessingReport;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import javax.validation.Valid;
import org.devgateway.ocds.validator.OcdsValidatorConstants;
import org.devgateway.ocds.validator.OcdsValidatorNodeRequest;
import org.devgateway.ocds.validator.OcdsValidatorRequest;
import org.devgateway.ocds.validator.OcdsValidatorService;
import org.devgateway.ocds.validator.OcdsValidatorStringRequest;
import org.devgateway.ocds.validator.OcdsValidatorUrlRequest;
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
    @RequestMapping(value = "/api/ocds/validator/validateFormInline", method = {RequestMethod.GET, RequestMethod.POST},
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JsonNode> validateFormInline(@Valid @ModelAttribute OcdsValidatorStringRequest request)
            throws IOException {
        return new ResponseEntity<JsonNode>(processingReportToJsonNode(ocdsValidatorService.validate(request), request),
                HttpStatus.OK);
    }

    @ApiOperation(value = "Validates data against Open Contracting Data Standard using application/json "
            + "media type")
    @RequestMapping(value = "/api/ocds/validator/validateJsonInline", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JsonNode> validateJsonInline(@RequestBody @Valid
                                                               OcdsValidatorNodeRequest request)
            throws IOException {
        return new ResponseEntity<JsonNode>(processingReportToJsonNode(
                ocdsValidatorService.validate(request), request), HttpStatus.OK);
    }

    @ApiOperation(value = "Validates data against Open Contracting Data Standard using the "
            + "data fetched from a given URL")
    @RequestMapping(value = "/api/ocds/validator/validateJsonUrl", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JsonNode> validateJsonUrl(@RequestBody @Valid
                                                                OcdsValidatorUrlRequest request)
            throws IOException {
        return new ResponseEntity<JsonNode>(processingReportToJsonNode(
                ocdsValidatorService.validate(request), request), HttpStatus.OK);
    }


    private JsonNode processingReportToJsonNode(ProcessingReport report, OcdsValidatorRequest request) {
        if (report.isSuccess() && request.getOperation().equals(OcdsValidatorConstants.Operations.VALIDATE)) {
            return TextNode.valueOf("OK");
        }
        if (report instanceof ListProcessingReport) {
            return ((ListProcessingReport) report).asJson();
        }
        throw new RuntimeException("Unsupported non ListProcessingReport!");
    }
}
