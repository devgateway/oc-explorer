package org.devgateway.ocds.web.rest.controller.request;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * Created by mpostelnicu on 2/9/17.
 */
public class LocationFilterRequest extends GenericPagingRequest {

    @ApiModelProperty(value = "This will filter after tender.items.deliveryLocation._id")
    private List<String> tenderLoc;

    public List<String> getTenderLoc() {
        return tenderLoc;
    }

    public void setTenderLoc(List<String> tenderLoc) {
        this.tenderLoc = tenderLoc;
    }
}
