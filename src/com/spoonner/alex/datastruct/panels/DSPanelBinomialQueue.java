package com.spoonner.alex.datastruct.panels;


import com.spoonner.alex.datastruct.DSWindow;
import com.spoonner.alex.datastruct.views.DSViewBinomialQueue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DSPanelBinomialQueue extends DSPanel {


    private JTextField insertfield;
    private DSViewBinomialQueue queueView;
    private JRadioButton viewLogicalButton;
    private JRadioButton viewInternalButton;
    private int viewingCurrent = DSViewBinomialQueue.VIEW_LOGICAL;


    protected JButton insertButton;
    private JButton removeSmallestButton;

    public DSPanelBinomialQueue(DSWindow window) {
        super(window);

        Box box = Box.createHorizontalBox();


        insertfield = new JTextField("");
        insertfield.setMaximumSize(new Dimension(50, 30));
        insertfield.addActionListener(event -> {
            if (insertfield.getText().length() != 0) {

                    animate(DSViewBinomialQueue.INSERT, extractString(insertfield.getText(),4));

                insertfield.setText("");
            }
        });
        box.add(insertfield);

        insertButton = new JButton("Insert");
        insertButton.addActionListener(event -> {
                animate(DSViewBinomialQueue.INSERT, extractString(insertfield.getText(),4));
            insertfield.setText("");
        });
        box.add(insertButton);

        removeSmallestButton = new JButton("Remove Smallest");
        removeSmallestButton.addActionListener(event -> animate(DSViewBinomialQueue.REMOVE_SMALLEST));
        box.add(removeSmallestButton);



        viewLogicalButton = new JRadioButton("View Logical Representation");
           viewLogicalButton.addActionListener(event -> {
               if (viewingCurrent != DSViewBinomialQueue.VIEW_LOGICAL) {
                   animate(DSViewBinomialQueue.VIEW_LOGICAL);
                   viewingCurrent = DSViewBinomialQueue.VIEW_LOGICAL;
               }
           });
        viewLogicalButton.setSelected(true);

        box.add(viewLogicalButton);

        viewInternalButton = new JRadioButton("View Internal Represntation");
           viewInternalButton.addActionListener(event -> {
               if (viewingCurrent != DSViewBinomialQueue.VIEW_INTERNAL) {
                   animate(DSViewBinomialQueue.VIEW_INTERNAL);
                   viewingCurrent = DSViewBinomialQueue.VIEW_INTERNAL;
               }
           });
           box.add(viewInternalButton);


        ButtonGroup group = new ButtonGroup();
        group.add(viewLogicalButton);
        group.add(viewInternalButton);

        this.add(box, BorderLayout.NORTH);
        this.add(view = queueView = new DSViewBinomialQueue(), BorderLayout.CENTER);
        setupAnimationPanel(queueView);

    }


    public void disableSpecific() {
        super.disableSpecific();
        insertfield.setEnabled(false);
        insertButton.setEnabled(false);
        removeSmallestButton.setEnabled(false);
        viewInternalButton.setEnabled(false);
        viewLogicalButton.setEnabled(false);
    }



    public void enableSpecific() {
        super.enableSpecific();
        insertfield.setEnabled(true);
        insertButton.setEnabled(true);
        removeSmallestButton.setEnabled(true);
        viewInternalButton.setEnabled(true);
        viewLogicalButton.setEnabled(true);
    }


    public void setData(Object data) {
    }

    public Object getData() {
        return null;
    }

}
