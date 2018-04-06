package com.spoonner.alex.datastruct.panels;

import com.spoonner.alex.datastruct.DSWindow;
import com.spoonner.alex.datastruct.views.DSViewDynamicProg;

import javax.swing.*;
import java.awt.*;

public class DSPanelDynamicProg extends DSPanel {

    protected DSViewDynamicProg DPView;
    protected JTextField inputfield;
    protected JButton recursivebutton;
    protected JButton tablebutton;
    protected JButton memoizedbutton;
    protected JLabel noteLabel;

    public DSPanelDynamicProg(DSWindow window) {
        super(window);

        paused = false;
        running = false;
        Box box = Box.createHorizontalBox();

        noteLabel = new JLabel(" (Max value = 46) ");
        box.add(noteLabel);
        inputfield = new JTextField("");
        inputfield.setMaximumSize(new Dimension(50, 30));
        inputfield.addActionListener(event -> animate(DPView.FIB_ERROR));
        box.add(inputfield);

        tablebutton = new JButton("Fibonacci: Table");
        tablebutton.addActionListener(event -> {
            int inputelem = extractInt(inputfield.getText(), 3);
            if (inputelem < 47)
                animate(DPView.FIB_TABLE, new Integer(inputelem));
            else {
                inputfield.setText("46");
                animate(DPView.FIB_TABLE, new Integer(46));
            }
            inputfield.setText("");
        });
        box.add(tablebutton);

        memoizedbutton = new JButton("Fibonacci: Memoized");
        memoizedbutton.addActionListener(event -> {
            int inputelem = extractInt(inputfield.getText(), 3);
            if (inputelem < 47)
                animate(DPView.FIB_MEMOIZED, new Integer(inputelem));
            else {
                inputfield.setText("46");
                animate(DPView.FIB_MEMOIZED, new Integer(46));
            }

            inputfield.setText("");
        });
        box.add(memoizedbutton);

        recursivebutton = new JButton("Fibonacci: Recurisve");
        recursivebutton.addActionListener(event -> {
            int inputelem = extractInt(inputfield.getText(), 3);
            if (inputelem < 47)
                animate(DPView.FIB_RECURSIVE, new Integer(inputelem));
            else {
                inputfield.setText("46");
                animate(DPView.FIB_RECURSIVE, new Integer(46));
            }

            inputfield.setText("");
        });
        box.add(recursivebutton);
        this.add(box, BorderLayout.NORTH);
        this.add(view = DPView = new DSViewDynamicProg(), BorderLayout.CENTER);
        setupAnimationPanel(DPView);

    }


    public void disableSpecific() {
        recursivebutton.setEnabled(false);
        memoizedbutton.setEnabled(false);
        tablebutton.setEnabled(false);
    }


    public void enableSpecific() {
        recursivebutton.setEnabled(true);
        memoizedbutton.setEnabled(true);
        tablebutton.setEnabled(true);

    }


    // Persistence

    public void setData(Object data) {

    }

    public Object getData() {
        return null;
    }

}
