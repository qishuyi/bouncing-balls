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

import javax.swing.*;

public class BouncingBalls {
    private class GUI {
        // Reference: https://www.javatpoint.com/java-jframe
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JPanel control_panel = new JPanel();
        GraphicPanel graphic_panel;
        JButton stop_button = new JButton();

        public GUI(int counter, ArrayList<Double> ballSize, ArrayList<Double> ballSpeed, ArrayList<Integer> ballColors) {
            this.graphic_panel = new GraphicPanel(700, 1000, 60, counter, ballSize, ballSpeed, ballColors);
        }

        private void configure() {
            // Reference: https://docs.oracle.com/javase%2Ftutorial%2Fuiswing%2F%2F/layout/box.html
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

    public static void main(String[] args) {
        // Reference: https://harith-sankalpa.medium.com/how-to-run-system-commands-from-java-applications-a914223edd24
        Runtime runTime = Runtime.getRuntime();
        ArrayList<Double> ballSize = new ArrayList<>();
        ArrayList<Double> ballSpeed = new ArrayList<>();
        ArrayList<Integer> ballColors = new ArrayList<>();
        int counter = 0;
        try {
            Process process = runTime.exec("ps x -o rss,%cpu,time");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));     
            bufferedReader.readLine();
            String nextLine = bufferedReader.readLine();
            for (int i = 0; i < 20; ++i) {
                if (nextLine == null) {
                    break;
                }
                ++counter;
                String[] data = nextLine.split(" ");
                data = Arrays.stream(data)
                     .filter(s -> (s.length() > 0))
                     .toArray(String[]::new);  
                ballSize.add(Math.log(Integer.parseInt(data[0])));
                ballSpeed.add(Double.parseDouble(data[1]));
                String[] splittedTime = data[2].split(":");
                int hours = Integer.parseInt(splittedTime[0]);
                String[] minsAndSecs = splittedTime[1].split("\\.");
                int minutes = Integer.parseInt(minsAndSecs[0]);
                int seconds = Integer.parseInt(minsAndSecs[1]);
                ballColors.add((hours * 3600 + minutes * 60 + seconds) % 20);
                nextLine = bufferedReader.readLine();
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
        BouncingBalls game = new BouncingBalls();
        GUI gui = game.new GUI(counter, ballSize, ballSpeed, ballColors);
        gui.configure();
    }
}
