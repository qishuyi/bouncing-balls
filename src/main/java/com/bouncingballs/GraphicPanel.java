/**
 * This file defines the panel with the bouncing balls.
 */
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
    // Constants: colors and number of balls
    private static final Color[] ALL_COLORS = { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.GRAY,
        Color.PINK };
    private static final int NUM_BALLS = 20;

    // List of ball objects and list of coordinates
    private ArrayList<Ball> balls_list = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> coordinates = new ArrayList<>();

    // Random objects
    private Random rand = new Random();
    private Random coord_x_rand = new Random();
    private Random coord_y_rand = new Random();

    // Swing components
    private JPanel end_panel = new JPanel();
    private JButton end_yes_button = new JButton("Yes");
    private JButton end_no_button = new JButton("No");

    // Parameters to this class
    private int width;
    private int height;
    private int y_distance;

    public GraphicPanel(int width, int height, int yDistance) {
        configurePanels();
        this.width = width;
        this.height = height;
        this.y_distance = yDistance;
        initialize();
    }

    public void showPausePanel() {
        end_panel.setVisible(false);
        // pausePanel.setVisible(true);
    }

    public void showEndPanel() {
        // pausePanel.setVisible(false);
        end_panel.setVisible(true);
    }

    // Reference:
    // https://stackoverflow.com/questions/23819196/how-to-move-a-circle-automatically-in-java
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < balls_list.size(); ++i) {
            Ball cur = balls_list.get(i);
            int x = cur.getX();
            int y = cur.getY();
            int diameter = cur.getDiameter();
            Color color = cur.getColor();
            g.setColor(color);
            g.fillOval(x, y - diameter, diameter, diameter);
        }
    }

    // Configure panels and the buttons on the panel
    private void configurePanels() {
        end_panel.setLayout(new BorderLayout());
        JLabel endLabel = new JLabel("Do you want to restart the game? Clicing \"No\" will close the window.");
        end_panel.add(endLabel, BorderLayout.PAGE_START);
        end_yes_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                end_panel.setVisible(false);
                // Reinitialize the balls
                initialize();
            }
        });
        end_no_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Exit the game
                System.exit(0);
            }
        });
        end_panel.add(end_yes_button, BorderLayout.WEST);
        end_panel.add(end_no_button, BorderLayout.EAST);

        // pausePanel.setVisible(false);
        end_panel.setVisible(false);

        // Add the panels to this big panel
        // add(pausePanel);
        add(end_panel);
    }

    private void initialize() {
        // Balls are positioned starting from these coordinates
        int x = 10;
        int y = 10;

        for (int i = 0; i < NUM_BALLS; ++i) {
            // Get a random color
            Random colorRand = new Random();
            Color color = ALL_COLORS[colorRand.nextInt(ALL_COLORS.length)];
            
            // Get a random diameter
            int diameter = 10 + rand.nextInt(40);

            // Get the next random x coordinate and adjust relative to the width
            int ballX = (x + 25 + coord_x_rand.nextInt(100)) % width;
            if (ballX + diameter / 2 > width) {
                ballX = (ballX - diameter / 2) % width - diameter / 2;
            } else if (ballX - diameter / 2 < 0) {
                ballX = diameter / 2 + ballX % width;
            }

            // Get the next random y coordinate and adjust relative to the height
            int ballY = (y + 25 + coord_y_rand.nextInt(100)) % height;
            if (ballY + diameter / 2 > height) {
                ballY = y_distance + (ballY - diameter / 2) % (height - y_distance) - diameter / 2;
            } else if (ballY - diameter / 2 < y_distance) {
                ballY = y_distance + diameter / 2 + ballY % (height - y_distance);
            }

            // Add the ball to the list
            balls_list.add(new Ball(ballX, ballY, diameter, color));

            // Add the x and y coordinates to the list
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

            private void bounceBalls(ArrayList<Integer> indices) {
                // Reference:
                // https://stackoverflow.com/questions/23819196/how-to-move-a-circle-automatically-in-java
                Timer timer = new Timer(100, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for (int i = 0; i < indices.size(); ++i) {
                            // Skip out of range indices
                            if (indices.get(i) >= balls_list.size()) {
                                continue;
                            }
                            
                            Ball ball = balls_list.get(indices.get(i));
                            // Skip null elements
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
                            // Bounce the ball towards the right and bottom sides of the panel
                            if (ballX + d > width || ballY + d > height) {
                                if (ballX + d > width) {
                                    ball.setX(width - d);
                                }
                                if (ballY + d > height) {
                                    ball.setY(height - d);
                                }
                                // Move the ball in opposite direction
                                ball.setXMovePerSecond(xMovePerSecond * -1);
                                ball.setYMovePerSecond(yMovePerSecond * -1);
                            // Bounce the ball towards the left and top sides of the panel
                            } else if (ballX - d < 0 || ballY - d < y_distance) {
                                if (ballX - d < 0) {
                                    ball.setX(d);
                                }
                                if (ballY - d < y_distance) {
                                    ball.setY(y_distance + d);
                                }
                                // Move the ball in opposite direction
                                ball.setXMovePerSecond(xMovePerSecond * -1);
                                ball.setYMovePerSecond(yMovePerSecond * -1);
                            }

                            ArrayList<Integer> xAndY = new ArrayList<>();
                            xAndY.add(ball.getX());
                            xAndY.add(ball.getY());
                            // Remove smaller balls when they collide
                            while (coordinates.contains(xAndY)) {
                                // Find colliding ball
                                int otherIndex = coordinates.indexOf(xAndY);
                                // Skip out of range indices
                                if (otherIndex < balls_list.size()) {
                                    Ball otherBall = balls_list.get(otherIndex);
                                    // Skip null elements
                                    if (otherBall != null) {
                                        if (ball.getDiameter() < otherBall.getDiameter()) {
                                            balls_list.set(indices.get(i), null);
                                            coordinates.set(otherIndex, null);
                                        } else if (ball.getDiameter() > otherBall.getDiameter()) {
                                            balls_list.set(otherIndex, null);
                                            coordinates.set(otherIndex, null);
                                            coordinates.set(indices.get(i), xAndY);
                                            break;
                                        } else {
                                            balls_list.set(indices.get(i), null);
                                            balls_list.set(otherIndex, null);
                                            coordinates.set(indices.get(i), null);
                                            coordinates.set(otherIndex, null);
                                        }
                                    }
                                }
                            }
                        }
                        balls_list.removeIf(Objects::isNull);
                        coordinates.removeIf(Objects::isNull);
                        if (balls_list.size() <= 1) {
                            if (balls_list.size() == 1) {
                                Ball ball = balls_list.get(0);
                                ball.setXMovePerSecond(0);
                                ball.setYMovePerSecond(0);
                            }
                            end_panel.setVisible(true);
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
                int count = numRandom.nextInt(balls_list.size());
                Random indexRandom = new Random();
                ArrayList<Integer> indicesToBounce = new ArrayList<>();
                for (int i = 0; i < count; ++i) {
                    indicesToBounce.add(indexRandom.nextInt(balls_list.size()));
                }

                int xMovePerSecond = 2;
                int yMovePerSecond = 2;
                if (x < width / 2 && y < (height + y_distance) / 2) {
                    xMovePerSecond = -2;
                    yMovePerSecond = -2;
                } else if (x > width / 2 && y < (height + y_distance) / 2) {
                    yMovePerSecond = -2;
                } else if (x < width / 2 && y > (height + y_distance) / 2) {
                    xMovePerSecond = -2;
                }

                for (int i = 0; i < indicesToBounce.size(); ++i) {
                    Ball ball = balls_list.get(indicesToBounce.get(i));
                    ball.setXMovePerSecond(xMovePerSecond);
                    ball.setYMovePerSecond(yMovePerSecond);
                }

                bounceBalls(indicesToBounce);
            }
        });
    }
}