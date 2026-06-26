package com.github.samuelz47.core;

import io.restassured.http.ContentType;

public interface env {
    String APP_BASE_URL = "http://barrigarest.wcaquino.me";
    String APP_BASE_PATH = "";
    ContentType APP_CONTENT_TYPE = ContentType.JSON;
    Long MAX_TIMEOUT = 10000L;
}
