package com.spoonner.alex.datastruct.panels;

import com.spoonner.alex.datastruct.DSWindow;
import com.spoonner.alex.datastruct.views.DSViewTopological;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DSPanelTopological extends DSPanelGraph {


    protected DSViewTopological graphView;
    protected JButton topo1Button;
    protected JButton topo2Button;


    protected boolean directed = true;
    protected boolean showDF = false;

    public DSPanelTopological(DSWindow window) {
        super(window);

        Box box = Box.createHorizontalBox();

        topo1Button = new JButton("Topological Sort");
        topo1Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                    animate(DSViewTopological.TOPOLOGICAL_INDEGREE);
            }
        });
        box.add(topo1Button);

/*        topo2Button = new JButton("Topological Sort: DFS");
        topo2Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                    animate(DSViewTopological.TOPOLOGICAL_DFS);
            }
        });
        box.add(topo2Button); */





        AddGraphControls(box);



        this.add(box, BorderLayout.NORTH);
        this.add(view = graphView = new DSViewTopological(), BorderLayout.CENTER);
        setupAnimationPanel(graphView);

    }


    public void disableSpecific() {
        super.disableSpecific();
        topo1Button.setEnabled(false);
     //   topo2Button.setEnabled(false);

    }



    public void enableSpecific() {
        super.enableSpecific();
        topo1Button.setEnabled(true);
   //     topo2Button.setEnabled(true);

    }

    // Persistence

    public void setData(Object data) {
        graphView.setData(data);
    }

    public Object getData() {
        return graphView.getData();
    }

}
