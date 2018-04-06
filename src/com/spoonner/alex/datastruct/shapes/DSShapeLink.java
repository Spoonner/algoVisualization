package com.spoonner.alex.datastruct.shapes;


import com.spoonner.alex.appkit.core.gview.object.Element;
import com.spoonner.alex.appkit.core.gview.object.Link;
import com.spoonner.alex.appkit.misc.XMLSerializable;

import java.awt.*;

public class DSShapeLink extends Link implements XMLSerializable {
    public DSShapeLink(Element source, String sourceAnchorKey, Element target, String targetAnchorKey, int shape, String pattern, Point mouse, double flateness) {
           super(source,sourceAnchorKey,target,targetAnchorKey,shape,pattern,mouse,flateness);

       }

       public DSShapeLink(Element source, String sourceAnchorKey, Element target, String targetAnchorKey, int shape, String pattern, double flateness) {
      super(source,sourceAnchorKey,target,targetAnchorKey,shape,pattern,flateness);
       }


    public void setArrowVisible(boolean visable) {
          link.setArrowVisible(visable);
    }
}
