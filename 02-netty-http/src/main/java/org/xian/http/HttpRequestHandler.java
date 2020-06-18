package org.xian.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.xian.http.common.BaseServlet;
import org.xian.http.common.HttpRequest;
import org.xian.http.common.HttpResponse;
import org.xian.http.common.HttpServletMapping;

import java.util.Map;

/**
 * 处理 HTTP 请求
 *
 * @author xian
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    @Override
    protected void channelRead0(ChannelHandlerContext context, FullHttpRequest fullHttpRequest) {
        HttpRequest request = new HttpRequest(context, fullHttpRequest);
        String uri = request.getUri();
        // 封装成自定义的 Response，因为自带的 DefaultFullHttpResponse 的 content（ByteBuffer）是使用替换的方式处理的
        HttpResponse response = new HttpResponse(context, request);
        // Http 路径和其实例的对象
        Map<String, BaseServlet> servletMapping = HttpServletMapping.getServletMapping();
        // 根据 servletMapping 调用 Servlet 的 service() 方法
        if (servletMapping != null && servletMapping.containsKey(uri)) {
            servletMapping.get(uri).service(request, response);
        } else {
            response.write("404 -- Not Found");
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        //
        ctx.flush();
    }


}
