package com.spoonner.alex.ds.panels;

import com.spoonner.alex.ds.DSWindow;
import com.spoonner.alex.ds.views.DSViewDijkstra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DSPanelDijkstra extends DSPanelGraph {


    protected JTextField startfield;
    protected DSViewDijkstra graphView;
    protected JLabel startLabel;
    protected JButton dijkstraButton;

    public DSPanelDijkstra(DSWindow window) {
        super(window);

        Box box = Box.createHorizontalBox();

         startLabel = new JLabel("Starting Vertex:");
        box.add(startLabel);

        startfield = new JTextField("");
        startfield.setMaximumSize(new Dimension(50, 30));
        startfield.addActionListener(event -> {
            if (startfield.getText().length() != 0) {
                int elem = ExtractInt(startfield.getText(),3);
                if (elem < 1000) {
                    Animate(DSViewDijkstra.DIJKSTRA, new Integer(elem));
                }
                startfield.setText("");
                changeDone();
            }
        });
        box.add(startfield);

        dijkstraButton = new JButton("Run Dijkstra");
        dijkstraButton.addActionListener(event -> {
            int insertelem = ExtractInt(startfield.getText(),3);
            if (insertelem < 1000)
                Animate(DSViewDijkstra.DIJKSTRA, new Integer(insertelem));
            startfield.setText("");
            changeDone();
        });
        box.add(dijkstraButton);



        AddGraphControls(box);



        this.add(box, BorderLayout.NORTH);
        this.add(view = graphView = new DSViewDijkstra(), BorderLayout.CENTER);
        SetupAnimationPanel(graphView);

    }


    public void DisableSpecific() {
        super.DisableSpecific();
        startfield.setEnabled(false);
        dijkstraButton.setEnabled(false);
        startLabel.setEnabled(false);
    }



    public void EnableSpecific() {
        super.EnableSpecific();
        startfield.setEnabled(true);
        dijkstraButton.setEnabled(true);
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
