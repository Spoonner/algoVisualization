package com.spoonner.alex.datastruct.shapes;

import com.spoonner.alex.appkit.core.gview.shape.LabelShape;
import com.spoonner.alex.appkit.misc.XMLSerializable;

import java.awt.*;

public class DSShapeSingleLLU extends DSShapeRect implements XMLSerializable {

    protected boolean isNull = false;
    protected double percentlink = 0.25;



    public DSShapeSingleLLU() {
        super();
    }

    public void setpercentLink(double newpercent) {
        if (newpercent > 0 && newpercent < 1)
            percentlink = newpercent;
    }

    public double getpercentLink() {
        return percentlink;
    }

    public void drawShape(Graphics2D g) {
        super.drawShape(g);

        Rectangle r = getFrame().rectangle();
        g.drawLine(r.x, (int)(r.y + r.height*percentlink), r.x+r.width, (int) (r.y+r.height*percentlink));


        if (isNull) {
            g.drawLine(r.x,r.y, r.x+r.width, (int) (r.y+r.height*percentlink));
        } else  {
         //   g.drawLine((int) (r.x+r.width/2),  r.y, r.x+r.width/2, (int) (r.y+r.height*percentlink/2));
        }


    }


    public void drawLabel(Graphics2D g) {
        double strwidth,strheight;

        FontMetrics fm = g.getFontMetrics();
        strwidth = fm.getStringBounds(label,g).getWidth();
        strheight = fm.getStringBounds(label,g).getHeight();
        if (strwidth > width) {
            Rectangle r = getFrame().rectangle();
            LabelShape.drawCenteredString(label.substring(0,label.length()/2), getPositionX(),
                                      (int)(getPositionY()+r.height*percentlink/2)  -strheight/2-1, g);
            LabelShape.drawCenteredString(label.substring(label.length()/2,label.length()), getPositionX(),
                                      (int)(getPositionY()+r.height*percentlink/2) + strheight/2, g);

        }   else {

            Rectangle r = getFrame().rectangle();
            LabelShape.drawCenteredString(label, getPositionX(), (int)(getPositionY()+r.height*percentlink/2), g);
        }
    }

     public void setPointerVoid(boolean isNull_) {
         isNull = isNull_;


    }

}
