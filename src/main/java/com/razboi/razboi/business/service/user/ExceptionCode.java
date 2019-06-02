package com.razboi.razboi.business.service.user;


public enum ExceptionCode {
    //    Error codes are grouped by logic  -> 1XXX for user, 2xxx for permission, etc
    UNKNOWN_EXCEPTION(1000, "Unknown Exception"),
    INVALID_LOGIN_EXCEPTION(1001, "Invalid login exception");
    int id;
    String message;


    ExceptionCode(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }


    public String getMessage() {
        return message;
    }
}
