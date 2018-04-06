package com.spoonner.alex.datastruct.panels;

import com.spoonner.alex.datastruct.DSWindow;
import com.spoonner.alex.datastruct.views.DSViewCC;

import javax.swing.*;
import java.awt.*;

public class DSPanelCC extends DSPanelGraph {


    protected DSViewCC graphView;
    protected JButton dfsButton;



    protected boolean showDF = false;

    public DSPanelCC(DSWindow window) {
        super(window);

        Box box = Box.createHorizontalBox();

        dfsButton = new JButton("Run Connected Components");
        dfsButton.addActionListener(event -> animate(DSViewCC.CC));
        box.add(dfsButton);






        AddGraphControls(box);



        this.add(box, BorderLayout.NORTH);
        this.add(view = graphView = new DSViewCC(), BorderLayout.CENTER);
        setupAnimationPanel(graphView);

    }


    public void disableSpecific() {
        super.disableSpecific();
        dfsButton.setEnabled(false);
    }



    public void enableSpecific() {
        super.enableSpecific();
        dfsButton.setEnabled(true);

    }

    // Persistence

    public void setData(Object data) {
        graphView.setData(data);
    }

    public Object getData() {
        return graphView.getData();
    }

}
