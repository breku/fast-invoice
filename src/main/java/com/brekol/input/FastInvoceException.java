package com.brekol.input;

import java.io.IOException;

/**
 * Created by brekol on 17.01.16.
 */
public class FastInvoceException extends RuntimeException {

    public FastInvoceException(String message, Throwable e) {
        super(message, e);
    }
}
