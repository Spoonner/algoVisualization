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

package com.spoonner.alex.appkit.core.gview.timer;


import com.spoonner.alex.appkit.core.gview.object.Element;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Timer {

    protected List elements = new ArrayList();

    protected javax.swing.Timer timer = null;
    protected TimerDelegate delegate = null;
    protected int delay = 50;

    public Timer(TimerDelegate delegate) {
        this.delegate = delegate;
    }

    public Timer(TimerDelegate delegate, int delay) {
        this.delegate = delegate;
        this.delay = delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void refresh() {
        if(elements.isEmpty())
            stop();
        else
            start();
    }

    public void start() {
        if(timer == null) {
            timer = new javax.swing.Timer(delay, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    delegate.timerFired(Timer.this);
                }
            });
            timer.start();
        } else if(!timer.isRunning()) {
            timer.start();
        }
    }

    public void stop() {
        if(timer == null)
            return;

        if(timer.isRunning())
            timer.stop();
    }

    public synchronized void add(Element element) {
        if(elements.contains(element))
            return;

        elements.add(element);
        refresh();
    }

    public synchronized void remove(Element element) {
        elements.remove(element);
        refresh();
    }

    public synchronized void clear() {
        elements.clear();
        refresh();
    }

    public synchronized boolean contains(Element element) {
        return elements.contains(element);
    }

    public List getElements() {
        return elements;
    }
}
