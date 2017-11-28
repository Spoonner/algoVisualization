package com.spoonner.alex.ds.shapes;


import com.spoonner.alex.xj.foundation.XJXMLSerializable;

import java.awt.*;

public class DSShapeNullPointer extends DSShapeRect implements XJXMLSerializable {


    protected boolean isNull = true;

    public DSShapeNullPointer() {
        super();
    }


    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean nullval) {
        isNull = nullval;

    }

    public void drawShape(Graphics2D g) {
        super.drawShape(g);

        Rectangle r = getFrame().rectangle();
        if (isNull)
          g.drawLine(r.x, r.y, r.x+r.width, r.y+r.height);
    }


}
