package com.github.nigthcrawlerx1.jdacommands.commands.exception;

public class CommandSetException extends RuntimeException {
    public CommandSetException(String msg) {
        super(msg);
    }

    public CommandSetException(String msg, Throwable cause) {
        super(msg, cause);
    }
}