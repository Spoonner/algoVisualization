package com.spoonner.alex.datastruct.panels;

import com.spoonner.alex.datastruct.DSWindow;
import com.spoonner.alex.datastruct.views.DSView;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

public class DSPanel extends JPanel {

    protected DSWindow window;
    private JButton stepButton;
    private JButton pauseButton;
    private JButton skipButton;
    private JSlider speed;
    private JLabel runningMsg;
    boolean paused;
    boolean running;
    protected DSView view;

    public DSPanel(DSWindow window) {
        super(new BorderLayout());
        this.window = window;
    }

    protected void animate(final int Function, final Object param1, final Object param2) {

        Thread v = new Thread(() -> {
            startingAnimation();
            view.Animate(Function, param1, param2);
            endingAnimation();
            repaint();
        });
        v.start();
    }


    protected void animate(final int Function, final Object param) {

        Thread v = new Thread(() -> {
            startingAnimation();
            view.Animate(Function, param);
            endingAnimation();
            repaint();
        });
        v.start();
    }

    protected void animate(final int Function) {

        Thread v = new Thread(() -> {
            startingAnimation();
            view.Animate(Function);
            endingAnimation();
            repaint();
        });
        v.start();
    }

    protected void setupAnimationPanel(final DSView view) {
        Box bottombox = Box.createHorizontalBox();

        Box buttonLabelBox = Box.createVerticalBox();
        Box buttonBox = Box.createHorizontalBox();


        runningMsg = new JLabel("Animation Completed");

        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(event -> {
            if (paused) {
                pauseButton.setText("Pause");
                view.go();
                paused = false;
                if (running) {
                    runningMsg.setText("Animation Running");
                    runningMsg.setForeground(Color.GREEN);
                }
                stepButton.setEnabled(false);
            } else {
                pauseButton.setText("Go");
                if (running) {
                    runningMsg.setText("Animation Paused");
                    runningMsg.setForeground(Color.RED);
                    stepButton.setEnabled(true);
                }
                paused = true;
                view.pause();

            }
            changeDone();
        });
        view.setDelay(25);

        buttonBox.add(pauseButton);


        stepButton = new JButton("Step");
        stepButton.addActionListener(event -> {
            view.step();
            changeDone();
        });
        buttonBox.add(stepButton);

        stepButton.setEnabled(false);

        skipButton = new JButton("Skip");
        skipButton.addActionListener(event -> {
            view.skip();
            changeDone();
        });
        buttonBox.add(skipButton);
        stepButton.setEnabled(false);


        buttonLabelBox.add(runningMsg, BorderLayout.WEST);
        buttonLabelBox.add(buttonBox, BorderLayout.WEST);
        bottombox.add(buttonLabelBox);

        speed = new JSlider();
        speed.setBorder(BorderFactory.createTitledBorder("Animation Speed"));

        Hashtable labelTable = new Hashtable();
        labelTable.put(0, new JLabel("Fast"));
        labelTable.put(100, new JLabel("Slow"));
        speed.setLabelTable(labelTable);
        speed.setInverted(true);
        speed.setPaintLabels(true);
        speed.setValue(25);

        speed.addChangeListener(event -> {
            view.setDelay(speed.getValue());
            changeDone();
        });
        bottombox.add(speed);


        this.add(bottombox, BorderLayout.SOUTH);

    }

    public void disableSpecific() {
        /* Override this method to disable specific buttons */
    }

    private void startingAnimation() {
        running = true;
        if (paused) {
            runningMsg.setText("Animation Paused");
            runningMsg.setForeground(Color.RED);
            stepButton.setEnabled(true);
        } else {
            runningMsg.setText("Animation Running");
            runningMsg.setForeground(Color.GREEN);
            stepButton.setEnabled(false);
        }
        skipButton.setEnabled(true);
        disableSpecific();
    }

    protected int extractInt(String text, int digits) {
        int extracted;

        try {
            extracted = Integer.parseInt(extractString(text, digits));
        } catch (Exception e) {
            extracted = Integer.MAX_VALUE;
        }
        return extracted;
    }


    protected String extractString(String val, int maxsize) {
        if (val.length() <= maxsize)
            return val;
        return val.substring(0, maxsize);
    }


    public void enableSpecific() {
/* Override this method to enable specific buttons */
    }

    public void endingAnimation() {

        running = false;
        runningMsg.setText("Animation Completed");
        runningMsg.setForeground(Color.BLACK);
        stepButton.setEnabled(false);
        skipButton.setEnabled(false);
        enableSpecific();
    }

    // This method mark the document as dirty (the Save menu item is enabled)
    public void changeDone() {
        window.getDocument().changeDone();
    }

    public BufferedImage getImage() {
        return view.getImage();
    }

    public String getEPS() {
        return view.getEPS();
    }
}
