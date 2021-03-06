package com.spoonner.alex.datastruct.panels;

import com.spoonner.alex.datastruct.DSWindow;
import com.spoonner.alex.datastruct.views.DSViewHuff;

import javax.swing.*;
import java.awt.*;

public class DSPanelHuff extends DSPanel {

    protected DSViewHuff huffView;
    protected JTextField encodefield;
    protected JButton encodeButton;

    public DSPanelHuff(DSWindow window) {
        super(window);

        Box box = Box.createHorizontalBox();


        encodefield = new JTextField("");
        encodefield.setMinimumSize(new Dimension(100,200));
        encodefield.addActionListener(event -> {
            if (encodefield.getText().length() != 0) {
                animate(huffView.ENCODE, encodefield.getText());
                encodefield.setText("");
                changeDone();
            }
        });

        box.add(encodefield);


        encodeButton = new JButton("Encode");
        encodeButton.addActionListener(event -> {
            if (encodefield.getText().length() != 0) {
                animate(huffView.ENCODE, encodefield.getText());
                encodefield.setText("");
                changeDone();
            }
        });

        box.add(encodeButton);


        this.add(box, BorderLayout.NORTH);
        this.add(view =huffView = new DSViewHuff(), BorderLayout.CENTER);
        setupAnimationPanel(huffView);

    }


    public void disableSpecific() {
        encodeButton.setEnabled(false);
        encodefield.setEnabled(false);
    }


    public void enableSpecific() {
        encodeButton.setEnabled(true);
        encodefield.setEnabled(true);

    }



    // Persistence

    public void setData(Object data) {
        huffView.setData(data);
    }

    public Object getData() {
        return huffView.getData();
    }

}
