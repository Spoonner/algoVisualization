package com.spoonner.alex.datastruct.views;


import com.spoonner.alex.appkit.core.gview.base.Vector2D;
import com.spoonner.alex.appkit.core.gview.object.Element;
import com.spoonner.alex.appkit.core.gview.object.ElementArrow;
import com.spoonner.alex.appkit.core.gview.object.ElementLabel;

import java.awt.*;



public class DSViewStackArray extends DSView {

    public final int PUSH = 1;
    public final int POP = 2;


    protected int stacktop;
    protected String stack[];
    protected ElementArrow toparrow;
    protected ElementLabel toplabel;
    public final int stacksize = 18;
    public final int NUMSTEPS = 40;
    protected ElementLabel index[];


    protected void CallFunction(int function) {
        switch (function) {
            case POP:
                pop();
                break;
        }

    }

    protected void CallFunction(int function, Object param1) {
        switch (function) {
            case PUSH:
                push((String) param1);
                break;
        }
    }


    public DSViewStackArray() {
        super();

        int i;
        waitscalefactor /= 2;

        stack = new String[stacksize];
        index = new ElementLabel[stacksize];
        for (i = 0; i < stacksize; i++) {
            createRectangle("", i * 50 + 50, 150, 50, 50, false);
            index[i] = createLabel(String.valueOf(i),i*50+50,187);
            index[i].setLabelColor(Color.BLUE);
            stack[i] = "";

        }


        toparrow = createArrow(50, 245, 50, 200, 20, false);
        toplabel = createLabel("top", 50, 255);


        /*      createLabel("label_1", 200, 250);

              DSShapeSingleLLR r1 = createSingleLinkedListRect("A", 200, 300, 100, 50);
              DSShapeSingleLLR r2 = createSingleLinkedListRect("B", 350, 300, 100, 50);

              Link l = createLink(r1, r2, Link.SHAPE_ARC, Element.ANCHOR_CENTER, Element.ANCHOR_CENTER, "A-B", 0);
              l.setSourceOffset(30, 0);
              l.setTargetOffset(-30, 0);

              DSShapeDoubleLLR r3 = createDoubleLinkedListRect("C", 200, 400, 100, 50);
              DSShapeDoubleLLR r4 = createDoubleLinkedListRect("D", 350, 400, 100, 50);

              l = createLink(r3, r4, Link.SHAPE_ARC, Element.ANCHOR_RIGHT, Element.ANCHOR_LEFT, "C-D", 0);
              l.setSourceOffset(-20, -10);
              l.setTargetOffset(20, -10);

              l = createLink(r4, r3, Link.SHAPE_ARC, Element.ANCHOR_LEFT, Element.ANCHOR_RIGHT, "D-C", 0);
              l.setSourceOffset(20, 10);
              l.setTargetOffset(-20, 10);  */
    }


    public void push(String value) {

        Element Pushlabel = createLabel("Pushing: ", 100, 40, false);
        Element Pushed = null;
        int i;


        if (stacktop < stacksize) {
            Pushed = createLabel(value, -10, -10, false);
            Element lst[] = {Pushlabel, Pushed};
            LineupHorizontal(lst);

            stack[stacktop++] = value;

            Element arraynode = (Element) shapes.get(stacktop - 1);


            Vector2D path[] = createPath(Pushed.getPosition(), arraynode.getPosition(), NUMSTEPS);
            repaintwait();
            for (i = 0; i < NUMSTEPS; i++) {
                Pushed.setPosition(path[i]);
                repaintwaitmin();
            }
            repaintwait();

            removeAny(Pushed);
            arraynode.setLabel(String.valueOf(value));

            for (i = 0; i < 10; i++) {
                toparrow.move(5, 0);
                toplabel.move(5, 0);
                repaintwaitmin();
            }
            removeAny(Pushlabel);

        } else {
            Pushed = createLabel("Stack is full", 175, 40, false);
            HoldoverGraphics.addElement(Pushlabel);
            HoldoverGraphics.addElement(Pushed);

        }


        repaint();
    }


    public void pop() {

        Element Poplabel = createLabel("Popping: ", 100, 40, false);
        Element Popped = null;
        int i;


        if (stacktop > 0) {
            stacktop = stacktop - 1;
            Element arraynode = (Element) shapes.get(stacktop);
            Popped = createLabel(arraynode.getLabel(), arraynode.getPositionX(), arraynode.getPositionY(), false);
            arraynode.setLabel("");
            Vector2D path[] = createPath(Popped.getPosition(), new Vector2D(150, 40), NUMSTEPS);
            repaintwait();
            for (i = 0; i < 10; i++) {
                toparrow.move(-5, 0);
                toplabel.move(-5, 0);
                repaintwaitmin();
            }
            repaintwait();

            for (i = 0; i < NUMSTEPS; i++) {
                Popped.setPosition(path[i]);
                repaintwaitmin();
            }
            Element lst[] = {Poplabel,Popped};
            LineupHorizontal(lst);
            repaintwait();
        } else {
            Popped = createLabel("Stack is Empty", 175, 40, false);

        }
        HoldoverGraphics.addElement(Poplabel);
        HoldoverGraphics.addElement(Popped);
    }
}
