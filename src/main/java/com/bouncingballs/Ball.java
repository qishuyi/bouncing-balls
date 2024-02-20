/**
 * This file defines the properties of the balls in the game.
 */
package com.bouncingballs;

import java.awt.Color;

public class Ball {
    private int x;
    private int y;
    private int diameter;
    private Color color;
    private int x_move_per_second = 2;
    private int y_move_per_second = 2;

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
        return this.x_move_per_second;
    }

    public int getYMovePerSecond() {
        return this.y_move_per_second;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setXMovePerSecond(int second) {
        this.x_move_per_second = second;
    }

    public void setYMovePerSecond(int second) {
        this.y_move_per_second = second;
    }
}