package cn.hsy.me.hetool.http.enums;

import lombok.Getter;

/**
 * @author heshiyuan
 * @date 2020/7/22 16:52
 */
public enum Protocol {
    HTTPS("https"),
    HTTP("http")
    ;
    @Getter
    private String protocol;

    Protocol(String protocol) {
        this.protocol = protocol;
    }
}
