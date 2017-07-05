package org.devgateway.ocds.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mpostelnicu on 7/5/17.
 */
@Service
public class OcdsValidatorService {

    @Autowired
    private ObjectMapper jacksonObjectMapper;



    public ProcessingReport validateReleases(OcdsValidatorRequest request) {
        return null;
    }


    public ProcessingReport validateRelease(OcdsValidatorRequest request) {
        return null;
    }


}
