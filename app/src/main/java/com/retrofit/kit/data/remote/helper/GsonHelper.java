package com.retrofit.kit.data.remote.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GsonHelper {

    public static final String TAG = GsonHelper.class.getSimpleName();

    private static GsonHelper instance;

    public static GsonHelper getInstance() {
        if (instance == null) {
            synchronized (GsonHelper.class) {
                if (instance == null) {
                    instance = new GsonHelper();
                }
            }
        }
        return instance;
    }

    public static GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        return gsonBuilder;
    }

    /**
     * Convert String Json into objects;
     *
     * @param jsonString Json data;
     * @param cls        converted class;
     * @return
     */
    public static <T> T getObject(String jsonString, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return t;
    }

    /**
     * Convert Object into String json;
     *
     * @param object class object;
     * @return
     */
    public static String getString(Object object) {
        String jsonString = null;
        try {
            Gson gson = new Gson();
            jsonString = gson.toJson(object);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return jsonString;
    }

    /**
     * Convert Json data into List<Object> collection;
     *
     * @param jsonString Json data;
     * @param cls        will be converted into a collection class;
     * @return
     */
    public static <T> List<T> getArrayList(String jsonString, Class<T> cls) {
        List<T> arrayList = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            arrayList = gson.fromJson(jsonString, new TypeToken<List<T>>() {
            }.getType());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return arrayList;
    }

    /**
     * Convert Json data into Map<String, Object> objects;
     *
     * @param jsonString Json data;
     * @return
     */
    public static Map<String, Object> jsonStringToMap(String jsonString) {
        Map<String, Object> map = new HashMap<>();
        try {
            Gson gson = new Gson();
            map = gson.fromJson(jsonString, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return map;
    }

    /**
     * Convert Json data into List<Map<String, Object>> objects;
     *
     * @param jsonString Json data;
     * @return
     */
    public static List<Map<String, Object>> jsonStringToListMap(String jsonString) {
        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        try {
            Gson gson = new Gson();
            mapArrayList = gson.fromJson(jsonString, new TypeToken<List<Map<String, Object>>>() {
            }.getType());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return mapArrayList;
    }
}
