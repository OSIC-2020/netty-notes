package org.xian.servlet;

import org.xian.http.common.BaseServlet;
import org.xian.http.common.HttpRequest;
import org.xian.http.common.HttpResponse;

public class SecondServlet extends BaseServlet {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        String username = request.getParameter("username");
        response.write("用户名是 " + username);
    }
}
