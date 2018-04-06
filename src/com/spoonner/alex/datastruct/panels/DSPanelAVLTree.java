package com.spoonner.alex.datastruct.panels;

import com.spoonner.alex.datastruct.DSWindow;
import com.spoonner.alex.datastruct.views.DSViewAVLTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DSPanelAVLTree extends DSPanel {

    private DSViewAVLTree AVLView;
    private JTextField insertfield;
    protected JButton insertButton;
    private JTextField findfield;
    private JButton findButton;
    private JTextField deletefield;
    private JButton deleteButton;
    private JButton swapViewButton;

    private boolean viewingNPL = true;


    public DSPanelAVLTree(DSWindow window) {
        super(window);

        Box box = Box.createHorizontalBox();


        insertfield = new JTextField("");
        insertfield.setMaximumSize(new Dimension(40, 30));
        insertfield.addActionListener(event -> {
            if (insertfield.getText().length() != 0) {
                    animate(AVLView.INSERT, extractString(insertfield.getText(),4));
                insertfield.setText("");
                changeDone();
            }
        });
        box.add(insertfield);

        insertButton = new JButton("Insert");
        insertButton.addActionListener(event -> {
            if (insertfield.getText().length() != 0) {
            animate(AVLView.INSERT, extractString(insertfield.getText(),4));
              insertfield.setText("");
            changeDone();
          }
        });
        box.add(insertButton);



        findfield = new JTextField("");
        findfield.setMaximumSize(new Dimension(40, 30));
        findfield.addActionListener(event -> {
            if (findfield.getText().length() != 0) {
                    animate(AVLView.FIND, extractString(findfield.getText(),4));
                findfield.setText("");

                changeDone();
            }
        });
        box.add(findfield);

        findButton = new JButton("Find");
        findButton.addActionListener(event -> {
            if (findfield.getText().length() != 0) {
                    animate(AVLView.FIND, extractString(findfield.getText(),4));
                findfield.setText("");
            changeDone();
          }
        });
        box.add(findButton);



        deletefield = new JTextField("");
        deletefield.setMaximumSize(new Dimension(40, 30));
        deletefield.addActionListener(event -> {
            if (deletefield.getText().length() != 0) {
                    animate(AVLView.DELETE, extractString(deletefield.getText(),4));
                deletefield.setText("");
                changeDone();
            }
        });
        box.add(deletefield);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(event -> {
            if (deletefield.getText().length() != 0) {
            animate(AVLView.DELETE, extractString(deletefield.getText(),4));
            deletefield.setText("");
            changeDone();
            }
          });
        box.add(deleteButton);

        swapViewButton = new JButton("Hide Heights");
        swapViewButton.addActionListener(event -> {
            if (viewingNPL) {
                animate(AVLView.HIDE_HEIGHT);
                viewingNPL = false;
                swapViewButton.setText("Show Heights");


            }   else {
                animate(AVLView.SHOW_HEIGHT);
                viewingNPL = true;
                swapViewButton.setText("Hide Heights");

            }

          });
        box.add(swapViewButton);



        this.add(box, BorderLayout.NORTH);
        this.add(view = AVLView = new DSViewAVLTree(), BorderLayout.CENTER);
        setupAnimationPanel(AVLView);

    }

    public void disableSpecific() {
        insertfield.setEnabled(false);
        insertButton.setEnabled(false);
        findfield.setEnabled(false);
        findButton.setEnabled(false);
        deletefield.setEnabled(false);
        deleteButton.setEnabled(false);
        swapViewButton.setEnabled(false);
    }



    public void enableSpecific() {
        insertfield.setEnabled(true);
        insertButton.setEnabled(true);
        findfield.setEnabled(true);
        findButton.setEnabled(true);
        deletefield.setEnabled(true);
        deleteButton.setEnabled(true);
        swapViewButton.setEnabled(true);

    }







    // Persistence

    public void setData(Object data) {
        AVLView.setData(data);
    }

    public Object getData() {
        return AVLView.getData();
    }

}
