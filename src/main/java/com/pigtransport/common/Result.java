package com.pigtransport.common;


import lombok.Data;
import java.io.Serializable;

/**
 * 统一返回结果类
 * 用于所有Controller接口的返回值
 */
@Data
public class Result<T> implements Serializable {

    /**
     * 状态码
     * 200: 成功
     * 400: 客户端错误（参数错误等）
     * 401: 未登录
     * 403: 无权限
     * 404: 资源不存在
     * 500: 服务器错误
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 私有构造方法
     */
    private Result() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功结果（无数据）
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功结果（有数据）
     */
    public static <T> Result<T> success(T data) {
        return success("操作成功", data);
    }

    /**
     * 成功结果（自定义消息）
     */
    public static <T> Result<T> success(String message) {
        return success(message, null);
    }

    /**
     * 成功结果（自定义消息和数据）
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 失败结果（默认消息）
     */
    public static <T> Result<T> error() {
        return error("操作失败");
    }

    /**
     * 失败结果（自定义消息）
     */
    public static <T> Result<T> error(String message) {
        return error(500, message);
    }

    /**
     * 失败结果（自定义状态码和消息）
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    /**
     * 参数错误
     */
    public static <T> Result<T> paramError(String message) {
        return error(400, message);
    }

    /**
     * 未登录
     */
    public static <T> Result<T> notLogin() {
        return error(401, "用户未登录");
    }

    /**
     * 无权限
     */
    public static <T> Result<T> noPermission() {
        return error(403, "无操作权限");
    }

    /**
     * 资源不存在
     */
    public static <T> Result<T> notFound() {
        return error(404, "资源不存在");
    }

    /**
     * 业务异常
     */
    public static <T> Result<T> businessError(String message) {
        return error(400, message);
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return this.code != null && this.code == 200;
    }

    /**
     * 获取数据（安全方式）
     */
    public T getDataOrDefault(T defaultValue) {
        return this.data != null ? this.data : defaultValue;
    }

    /**
     * 链式调用设置消息（不常用）
     */
    public Result<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    /**
     * 链式调用设置数据（不常用）
     */
    public Result<T> data(T data) {
        this.setData(data);
        return this;
    }
}