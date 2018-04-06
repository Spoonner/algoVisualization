package com.spoonner.alex.datastruct.panels;

import com.spoonner.alex.datastruct.DSWindow;
import com.spoonner.alex.datastruct.views.DSViewDijkstra;
import com.spoonner.alex.datastruct.views.DSViewPrim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DSPanelPrim extends DSPanelGraph {


    protected JTextField startfield;
    protected DSViewPrim graphView;
    protected JLabel startLabel;
    protected JButton primButton;

    public DSPanelPrim(DSWindow window) {
        super(window);

        Box box = Box.createHorizontalBox();

         startLabel = new JLabel("Starting Vertex:");
        box.add(startLabel);

        startfield = new JTextField("");
        startfield.setMaximumSize(new Dimension(50, 30));
        startfield.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (startfield.getText().length() != 0) {
                    int elem = extractInt(startfield.getText(),3);
                    if (elem < 1000) {
                        animate(DSViewDijkstra.DIJKSTRA, new Integer(elem));
                    }
                    startfield.setText("");
                    changeDone();
                }
            }
        });
        box.add(startfield);

        primButton = new JButton("Run Prim");
        primButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                int insertelem = extractInt(startfield.getText(),3);
                if (insertelem < 1000)
                    animate(DSViewPrim.PRIM, new Integer(insertelem));
                startfield.setText("");
                changeDone();
            }
        });
        box.add(primButton);



        AddGraphControls(box);



        this.add(box, BorderLayout.NORTH);
        this.add(view = graphView = new DSViewPrim(), BorderLayout.CENTER);
        setupAnimationPanel(graphView);

    }


    public void disableSpecific() {
        super.disableSpecific();
        startfield.setEnabled(false);
        primButton.setEnabled(false);
        startLabel.setEnabled(false);
    }



    public void enableSpecific() {
        super.enableSpecific();
        startfield.setEnabled(true);
        primButton.setEnabled(true);
        startLabel.setEnabled(true);
    }

    // Persistence

    public void setData(Object data) {
        graphView.setData(data);
    }

    public Object getData() {
        return graphView.getData();
    }

}
