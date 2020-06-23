package org.xian.rpc.test.interfaces;

/**
 * @author xian
 */
public interface MessageService {
    /**
     * 返回一个自定义 Java 对象
     *
     * @param name name
     * @return message
     */
    Message sayMessage(String name);
}
