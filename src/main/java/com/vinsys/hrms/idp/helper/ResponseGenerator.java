package com.vinsys.hrms.idp.helper;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.ResponseCode;

public class ResponseGenerator {

    public static HRMSBaseResponse<String> getValidationResponse(HRMSException e, String appVersion) {
        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
        response.setResponseCode(e.getResponseCode());
        response.setResponseMessage(e.getResponseMessage());
        response.setApplicationVersion(appVersion);
        return response;
    }

    public static HRMSBaseResponse<String> getErrorResponse(Exception e, String appVersion) {
        HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
        response.setResponseCode(1500);
        response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
        response.setApplicationVersion(appVersion);
        return response;
    }
}
