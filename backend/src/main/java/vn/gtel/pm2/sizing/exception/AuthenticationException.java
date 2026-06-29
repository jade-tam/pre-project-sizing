package vn.gtel.pm2.sizing.exception;

import vn.gtel.pm2.sizing.enums.ResponseCode;

public class AuthenticationException extends BaseException {
    public AuthenticationException(ResponseCode responseCode) {
        super(responseCode);
    }
}
