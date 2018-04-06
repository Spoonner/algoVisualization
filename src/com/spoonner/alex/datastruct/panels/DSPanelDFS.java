package com.spoonner.alex.datastruct.panels;

import com.spoonner.alex.datastruct.DSWindow;
import com.spoonner.alex.datastruct.views.DSViewDFS;

import javax.swing.*;
import java.awt.*;

public class DSPanelDFS extends DSPanelGraph {


    protected DSViewDFS graphView;
    protected JButton dfsButton;
    protected JButton changeDirectedButton;
    protected JButton showDFButton;


    protected boolean directed = true;
    protected boolean showDF = false;

    public DSPanelDFS(DSWindow window) {
        super(window);

        Box box = Box.createHorizontalBox();

        dfsButton = new JButton("Run DFS");
        dfsButton.addActionListener(event -> animate(DSViewDFS.DFS));
        box.add(dfsButton);

        changeDirectedButton = new JButton("Undirected Graph");
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


        showDFButton = new JButton("Show d/f");
        showDFButton.addActionListener(event -> {
            if (showDF) {
                animate(DSViewDFS.HIDEDF);
                showDFButton.setText("Show d/f");
                showDF = false;
            } else {
                animate(DSViewDFS.SHOWDF);
                showDFButton.setText("Hide d/f");
                showDF = true;
            }
        });
        box.add(showDFButton);




        AddGraphControls(box);



        this.add(box, BorderLayout.NORTH);
        this.add(view = graphView = new DSViewDFS(), BorderLayout.CENTER);
        setupAnimationPanel(graphView);

    }


    public void disableSpecific() {
        super.disableSpecific();
        dfsButton.setEnabled(false);
        changeDirectedButton.setEnabled(false);
        showDFButton.setEnabled(false);
    }



    public void enableSpecific() {
        super.enableSpecific();
        dfsButton.setEnabled(true);
        changeDirectedButton.setEnabled(true);
        showDFButton.setEnabled(true);

    }

    // Persistence

    public void setData(Object data) {
        graphView.setData(data);
    }

    public Object getData() {
        return graphView.getData();
    }

}
