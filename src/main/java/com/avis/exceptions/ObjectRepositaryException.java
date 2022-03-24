package com.avis.exceptions;

public class ObjectRepositaryException extends RuntimeException{


    public ObjectRepositaryException() {
    }

    private ObjectRepositaryException(String var1) {
        super(var1);
    }

    public ObjectRepositaryException(Object var1) {
        this(String.valueOf(var1));
        if (var1 instanceof Throwable) {
            this.initCause((Throwable)var1);
        }

    }

    public ObjectRepositaryException(boolean var1) {
        this(String.valueOf(var1));
    }

    public ObjectRepositaryException(char var1) {
        this(String.valueOf(var1));
    }

    public ObjectRepositaryException(int var1) {
        this(String.valueOf(var1));
    }

    public ObjectRepositaryException(long var1) {
        this(String.valueOf(var1));
    }

    public ObjectRepositaryException(float var1) {
        this(String.valueOf(var1));
    }

    public ObjectRepositaryException(double var1) {
        this(String.valueOf(var1));
    }

    public ObjectRepositaryException(String var1, Throwable var2) {
        super(var1, var2);
    }


}
