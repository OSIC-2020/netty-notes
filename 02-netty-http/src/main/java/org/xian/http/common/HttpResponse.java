package org.xian.http.common;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.xian.http.common.HttpRequest;

/**
 * @author xian
 */
public class HttpResponse {
    private ChannelHandlerContext context;
    private HttpRequest request;

    public HttpResponse(ChannelHandlerContext context, HttpRequest request) {
        this.context = context;
        this.request = request;
    }

    public void write(String out) {
        if (out != null && out.length() != 0) {
            // Response 返回输出的内容
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    // 将输出的信息封装为 Netty 的 ByteBuffer
                    Unpooled.copiedBuffer(out, CharsetUtil.UTF_8)
            );
            // 设置 Http 的头部信息
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
            context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
