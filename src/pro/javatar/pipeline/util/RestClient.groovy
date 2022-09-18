/*
 * Copyright (c) 2020 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.util

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.jenkins.api.JenkinsDsl

/**
 * @author Borys Zora
 * @version 2020-11-21
 */
class RestClient implements Serializable {

    String url
    String body
    HttpMethod httpMethod
    HttpSecurity httpSecurity = null // TODO = HttpSecurity.BEARER
    String credentialId
    // HttpMediaType httpMediaType = HttpMediaType.JSON
    Map<HttpHeader, String> headers = new HashMap<>()
    JenkinsDsl service

    RestClient(JenkinsDsl service) {
        this.service = service
    }

    @NonCPS
    String execute() {
        String curlCommand = CurlUtils.prepareCommand(this)
        return service.getShellExecutionResponse(curlCommand)
    }

    @NonCPS
    RestClient post(String url) {
        this.url = url
        httpMethod = HttpMethod.POST
        return this
    }

    @NonCPS
    RestClient get(String url) {
        this.url = url
        httpMethod = HttpMethod.GET
        return this
    }

    @NonCPS
    RestClient put(String url) {
        this.url = url
        httpMethod = HttpMethod.PUT
        return this
    }

    @NonCPS
    RestClient delete(String url) {
        this.url = url
        httpMethod = HttpMethod.DELETE
        return this
    }

    @NonCPS
    RestClient withBody(String body) {
        this.body = body
        return this
    }

    @NonCPS
    RestClient security(HttpSecurity httpSecurity, String credentialId) {
        this.httpSecurity = httpSecurity
        this.credentialId = credentialId
        return this
    }

    @NonCPS
    RestClient contentType(HttpMediaType httpMediaType) {
        // this.httpMediaType = httpMediaType
        headers.put("Content-Type", httpMediaType.value)
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
