package com.dsapps.newsreader.Model;

import java.util.List;

/**
 * Created by Shridhar on 07-May-18.
 */

public class Website
{
    private String status;
    private List<Source> sources;

    public Website() {
    }

    public Website(String status, List<Source> sources) {
        this.status = status;
        this.sources = sources;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }
}
