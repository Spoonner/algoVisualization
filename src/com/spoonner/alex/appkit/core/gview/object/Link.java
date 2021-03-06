/*

[The "BSD licence"]
Copyright (c) 2005 Jean Bovet
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

1. Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
3. The name of the author may not be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/

package com.spoonner.alex.appkit.core.gview.object;

import com.spoonner.alex.appkit.core.gview.base.Rect;
import com.spoonner.alex.appkit.core.gview.base.Vector2D;
import com.spoonner.alex.appkit.core.gview.shape.LinkShape;
import com.spoonner.alex.appkit.core.gview.shape.LinkShapeArc;
import com.spoonner.alex.appkit.core.gview.shape.LinkShapeBezier;
import com.spoonner.alex.appkit.core.gview.shape.LinkShapeElbow;
import com.spoonner.alex.appkit.misc.XMLSerializable;

import java.awt.*;


public class Link extends Element implements XMLSerializable {

    public static final int SHAPE_ARC = 0;
    public static final int SHAPE_ELBOW = 1;
    public static final int SHAPE_BEZIER = 2;

    public Element source = null;
    public Element target = null;
    public String sourceAnchorKey = null;
    public String targetAnchorKey = null;
    public String pattern = null;

    protected LinkShape link = null;

    protected int shape = SHAPE_ARC;

    public Link() {
        super();
    }

    public Link(Element source, String sourceAnchorKey, Element target, String targetAnchorKey, int shape, String pattern, Point mouse, double flateness) {
        this.source = source;
        this.target = target;
        this.sourceAnchorKey = sourceAnchorKey;
        this.targetAnchorKey = targetAnchorKey;
        this.shape = shape;
        this.pattern = pattern;
        initializeLink(flateness);
        setSourceTangentOffset(source.getDefaultAnchorOffset(sourceAnchorKey));
        setTargetTangentOffset(target.getDefaultAnchorOffset(targetAnchorKey));
        link.setDirection(Vector2D.vector(mouse).sub(target.getPosition()));
    }

    public Link(Element source, String sourceAnchorKey, Element target, String targetAnchorKey, int shape, String pattern, double flateness) {
        this.source = source;
        this.target = target;
        this.sourceAnchorKey = sourceAnchorKey;
        this.targetAnchorKey = targetAnchorKey;
        this.shape = shape;
        this.pattern = pattern;
        initializeLink(flateness);
        if(source == target)
            link.setDirection(new Vector2D(0, 1));
        else
            link.setDirection(source.getPosition().sub(target.getPosition()));
        setSourceTangentOffset(source.getDefaultAnchorOffset(sourceAnchorKey));
        setTargetTangentOffset(target.getDefaultAnchorOffset(targetAnchorKey));
    }

    public void setBezierControlPoints(Vector2D points[]) {
        if(link instanceof LinkShapeBezier) {
            LinkShapeBezier lb = (LinkShapeBezier)link;
            lb.setControlPoints(points);
        }
    }

    public void setBezierLabelPosition(Vector2D position) {
        if(link instanceof LinkShapeBezier) {
            LinkShapeBezier lb = (LinkShapeBezier)link;
            lb.setLabelPosition(position);
        }
    }

    protected LinkShape createLinkInstance() {
        switch(shape) {
            case SHAPE_ARC: return new LinkShapeArc();
            case SHAPE_ELBOW: return new LinkShapeElbow();
            case SHAPE_BEZIER: return new LinkShapeBezier();
        }
        return null;
    }

    protected void initializeLink(double flateness) {
        if(link == null) {
            link = createLinkInstance();
            link.setFlateness(flateness);
        }
    }

    public void setSource(Element source) {
        this.source = source;
    }

    public Element getSource() {
        return source;
    }

    public void setTarget(Element target) {
        this.target = target;
    }

    public Element getTarget() {
        return target;
    }

    public void setSourceAnchorKey(String key) {
        this.sourceAnchorKey = key;
    }

    public String getSourceAnchorKey() {
        return sourceAnchorKey;
    }

    public void setTargetAnchorKey(String key) {
        this.targetAnchorKey = key;
    }

    public String getTargetAnchorKey() {
        return targetAnchorKey;
    }

    public void setSourceTangentOffset(double offset) {
        link.setStartTangentOffset(offset);
    }

    public void setTargetTangentOffset(double offset) {
        link.setEndTangentOffset(offset);
    }

    public void setSourceOffset(double x, double y) {
        setSourceOffset(new Vector2D(x, y));
    }

    public void setSourceOffset(Vector2D offset) {
        link.setStartOffset(offset);
    }

    public void setTargetOffset(double x, double y) {
        setTargetOffset(new Vector2D(x, y));
    }

    public void setTargetOffset(Vector2D offset) {
        link.setEndOffset(offset);
    }

    public void setLink(LinkShape link) {
        this.link = link;
    }

    public LinkShape getLink() {
        return link;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public void setLabel(String label) {
        this.pattern = label;
    }

    public void setLabelColor(Color color) {
        link.setLabelColor(color);
    }

    public void setLabelVisible(boolean flag) {
        if(link != null)
            link.setLabelVisible(flag);
    }

    public boolean isLabelVisible() {
        if(link == null)
            return false;
        else
            return link.isLabelVisible();
    }

    public void setShape(int type) {
        this.shape = type;
    }

    public int getShape() {
        return shape;
    }

    public void toggleShape() {
        switch(shape) {
            case SHAPE_ARC:
                shape = SHAPE_ELBOW;
                break;
            case SHAPE_ELBOW:
                shape = SHAPE_ARC;
                break;
            case SHAPE_BEZIER:
                // Cannot toggle a bezier link
                return;
        }
        double flateness = link.getFlateness();
        Vector2D direction = link.getDirection();

        link = createLinkInstance();
        link.setFlateness(flateness);
        link.setDirection(direction);
    }

    public void setMousePosition(Point mouse) {
        link.setDirection(Vector2D.vector(mouse).sub(target.getPosition()));
        link.setMousePosition(Vector2D.vector(mouse));
    }

    public Rect getFrame() {
        update();
        return link.getFrame();
    }

    public boolean isInside(Point p) {
        if(link == null)
            return false;
        else
            return link.contains(p.x, p.y);
    }

    public void update() {
        initializeLink(0);

        source.updateAnchors();
        target.updateAnchors();

        link.setStartAnchor(source.getAnchor(sourceAnchorKey));
        link.setEndAnchor(target.getAnchor(targetAnchorKey));
        link.setLabel(pattern);
        link.setSelfLoop(source == target);

        link.update();
    }

    public void draw(Graphics2D g) {
        update();

        g.setStroke(new BasicStroke(penSize));

        if(color != null)
            link.setColor(color);
        else
            link.setColor(Color.black);

        link.draw(g);
    }

    public void drawShape(Graphics2D g) {
        link.drawShape(g);
    }

}
