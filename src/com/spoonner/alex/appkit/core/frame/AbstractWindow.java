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

package com.spoonner.alex.appkit.core.frame;

import com.spoonner.alex.appkit.core.app.Application;
import com.spoonner.alex.appkit.core.document.Data;
import com.spoonner.alex.appkit.core.document.Document;
import com.spoonner.alex.appkit.core.menu.MainMenuBar;
import com.spoonner.alex.appkit.core.menu.Menu;
import com.spoonner.alex.appkit.core.menu.MenuItem;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AbstractWindow extends AbstractFrame {

    protected Document document = null;

    public AbstractWindow() {
        Application.shared().addWindow(this);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jFrame.addWindowListener(new WindowEventListener());
    }

    public boolean isAuxiliaryWindow() {
        return false;
    }

    public boolean shouldAppearsInWindowMenu() {
        return true;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

    public Data getDocumentData() {
        if(document != null)
            return document.getDocumentData();
        else
            return null;
    }

    public void close() {
        Application.shared().removeWindow(this);
        super.close();
    }

    public void performClose() {
        if(document == null)
            close();
        else
            document.performClose();
    }

    public void menuItemState(MenuItem item) {
        super.menuItemState(item);

        if(document == null) {
            if(item.getTag() == MainMenuBar.MI_CLOSE)
                item.setEnabled(true);
            else
                item.setEnabled(false);
        }

        switch(item.getTag()) {
            case MainMenuBar.MI_SAVE:
                item.setEnabled(document != null && document.isDirty());
                break;
        }
    }

    public void handleMenuEvent(Menu menu, MenuItem item) {
        super.handleMenuEvent(menu, item);
        
        if(item.getTag() == MainMenuBar.MI_CLOSE) {
            performClose();
        }
    }

    public void windowActivated() {
        if(getDocument() == null)
            return;

        if(getDocument().isModifiedOnDisk()) {
            windowDocumentPathDidChange();
            getDocument().synchronizeLastModifiedDate();
        }
    }

    public void windowDocumentPathDidChange() {
        // can be used by subclasses to perform something when the document
        // associated file has changed (based on the file modification date)
    }

    // *** Private methods

    private class WindowEventListener extends WindowAdapter {

        public void windowActivated(WindowEvent e) {
            MainMenuBar.refreshAllMenuBars();
        }

        public void windowClosing(WindowEvent e) {
            performClose();
        }
    }
}
