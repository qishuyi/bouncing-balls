package com.bouncingballs;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GraphicPanel extends JPanel {
    ArrayList<Ball> ballsList = new ArrayList<>();
    private final Color[] allColors = { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.GRAY,
            Color.PINK };
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
        addMouseListener(new MouseAdapter() {
            int x;
            int y;

            private void bounceBalls(ArrayList<Integer> indices) {
                // Reference:
                // https://stackoverflow.com/questions/23819196/how-to-move-a-circle-automatically-in-java
                Timer timer = new Timer(10, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for (int i = 0; i < indices.size(); ++i) {
                            Ball ball = ballsList.get(indices.get(i));
                            int ballX = ball.getX();
                            int ballY = ball.getY();
                            
                            int xMovePerSecond = ball.getXMovePerSecond();
                            int yMovePerSecond = ball.getYMovePerSecond();
                            ball.setX(ballX + xMovePerSecond);
                            ball.setY(ballY + yMovePerSecond);
                            ballX = ball.getX();
                            ballY = ball.getY();
                            int d = ball.getDiameter();
                            // Make the ball bounce toward the sides and back
                            if (ballX + d > width || ballY + d > height) {
                                if (ballX + d > width) {
                                    System.out.println("X" + (width - d));
                                    ball.setX(width - d);
                                }
                                if (ballY + d > height) {
                                    System.out.println("Y" + (height - d));
                                    ball.setY(height - d);
                                }
                                ball.setXMovePerSecond(xMovePerSecond * -1);
                                ball.setYMovePerSecond(yMovePerSecond * -1);
                            } else if (ballX - d < 0 || ballY - d < yDistance) {
                                if (ballX - d < 0) {
                                    System.out.println("X" + d);
                                    ball.setX(d);
                                }
                                if (ballY - d < yDistance) {
                                    System.out.println("Y" + (yDistance + d));
                                    ball.setY(yDistance + d);
                                }
                                ball.setXMovePerSecond(xMovePerSecond * -1);
                                ball.setYMovePerSecond(yMovePerSecond * -1);
                            }
                        }
                        repaint();
                    }
                });
                timer.start();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Random numRandom = new Random();
                int count = numRandom.nextInt(20);
                Random indexRandom = new Random();
                ArrayList<Integer> indicesToBounce = new ArrayList<>();
                for (int i = 0; i < count; ++i) {
                    indicesToBounce.add(indexRandom.nextInt(20));
                }
                int xMovePerSecond = 2;
                int yMovePerSecond = 2;
                int mouseX = e.getX();
                int mouseY = e.getY();
                if (mouseX < width / 2 && mouseY < (height + yDistance) / 2) {
                    xMovePerSecond = -2;
                    yMovePerSecond = -2;
                } else if (mouseX > width / 2 && mouseY < (height + yDistance) / 2) {
                    yMovePerSecond = -2;
                } else if (mouseX < width / 2 && mouseY > (height + yDistance) / 2) {
                    xMovePerSecond = -2;
                }
                for (int i = 0; i < indicesToBounce.size(); ++i) {
                    Ball ball = ballsList.get(indicesToBounce.get(i));
                    ball.setXMovePerSecond(xMovePerSecond);
                    ball.setYMovePerSecond(yMovePerSecond);
                }
                bounceBalls(indicesToBounce);
            }
        });
    }

    // Reference:
    // https://stackoverflow.com/questions/23819196/how-to-move-a-circle-automatically-in-java
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