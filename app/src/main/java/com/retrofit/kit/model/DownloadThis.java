package com.retrofit.kit.model;

public class DownloadThis {

    private int id;
    private String name;
    public String description;
    private String fileDownloadFromThisUrl;

    public DownloadThis(int id, String name, String description, String fileDownloadFromThisUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.fileDownloadFromThisUrl = fileDownloadFromThisUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileDownloadFromThisUrl() {
        return fileDownloadFromThisUrl;
    }

    public void setFileDownloadFromThisUrl(String fileDownloadFromThisUrl) {
        this.fileDownloadFromThisUrl = fileDownloadFromThisUrl;
    }
}