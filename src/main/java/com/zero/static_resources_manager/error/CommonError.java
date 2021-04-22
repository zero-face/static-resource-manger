package com.zero.static_resources_manager.error;

/**
 * @author: Zero
 * @Date: 2021/4/2 - 20:09
 * @since: jdk 1.8
 */
public interface CommonError {
    public int getErrorCode();
    public String getErrMsg();
    public CommonError setErrMsg(String errMsg);
}
