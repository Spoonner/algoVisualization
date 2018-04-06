package com.spoonner.alex.datastruct.panels;

import com.spoonner.alex.datastruct.DSWindow;
import com.spoonner.alex.datastruct.views.DSViewDijkstra;

import javax.swing.*;
import java.awt.*;

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
                int elem = extractInt(startfield.getText(),3);
                if (elem < 1000) {
                    animate(DSViewDijkstra.DIJKSTRA, new Integer(elem));
                }
                startfield.setText("");
                changeDone();
            }
        });
        box.add(startfield);

        dijkstraButton = new JButton("Run Dijkstra");
        dijkstraButton.addActionListener(event -> {
            int insertelem = extractInt(startfield.getText(),3);
            if (insertelem < 1000)
                animate(DSViewDijkstra.DIJKSTRA, new Integer(insertelem));
            startfield.setText("");
            changeDone();
        });
        box.add(dijkstraButton);



        AddGraphControls(box);



        this.add(box, BorderLayout.NORTH);
        this.add(view = graphView = new DSViewDijkstra(), BorderLayout.CENTER);
        setupAnimationPanel(graphView);

    }


    public void disableSpecific() {
        super.disableSpecific();
        startfield.setEnabled(false);
        dijkstraButton.setEnabled(false);
        startLabel.setEnabled(false);
    }



    public void enableSpecific() {
        super.enableSpecific();
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
