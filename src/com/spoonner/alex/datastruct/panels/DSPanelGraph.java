package com.spoonner.alex.datastruct.panels;

import com.spoonner.alex.datastruct.DSWindow;
import com.spoonner.alex.datastruct.views.DSViewDijkstra;
import com.spoonner.alex.datastruct.views.DSViewGraph;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DSPanelGraph extends DSPanel {

    protected boolean smallList = true;
    protected JButton randomizeButton;
    protected JRadioButton viewLogicalButton;
    protected JRadioButton viewListButton;
    protected JRadioButton viewArrayButton;
    protected JButton changeSizeButton;


    protected boolean viewingSmall = true;
    protected int viewingCurrent = DSViewGraph.VIEWLOGICAL;

    public DSPanelGraph(DSWindow window) {
        super(window);
    }

    protected void AddGraphControls(Box box) {


        changeSizeButton = new JButton("Large Graph");
        changeSizeButton.addActionListener(event -> {
            if (viewingSmall) {
                animate(DSViewGraph.CHANGETOLARGE );
                changeSizeButton.setText("Small Graph");
                viewingSmall = false;
            } else {
                animate(DSViewGraph.CHANGETOSMALL );
                changeSizeButton.setText("Large Graph");
                viewingSmall = true;
            }
            changeDone();
        });
        box.add(changeSizeButton);

        randomizeButton = new JButton("New Graph");
        randomizeButton.addActionListener(event -> {
            animate(DSViewGraph.RANDOMIZE );
            changeDone();
        });
        box.add(randomizeButton);

        viewLogicalButton = new JRadioButton("View Logical");
        viewLogicalButton.addActionListener(event -> {
            if (viewingCurrent != DSViewDijkstra.VIEWLOGICAL) {
                animate(DSViewGraph.VIEWLOGICAL);
                viewingCurrent = DSViewGraph.VIEWLOGICAL;
            }
        });
        viewLogicalButton.setSelected(true);
        box.add(viewLogicalButton);


        viewListButton = new JRadioButton("View Adjacency List");
           viewListButton.addActionListener(event -> {
               if (viewingCurrent != DSViewDijkstra.VIEWINTERNALLIST ) {
                   animate(DSViewGraph.VIEWINTERNALLIST);
                   viewingCurrent = DSViewDijkstra.VIEWINTERNALLIST;
               }
           });
           box.add(viewListButton);

        viewArrayButton = new JRadioButton("View Adjacency Matrix");
           viewArrayButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent event) {
                   if (viewingCurrent != DSViewDijkstra.VIEWINTERNALARRAY) {
                       animate(DSViewGraph.VIEWINTERNALARRAY);
                       viewingCurrent = DSViewGraph.VIEWINTERNALARRAY;
                   }
               }
           });
           box.add(viewArrayButton);


        ButtonGroup group = new ButtonGroup();
        group.add(viewLogicalButton);
        group.add(viewListButton);
        group.add(viewArrayButton);

    }


    public void disableSpecific() {
        randomizeButton.setEnabled(false);
        viewArrayButton.setEnabled(false);
        viewListButton.setEnabled(false);
        viewLogicalButton.setEnabled(false);
        changeSizeButton.setEnabled(false);
    }


    public void enableSpecific() {
        randomizeButton.setEnabled(true);
        viewArrayButton.setEnabled(true);
        viewListButton.setEnabled(true);
        viewLogicalButton.setEnabled(true);
        changeSizeButton.setEnabled(true);
    }

    // Persistence



}
