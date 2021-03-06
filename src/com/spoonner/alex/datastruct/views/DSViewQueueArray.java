package com.spoonner.alex.datastruct.views;


import com.spoonner.alex.appkit.core.gview.base.Vector2D;
import com.spoonner.alex.appkit.core.gview.object.Element;
import com.spoonner.alex.appkit.core.gview.object.ElementArrow;
import com.spoonner.alex.appkit.core.gview.object.ElementLabel;

import java.awt.*;


public class DSViewQueueArray extends DSView {

    public final int ENQUEUE = 1;
    public final int DEQUEUE = 2;

    protected int head;
    protected int tail;
    protected String queue[];
    protected ElementArrow headarrow;
        protected ElementArrow tailarrow;
    protected ElementLabel headlabel;
    protected ElementLabel taillabel;
    protected ElementLabel index[];
    public final int queuesize = 18;
    public final int NUMSTEPS = 40;

    public DSViewQueueArray() {
     //   super(container);

        int i;

        index = new ElementLabel[queuesize];
        waitscalefactor /= 2;
        queue = new String[queuesize];
        for (i = 0; i < queuesize; i++) {
            createRectangle("", i * 50 + 50, 210, 50,50, false);
            index[i] = createLabel(String.valueOf(i),i*50+50,247);
             index[i].setLabelColor(Color.BLUE);

           queue[i] = "";
        }


        tailarrow = createArrow(50, 315, 50, 260, 20, false);
        taillabel = createLabel("tail", 50, 325);
        headarrow = createArrow(50, 130, 50, 185, 20, false);
        headlabel = createLabel("head", 50, 110);



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


    protected void CallFunction(int function) {
        switch (function) {
            case DEQUEUE:
                dequeue();
                break;
        }

    }

    protected void CallFunction(int function, Object param1) {
        switch (function) {
            case ENQUEUE:
                String param = (String) param1;
                enqueue(param);
                break;

        }
    }



    public void enqueue(String value) {

        Element Enqueuedlabel = createLabel("Enqueueing: ", 100, 40, false);
        Element Enqueued = null;
        int i;


        if (tail + 1 % queuesize != head) {
            Enqueued = createLabel(value,-10,-10,false);
            Element lst[] = {Enqueuedlabel ,Enqueued };
            LineupHorizontal(lst);
            queue[tail] = value;
            Element arraynode = (Element) shapes.get(tail);
            tail = (tail + 1) % queuesize;
            Vector2D path[] = createPath(Enqueued.getPosition(), arraynode.getPosition(),NUMSTEPS);
            repaintwait();
            for (i=0; i<NUMSTEPS;i++) {
                  Enqueued.setPosition(path[i]);
                  repaintwaitmin();
              }
            repaintwait();
            if (tail == 0) {
                for (i=0; i<10; i++) {
                   tailarrow.move(-5*(queuesize-1),0);
                   taillabel.move(-5*(queuesize-1),0);
                   repaintwaitmin();
                }
            } else {
                for (i=0; i<10; i++) {
                   tailarrow.move(5, 0);
                   taillabel.move(5,0);
                   repaintwaitmin();
                }
            }
            removeAny(Enqueuedlabel);
            removeAny(Enqueued);
            arraynode.setLabel(String.valueOf(value));
        } else {
            Enqueued = createLabel("Queue is full", 200, 40, false);
            HoldoverGraphics.addElement(Enqueuedlabel);
            HoldoverGraphics.addElement(Enqueued);
        }


        repaint();
    }




    public void dequeue() {

        Element Dequeuelabel = createLabel("Dequeueing: ", 100, 40, false);
        Element Dequeued = null;
        int i;


        if (head != tail) {
            Element arraynode = (Element) shapes.get(head);
            Dequeued = createLabel(arraynode.getLabel(),arraynode.getPositionX(),arraynode.getPositionY(),false);
            Vector2D path[] = createPath(Dequeued.getPosition(),new Vector2D(165,40),NUMSTEPS);
            arraynode.setLabel("");
            for (i=0; i<NUMSTEPS;i++) {
                Dequeued.setPosition(path[i]);
                repaintwaitmin();
            }
            Element lst[] = {Dequeuelabel, Dequeued};
            LineupHorizontal(lst);
            repaintwait();
            head = (head + 1) % queuesize;
            if (head == 0) {
                for (i=0; i<10; i++) {
                    headarrow.move(-5*(queuesize-1),0);
                    headlabel.move(-5*(queuesize-1),0);
                    repaintwaitmin();
                }
            } else {
                for (i=0; i< 10;i++) {
                    headarrow.move(5,0);
                    headlabel.move(5,0);
                    repaintwaitmin();
                }
            }
            repaint();
        } else {
            Dequeued = createLabel("Queue is empty", 190, 40, false);
        }
        HoldoverGraphics.addElement(Dequeued);
        HoldoverGraphics.addElement(Dequeuelabel);
    }
}
