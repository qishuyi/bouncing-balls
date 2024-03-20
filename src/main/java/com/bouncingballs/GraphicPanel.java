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
    private static final Color[] ALL_COLORS = { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE,
            Color.GRAY,
            Color.PINK };

    // BouncingBall instance this GraphicPanel instance is associated with
    BouncingBalls game;
    Timer timer;

    // List of ball objects and list of coordinates
    private ArrayList<Ball> balls_list = new ArrayList<>();
    private ArrayList<ArrayList<Double>> coordinates = new ArrayList<>();

    // Random objects
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
    private int num_balls;
    private ArrayList<Double> ball_size;
    private ArrayList<Double> ball_speed;
    private ArrayList<Integer> ball_color;

    public GraphicPanel(BouncingBalls game, int width, int height, int yDistance, int counter,
            ArrayList<Double> ballSize, ArrayList<Double> ballSpeed, ArrayList<Integer> ballColors) {
        configurePanels();
        this.game = game;
        this.width = width;
        this.height = height;
        this.y_distance = yDistance;
        this.num_balls = counter;
        this.ball_size = ballSize;
        this.ball_speed = ballSpeed;
        this.ball_color = ballColors;
        setUpCoordinates();
        initialize();
        bounceBalls();
    }

    public void showEndPanel() {
        end_panel.setVisible(true);
    }

    public void update(BouncingBalls game, int counter, ArrayList<Double> ballSize, ArrayList<Double> ballSpeed,
            ArrayList<Integer> ballColors) {
        this.num_balls = counter;
        this.ball_size = ballSize;
        this.ball_speed = ballSpeed;
        this.ball_color = ballColors;
        setUpCoordinates();
        initialize();
        timer.restart();
    }

    public int getNumBalls() {
        return this.balls_list.size();
    }

    // Reference:
    // https://stackoverflow.com/questions/23819196/how-to-move-a-circle-automatically-in-java
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < balls_list.size(); ++i) {
            Ball cur = balls_list.get(i);
            double x = cur.getX();
            double y = cur.getY();
            int diameter = cur.getDiameter();
            Color color = cur.getColor();
            g.setColor(color);
            g.fillOval((int) x, (int) (y - diameter), diameter, diameter);
        }
    }

    private void setUpCoordinates() {
        ArrayList<ArrayList<Double>> newCoordinates = new ArrayList<>();
        for (int i = 0; i < balls_list.size(); ++i) {
            ArrayList<Double> newPair = new ArrayList<>();
            newPair.add(balls_list.get(i).getX());
            newPair.add(balls_list.get(i).getY());
            newCoordinates.add(newPair);
        }
        coordinates = newCoordinates;
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
                // Restart game
                game.restartGame(game);
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

        end_panel.setVisible(false);

        // Add the panels to this big panel
        add(end_panel);
    }

    private int getNextIndexOfCoordinates(int curIndex, ArrayList<ArrayList<Double>> coords, ArrayList<Double> target,
            int selfIndex, int targetDiameter, int curDiameter) {
        if (curIndex >= coords.size()) {
            return -1;
        }
        for (int i = curIndex; i < balls_list.size(); ++i) {
            if (i == selfIndex) {
                continue;
            }
            ArrayList<Double> curCoordinates = coords.get(i);
            if (curCoordinates == null) {
                continue;
            }
            double distanceBetweenCenters = Math.sqrt(Math.pow(curCoordinates.get(0) - target.get(0), 2)
                    + Math.pow(curCoordinates.get(1) - target.get(1), 2));
            if (distanceBetweenCenters <= (curDiameter + targetDiameter) / 2) {
                return i;
            }
        }
        return -1;
    }

    private void bounceBalls() {
        // Reference:
        // https://stackoverflow.com/questions/23819196/how-to-move-a-circle-automatically-in-java
        Timer timer = new Timer(80, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < balls_list.size(); ++i) {
                    // Skip out of range indices
                    if (i >= balls_list.size()) {
                        continue;
                    }

                    Ball ball = balls_list.get(i);
                    // Skip null elements
                    if (ball == null) {
                        continue;
                    }

                    double ballX = ball.getX();
                    double ballY = ball.getY();
                    double xMovePerSecond = ball.getXMovePerSecond();
                    double yMovePerSecond = ball.getYMovePerSecond();
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

                    ArrayList<Double> xAndY = new ArrayList<>();
                    xAndY.add(ball.getX());
                    xAndY.add(ball.getY());

                    coordinates.set(i, xAndY);

                    int curIndex = 0;
                    ArrayList<Double> curCoordinates = coordinates.get(curIndex);
                    Ball curBall = balls_list.get(curIndex);
                    while (curBall == null || curCoordinates == null) {
                        if (curIndex >= balls_list.size()) {
                            break;
                        }
                        curIndex++;
                        curCoordinates = coordinates.get(curIndex);
                        curBall = balls_list.get(curIndex);
                    }
                    int otherIndex = -1;
                    if (curIndex < balls_list.size()) {
                        otherIndex = getNextIndexOfCoordinates(curIndex, coordinates, xAndY, i, curBall.getDiameter(),
                                ball.getDiameter());
                    }
                    // Remove smaller balls when they collide
                    while (otherIndex != -1) {
                        // Skip out of range indices
                        if (otherIndex < balls_list.size()) {
                            Ball otherBall = balls_list.get(otherIndex);
                            // Skip null elements
                            if (otherBall != null) {
                                if (ball.getDiameter() < otherBall.getDiameter()) {
                                    balls_list.set(i, null);
                                    coordinates.set(i, null);
                                    break;
                                } else if (ball.getDiameter() > otherBall.getDiameter()) {
                                    balls_list.set(otherIndex, null);
                                    coordinates.set(otherIndex, null);
                                    coordinates.set(i, xAndY);
                                    balls_list.set(i, ball);
                                } else {
                                    balls_list.set(i, null);
                                    balls_list.set(otherIndex, null);
                                    coordinates.set(i, null);
                                    coordinates.set(otherIndex, null);
                                }
                            }
                        }
                        curIndex = otherIndex + 1;
                        if (curIndex < balls_list.size()) {
                            curCoordinates = coordinates.get(curIndex);
                            curBall = balls_list.get(curIndex);
                            while (curBall == null || curCoordinates == null) {
                                curIndex++;
                                if (curIndex >= balls_list.size()) {
                                    break;
                                }
                                curCoordinates = coordinates.get(curIndex);
                                curBall = balls_list.get(curIndex);
                            }
                            if (curIndex < balls_list.size() - 1) {
                                if (balls_list.get(i) == null) {
                                    // Break out of the loop which searches for balls to collide and get rid of
                                    break;
                                }
                                otherIndex = getNextIndexOfCoordinates(curIndex, coordinates, xAndY, i,
                                        curBall.getDiameter(), balls_list.get(i).getDiameter());
                            } else {
                                otherIndex = -1;
                            }
                        }
                        otherIndex = -1;
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
        this.timer = timer;
        timer.start();
    }

    private void initialize() {
        // Balls are positioned starting from these coordinates
        double x = 10;
        double y = 10;

        ArrayList<Ball> newBallList = new ArrayList<>();

        for (int i = 0; i < num_balls; ++i) {
            // Get a random color
            Color color = ALL_COLORS[ball_color.get(i) % ALL_COLORS.length];

            // Get a random diameter
            int diameter = 10 + (int) (Math.round(ball_size.get(i))) % 40;

            // Get the next random x coordinate and adjust relative to the width
            double ballX = (x + 25 + coord_x_rand.nextInt(100)) % width;
            if (ballX + diameter / 2 > width) {
                ballX = (ballX - diameter / 2) % width - diameter / 2;
            } else if (ballX - diameter / 2 < 0) {
                ballX = diameter / 2 + ballX % width;
            }

            // Get the next random y coordinate and adjust relative to the height
            double ballY = (y + 25 + coord_y_rand.nextInt(100)) % height;
            if (ballY + diameter / 2 > height) {
                ballY = y_distance + (ballY - diameter / 2) % (height - y_distance) - diameter / 2;
            } else if (ballY - diameter / 2 < y_distance) {
                ballY = y_distance + diameter / 2 + ballY % (height - y_distance);
            }

            double speed = this.ball_speed.get(i) * 4 + 1;

            // Add the ball to the list
            newBallList.add(new Ball(ballX, ballY, diameter, color, speed, speed));

            // Add the x and y coordinates to the list
            ArrayList<Double> xAndY = new ArrayList<>();
            xAndY.add(ballX);
            xAndY.add(ballY);
            coordinates.add(xAndY);

            x = ballX;
            y = ballY;
        }

        balls_list = newBallList;

        addMouseListener(new MouseAdapter() {
            int x;
            int y;

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

                for (int i = 0; i < indicesToBounce.size(); ++i) {
                    int index = indicesToBounce.get(i);
                    Ball ball = balls_list.get(index);
                    if (ball == null) {
                        continue;
                    }
                    double xMovePerSecond = ball.getXMovePerSecond();
                    double yMovePerSecond = ball.getYMovePerSecond();
                    // Change direction of balls based on which quadrant the user clicked
                    if (x < width / 2 && y < (height + y_distance) / 2) {
                        xMovePerSecond = -1 * xMovePerSecond;
                        yMovePerSecond = -1 * yMovePerSecond;
                    } else if (x > width / 2 && y < (height + y_distance) / 2) {
                        yMovePerSecond = -1 * yMovePerSecond;
                    } else if (x < width / 2 && y > (height + y_distance) / 2) {
                        xMovePerSecond = -1 * xMovePerSecond;
                    }
                    ball.setXMovePerSecond(xMovePerSecond);
                    ball.setYMovePerSecond(yMovePerSecond);
                }
            }
        });
    }
}