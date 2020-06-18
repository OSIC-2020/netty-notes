package org.xian.http.common;


/**
 * Servlet 抽象类
 *
 * @author xian
 */
public abstract class BaseServlet {

    private final static String GET = "GET";

    public void service(HttpRequest request, HttpResponse response) {
        if (GET.equalsIgnoreCase(request.getMethod())) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }
    }


    /**
     * Get 方法访问的抽象函数
     *
     * @param request  Request
     * @param response Response
     */
    public abstract void doGet(HttpRequest request, HttpResponse response);

    /**
     * Post 方法访问的抽象函数
     *
     * @param request  Request
     * @param response Response
     */
    public abstract void doPost(HttpRequest request, HttpResponse response);
}
