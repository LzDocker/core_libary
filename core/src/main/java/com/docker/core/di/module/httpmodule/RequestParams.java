package com.docker.core.di.module.httpmodule;

import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by zhangshaofang on 2017/8/9.
 */

public class RequestParams {
    private Map<String, Object> header;
    private Map<String, Object> body;

    public Map<String, Object> getHeader() {
        return header;
    }

    public void setHeader(Map<String, Object> header) {
        this.header = header;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }
    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }
}
