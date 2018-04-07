package com.spoonner.alex.appkit.core.utils;


import com.spoonner.alex.appkit.core.gview.base.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SmoothScrolling implements ActionListener {

    protected static final int steps = 10;

    protected JComponent c;
    protected Rectangle source;
    protected Rectangle dest;
    protected Vector2D v;
    protected int k;
    protected Timer timer;
    protected ScrollingDelegate delegate;

    public SmoothScrolling(JComponent c, ScrollingDelegate delegate) {
        this.c = c;
        this.delegate = delegate;
    }

    public synchronized void scrollTo(Rectangle dest) {
        this.dest = dest;
        computeSource();
        computeVector();
        if(c.getVisibleRect().intersects(dest)) {
            c.scrollRectToVisible(dest);
            completed();
        } else {
            startTimer();
        }
    }

    public synchronized void startTimer() {
        if(timer != null)
            timer.stop();

        timer = new Timer(30, this);
        timer.start();
        k = 0;
    }

    public void completed() {
        if(delegate != null)
            delegate.smoothScrollingDidComplete();
    }

    public void computeVector() {
        v = new Vector2D(dest.x-source.x, dest.y-source.y);
    }

    public void computeSource() {
        Rectangle vr = c.getVisibleRect();
        source = new Rectangle(dest);
        if(source.x < vr.x)
            source.x = vr.x;
        else if(source.x > vr.x+vr.width)
            source.x = vr.x+vr.width-source.width;

        if(source.y < vr.y)
            source.y = vr.y;
        else if(source.y > vr.y+vr.height)
            source.y = vr.y+vr.height-source.height;
    }

    public void actionPerformed(ActionEvent e) {
        k++;
        if(k>steps) {
            c.scrollRectToVisible(dest);
            timer.stop();
            timer = null;
            completed();
        } else {
            Rectangle current = new Rectangle(source);
            current.x += v.x*k/steps;
            current.y += v.y*k/steps;
            c.scrollRectToVisible(current);
        }
    }

    public interface ScrollingDelegate {
        public void smoothScrollingDidComplete();
    }
}
