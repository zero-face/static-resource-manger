package com.zero.static_resources_manager.error;

/**
 * @author: Zero
 * @Date: 2021/4/2 - 20:12
 * @since: jdk 1.8
 */
public enum EmBustinessError implements CommonError{
    //定义一个通用的错误类型00001
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    //定义一个位置错误的类型来处理一些无法言明的错误
    UNKNOWN_ERROR(10002,"未知错误"),
    //10000开头为用户信息相关错误定义
    USER_NOT_EXIST(20001,"用户不存在"),
    USER_NOT_LOGIN(20003,"用户还未登录"),
    //30000开头的信息为交易信息错误
    STOCK_NOT_ENOUGH(30001,"库存不足")
    ;
    private int errCode;
    private String errMsg;

    EmBustinessError(int errCode,String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrorCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
