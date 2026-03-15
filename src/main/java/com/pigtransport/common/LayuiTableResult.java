package com.pigtransport.common;

/**
 * Layui表格专用返回结果类
 * 适配Layui表格对返回格式的强制要求：code=0表示成功，包含count和data字段
 */
public class LayuiTableResult {
    private int code;       // 状态码：0=成功，1=失败
    private String msg;     // 提示信息
    private long count;     // 数据总条数（分页必填）
    private Object data;    // 表格渲染数据

    // 成功响应（带分页）
    public static LayuiTableResult success(long count, Object data) {
        LayuiTableResult result = new LayuiTableResult();
        result.setCode(0);
        result.setMsg("");
        result.setCount(count);
        result.setData(data);
        return result;
    }

    // 失败响应
    public static LayuiTableResult error(String msg) {
        LayuiTableResult result = new LayuiTableResult();
        result.setCode(1);
        result.setMsg(msg);
        result.setCount(0);
        result.setData(null);
        return result;
    }

    // Getter & Setter
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}