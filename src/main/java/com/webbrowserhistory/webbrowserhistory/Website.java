package com.webbrowserhistory.webbrowserhistory;

public class Website {
    private final String url;
    private final String websiteTitle;
    private final String pageTitle;
    private final String description;

    public Website(String url, String websiteTitle, String pageTitle, String description) {
        this.url = url;
        this.websiteTitle = websiteTitle;
        this.pageTitle = pageTitle;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public String getWebsiteTitle() {
        return websiteTitle;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return websiteTitle;
    }

}
