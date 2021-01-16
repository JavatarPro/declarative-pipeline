/*
 * Copyright (c) 2020 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.util

import pro.javatar.pipeline.jenkins.api.JenkinsDslService

/**
 * @author Borys Zora
 * @version 2020-11-21
 */
class RestClient {

    String url
    String body
    HttpMethod httpMethod
    HttpSecurity httpSecurity = HttpSecurity.BEARER
    String credentialId
    HttpMediaType httpMediaType = HttpMediaType.JSON
    Map<HttpHeader, String> headers = new HashMap<>()
    JenkinsDslService service

    RestClient(JenkinsDslService service) {
        this.service = service
    }

    String execute() {
        String curlCommand = CurlUtils.prepareCommand(this)
        return service.getShellExecutionResponse(curlCommand)
    }

    RestClient post(String url) {
        this.url = url
        httpMethod = HttpMethod.POST
        return this
    }

    RestClient get(String url) {
        this.url = url
        httpMethod = HttpMethod.GET
        return this
    }

    RestClient put(String url) {
        this.url = url
        httpMethod = HttpMethod.PUT
        return this
    }

    RestClient delete(String url) {
        this.url = url
        httpMethod = HttpMethod.DELETE
        return this
    }

    RestClient withBody(String body) {
        this.body = body
        return this
    }

    RestClient security(HttpSecurity httpSecurity, String credentialId) {
        this.httpSecurity = httpSecurity
        this.credentialId = credentialId
        return this
    }

    RestClient contentType(HttpMediaType httpMediaType) {
        this.httpMediaType = httpMediaType
        return this
    }

    enum HttpMethod {
        POST,
        GET,
        PUT,
        DELETE
    }

    enum HttpHeader {
        CONTENT_TYPE("Content-type"),
        AUTHORIZATION("Authorization"),
        COOKIE("Cookie")

        String value

        HttpHeader(String value) {
            this.value = value
        }
    }

    enum HttpSecurity {
        BASIC,
        BEARER,
        COOKIE
    }

    enum HttpMediaType {
        JSON("application/json"),
        FORM("application/x-www-form-urlencoded")

        String value
        HttpMediaType(String value) {
            this.value = value
        }
    }
}
