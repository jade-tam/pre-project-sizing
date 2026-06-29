package vn.gtel.pm2.sizing.exception;

import vn.gtel.pm2.sizing.enums.ResponseCode;

public class BusinessException extends BaseException {
    public BusinessException(ResponseCode responseCode) {
        super(responseCode);
    }
}
