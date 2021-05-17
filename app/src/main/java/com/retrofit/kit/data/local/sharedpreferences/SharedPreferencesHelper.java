package com.retrofit.kit.data.local.sharedpreferences;

import android.content.Context;

public class SharedPreferencesHelper extends ApplicationSharedPreference {

    public SharedPreferencesHelper(Context context, String preferenceFileName) {
        super(context, preferenceFileName);
    }

    public void setLanguageStatus(boolean value) {
        setBooleanData(SharedPreferencesConstants.IS_LANGUAGE_SELECT, value);
    }

    public boolean getLanguageStatus() {
        return getBooleanData(SharedPreferencesConstants.IS_LANGUAGE_SELECT);
    }

    public void setLoginStatus(boolean value) {
        setBooleanData(SharedPreferencesConstants.LOGIN_STATUS, value);
    }

    public boolean getLoginStatus() {
        return getBooleanData(SharedPreferencesConstants.LOGIN_STATUS);
    }

    public void setId(String value) {
        setStringData(SharedPreferencesConstants.ID, value);
    }

    public String getId() {
        return getStringData(SharedPreferencesConstants.ID);
    }

    public void setProvider(String value) {
        setStringData(SharedPreferencesConstants.PROVIDER, value);
    }

    public String getProvider() {
        return getStringData(SharedPreferencesConstants.PROVIDER);
    }

    public void setSocialId(String value) {
        setStringData(SharedPreferencesConstants.SOCIAL_ID, value);
    }

    public String getSocialId() {
        return getStringData(SharedPreferencesConstants.SOCIAL_ID);
    }

    public void setPicture(String value) {
        setStringData(SharedPreferencesConstants.PICTURE, value);
    }

    public String getPicture() {
        return getStringData(SharedPreferencesConstants.PICTURE);
    }

    public void setFirstName(String value) {
        setStringData(SharedPreferencesConstants.FIRST_NAME, value);
    }

    public String getFirstName() {
        return getStringData(SharedPreferencesConstants.FIRST_NAME);
    }

    public void setLastName(String value) {
        setStringData(SharedPreferencesConstants.LAST_NAME, value);
    }

    public String getLastName() {
        return getStringData(SharedPreferencesConstants.LAST_NAME);
    }

    public void setGender(String value) {
        setStringData(SharedPreferencesConstants.GENDER, value);
    }

    public String getGender() {
        return getStringData(SharedPreferencesConstants.GENDER);
    }

    public void setCountryCode(String value) {
        setStringData(SharedPreferencesConstants.COUNTRY_CODE, value);
    }

    public String getCountryCode() {
        return getStringData(SharedPreferencesConstants.COUNTRY_CODE);
    }

    public void setPhone(String value) {
        setStringData(SharedPreferencesConstants.PHONE_NUMBER, value);
    }

    public String getPhone() {
        return getStringData(SharedPreferencesConstants.PHONE_NUMBER);
    }

    public void setPhoneNumberVerified(String value) {
        setStringData(SharedPreferencesConstants.PHONE_NUMBER_VERIFIED, value);
    }

    public String getPhoneNumberVerified() {
        return getStringData(SharedPreferencesConstants.PHONE_NUMBER_VERIFIED);
    }

    public void setEmail(String value) {
        setStringData(SharedPreferencesConstants.EMAIL, value);
    }

    public String getEmail() {
        return getStringData(SharedPreferencesConstants.EMAIL);
    }

    public void setEmailVerified(String value) {
        setStringData(SharedPreferencesConstants.EMAIL_VERIFIED, value);
    }

    public String getEmailVerified() {
        return getStringData(SharedPreferencesConstants.EMAIL_VERIFIED);
    }

    public void setFcmToken(String value) {
        setStringData(SharedPreferencesConstants.FCM_TOKEN, value);
    }

    public String getFcmToken() {
        return getStringData(SharedPreferencesConstants.FCM_TOKEN);
    }

    public void setLastLogin(String value) {
        setStringData(SharedPreferencesConstants.LAST_LOGIN, value);
    }

    public String getLastLogin() {
        return getStringData(SharedPreferencesConstants.LAST_LOGIN);
    }

    public void setCreatedAt(String value) {
        setStringData(SharedPreferencesConstants.CREATED_AT, value);
    }

    public String getCreatedAt() {
        return getStringData(SharedPreferencesConstants.CREATED_AT);
    }

    public void setUpdatedAt(String value) {
        setStringData(SharedPreferencesConstants.UPDATED_AT, value);
    }

    public String getUpdatedAt() {
        return getStringData(SharedPreferencesConstants.UPDATED_AT);
    }

    public void setExpiredAt(String value) {
        setStringData(SharedPreferencesConstants.EXPIRED_AT, value);
    }

    public String getExpiredAt() {
        return getStringData(SharedPreferencesConstants.EXPIRED_AT);
    }

    public void setAccountVerifiedByAdmin(String value) {
        setStringData(SharedPreferencesConstants.ACCOUNT_VERIFIED_BY_ADMIN, value);
    }

    public String getAccountVerifiedByAdmin() {
        return getStringData(SharedPreferencesConstants.ACCOUNT_VERIFIED_BY_ADMIN);
    }
}
