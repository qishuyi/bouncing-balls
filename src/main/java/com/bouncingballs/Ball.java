/**
 * This file defines the properties of the balls in the game.
 */
package com.bouncingballs;

import java.awt.Color;

public class Ball {
    private double x;
    private double y;
    private int diameter;
    private Color color;
    private double x_move_per_second;
    private double y_move_per_second;

    public Ball(double x, double y, int diameter, Color color, double xMovePerSecond, double yMovePerSecond) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.color = color;
        this.x_move_per_second = xMovePerSecond;
        this.y_move_per_second = yMovePerSecond;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public int getDiameter() {
        return this.diameter;
    }

    public Color getColor() {
        return this.color;
    }

    public double getXMovePerSecond() {
        return this.x_move_per_second;
    }

    public double getYMovePerSecond() {
        return this.y_move_per_second;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setXMovePerSecond(double second) {
        this.x_move_per_second = second;
    }

    public void setYMovePerSecond(double second) {
        this.y_move_per_second = second;
    }
}