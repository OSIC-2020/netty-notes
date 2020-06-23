package org.xian.rpc.test.consumer;

import lombok.extern.slf4j.Slf4j;
import org.xian.rpc.consumer.ConsumerProxy;
import org.xian.rpc.test.interfaces.HelloService;
import org.xian.rpc.test.interfaces.Message;
import org.xian.rpc.test.interfaces.MessageService;

/**
 * @author xian
 */
@Slf4j
public class ConsumerApplication {

    public static void main(String[] args) {
        ConsumerProxy proxy = new ConsumerProxy("127.0.0.1", 8080);
        HelloService helloService = proxy.getProxy(HelloService.class);
        String result = helloService.hello("xian");
        log.info("HelloService 调用结果是 {}", result);

        MessageService messageService = proxy.getProxy(MessageService.class);
        Message message = messageService.sayMessage("xiaoxian");
        log.info("MessageService 调用结果是 {}", message.toString());
    }
}
