package vn.gtel.pm2.sizing.exception;

import vn.gtel.pm2.sizing.enums.ResponseCode;

public abstract class BaseException extends RuntimeException {
    private final ResponseCode responseCode;

    public BaseException(ResponseCode responseCode) {
        super(responseCode.getMessageKey());
        this.responseCode = responseCode;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }
}
