package com.baima.exception;

public class SimpleFactoryException extends BeanFactoryException{
    public SimpleFactoryException(){
        super();
    }

    public SimpleFactoryException(String msg){
        super(msg);
    }
}
