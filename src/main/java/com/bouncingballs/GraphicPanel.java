package com.bouncingballs;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class GraphicPanel extends JPanel {
    ArrayList<Ball> ballsList = new ArrayList<>();
    private final Color[] allColors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.GRAY, Color.PINK};
    private final int numBalls = 20;
    Random rand = new Random();
    Random coordXRand = new Random();
    Random coordYRand = new Random();

    public GraphicPanel(int width, int height, int yDistance) {
        int x = 10;
        int y = 10;
        for (int i = 0; i < numBalls; ++i) {
            Random colorRand = new Random();
            Color color = allColors[colorRand.nextInt(allColors.length)];
            int diameter = 10 + rand.nextInt(40);
            int ballX = (x + 25 + coordXRand.nextInt(100)) % width;
            if (ballX + diameter / 2 > width) {
                ballX = (ballX - diameter / 2) % width - diameter / 2;
            } else if (ballX - diameter / 2 < 0) {
                ballX = diameter / 2 + ballX % width;
            }
            int ballY = (y + 25 + coordYRand.nextInt(100)) % height;
            if (ballY + diameter / 2 > height) {
                ballY = yDistance + (ballY - diameter / 2) % (height - yDistance) - diameter / 2;
            } else if (ballY - diameter / 2 < yDistance) {
                ballY = yDistance + diameter / 2 + ballY % (height - yDistance);
            }
            ballsList.add(new Ball(ballX, ballY, diameter, color));
            x = ballX;
            y = ballY;
        }
    }

    // Reference: https://stackoverflow.com/questions/23819196/how-to-move-a-circle-automatically-in-java
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < ballsList.size(); ++i) {
            Ball cur = ballsList.get(i);
            int x = cur.getX();
            int y = cur.getY();
            int diameter = cur.getDiameter();
            Color color = cur.getColor();
            g.setColor(color);
            g.fillOval(x, y - diameter, diameter, diameter);
        }
    }
}