package com.apprating.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map;

public class JsonResponse {
    private static final Gson gson = new GsonBuilder().serializeNulls().create();

    public static String success(Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("data", data);
        return gson.toJson(map);
    }

    public static String success(String message, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("message", message);
        map.put("data", data);
        return gson.toJson(map);
    }

    public static String success(String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("message", message);
        return gson.toJson(map);
    }

    public static String error(String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("error", message);
        return gson.toJson(map);
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }
}
