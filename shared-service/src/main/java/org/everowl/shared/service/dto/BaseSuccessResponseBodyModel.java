package org.everowl.shared.service.dto;

import lombok.Data;

@Data
public class BaseSuccessResponseBodyModel {
    private int respCode;
    private String respDesc;
    private Object result;

    public BaseSuccessResponseBodyModel(Object result) {
        this.respCode = 2000;
        this.respDesc = "Success";
        this.result = result;
    }
}

