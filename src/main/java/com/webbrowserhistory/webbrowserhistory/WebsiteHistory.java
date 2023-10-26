package com.webbrowserhistory.webbrowserhistory;

import java.time.LocalDateTime;

public class WebsiteHistory extends Website {
    private final LocalDateTime visited;

    public WebsiteHistory(String url, String pageTitle, String websiteTitle, String description) {
        super(url, pageTitle, websiteTitle, description);
        this.visited = LocalDateTime.now();
    }

    public LocalDateTime getVisited() {
        return visited;
    }
}
