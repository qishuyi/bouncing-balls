/**
 * This file is the top-level definition of the game.
 */
package com.bouncingballs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

public class BouncingBalls {
    private static Timer timer;

    private GraphicPanel graphic_panel = null;
    private boolean restarted_game = false;
    private GUI gui = null;

    private class GUI {
        // Reference: https://www.javatpoint.com/java-jframe
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JPanel control_panel = new JPanel();
        JButton stop_button = new JButton();

        public GUI(BouncingBalls game, int counter, ArrayList<Double> ballSize, ArrayList<Double> ballSpeed,
                ArrayList<Integer> ballColors) {
            graphic_panel = new GraphicPanel(game, 700, 1000, 60, counter, ballSize, ballSpeed, ballColors);
        }

        private void update(BouncingBalls game, int counter, ArrayList<Double> ballSize, ArrayList<Double> ballSpeed,
                ArrayList<Integer> ballColors) {
            graphic_panel.update(game, counter, ballSize, ballSpeed, ballColors);
        }

        private void configure() {
            // Reference:
            // https://docs.oracle.com/javase%2Ftutorial%2Fuiswing%2F%2F/layout/box.html
            panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

            stop_button.setText("Stop");
            stop_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    graphic_panel.showEndPanel();
                }
            });

            control_panel.add(stop_button);
            control_panel.setBackground(Color.BLUE);
            control_panel.setMaximumSize(new Dimension(700, 50));
            panel.add(control_panel);

            panel.add(Box.createRigidArea(new Dimension(10, 10)));

            graphic_panel.setBackground(Color.BLACK);
            panel.add(graphic_panel);

            frame.add(panel);
            frame.setSize(new Dimension(700, 1000));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
    }

    private class GetProcessVariables extends TimerTask {
        BouncingBalls game;

        public GetProcessVariables(BouncingBalls game) {
            this.game = game;
        }

        public void run() {
            rerender(game);
        }
    }

    private void rerender(BouncingBalls game) {
        // Reference:
        // https://harith-sankalpa.medium.com/how-to-run-system-commands-from-java-applications-a914223edd24
        Runtime runTime = Runtime.getRuntime();
        int newCounter = restarted_game || graphic_panel == null ? 20 : graphic_panel.getNumBalls();
        ArrayList<Double> newBallSize = new ArrayList<>();
        ArrayList<Double> newBallSpeed = new ArrayList<>();
        ArrayList<Integer> newBallColors = new ArrayList<>();
        try {
            Process process = runTime.exec("ps x -o rss,%cpu,time");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            bufferedReader.readLine();
            String nextLine = bufferedReader.readLine();
            for (int i = 0; i < newCounter; ++i) {
                if (nextLine == null) {
                    break;
                }
                String[] data = nextLine.split(" ");
                data = Arrays.stream(data)
                        .filter(s -> (s.length() > 0))
                        .toArray(String[]::new);
                newBallSize.add(Math.log(Integer.parseInt(data[0])));
                newBallSpeed.add(Double.parseDouble(data[1]));
                String[] splittedTime = data[2].split(":");
                int hours = Integer.parseInt(splittedTime[0]);
                String[] minsAndSecs = splittedTime[1].split("\\.");
                int minutes = Integer.parseInt(minsAndSecs[0]);
                int seconds = Integer.parseInt(minsAndSecs[1]);
                newBallColors.add((hours * 3600 + minutes * 60 + seconds) % 7);
                nextLine = bufferedReader.readLine();
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }

        if (gui == null) {
            gui = game.new GUI(game, newCounter, newBallSize, newBallSpeed, newBallColors);
            gui.configure();
        } else {
            gui.update(game, newCounter, newBallSize, newBallSpeed, newBallColors);
        }
    }

    public static void main(String[] args) {
        BouncingBalls game = new BouncingBalls();
        timer = new Timer();
        // Set timer, get process variables every 10 seconds and update the GUI
        timer.schedule(game.new GetProcessVariables(game), 0, 10000);
    }

    public void restartGame(BouncingBalls game) {
        this.restarted_game = true;
        rerender(game);
        timer.cancel();
        timer = new Timer();
        timer.schedule(game.new GetProcessVariables(game), 0, 10000);
        this.restarted_game = false;
    }
}
