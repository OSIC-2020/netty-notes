package org.xian.rpc.test.provider;

import org.xian.rpc.test.interfaces.Message;
import org.xian.rpc.test.interfaces.MessageService;

/**
 * @author xian
 */
public class MessageServiceImpl implements MessageService {
    @Override
    public Message sayMessage(String name) {
        return new Message(200, "Your name is " + name);
    }
}
