package com.spoonner.alex.datastruct.panels;

import com.spoonner.alex.datastruct.DSWindow;
import com.spoonner.alex.datastruct.views.DSViewBFS;
import com.spoonner.alex.datastruct.views.DSViewDFS;

import javax.swing.*;
import java.awt.*;

public class DSPanelBFS extends DSPanelGraph {


    private DSViewBFS graphView;
    private JButton dfsButton;
    private JButton changeDirectedButton;
    protected JButton showDFButton;
    private JTextField startfield;
    private JLabel startLabel;


    private boolean directed = false;

    public DSPanelBFS(DSWindow window) {
        super(window);

        Box box = Box.createHorizontalBox();
        startLabel = new JLabel("Start:");
        box.add(startLabel);

        startfield = new JTextField("");
        startfield.setMaximumSize(new Dimension(50, 30));
        startfield.addActionListener(event -> {
            if (startfield.getText().length() != 0) {
                int elem = extractInt(startfield.getText(), 3);
                if (elem < 1000) {
                    animate(DSViewBFS.BFS, elem);
                }
                startfield.setText("");
            }
        });
        box.add(startfield);

        dfsButton = new JButton("BFS");
        dfsButton.addActionListener(event -> {
            int startelem = extractInt(startfield.getText(), 3);
            if (startelem < 1000)
                animate(DSViewBFS.BFS, startelem);
            startfield.setText("");
        });
        box.add(dfsButton);


        changeDirectedButton = new JButton("Directed Graph");
        changeDirectedButton.addActionListener(event -> {
            if (directed) {
                animate(DSViewDFS.CHANGETOUNDIRECTED);
                changeDirectedButton.setText("Directed Graph");
                directed = false;
            } else {
                animate(DSViewDFS.CHANGETODIRECTED);
                changeDirectedButton.setText("Undirected Graph");
                directed = true;
            }
        });
        box.add(changeDirectedButton);


        AddGraphControls(box);


        this.add(box, BorderLayout.NORTH);
        this.add(view = graphView = new DSViewBFS(), BorderLayout.CENTER);
        setupAnimationPanel(graphView);

    }


    public void disableSpecific() {
        super.disableSpecific();
        dfsButton.setEnabled(false);
        changeDirectedButton.setEnabled(false);
        startfield.setEnabled(false);
    }


    public void enableSpecific() {
        super.enableSpecific();
        dfsButton.setEnabled(true);
        changeDirectedButton.setEnabled(true);
        startfield.setEnabled(true);

    }

    // Persistence

    public void setData(Object data) {
        graphView.setData(data);
    }

    public Object getData() {
        return graphView.getData();
    }

}
