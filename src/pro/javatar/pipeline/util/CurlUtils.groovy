/*
 * Copyright (c) 2020 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.util

import com.cloudbees.groovy.cps.NonCPS

/**
 * @author Borys Zora
 * @version 2020-11-21
 */
class CurlUtils implements Serializable {

    @NonCPS
    static String prepareCommand(RestClient restClient) {
        String method = toMethod(restClient)
        String security = toSecurity(restClient)
        String body = toBody(restClient)
        String headers = toHeaders(restClient)
        String url = toUrl(restClient)
        return "curl ${method} ${security} ${headers} ${url} ${body}"
    }

    @NonCPS
    static String toMethod(RestClient restClient) {
        return "-X ${restClient.httpMethod}"
    }

    @NonCPS
    static String toUrl(RestClient restClient) {
        return "'${restClient.url}'"
    }

    @NonCPS
    static String toSecurity(RestClient restClient) {
        if(restClient.httpSecurity == null) {
            return ""
        }
        if(restClient.httpSecurity == RestClient.HttpSecurity.BASIC) {
            return "-u {USERNAME}:{PASSWORD}"
        }
        if(restClient.httpSecurity == RestClient.HttpSecurity.BEARER) {
            return "-H ${RestClient.HttpHeader.AUTHORIZATION.value}: Bearer ${restClient.credentialId}"
        }
        if(restClient.httpSecurity == RestClient.HttpSecurity.COOKIE) {
            return "-H ${RestClient.HttpHeader.COOKIE.value}: COOKIE_NAME_VALUE"
        }
        return ""
    }

    @NonCPS
    static String toBody(RestClient restClient) {
        return "-d '${restClient.body}'"
    }

    @NonCPS
    static String toHeaders(RestClient restClient) {
        String result = ""
        restClient.headers.each {result += " -H '${it.key}: ${it.value}' " }
        return result
    }

}
