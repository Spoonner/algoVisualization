package com.spoonner.alex.datastruct.shapes;


import com.spoonner.alex.appkit.appkit.gview.object.GElement;
import com.spoonner.alex.appkit.appkit.gview.object.GLink;
import com.spoonner.alex.appkit.misc.XMLSerializable;

import java.awt.*;

public class DSShapeLink extends GLink implements XMLSerializable {
    public DSShapeLink(GElement source, String sourceAnchorKey, GElement target, String targetAnchorKey, int shape, String pattern, Point mouse, double flateness) {
           super(source,sourceAnchorKey,target,targetAnchorKey,shape,pattern,mouse,flateness);

       }

       public DSShapeLink(GElement source, String sourceAnchorKey, GElement target, String targetAnchorKey, int shape, String pattern, double flateness) {
      super(source,sourceAnchorKey,target,targetAnchorKey,shape,pattern,flateness);
       }


    public void setArrowVisible(boolean visable) {
          link.setArrowVisible(visable);
    }
}
