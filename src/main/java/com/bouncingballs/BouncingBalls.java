/**
 * This file is the top-level definition of the game.
 */
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
        JPanel control_panel = new JPanel();
        GraphicPanel graphic_panel = new GraphicPanel(700, 1000, 60);
        // JButton pauseButton = new JButton();
        JButton stop_button = new JButton();

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
        BouncingBalls game = new BouncingBalls();
        GUI gui = game.new GUI();
        gui.configure();
    }
}
