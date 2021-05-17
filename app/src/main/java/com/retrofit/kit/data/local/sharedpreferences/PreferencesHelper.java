package com.retrofit.kit.data.local.sharedpreferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PreferencesHelper {

    private static PreferencesHelper instance = null;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    @SuppressLint("CommitPrefEdits")
    private PreferencesHelper(Context context, String preferenceFileName) {
        sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
    }

    /**
     * Initialize this class using application Context,
     * should be called once in the beginning by any application Component
     *
     * @param context application context
     */
    public static PreferencesHelper getInstance(Context context) {
        if (context == null) {
            throw new NullPointerException("Provided application context is null");
        }
        if (instance == null) {
            synchronized (PreferencesHelper.class) {
                if (instance == null) {
                    instance = new PreferencesHelper(context, SharedPreferencesConstants.PREFERENCES_HELPER);
                }
            }
        }
        return instance;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    private SharedPreferences.Editor getSharedPreferencesEditor() {
        return sharedPreferencesEditor;
    }

    /**
     * Save data to SharedPreferences
     *
     * @param key   key
     * @param value The data to be saved
     * @return save the result
     */
    public boolean putData(String key, Object value) {
        boolean result;
        String type = value.getClass().getSimpleName();
        try {
            switch (type) {
                case "Boolean":
                    getSharedPreferencesEditor().putBoolean(key, (Boolean) value);
                    break;
                case "Long":
                    getSharedPreferencesEditor().putLong(key, (Long) value);
                    break;
                case "Float":
                    getSharedPreferencesEditor().putFloat(key, (Float) value);
                    break;
                case "String":
                    getSharedPreferencesEditor().putString(key, (String) value);
                    break;
                case "Integer":
                    getSharedPreferencesEditor().putInt(key, (Integer) value);
                    break;
                default:
                    Gson gson = new Gson();
                    String json = gson.toJson(value);
                    getSharedPreferencesEditor().putString(key, json);
                    break;
            }
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        getSharedPreferencesEditor().apply();
        return result;
    }

    /**
     * Get the data saved in SharedPreferences
     *
     * @param key          key
     * @param defaultValue Get failed default value
     * @return data read from SharedPreferences
     */
    public Object getData(String key, Object defaultValue) {
        Object result;
        String type = defaultValue.getClass().getSimpleName();
        try {
            switch (type) {
                case "Boolean":
                    result = getSharedPreferences().getBoolean(key, (Boolean) defaultValue);
                    break;
                case "Long":
                    result = getSharedPreferences().getLong(key, (Long) defaultValue);
                    break;
                case "Float":
                    result = getSharedPreferences().getFloat(key, (Float) defaultValue);
                    break;
                case "String":
                    result = getSharedPreferences().getString(key, (String) defaultValue);
                    break;
                case "Integer":
                    result = getSharedPreferences().getInt(key, (Integer) defaultValue);
                    break;
                default:
                    Gson gson = new Gson();
                    String json = getSharedPreferences().getString(key, "");
                    if (!json.equals("") && json.length() > 0) {
                        result = gson.fromJson(json, defaultValue.getClass());
                    } else {
                        result = defaultValue;
                    }
                    break;
            }
        } catch (Exception e) {
            result = null;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Used to save the collection
     *
     * @param key  key
     * @param list collection data
     * @return save the result
     */
    public <T> boolean putListData(String key, List<T> list) {
        boolean result;
        String type = list.get(0).getClass().getSimpleName();
        JsonArray array = new JsonArray();
        try {
            switch (type) {
                case "Boolean":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Boolean) list.get(i));
                    }
                    break;
                case "Long":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Long) list.get(i));
                    }
                    break;
                case "Float":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Float) list.get(i));
                    }
                    break;
                case "String":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((String) list.get(i));
                    }
                    break;
                case "Integer":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Integer) list.get(i));
                    }
                    break;
                default:
                    Gson gson = new Gson();
                    for (int i = 0; i < list.size(); i++) {
                        JsonElement obj = gson.toJsonTree(list.get(i));
                        array.add(obj);
                    }
                    break;
            }
            getSharedPreferencesEditor().putString(key, array.toString());
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        getSharedPreferencesEditor().apply();
        return result;
    }

    /**
     * Get the saved List
     *
     * @param key key
     * @return corresponding list collection
     */
    public <T> List<T> getListData(String key, Class<T> cls) {
        List<T> list = new ArrayList<>();
        String json = getSharedPreferences().getString(key, "");
        if (!json.equals("") && json.length() > 0) {
            Gson gson = new Gson();
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement elem : array) {
                list.add(gson.fromJson(elem, cls));
            }
        }
        return list;
    }

    /**
     * Used to save the collection
     *
     * @param key key
     * @param map map data
     * @return save the result
     */
    public <K, V> boolean putHashMapData(String key, Map<K, V> map) {
        boolean result;
        try {
            Gson gson = new Gson();
            String json = gson.toJson(map);
            getSharedPreferencesEditor().putString(key, json);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        getSharedPreferencesEditor().apply();
        return result;
    }

    /**
     * Used to save the collection
     *
     * @param key key
     * @return HashMap
     */
    public <V> HashMap<String, V> getHashMapData(String key, Class<V> clsV) {
        String json = getSharedPreferences().getString(key, "");
        HashMap<String, V> map = new HashMap<>();
        Gson gson = new Gson();
        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            String entryKey = entry.getKey();
            JsonObject value = (JsonObject) entry.getValue();
            map.put(entryKey, gson.fromJson(value, clsV));
        }
        return map;
    }

    /**
     * Clears all data in SharedPreferences
     */
    public void clearPrefs() {
        getSharedPreferencesEditor().clear().apply();
    }

    public void removeKey(String key) {
        getSharedPreferencesEditor().remove(key).commit();
    }

    public boolean containsKey(String key) {
        return getSharedPreferences().contains(key);
    }
}