package com.hoangpro.amazingwords.model;

public class ViewTranslate {
    public float floatX;
    public float floatY;
    public int posionRelative;

    public ViewTranslate(float floatX, float floatY) {
        this.floatX = floatX;
        this.floatY = floatY;
    }

    @Override
    public String toString() {
        return "ViewTranslate{" +
                "floatX=" + floatX +
                ", floatY=" + floatY +
                '}';
    }
}
