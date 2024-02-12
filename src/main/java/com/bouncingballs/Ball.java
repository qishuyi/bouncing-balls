package com.bouncingballs;

import java.awt.Color;

public class Ball {
    private int x;
    private int y;
    private int diameter;
    private Color color;

    public Ball(int x, int y, int diameter, Color color) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.color = color;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getDiameter() {
        return this.diameter;
    }

    public Color getColor() {
        return this.color;
    }
}