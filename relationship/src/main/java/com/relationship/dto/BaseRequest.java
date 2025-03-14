package com.relationship.dto;

public class BaseRequest {
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "BaseRequest{" +
                "version='" + version + '\'' +
                '}';
    }
}
