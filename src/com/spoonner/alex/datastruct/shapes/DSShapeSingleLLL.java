package com.spoonner.alex.datastruct.shapes;

import com.spoonner.alex.appkit.core.gview.shape.LabelShape;
import com.spoonner.alex.appkit.misc.XMLSerializable;

import java.awt.*;

public class DSShapeSingleLLL extends DSShapeRect implements XMLSerializable {


    protected boolean isNull = false;
    protected double percentlink = 0.25;

    public DSShapeSingleLLL() {
        super();
    }



    public void setPointerVoid(boolean isNull_) {
         isNull = isNull_;

    }
    public void setpercentLink(double newpercent) {
        if (newpercent > 0 && newpercent < 1)
            percentlink = newpercent;
    }

    public void drawShape(Graphics2D g) {
        super.drawShape(g);

        Rectangle r = getFrame().rectangle();
        g.drawLine((int)(r.x+r.width*percentlink), r.y, (int)(r.x+r.width*percentlink), r.y+r.height);


        if (isNull) {
            g.drawLine(r.x,  r.y, (int) (r.x+r.width*percentlink), r.y+r.height);
        } else
            g.drawLine(r.x, r.y+(r.height/2), (int) (r.x+r.width*percentlink/2), r.y+r.height/2);



    }


    public void drawLabel(Graphics2D g) {
        Rectangle r = getFrame().rectangle();
        LabelShape.drawCenteredString(label, (int)(getPositionX()+r.width*percentlink/2), (int)getPositionY(), g);
    }





}
