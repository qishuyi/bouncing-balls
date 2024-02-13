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
    private final Color[] allColors = { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.GRAY,
        Color.PINK };
    private final int numBalls = 20;

    // List of ball objects and list of coordinates
    private ArrayList<Ball> ballsList = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> coordinates = new ArrayList<>();

    // Random objects
    private Random rand = new Random();
    private Random coordXRand = new Random();
    private Random coordYRand = new Random();

    // Swing components
    private JPanel endPanel = new JPanel();
    private JButton endYesButton = new JButton("Yes");
    private JButton endNoButton = new JButton("No");
    // private JPanel pausePanel = new JPanel();
    // private JButton pauseYesButton = new JButton("Yes");
    // private JButton pauseNoButton = new JButton("No");

    // Parameters to this class
    private int width;
    private int height;
    private int yDistance;
    // private ArrayList<Timer> timers;

    // Configure panels and the buttons on the panel
    private void configurePanels() {
        // pausePanel.setLayout(new BorderLayout());
        // JLabel pauseLabel = new JLabel("Continue the game?");
        // pausePanel.add(pauseLabel, BorderLayout.PAGE_START);
        // pauseYesButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         pausePanel.setVisible(false);
        //     }
        // });
        // pauseNoButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         // Exit the game
        //         System.exit(0);
        //     }
        // });
        // pausePanel.add(pauseYesButton, BorderLayout.WEST);
        // pausePanel.add(pauseNoButton, BorderLayout.EAST);

        endPanel.setLayout(new BorderLayout());
        JLabel endLabel = new JLabel("Do you want to restart the game? Clicing \"No\" will close the window.");
        endPanel.add(endLabel, BorderLayout.PAGE_START);
        endYesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endPanel.setVisible(false);
                // Reinitialize the balls
                initialize();
            }
        });
        endNoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Exit the game
                System.exit(0);
            }
        });
        endPanel.add(endYesButton, BorderLayout.WEST);
        endPanel.add(endNoButton, BorderLayout.EAST);

        // pausePanel.setVisible(false);
        endPanel.setVisible(false);

        // Add the panels to this big panel
        // add(pausePanel);
        add(endPanel);
    }

    private void initialize() {
        // Balls are positioned starting from these coordinates
        int x = 10;
        int y = 10;

        for (int i = 0; i < numBalls; ++i) {
            // Get a random color
            Random colorRand = new Random();
            Color color = allColors[colorRand.nextInt(allColors.length)];
            
            // Get a random diameter
            int diameter = 10 + rand.nextInt(40);

            // Get the next random x coordinate and adjust relative to the width
            int ballX = (x + 25 + coordXRand.nextInt(100)) % width;
            if (ballX + diameter / 2 > width) {
                ballX = (ballX - diameter / 2) % width - diameter / 2;
            } else if (ballX - diameter / 2 < 0) {
                ballX = diameter / 2 + ballX % width;
            }

            // Get the next random y coordinate and adjust relative to the height
            int ballY = (y + 25 + coordYRand.nextInt(100)) % height;
            if (ballY + diameter / 2 > height) {
                ballY = yDistance + (ballY - diameter / 2) % (height - yDistance) - diameter / 2;
            } else if (ballY - diameter / 2 < yDistance) {
                ballY = yDistance + diameter / 2 + ballY % (height - yDistance);
            }

            // Add the ball to the list
            ballsList.add(new Ball(ballX, ballY, diameter, color));

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
                Timer timer = new Timer(10, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for (int i = 0; i < indices.size(); ++i) {
                            // Skip out of range indices
                            if (indices.get(i) >= ballsList.size()) {
                                continue;
                            }
                            
                            Ball ball = ballsList.get(indices.get(i));
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
                            } else if (ballX - d < 0 || ballY - d < yDistance) {
                                if (ballX - d < 0) {
                                    ball.setX(d);
                                }
                                if (ballY - d < yDistance) {
                                    ball.setY(yDistance + d);
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
                                if (otherIndex < ballsList.size()) {
                                    Ball otherBall = ballsList.get(otherIndex);
                                    // Skip null elements
                                    if (otherBall != null) {
                                        if (ball.getDiameter() < otherBall.getDiameter()) {
                                            ballsList.set(indices.get(i), null);
                                            coordinates.set(otherIndex, null);
                                        } else if (ball.getDiameter() > otherBall.getDiameter()) {
                                            ballsList.set(otherIndex, null);
                                            coordinates.set(otherIndex, null);
                                            coordinates.set(indices.get(i), xAndY);
                                            break;
                                        } else {
                                            ballsList.set(indices.get(i), null);
                                            ballsList.set(otherIndex, null);
                                            coordinates.set(indices.get(i), null);
                                            coordinates.set(otherIndex, null);
                                        }
                                    }
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
        // pausePanel.setVisible(true);
    }

    public void showEndPanel() {
        // pausePanel.setVisible(false);
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