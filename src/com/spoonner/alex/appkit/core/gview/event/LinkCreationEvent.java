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

package com.spoonner.alex.appkit.core.gview.event;


import com.spoonner.alex.appkit.core.gview.View;
import com.spoonner.alex.appkit.core.gview.base.Anchor2D;
import com.spoonner.alex.appkit.core.gview.base.Vector2D;
import com.spoonner.alex.appkit.core.gview.object.Element;
import com.spoonner.alex.appkit.core.gview.object.Link;
import com.spoonner.alex.appkit.core.gview.shape.LinkShape;
import com.spoonner.alex.appkit.core.gview.shape.LinkShapeArc;
import com.spoonner.alex.appkit.core.gview.shape.LinkShapeElbow;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

public class LinkCreationEvent extends AbstractEvent {

    public Element startElement = null;
    public String startAnchorKey = null;
    public LinkShape linkElement = null;
    public LinkShapeArc linkArc = null;
    public LinkShapeElbow linkElbow = null;

    public LinkCreationEvent(View view) {
        super(view);
    }

    public void mousePressed(MouseEvent e, Point mousePosition) {
        Element selectedElement = delegate.eventQueryElementAtPoint(mousePosition);

        if(startElement != null) {
            if(selectedElement != null) {
                int type;
                if(linkElement instanceof LinkShapeElbow)
                    type = Link.SHAPE_ELBOW;
                else
                    type = Link.SHAPE_ARC;

                delegate.eventCreateLink(startElement, startAnchorKey,
                                            selectedElement, selectedElement.getAnchorKeyClosestToPoint(mousePosition),
                                            type, mousePosition);
            }
            removeExclusiveValue(EventManager.EXCLUSIVE_CREATE_LINK_VALUE);
            startElement = null;
            linkElement = null;
            delegate.eventShouldRepaint();
            return;
        }

        if(selectedElement == null || !selectedElement.acceptOutgoingLink()) {
            return;
        }

        int mask = InputEvent.SHIFT_DOWN_MASK + InputEvent.BUTTON1_DOWN_MASK;
        if((e.getModifiersEx() & mask) == mask || delegate.eventCanCreateLink()) {
            startElement = selectedElement;
            startAnchorKey = startElement.getAnchorKeyClosestToPoint(mousePosition);

            linkArc = new LinkShapeArc();
            linkArc.setStartTangentOffset(startElement.getDefaultAnchorOffset(startAnchorKey));

            linkElbow = new LinkShapeElbow();

            if(view.defaultLinkShape() == Link.SHAPE_ARC)
                linkElement = linkArc;
            else
                linkElement = linkElbow;

            linkElement.setFlateness(delegate.eventLinkFlateness());
            addExclusiveValue(EventManager.EXCLUSIVE_CREATE_LINK_VALUE);
        }
    }

    public void mouseMoved(MouseEvent e, Point mousePosition) {
        if(startElement == null)
            return;

        updateLink(mousePosition);
        delegate.eventShouldRepaint();
    }

    public boolean shouldFocusOnElement(Element element) {
        if(startElement == null)
            return true;
        else
            return element.acceptIncomingLink();
    }

    public void updateLink(Point mouse) {
        Element ce = delegate.eventQueryElementAtPoint(mouse);

        boolean selfLoop = ce == startElement;
        setLinkStartAnchor(startElement.getAnchor(startAnchorKey));

        if(ce == null || ce instanceof Link) {
            setLinkEnd(Vector2D.vector(mouse), Anchor2D.DIRECTION_BOTTOM);
        } else {
            Anchor2D anchor = ce.getAnchorClosestToPoint(mouse);
            String anchorKey = ce.getAnchorKeyClosestToPoint(mouse);
            setLinkEnd(anchor.position, anchor.direction);

            if(selfLoop) {
                if(anchor.direction == Anchor2D.DIRECTION_FREE)
                    linkArc.setMouse(mouse);
                else
                    linkArc.setMouse(anchor.position.add(anchor.direction));
                linkArc.setEndTangentOffset(startElement.getDefaultAnchorOffset(anchorKey));
            } else {
                linkArc.setMouse(mouse);
                linkArc.setEndTangentOffset(ce.getDefaultAnchorOffset(anchorKey));
            }

            if(selfLoop && view.defaultLinkShape() == Link.SHAPE_ELBOW
                && startElement.getAnchor(startAnchorKey).equals(anchor))
            {
                linkElement = linkArc;
            } else if(view.defaultLinkShape() == Link.SHAPE_ELBOW)
                linkElement = linkElbow;
            else if(view.defaultLinkShape() == Link.SHAPE_ARC)
                linkElement = linkArc;
        }
        setLinkSelfLoop(selfLoop);

        linkElement.update();
    }

    private void setLinkSelfLoop(boolean selfLoop) {
        linkArc.setSelfLoop(selfLoop);
        linkElbow.setSelfLoop(selfLoop);
    }

    private void setLinkStartAnchor(Anchor2D anchor) {
        linkArc.setStartAnchor(anchor);
        linkElbow.setStartAnchor(anchor);
    }

    private void setLinkEnd(Vector2D position, Vector2D direction) {
        linkArc.setEnd(position);
        linkArc.setEndDirection(direction);

        linkElbow.setEnd(position);
        linkElbow.setEndDirection(direction);
    }

    public void draw(Graphics g) {
        if(linkElement == null)
            return;

        linkElement.draw((Graphics2D)g);
    }
}
