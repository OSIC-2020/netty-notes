package org.xian.http.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 初始化 Http 请求路径和其对应的 Servlet 实例
 *
 * @author xian
 */
public class HttpServletMapping {

    private static final Logger log = LoggerFactory.getLogger(HttpServletMapping.class);
    /**
     * 存储 Servlet 的配置和其实例对象
     */
    private volatile static Map<String, BaseServlet> servletMapping;

    /**
     * 用于加载配置文件
     */
    private static final Properties PROPERTIES = new Properties();

    private HttpServletMapping() {
    }

    /**
     * @return Http Servlet 的路径和其对应的实例
     */
    public static Map<String, BaseServlet> getServletMapping() {
        if (servletMapping == null) {
            init();
        }
        return servletMapping;
    }

    private static synchronized void init() {
        if (servletMapping == null) {
            servletMapping = new HashMap<>(32);
            // 创建 servletMapping
            // 读取配置文件 http.properties ,同时初始化 ServletMapping
            try {
                String path = HttpServletMapping.class.getResource("/").getPath();
                FileInputStream fs = new FileInputStream(path + "http.properties");
                PROPERTIES.load(fs);

                for (Object obj : PROPERTIES.keySet()) {
                    String key = obj.toString();
                    // servlet. 开头的内容定义为 Servlet
                    if (key.startsWith("servlet.") && key.endsWith(".uri")) {
                        // 获取 uri 的值
                        String uri = PROPERTIES.getProperty(key);
                        // 获取 class name，替换 .uri 为 .class
                        String className = PROPERTIES.getProperty(key.replace(".uri", ".class"));
                        // 使用反射实例化这个对象
                        BaseServlet servlet = (BaseServlet) Class.forName(className)
                                .getDeclaredConstructor().newInstance();
                        servletMapping.put(uri, servlet);
                    }
                }

            } catch (Exception e) {
                log.debug("请检查 http.properties 的 Servlet 配置字段");
                e.printStackTrace();
            }
        }
    }
}

