package com.zscat.mallplus.manage.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理返回值工具
 */
public class ResponseUtil {

    public static Map<String, Object> toResponsSuccess(Object data) {
        Map<String, Object> rp =  toResponsObject(200, "执行成功", data);
        return rp;
    }

    public static Map<String, Object> toResponsMsgSuccess(String msg) {
        return  toResponsObject(200, msg, "");
    }

    public static Map<String, Object> toResponsSuccessForSelect(Object data) {
        Map<String, Object> result = new HashMap<>(2);
        result.put("list", data);
        return  toResponsObject(200, "执行成功", result);
    }


    public static Map<String, Object> toResponsFail(String msg) {
        return toResponsObject(1, msg, null);
    }

    /**
     * @param requestCode
     * @param msg
     * @param data
     * @return Map<String   ,   Object>
     * @throws
     * @Description:构建统一格式返回对象
     * @date 2016年9月2日
     * @author zhuliyun
     */
    public static Map<String, Object> toResponsObject(int requestCode, String msg, Object data) {
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("code", requestCode);
        obj.put("msg", msg);
        if (data != null) {
            obj.put("data", data);
        }
        return obj;
    }





}
