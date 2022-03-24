package com.avis.exceptions;

public class EnvConfigFileException extends RuntimeException{


    public EnvConfigFileException() {
    }

    private EnvConfigFileException(String var1) {
        super(var1);
    }

    public EnvConfigFileException(Object var1) {
        this(String.valueOf(var1));
        if (var1 instanceof Throwable) {
            this.initCause((Throwable)var1);
        }

    }

    public EnvConfigFileException(boolean var1) {
        this(String.valueOf(var1));
    }

    public EnvConfigFileException(char var1) {
        this(String.valueOf(var1));
    }

    public EnvConfigFileException(int var1) {
        this(String.valueOf(var1));
    }

    public EnvConfigFileException(long var1) {
        this(String.valueOf(var1));
    }

    public EnvConfigFileException(float var1) {
        this(String.valueOf(var1));
    }

    public EnvConfigFileException(double var1) {
        this(String.valueOf(var1));
    }

    public EnvConfigFileException(String var1, Throwable var2) {
        super(var1, var2);
    }


}
