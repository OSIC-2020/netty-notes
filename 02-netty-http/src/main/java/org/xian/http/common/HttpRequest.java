package org.xian.http.common;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author xian
 */
public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private final ChannelHandlerContext context;

    private final FullHttpRequest fullHttpRequest;

    private final Map<String, List<String>> parameters;

    public HttpRequest(ChannelHandlerContext context, FullHttpRequest fullHttpRequest) {

        this.context = context;
        this.fullHttpRequest = fullHttpRequest;
        this.parameters = new QueryStringDecoder(fullHttpRequest.uri()).parameters();
        log.info("处理来自 " + context.channel().remoteAddress() + ",访问 " + getUri() + " 的请求");
    }

    public String getUri() {
        return this.fullHttpRequest.uri().split("\\?")[0];
    }

    public String getMethod() {
        return fullHttpRequest.method().name();
    }

    public Map<String, List<String>> getParameters() {
        return this.parameters;
    }

    public String getParameter(String name) {
        if (this.parameters.get(name) != null) {
            return this.parameters.get(name).get(0);
        } else {
            return null;
        }
    }

    public FullHttpRequest getFullHttpRequest() {
        return fullHttpRequest;
    }

    public ChannelHandlerContext getContext() {
        return context;
    }
}
