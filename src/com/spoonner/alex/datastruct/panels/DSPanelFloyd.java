package com.spoonner.alex.datastruct.panels;

import com.spoonner.alex.datastruct.DSWindow;
import com.spoonner.alex.datastruct.views.DSViewFloyd;

import javax.swing.*;
import java.awt.*;

public class DSPanelFloyd extends DSPanelGraph {


    protected DSViewFloyd graphView;
    protected JButton floydButton;

    public DSPanelFloyd(DSWindow window) {
        super(window);

        Box box = Box.createHorizontalBox();


       floydButton = new JButton("Run Floyd-Warshall");
        floydButton.addActionListener(event -> {
                animate(DSViewFloyd.FLOYD);
            changeDone();
        });
        box.add(floydButton);



        AddGraphControls(box);



        this.add(box, BorderLayout.NORTH);
        this.add(view = graphView = new DSViewFloyd(), BorderLayout.CENTER);
        setupAnimationPanel(graphView);

    }


    public void disableSpecific() {
        super.disableSpecific();
        floydButton.setEnabled(false);
    }



    public void enableSpecific() {
        super.enableSpecific();
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
