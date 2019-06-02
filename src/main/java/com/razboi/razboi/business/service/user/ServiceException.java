package com.razboi.razboi.business.service.user;

public class ServiceException extends Exception {
    private ExceptionCode exceptionCode;

    public ServiceException() {
        this.exceptionCode = ExceptionCode.UNKNOWN_EXCEPTION;
    }

    public ServiceException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public ServiceException(String message, ExceptionCode exceptionCode) {
        super(message);
        this.setExceptionCode(exceptionCode);
    }

    public ServiceException(String message, Throwable cause, ExceptionCode exceptionCode) {
        super(message, cause);
        this.setExceptionCode(exceptionCode);
    }

    public ServiceException(Throwable cause, ExceptionCode exceptionCode) {
        super(cause);
        this.setExceptionCode(exceptionCode);
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ExceptionCode exceptionCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.setExceptionCode(exceptionCode);
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
