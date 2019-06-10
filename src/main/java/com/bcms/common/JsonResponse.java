package com.bcms.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @className JsonResponse
 * @description: Json响应结果
 * @author: Jackie
 * @date: 2018/12/22 14:22
 */
public class JsonResponse {
    /**
     * 成功状态
     */
    private Boolean success;
    /**
     * 数据内容
     */
    private Object content;
    /**
     * 提示信息
     */
    private String msg;
    /**
     * 状态码
     */
    private String code;
    /**
     * 状态码key
     */
    private static final String CODE_KEY = "code";
    /**
     * 成功状态key
     */
    private static final String STATUS_SUCCESS = "success";
    // private static final String STATUS_FAILURE = "failure";
    /**
     * 数据字段key
     */
    private static final String DATA_KEY = "content";
    /**
     * 提示信息key
     */
    private static final String MSG_KEY = "msg";
    /**
     * 成功状态码
     */
    private static final String SUCCESS_CODE = "0";
    /**
     * 错误状态码
     */
    private static final String FAILURE_CODE = "-1";

    public Boolean getSuccess() {
        return success;
    }

    public Object getContent() {
        return content;
    }

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public JsonResponse buildSuccessResult(Object content) {
        this.success = true;
        this.content = content;
        this.msg = "执行成功";
        this.code = SUCCESS_CODE;
        return this;
    }

    public JsonResponse buildSuccessResult(Object content, String msg) {
        this.success = true;
        this.content = content;
        this.msg = msg;
        this.code = SUCCESS_CODE;
        return this;
    }

    public JsonResponse buildSuccessResult(Boolean success, Object content, String msg) {
        this.success = success;
        this.content = content;
        this.msg = msg;
        this.code = SUCCESS_CODE;
        return this;
    }

    public JsonResponse buildErrorResult() {
        this.success = false;
        this.content = null;
        this.msg = "执行失败";
        this.code = FAILURE_CODE;
        return this;
    }

    public JsonResponse buildErrorResult(String msg) {
        this.success = false;
        this.content = null;
        this.msg = msg;
        this.code = FAILURE_CODE;
        return this;
    }

    public JsonResponse buildErrorResult(String code, String msg) {
        this.success = false;
        this.content = null;
        this.msg = msg;
        this.code = code;
        return this;
    }

    public Map<String, Object> toMap() {
        if (this.code == null) {
            this.code = SUCCESS_CODE;
        }
        return getMapResult(this.code, this.content, this.msg);
    }

    /**
     * @Description: 取得执行成功的返回
     * @Params: []
     * @Return: java.util.Map<java.lang.String, java.lang.Object>
     * @Author: Jackie
     * @Date: 2018/12/22 14:32
     */
    public static Map<String, Object> getSuccessResult() {
        return getMapResult(SUCCESS_CODE, null, "执行成功");
    }

    /**
     * @Description: 取得执行成功的返回
     * @Params: [msg]
     * @Return: java.util.Map<java.lang.String, java.lang.Object>
     * @Author: Jackie
     * @Date: 2018/12/23 16:11
     */
    public static Map<String, Object> getSuccessResult(String msg) {
        return getMapResult(SUCCESS_CODE, null, msg);
    }

    /**
     * @Description: 取得执行成功的返回
     * @Params: [data, msg]
     * @Return: java.util.Map<java.lang.String, java.lang.Object>
     * @Author: Jackie
     * @Date: 2018/12/22 14:32
     */
    public static <T> Map<String, Object> getSuccessResult(T data, String msg) {
        return getMapResult(SUCCESS_CODE, data, msg);
    }

    public static Map<String, Object> getErrorResult(String errDesc) {
        return getMapResult(FAILURE_CODE, null, errDesc);
    }

    /**
     * @Description: 取得执行失败的返回，错误码不可为"0"
     * @Params: [code, errDesc]
     * @Return: java.util.Map<java.lang.String, java.lang.Object>
     * @Author: Jackie
     * @Date: 2018/12/22 14:31
     */
    public static Map<String, Object> getErrorResult(String code, String errDesc) {
        return getMapResult(code, null, errDesc);
    }

    public static <T> Map<String, Object> getErrorResult(T data, String errDesc) {
        return getMapResult(FAILURE_CODE, data, errDesc);
    }

    /**
     * @Description: 成功则为0
     * @Params: [code, data, msg]
     * @Return: java.util.Map<java.lang.String, java.lang.Object>
     * @Author: Jackie
     * @Date: 2018/12/22 14:31
     */
    private static <T> Map<String, Object> getMapResult(String code, T data, String msg) {
        Map<String, Object> map = new HashMap<>(16);
        map.put(STATUS_SUCCESS, SUCCESS_CODE.equals(code));
        map.put(DATA_KEY, (data == null) ? "" : data);
        map.put(MSG_KEY, (msg == null) ? "" : msg);
        map.put(CODE_KEY, code);
        return map;
    }

    @Override
    public String toString() {
        ObjectMapper om = new ObjectMapper();
        try {
            return om.writeValueAsString(toMap());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
