package cn.ponfee.web.framework.web;

import code.ponfee.commons.exception.BasicException;
import code.ponfee.commons.model.ResultCode;

/**
 * Exception for webapp
 * 
 * @author Ponfee
 */
public class WebException extends BasicException {

    private static final long serialVersionUID = -4318846668197402130L;

    public WebException(Integer code) {
        super(code);
    }

    public WebException(Integer code, String message) {
        super(code, message);
    }

    public WebException(ResultCode code) {
        super(code.getCode(), code.getMsg());
    }

    public WebException(ResultCode code, String message) {
        super(code.getCode(), message);
    }

}
