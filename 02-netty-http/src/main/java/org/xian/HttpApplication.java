package org.xian;

import org.xian.http.HttpServer;

/**
 * @author xian
 */
public class HttpApplication {
    public static void main(String[] args) {
        new HttpServer(8080).start();
    }
}
