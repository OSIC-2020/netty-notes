package org.xian.rpc.test.interfaces;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xian
 */
@Data
@AllArgsConstructor
public class Message implements Serializable {
    private static final long serialVersionUID = -4268077260739000146L;
    private int code;
    private String message;
}
