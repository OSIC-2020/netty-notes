package org.xian.rpc.test.provider;

import org.xian.rpc.test.interfaces.HelloService;

/**
 * @author xian
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String username) {
        return "Hi " + username;
    }
}
