package com.example.hanzh.gankio_han.model;

/**
 * Created by hanzh on 2015/10/6.
 */
public class BaseGank {

    private boolean error;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "BaseGank{" +
                "error=" + error +
                '}';
    }
}
