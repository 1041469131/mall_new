package com.zscat.mallplus.mbg.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author xiang.li
 * create date 2019/5/29
 * description
 */

@JsonIgnoreProperties(
    ignoreUnknown = true
)
public class CommonRequest<T> {
    private String memberId;
    private Long matcherUserId;

    T paramter;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public T getParamter() {
        return paramter;
    }

    public void setParamter(T paramter) {
        this.paramter = paramter;
    }

    public Long getMatcherUserId() {
        return matcherUserId;
    }

    public CommonRequest<T> setMatcherUserId(Long matcherUserId) {
        this.matcherUserId = matcherUserId;
        return this;
    }
}
