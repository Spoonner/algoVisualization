package com.spoonner.alex.datastruct.panels;

import com.spoonner.alex.datastruct.DSWindow;
import com.spoonner.alex.datastruct.views.DSViewHeap;

import javax.swing.*;
import java.awt.*;

public class DSPanelHeap extends DSPanel {

    protected DSViewHeap heapView;
    protected JTextField insertfield;
    protected JButton insertbutton;
    protected JButton removesmallestbutton;
    protected JButton buildheapbutton;
    protected JButton deletebutton;

    public DSPanelHeap(DSWindow window) {
        super(window);

        paused = false;
        running = false;
        Box box = Box.createHorizontalBox();

        insertfield = new JTextField("");
        insertfield.setMaximumSize(new Dimension(50, 30));
        insertfield.addActionListener(event -> {
            if (insertfield.getText().length() != 0) {
                int insertelem = extractInt(insertfield.getText(),3);
                if (insertelem < 1000) {
                    animate(heapView.INSERT, new Integer(insertelem));
                }
                insertfield.setText("");
                changeDone();
            }
        });
        box.add(insertfield);

        insertbutton = new JButton("Insert");
        insertbutton.addActionListener(event -> {
            int insertelem = extractInt(insertfield.getText(),3);
            if (insertelem < 1000)
                animate(heapView.INSERT, new Integer(insertelem));
            insertfield.setText("");
            changeDone();
        });
        box.add(insertbutton);

        removesmallestbutton = new JButton("Remove Smallest");
        removesmallestbutton.addActionListener(event -> {
            animate(heapView.REMOVESMALLEST);
            changeDone();
        });
        box.add(removesmallestbutton);

        deletebutton = new JButton("Delete Heap");
        deletebutton.addActionListener(event -> {
            animate(heapView.DELETEHEAP);
            changeDone();
        });
        box.add(deletebutton);

        buildheapbutton = new JButton("Build Heap");
        buildheapbutton.addActionListener(event -> {
            animate(heapView.BUILDHEAP);
            changeDone();
        });
        box.add(buildheapbutton);

        this.add(box, BorderLayout.NORTH);
        this.add(view = heapView = new DSViewHeap(), BorderLayout.CENTER);
       setupAnimationPanel(heapView);


    }


    public void disableSpecific() {
        buildheapbutton.setEnabled(false);
        deletebutton.setEnabled(false);
        insertbutton.setEnabled(false);
        removesmallestbutton.setEnabled(false);
        insertfield.setEnabled(false);
    }



    public void enableSpecific() {
        buildheapbutton.setEnabled(true);
        deletebutton.setEnabled(true);
        insertbutton.setEnabled(true);
        removesmallestbutton.setEnabled(true);
        insertfield.setEnabled(true);
        insertfield.setRequestFocusEnabled(true);
    }



    // Persistence

    public void setData(Object data) {
        heapView.setData(data);
    }

    public Object getData() {
        return heapView.getData();
    }

}
