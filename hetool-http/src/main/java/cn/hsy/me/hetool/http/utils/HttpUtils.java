package cn.hsy.me.hetool.http.utils;

import cn.hsy.me.hetool.http.enums.Header;
import cn.hsy.me.hetool.http.enums.Protocol;

import javax.servlet.http.HttpServletRequest;

/**
 * http简单工具类
 * @author heshiyuan
 * @date 2020/7/22 16:21
 */
public class HttpUtils {

    public static String getScheme(HttpServletRequest request) {
        if (Protocol.HTTPS.getProtocol().equals(request.getHeader(Header.PROTOCOL.name()))) {
            return Protocol.HTTPS.getProtocol();
        }
        return request.getScheme();
    }

    public static String getReferer(HttpServletRequest request) {
        String url = request.getHeader(Header.REFERER.name());
        if (url == null) {
            return "/";
        }
        if (url.startsWith("https://") || url.startsWith("http://")) {
            int n = url.indexOf('/', 8);
            if (n == (-1)) {
                return "/";
            }
            url = url.substring(n);
        }
        if (url.startsWith("/auth/")) {
            return "/";
        }
        return url;
    }

    public static boolean isSecure(HttpServletRequest request) {
        return "https".equals(getScheme(request));
    }

    /**
     * Try parse location from IP.
     *
     * @param request The http request.
     * @return Location like "US".
     */
    public static String getIPLocation(HttpServletRequest request) {
        String location = null;
        location = request.getHeader(Header.IP_COUNTRY.name());
        if (location != null && !location.isEmpty()) {
            return location;
        }
        return "UNKNOWN";
    }

    /**
     * Try get IP address from request.
     *
     * @param request The http request.
     * @return IPv4 address like "10.0.1.1".
     */
    public static String getIPAddress(HttpServletRequest request) {
        String ip = null;
        ip = request.getHeader(Header.CF_CONNECTING_IP.name());
        if (ip != null && !ip.isEmpty()) {
            return ip;
        }
        ip = request.getHeader(Header.X_REAL_IP.name());
        if (ip != null && !ip.isEmpty()) {
            return ip;
        }
        ip = request.getHeader(Header.X_FORWARDED_FOR.name());
        if (ip != null && !ip.isEmpty()) {
            int pos = ip.indexOf(',');
            if (pos == -1) {
                return ip;
            }
            return ip.substring(0, pos);
        }
        ip = request.getRemoteAddr();
        // convert IPv6 to IPv4:
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    /**
     * Try get user-agent from request.
     *
     * @param request The http request.
     * @return The user agent like "Mozilla/5.0 (Macintosh) Chrome/71.0"
     */
    public static String getUserAgent(HttpServletRequest request) {
        String ua = request.getHeader(Header.USER_AGENT.name());
        if (ua == null) {
            return "";
        }
        if (ua.length() > 997) {
            ua = ua.substring(0, 997) + "...";
        }
        return ua;
    }
}
