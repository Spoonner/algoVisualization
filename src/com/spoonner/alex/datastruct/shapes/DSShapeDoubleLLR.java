package com.spoonner.alex.datastruct.shapes;


import com.spoonner.alex.appkit.misc.XMLSerializable;

import java.awt.*;

public class DSShapeDoubleLLR extends DSShapeRect implements XMLSerializable {

    public DSShapeDoubleLLR() {
        super();
    }

    public void drawShape(Graphics2D g) {
        super.drawShape(g);

        Rectangle r = getFrame().rectangle();
        g.drawLine((int)(r.x+r.width*0.75), r.y, (int)(r.x+r.width*0.75), r.y+r.height);

        g.drawLine((int)(r.x+r.width*0.25), r.y, (int)(r.x+r.width*0.25), r.y+r.height);
    }

}
