package com.spoonner.alex.datastruct.panels;

import com.spoonner.alex.datastruct.DSWindow;
import com.spoonner.alex.datastruct.views.DSViewKruskal;

import javax.swing.*;
import java.awt.*;

public class DSPanelKruskal extends DSPanelGraph {


    protected DSViewKruskal graphView;
    protected JButton kruskalButton;

    public DSPanelKruskal(DSWindow window) {
        super(window);

        Box box = Box.createHorizontalBox();


        kruskalButton = new JButton("Run Kruskal");
        kruskalButton.addActionListener(event -> {
                animate(DSViewKruskal.KRUSKAL);
            changeDone();
        });
        box.add(kruskalButton);



        AddGraphControls(box);



        this.add(box, BorderLayout.NORTH);
        this.add(view = graphView = new DSViewKruskal(), BorderLayout.CENTER);
        setupAnimationPanel(graphView);

    }


    public void disableSpecific() {
        super.disableSpecific();
        kruskalButton.setEnabled(false);
    }



    public void enableSpecific() {
        super.enableSpecific();
        kruskalButton.setEnabled(true);
    }

    // Persistence

    public void setData(Object data) {
        graphView.setData(data);
    }

    public Object getData() {
        return graphView.getData();
    }

}
