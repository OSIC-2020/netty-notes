package org.xian.servlet;

import org.xian.http.common.BaseServlet;
import org.xian.http.common.HttpRequest;
import org.xian.http.common.HttpResponse;

/**
 * @author xian
 */
public class HelloServlet extends BaseServlet {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        response.write("Hello,HTTP Server by Netty");
    }
}
