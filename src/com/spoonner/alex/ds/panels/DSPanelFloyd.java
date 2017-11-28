package com.spoonner.alex.ds.panels;

import com.spoonner.alex.ds.DSWindow;
import com.spoonner.alex.ds.views.DSViewFloyd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DSPanelFloyd extends DSPanelGraph {


    protected DSViewFloyd graphView;
    protected JButton floydButton;

    public DSPanelFloyd(DSWindow window) {
        super(window);

        Box box = Box.createHorizontalBox();


       floydButton = new JButton("Run Floyd-Warshall");
        floydButton.addActionListener(event -> {
                Animate(DSViewFloyd.FLOYD);
            changeDone();
        });
        box.add(floydButton);



        AddGraphControls(box);



        this.add(box, BorderLayout.NORTH);
        this.add(view = graphView = new DSViewFloyd(), BorderLayout.CENTER);
        SetupAnimationPanel(graphView);

    }


    public void DisableSpecific() {
        super.DisableSpecific();
        floydButton.setEnabled(false);
    }



    public void EnableSpecific() {
        super.EnableSpecific();
        floydButton.setEnabled(true);
    }

    // Persistence

    public void setData(Object data) {
        /* graphView.setData(data);*/
    }

    public Object getData() {
        return graphView.getData();
    }

}
