package com.bouncingballs;

import java.awt.Color;

public class Ball {
    private int x;
    private int y;
    private int diameter;
    private Color color;
    private int xMovePerSecond = 2;
    private int yMovePerSecond = 2;

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

    public int getXMovePerSecond() {
        return this.xMovePerSecond;
    }

    public int getYMovePerSecond() {
        return this.yMovePerSecond;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setXMovePerSecond(int second) {
        this.xMovePerSecond = second;
    }

    public void setYMovePerSecond(int second) {
        this.yMovePerSecond = second;
    }
}