/*
 * Copyright (c) 2020 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.util

/**
 * @author Borys Zora
 * @version 2020-11-21
 */
class CurlUtils {

    static String prepareCommand(RestClient restClient) {
        String method = toMethod(restClient)
        String security = toSecurity(restClient)
        String body = toBody(restClient)
        String headers = toHeaders(restClient)
        String url = toUrl(restClient)
        return "curl ${method} ${security} ${headers} ${url} ${body}"
    }

    static String toMethod(RestClient restClient) {
        return "-X ${restClient.httpMethod}"
    }

    static String toUrl(RestClient restClient) {
        return restClient.url
    }

    static String toSecurity(RestClient restClient) {
        if(restClient.httpSecurity == null) {
            return ""
        }
        if(restClient.httpSecurity == RestClient.HttpSecurity.BASIC) {
            return "-u {USERNAME}:{PASSWORD}"
        }
        if(restClient.httpSecurity == RestClient.HttpSecurity.BEARER) {
            return "-H ${RestClient.HttpHeader.AUTHORIZATION.value}: Bearer TOKEN"
        }
        if(restClient.httpSecurity == RestClient.HttpSecurity.COOKIE) {
            return "-H ${RestClient.HttpHeader.COOKIE.value}: COOKIE_NAME_VALUE"
        }
        return ""
    }

    static String toBody(RestClient restClient) {
        return "-d ${restClient.body}"
    }

    static String toHeaders(RestClient restClient) {
        return restClient.headers.stream()
                .map({ k, v -> " -H ${k.value}: ${v} " })
                .reduce(String.&concat)
    }

}
