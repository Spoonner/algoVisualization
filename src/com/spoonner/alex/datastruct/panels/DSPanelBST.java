package com.spoonner.alex.datastruct.panels;

import com.spoonner.alex.datastruct.DSWindow;
import com.spoonner.alex.datastruct.views.DSViewBST;

import javax.swing.*;
import java.awt.*;

public class DSPanelBST extends DSPanel {

    private DSViewBST BSTView;
    private JTextField insertfield;
    protected JButton insertButton;
    private JTextField findfield;
    private JButton findButton;
    private JTextField deletefield;
    private JButton deleteButton;

    public DSPanelBST(DSWindow window) {
        super(window);

        Box box = Box.createHorizontalBox();


        insertfield = new JTextField("");
        insertfield.setMaximumSize(new Dimension(40, 30));
        insertfield.addActionListener(event -> {
            if (insertfield.getText().length() != 0) {
                    animate(BSTView.INSERT, extractString(insertfield.getText(),4));
                insertfield.setText("");
                changeDone();
            }
        });
        box.add(insertfield);

        insertButton = new JButton("Insert");
        insertButton.addActionListener(event -> {
            if (insertfield.getText().length() != 0) {
            animate(BSTView.INSERT, extractString(insertfield.getText(),4));
              insertfield.setText("");
            changeDone();
          }
        });
        box.add(insertButton);



        findfield = new JTextField("");
        findfield.setMaximumSize(new Dimension(40, 30));
        findfield.addActionListener(event -> {
            if (findfield.getText().length() != 0) {
                    animate(BSTView.FIND, extractString(findfield.getText(),4));
                findfield.setText("");

                changeDone();
            }
        });
        box.add(findfield);

        findButton = new JButton("Find");
        findButton.addActionListener(event -> {
            if (findfield.getText().length() != 0) {
                    animate(BSTView.FIND, extractString(findfield.getText(),4));
                findfield.setText("");
            changeDone();
          }
        });
        box.add(findButton);



        deletefield = new JTextField("");
        deletefield.setMaximumSize(new Dimension(40, 30));
        deletefield.addActionListener(event -> {
            if (deletefield.getText().length() != 0) {
                    animate(BSTView.DELETE, extractString(deletefield.getText(),4));
                deletefield.setText("");
                changeDone();
            }
        });
        box.add(deletefield);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(event -> {
            if (deletefield.getText().length() != 0) {
            animate(BSTView.DELETE, extractString(deletefield.getText(),4));
            deletefield.setText("");
            changeDone();
            }
          });
        box.add(deleteButton);



        this.add(box, BorderLayout.NORTH);
        this.add(view = BSTView = new DSViewBST(), BorderLayout.CENTER);
        setupAnimationPanel(BSTView);

    }

    public void disableSpecific() {
        insertfield.setEnabled(false);
        insertButton.setEnabled(false);
        findfield.setEnabled(false);
        findButton.setEnabled(false);
        deletefield.setEnabled(false);
        deleteButton.setEnabled(false);
    }



    public void enableSpecific() {
        insertfield.setEnabled(true);
        insertButton.setEnabled(true);
        findfield.setEnabled(true);
        findButton.setEnabled(true);
        deletefield.setEnabled(true);
        deleteButton.setEnabled(true);

    }







    // Persistence

    public void setData(Object data) {
        BSTView.setData(data);
    }

    public Object getData() {
        return BSTView.getData();
    }

}
