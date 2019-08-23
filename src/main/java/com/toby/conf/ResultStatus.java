package com.toby.conf;

public enum  ResultStatus {

    SUCCESS(100, "成功"),
    FAILURE(200, "数据操作失败"),
    PHONE_NUMBER_IS_USED(200, "手机号已被注册"),
    VERTIFY_CODE_NOT_USEFUL(200, "验证码已失效"),
    NO_AUTHORITY(1000, "没有相应的权限"),
    USERNAME_OR_PASSWORD_ERROR(-1001, "用户名或密码错误"),
    USER_NOT_FOUND(-1002, "用户不存在"),
    USER_NOT_LOGIN(-1003, "用户未登录");

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    ResultStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
