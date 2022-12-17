package com.daniel.mychickenbreastshop.global.event.exception;

public class EventNotExistsException extends RuntimeException{

    public EventNotExistsException() {
        super("유효하지 않은 이벤트입니다.");
    }
}
