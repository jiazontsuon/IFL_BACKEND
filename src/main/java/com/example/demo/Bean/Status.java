package com.example.demo.Bean;

public enum Status {
	/**
     * 状态
     */
    OK("200", "正确"),
    BAD_REQUEST("400", "错误的请求"),
    UNAUTHORIZED("401", "禁止访问"),
    NOT_FOUND("404", "没有可用的数据"),
    PWD_USERNAME_ERROR("300", "密码或账号错误"),
    EXIT("403", "已经存在"),
    INTERNAL_SERVER_ERROR("500", "服务器遇到了一个未曾预料的状况"),
    SERVICE_UNAVAILABLE("503", "服务器当前无法处理请求"),
    ERROR("9999", "数据不能为空");
    /**
     * 状态码,长度固定为6位的字符串.
     */
    private String code;

    /**
     * 错误信息.
     */
    private String reason;

    Status(String code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public String getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return code + ": " + reason;
    }
}
