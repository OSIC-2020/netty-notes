package org.xian.rpc.test.provider;

import org.xian.rpc.provider.Provider;
import org.xian.rpc.test.interfaces.HelloService;
import org.xian.rpc.test.interfaces.MessageService;

public class ProviderApplication {
    public static void main(String[] args) {
        Provider server = new Provider("127.0.0.1", 8080);
        // 注册 HelloService 和 MessageService 服务
        server.addService(new HelloServiceImpl(), HelloService.class);
        server.addService(new MessageServiceImpl(), MessageService.class);
        server.start();
    }
}
