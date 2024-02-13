package com.bouncingballs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class BouncingBalls {
    private class GUI {
        // Reference: https://www.javatpoint.com/java-jframe
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JPanel controlPanel = new JPanel();
        GraphicPanel graphicPanel = new GraphicPanel(700, 1000, 60);
        JButton pauseButton = new JButton();
        JButton stopButton = new JButton();

        private void configure() {
            // Reference: https://docs.oracle.com/javase%2Ftutorial%2Fuiswing%2F%2F/layout/box.html
            panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

            pauseButton.setText("Pause");
            stopButton.setText("Stop");
            pauseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    graphicPanel.showPausePanel();
                }
            });
            stopButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    graphicPanel.showEndPanel();
                }
            });

            controlPanel.add(pauseButton);
            controlPanel.add(stopButton);
            controlPanel.setBackground(Color.BLUE);
            controlPanel.setMaximumSize(new Dimension(700, 50));
            panel.add(controlPanel);
            
            panel.add(Box.createRigidArea(new Dimension(10, 10)));
            
            graphicPanel.setBackground(Color.BLACK);
            panel.add(graphicPanel);

            frame.add(panel);
            frame.setSize(new Dimension(700, 1000));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
    }

    public static void main(String[] args) {
        BouncingBalls game = new BouncingBalls();
        GUI gui = game.new GUI();
        gui.configure();
    }
}
