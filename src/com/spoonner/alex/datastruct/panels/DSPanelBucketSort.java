package com.spoonner.alex.datastruct.panels;


import com.spoonner.alex.datastruct.DSWindow;
import com.spoonner.alex.datastruct.views.DSViewBucketSort;

import javax.swing.*;
import java.awt.*;

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
            animate(sortView.BUCKETSORT);
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
        setupAnimationPanel(sortView);

    }


    public void disableSpecific() {
        randomizeButton.setEnabled(false);
        sortButton.setEnabled(false);
    }


    public void enableSpecific() {
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
