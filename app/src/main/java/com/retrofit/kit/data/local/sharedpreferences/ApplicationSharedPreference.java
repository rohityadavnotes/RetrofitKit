package com.retrofit.kit.data.local.sharedpreferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import java.util.Map;

public class ApplicationSharedPreference {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor sharedPreferencesEditor;

    @SuppressLint("CommitPrefEdits")
    public ApplicationSharedPreference(Context context, String preferenceFileName) {
        sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    private SharedPreferences.Editor getSharedPreferencesEditor() {
        return sharedPreferencesEditor;
    }

    /**
     * Sets boolean data.
     *
     * @param key   the key
     * @param value the value
     */
    public void setBooleanData(String key, boolean value) {
        getSharedPreferencesEditor().putBoolean(key, value);
        getSharedPreferencesEditor().commit();
    }

    /**
     * Gets boolean data.
     *
     * @param key the key
     * @return the boolean data
     */
    public boolean getBooleanData(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    public boolean getBooleanData(String key, boolean defaultValue) {
        return getSharedPreferences().getBoolean(key, defaultValue);
    }

    /**
     * Sets string data.
     *
     * @param key   the key
     * @param value the value
     */
    public void setStringData(String key, String value) {
        getSharedPreferencesEditor().putString(key, value);
        getSharedPreferencesEditor().commit();
    }

    /**
     * Gets string data.
     *
     * @param key the key
     * @return the string data
     */
    public String getStringData(String key) {
        return getSharedPreferences().getString(key, null);
    }

    public String getStringData(String key, String defaultValue) {
        return getSharedPreferences().getString(key, defaultValue);
    }

    /**
     * Sets int data.
     *
     * @param key   the key
     * @param value the value
     */
    public void setIntData(String key, int value) {
        getSharedPreferencesEditor().putInt(key, value);
        getSharedPreferencesEditor().commit();
    }

    /**
     * Gets int data.
     *
     * @param key the key
     * @return the int data
     */
    public int getIntData(String key) {
        return getSharedPreferences().getInt(key, 0);
    }

    public int getIntData(String key, int defaultValue) {
        return getSharedPreferences().getInt(key, defaultValue);
    }

    /**
     * Sets float data.
     *
     * @param key   the key
     * @param value the value
     */
    public void setFloatData(String key, float value) {
        getSharedPreferencesEditor().putFloat(key, value);
        getSharedPreferencesEditor().commit();
    }

    /**
     * Gets float data.
     *
     * @param key the key
     * @return the float data
     */
    public float getFloatData(String key) {
        return getSharedPreferences().getFloat(key, 0f);
    }

    public float getFloatData(String key, float defaultValue) {
        return getSharedPreferences().getFloat(key, defaultValue);
    }

    /**
     * Sets long data.
     *
     * @param key   the key
     * @param value the value
     */
    public void setLongData(String key, float value) {
        getSharedPreferencesEditor().putFloat(key, value);
        getSharedPreferencesEditor().commit();
    }

    /**
     * Gets long data.
     *
     * @param key the key
     * @return the long data
     */
    public long getLongData(String key) {
        return getSharedPreferences().getLong(key, 0L);
    }

    public long getLongData(String key, long defaultValue) {
        return getSharedPreferences().getLong(key, defaultValue);
    }

    /**
     * Sets double data.
     * <p>
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to cast to and from String.
     *
     * @param key   the key
     * @param value the value
     */
    public void setDoubleData(String key, double value) {
        getSharedPreferencesEditor().putString(key, String.valueOf(value));
        getSharedPreferencesEditor().commit();
    }

    /**
     * Gets double data.
     * <p>
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to cast to and from String.
     *
     * @param key the key
     */
    public double getDoubleData(String key) {
        String value = getSharedPreferences().getString(key, "0.0");
        return Double.parseDouble(value);
    }

    public double getDoubleData(String key, double defaultValue) {
        try {
            return Double.parseDouble(getSharedPreferences().getString(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * Check key is present or not in SharedPreferences
     */
    public boolean containsKey(String key) {
        return getSharedPreferences().contains(key);
    }

    /**
     * Check key is exit or not in SharedPreferences
     * <p>
     * example :
     * public long getLong(String key) {
     * if (isKeyExists(key)) return sharedPreferences.getLong(key, 0);
     * return 0;
     * }
     */
    public boolean isKeyExists(String key) {
        Map<String, ?> map = sharedPreferences.getAll();
        if (map.containsKey(key)) return true;
        else {
            System.out.println("ApplicationSharedPreference : No such KEY " + key);
            return false;
        }
    }

    /**
     * Clears all data in SharedPreferences
     */
    public void clearSharedPreferences() {
        getSharedPreferencesEditor().clear();
        getSharedPreferencesEditor().commit();
    }
}