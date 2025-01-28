package org.everowl.shared.service.dto;

import lombok.Data;
import lombok.Getter;
import org.everowl.shared.service.enums.ErrorCode;

@Getter
@Data
public class BaseErrorResponseBodyModel {
    private int respCode;
    private String respDesc;
    private Object result;

    public BaseErrorResponseBodyModel(ErrorCode errorCode) {
        this.respCode = errorCode.getCode();
        this.respDesc = errorCode.getMessage();
    }

    public BaseErrorResponseBodyModel(ErrorCode errorCode, Object result) {
        this.respCode = errorCode.getCode();
        this.respDesc = errorCode.getMessage();
        this.result = result;
    }
}
