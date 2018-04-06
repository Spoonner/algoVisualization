package com.spoonner.alex.appkit.core.swing;


import com.spoonner.alex.appkit.core.frame.AbstractDialog;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;


public class AbstractColorChooser extends AbstractDialog {

    JColorChooser cc;

    JPanel targetPanel;
    Color oldTargetColor = Color.black;

    public AbstractColorChooser(Container owner, boolean modal) {
        this(owner, modal, null);
    }

    public AbstractColorChooser(Container owner, boolean modal, JPanel targetPanel) {
        super(owner, modal);

        setTitle("Choose a color");
        setSize(500, 400);

        if(targetPanel != null) {
            this.targetPanel = targetPanel;
            this.oldTargetColor = targetPanel.getBackground();
        }

        cc = new JColorChooser(oldTargetColor);
        cc.getSelectionModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                updateTargetColor();
            }
        });
        getContentPane().add(cc, BorderLayout.CENTER);

        JButton cancel = new JButton("Cancel");
        setCancelButton(cancel);

        JButton ok = new JButton("OK");
        setOKButton(ok);
        setDefaultButton(ok);

        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        box.add(cancel);
        box.add(ok);
        box.add(Box.createHorizontalStrut(15));
        getContentPane().add(box, BorderLayout.SOUTH);
    }

    protected void updateTargetColor() {
        updateTargetColor(cc.getColor());
    }

    protected void updateTargetColor(Color c) {
        if(targetPanel != null)
            targetPanel.setBackground(c);
    }

    public void dialogWillCloseCancel() {
        updateTargetColor(oldTargetColor);
    }

    public void dialogWillCloseOK() {
        updateTargetColor();
    }

    public Color getColor() {
        return cc.getColor();
    }
}
