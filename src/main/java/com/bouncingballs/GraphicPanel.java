package com.bouncingballs;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.Objects;

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
    ArrayList<ArrayList<Integer>> coordinates = new ArrayList<>();
    Random rand = new Random();
    Random coordXRand = new Random();
    Random coordYRand = new Random();
    JPanel endPanel = new JPanel();
    JButton endYesButton = new JButton("Yes");
    JButton endNoButton = new JButton("No");
    JPanel pausePanel = new JPanel();
    JButton pauseYesButton = new JButton("Yes");
    JButton pauseNoButton = new JButton("No");
    private int width;
    private int height;
    private int yDistance;
    private Timer timer;

    private void configurePanels() {
        pausePanel.setLayout(new BorderLayout());
        JLabel pauseLabel = new JLabel("Continue the game?");
        pausePanel.add(pauseLabel, BorderLayout.PAGE_START);
        pauseYesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.start();
                pausePanel.setVisible(false);
            }
        });
        pauseNoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        pausePanel.add(pauseYesButton, BorderLayout.WEST);
        pausePanel.add(pauseNoButton, BorderLayout.EAST);

        endPanel.setLayout(new BorderLayout());
        JLabel endLabel = new JLabel("Do you want to restart the game? Clicing \"No\" will close the window.");
        endPanel.add(endLabel, BorderLayout.PAGE_START);
        endYesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endPanel.setVisible(false);
                initialize();
            }
        });
        endNoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        endPanel.add(endYesButton, BorderLayout.WEST);
        endPanel.add(endNoButton, BorderLayout.EAST);

        pausePanel.setVisible(false);
        endPanel.setVisible(false);
        add(pausePanel);
        add(endPanel);
    }

    private void initialize() {
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
            ArrayList<Integer> xAndY = new ArrayList<>();
            xAndY.add(ballX);
            xAndY.add(ballY);
            coordinates.add(xAndY);
            x = ballX;
            y = ballY;
        }
        addMouseListener(new MouseAdapter() {
            int x;
            int y;
            GraphicPanel graphicPanel = GraphicPanel.this;

            private void bounceBalls(ArrayList<Integer> indices) {
                // Reference:
                // https://stackoverflow.com/questions/23819196/how-to-move-a-circle-automatically-in-java
                Timer timer = new Timer(10, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for (int i = 0; i < indices.size(); ++i) {
                            if (indices.get(i) >= ballsList.size()) {
                                continue;
                            }
                            Ball ball = ballsList.get(indices.get(i));
                            if (ball == null) {
                                continue;
                            }
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
                                    // System.out.println("X" + (width - d));
                                    ball.setX(width - d);
                                }
                                if (ballY + d > height) {
                                    // System.out.println("Y" + (height - d));
                                    ball.setY(height - d);
                                }
                                ball.setXMovePerSecond(xMovePerSecond * -1);
                                ball.setYMovePerSecond(yMovePerSecond * -1);
                            } else if (ballX - d < 0 || ballY - d < yDistance) {
                                if (ballX - d < 0) {
                                    // System.out.println("X" + d);
                                    ball.setX(d);
                                }
                                if (ballY - d < yDistance) {
                                    // System.out.println("Y" + (yDistance + d));
                                    ball.setY(yDistance + d);
                                }
                                ball.setXMovePerSecond(xMovePerSecond * -1);
                                ball.setYMovePerSecond(yMovePerSecond * -1);
                            }
                            // Remove smaller balls when they collide
                            ArrayList<Integer> xAndY = new ArrayList<>();
                            xAndY.add(ball.getX());
                            xAndY.add(ball.getY());
                            if (coordinates.contains(xAndY)) {
                                int otherIndex = coordinates.indexOf(xAndY);
                                if (otherIndex < ballsList.size()) {
                                    Ball otherBall = ballsList.get(otherIndex);
                                    if (otherBall != null) {
                                        if (ball.getDiameter() < otherBall.getDiameter()) {
                                            ballsList.set(i, null);
                                            coordinates.set(otherIndex, null);
                                        } else if (ball.getDiameter() > otherBall.getDiameter()) {
                                            ballsList.set(otherIndex, null);
                                            coordinates.set(otherIndex, null);
                                            coordinates.set(i, xAndY);
                                        } else {
                                            ballsList.set(i, null);
                                            ballsList.set(otherIndex, null);
                                            coordinates.set(i, null);
                                            coordinates.set(otherIndex, null);
                                        }
                                    }
                                } else {
                                    coordinates.set(i, xAndY);
                                }
                            }
                        }
                        ballsList.removeIf(Objects::isNull);
                        coordinates.removeIf(Objects::isNull);
                        if (ballsList.size() <= 1) {
                            if (ballsList.size() == 1) {
                                Ball ball = ballsList.get(0);
                                ball.setXMovePerSecond(0);
                                ball.setYMovePerSecond(0);
                            }
                            endPanel.setVisible(true);
                        }
                        repaint();
                    }
                });
                timer.start();
                graphicPanel.timer = timer;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Random numRandom = new Random();
                int count = numRandom.nextInt(ballsList.size());
                Random indexRandom = new Random();
                ArrayList<Integer> indicesToBounce = new ArrayList<>();
                for (int i = 0; i < count; ++i) {
                    indicesToBounce.add(indexRandom.nextInt(ballsList.size()));
                }
                int xMovePerSecond = 2;
                int yMovePerSecond = 2;
                if (x < width / 2 && y < (height + yDistance) / 2) {
                    xMovePerSecond = -2;
                    yMovePerSecond = -2;
                } else if (x > width / 2 && y < (height + yDistance) / 2) {
                    yMovePerSecond = -2;
                } else if (x < width / 2 && y > (height + yDistance) / 2) {
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

    public GraphicPanel(int width, int height, int yDistance) {
        configurePanels();
        this.width = width;
        this.height = height;
        this.yDistance = yDistance;
        initialize();
    }

    public void showPausePanel() {
        endPanel.setVisible(false);
        timer.stop();
        pausePanel.setVisible(true);
    }

    public void showEndPanel() {
        pausePanel.setVisible(false);
        timer.stop();
        endPanel.setVisible(true);
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