package com.spoonner.alex.ds.panels;


import com.spoonner.alex.ds.DSWindow;
import com.spoonner.alex.ds.views.DSViewBucketSort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DSPanelBucketSort extends DSPanel {



    protected boolean smallList = true;
    protected DSViewBucketSort sortView;
    protected JButton randomizeButton;
    protected JButton switchViewButton;
    protected JButton toggleIndexButton;
    protected JButton sortButton;
    protected JButton sizeButton;

    public DSPanelBucketSort(DSWindow window) {
        super(window);

        Box box = Box.createHorizontalBox();


        sortButton = new JButton("Bucket Sort");
        sortButton.addActionListener(event -> {
            Animate(sortView.BUCKETSORT);
            changeDone();
        });
        box.add(sortButton);


        randomizeButton = new JButton("Randomize");
        randomizeButton.addActionListener(event -> {
            sortView.Randomize();
            changeDone();
        });
        box.add(randomizeButton);


        this.add(box, BorderLayout.NORTH);
        this.add(view = sortView = new DSViewBucketSort(), BorderLayout.CENTER);
        SetupAnimationPanel(sortView);

    }


    public void DisableSpecific() {
        randomizeButton.setEnabled(false);
        sortButton.setEnabled(false);
    }


    public void EnableSpecific() {
        randomizeButton.setEnabled(true);
        sortButton.setEnabled(true);


    }

    // Persistence

    public void setData(Object data) {
        sortView.setData(data);
    }

    public Object getData() {
        return sortView.getData();
    }

}
