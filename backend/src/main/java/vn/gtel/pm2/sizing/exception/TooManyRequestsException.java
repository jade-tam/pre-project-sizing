package vn.gtel.pm2.sizing.exception;

import vn.gtel.pm2.sizing.enums.ResponseCode;

public class TooManyRequestsException extends BaseException {
    public TooManyRequestsException() {
        super(ResponseCode.TOO_MANY_REQUESTS);
    }
}
