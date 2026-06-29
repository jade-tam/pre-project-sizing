package vn.gtel.pm2.sizing.exception;

import vn.gtel.pm2.sizing.enums.ResponseCode;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(ResponseCode responseCode) {
        super(responseCode);
    }
}
