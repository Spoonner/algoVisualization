package com.spoonner.alex.datastruct.panels;

import com.spoonner.alex.datastruct.DSWindow;
import com.spoonner.alex.datastruct.views.DSViewDynamicProg2;

import javax.swing.*;
import java.awt.*;

public class DSPanelDynamicProg2 extends DSPanel {

    protected DSViewDynamicProg2 DPView;
    protected JTextField inputfield;
    protected JButton recursivebutton;
    protected JButton tablebutton;
    protected JButton memoizedbutton;
    protected JLabel noteLabel;

    public DSPanelDynamicProg2(DSWindow window) {
        super(window);

        paused = false;
        running = false;
        Box box = Box.createHorizontalBox();

        noteLabel = new JLabel("Make change for");
        box.add(noteLabel);
        inputfield = new JTextField("");
        inputfield.setMaximumSize(new Dimension(50, 30));
        inputfield.addActionListener(event -> animate(DPView.CHANGE_ERROR));
        box.add(inputfield);

        tablebutton = new JButton("Change: Table");
        tablebutton.addActionListener(event -> {
            int inputelem = extractInt(inputfield.getText(), 3);
            if (inputelem < 150)
                animate(DPView.CHANGE_TABLE, new Integer(inputelem));
            else {
                inputfield.setText("150");
                animate(DPView.CHANGE_TABLE, new Integer(150));
            }
            inputfield.setText("");
        });
        box.add(tablebutton);

        memoizedbutton = new JButton("Change: Memoized");
        memoizedbutton.addActionListener(event -> {
            int inputelem = extractInt(inputfield.getText(), 3);
            if (inputelem < 150)
                animate(DPView.CHANGE_MEMOIZED, new Integer(inputelem));
            else {
                inputfield.setText("150");
                animate(DPView.CHANGE_MEMOIZED, new Integer(150));
            }

            inputfield.setText("");
        });
        box.add(memoizedbutton);

        recursivebutton = new JButton("Change: Recurisve");
        recursivebutton.addActionListener(event -> {
            int inputelem = extractInt(inputfield.getText(), 3);
            if (inputelem < 150)
                animate(DPView.CHANGE_RECURSIVE, new Integer(inputelem));
            else {
                inputfield.setText("150");
                animate(DPView.CHANGE_RECURSIVE, new Integer(150));
            }

            inputfield.setText("");
        });
        box.add(recursivebutton);
        this.add(box, BorderLayout.NORTH);
        this.add(view = DPView = new DSViewDynamicProg2(), BorderLayout.CENTER);
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
