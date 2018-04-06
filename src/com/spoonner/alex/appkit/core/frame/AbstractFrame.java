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


import com.spoonner.alex.appkit.core.AbstractControl;
import com.spoonner.alex.appkit.core.app.Application;
import com.spoonner.alex.appkit.core.menu.*;
import com.spoonner.alex.appkit.core.menu.Menu;
import com.spoonner.alex.appkit.core.menu.MenuItem;
import com.spoonner.alex.appkit.core.undo.Undo;
import com.spoonner.alex.appkit.core.undo.UndoDelegate;
import com.spoonner.alex.appkit.core.undo.UndoEngine;
import com.spoonner.alex.appkit.core.utils.XJLocalizable;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AbstractFrame extends AbstractControl implements MenuBarCustomizer, MenuBarDelegate {

    private static final String PROPERTY_WINDOW_MODIFIED = "windowModified";

    protected MainMenuBar mainMenuBar;
    protected JFrame jFrame;
    protected FrameDelegate delegate;
    protected UndoEngine undoEngine;
    protected boolean alreadyBecomeVisible = false;
    protected boolean dirty = false;

    public AbstractFrame() {
        jFrame = new JFrame();
        jFrame.addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                AbstractFrame.this.windowActivated();
            }

            public void windowDeactivated(WindowEvent e) {
                // Don't send the deactivate event if the frame is closed
                // (because this event is send also when the window just closed
                // which is kind of weird)
                if(jFrame != null)
                    AbstractFrame.this.windowDeactivated();
            }
        });
        setDefaultSize();
        undoEngine = new UndoEngine();
    }

    public void awake() {
        if(shouldDisplayMainMenuBar()) {
            mainMenuBar = MainMenuBar.createInstance();
            mainMenuBar.setCustomizer(this);
            mainMenuBar.setDelegate(this);
            mainMenuBar.createMenuBar();
            setMainMenuBar(mainMenuBar);
            undoEngine.setMainMenuBar(mainMenuBar);
        }
    }

    public void setDelegate(FrameDelegate delegate) {
        this.delegate = delegate;
    }

    public FrameDelegate getDelegate() {
        return delegate;
    }

    public void setDefaultCloseOperation(int operation) {
        jFrame.setDefaultCloseOperation(operation);
    }

    public Container getContentPane() {
        return jFrame.getContentPane();
    }

    public JRootPane getRootPane() {
        return jFrame.getRootPane();
    }

    public JLayeredPane getLayeredPane() {
        return jFrame.getLayeredPane();
    }

    public Component getGlassPane() {
        return jFrame.getGlassPane();
    }

    public void setMainMenuBar(MainMenuBar menubar) {
        this.mainMenuBar = menubar;
        jFrame.setJMenuBar(mainMenuBar.getJMenuBar());
    }
    
    public MainMenuBar getMainMenuBar() {
        return mainMenuBar;
    }

    public void menuItemStatusChanged(int tag) {
        if(mainMenuBar == null)
            return;

        MainMenuBar.refreshAllMenuBars();
        mainMenuBar.refreshState();
    }

    public void setTitle(String title) {
        jFrame.setTitle(title);
    }

    public String getTitle() {
        return jFrame.getTitle();
    }

    public void setLocation(Point loc) {
        jFrame.setLocation(loc);
    }

    public Point getLocation() {
        return jFrame.getLocation();
    }

    public void setSize(int dx, int dy) {
        jFrame.setSize(dx, dy);
    }

    public void setSize(Dimension size) {
        jFrame.setSize(size);
    }

    public Dimension getSize() {
        return jFrame.getSize();
    }

    public void setDefaultSize() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jFrame.setSize((int)(dim.width*0.5), (int)(dim.height*0.5));
    }

    public void setResizable(boolean flag) {
        jFrame.setResizable(flag);
    }

    public void pack() {
        jFrame.pack();
    }

    public void bringToFront() {
        jFrame.toFront();
    }

    public void setVisible(boolean flag) {
        if(flag && !alreadyBecomeVisible) {
            alreadyBecomeVisible = true;
            becomingVisibleForTheFirstTime();
        }
        jFrame.setVisible(flag);
    }

    public void becomingVisibleForTheFirstTime() {

    }

    public boolean isVisible() {
        return jFrame.isVisible();
    }

    public boolean isActive() {
        return jFrame.isActive();
    }

    public void show() {
        setVisible(true);
    }

    public void showModal() {
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
    }

    public boolean isCompletelyOnScreen() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        return jFrame.getX()+jFrame.getWidth()<dim.getWidth() && jFrame.getY()+jFrame.getHeight()<dim.getHeight();
    }

    public void center() {
        jFrame.setLocationRelativeTo(null);
    }

    public void setPosition(int x, int y) {
        jFrame.setLocation(x, y);
    }

    public void offsetPosition(int dx, int dy) {
        Point p = jFrame.getLocation();
        jFrame.setLocation(p.x+dx, p.y+dy);
    }

    public void close() {
        MainMenuBar.removeInstance(mainMenuBar);
        if(mainMenuBar != null) {
            mainMenuBar.setDelegate(null);
            mainMenuBar = null;
        }

        jFrame.dispose();
        jFrame = null;

        if(delegate != null)
            delegate.frameDidClose(this);
    }

    public void setDirty() {
        if(!Application.shared().supportsPersistence())
            return;

        // Use dirty member to speed up
        if(!dirty) {
            dirty = true;
            jFrame.getRootPane().putClientProperty(PROPERTY_WINDOW_MODIFIED, Boolean.TRUE);
            menuItemStatusChanged(MainMenuBar.MI_SAVE);
        }
    }

    public void resetDirty() {
        if(!Application.shared().supportsPersistence())
            return;

        if(dirty) {
            dirty = false;
            jFrame.getRootPane().putClientProperty(PROPERTY_WINDOW_MODIFIED, Boolean.FALSE);
            menuItemStatusChanged(MainMenuBar.MI_SAVE);
        }
    }

    public boolean dirty() {
        if(!Application.shared().supportsPersistence())
            return false;

        Boolean b = (Boolean) jFrame.getRootPane().getClientProperty(PROPERTY_WINDOW_MODIFIED);
        if(b == null)
            return false;
        else
            return b.booleanValue();
    }

    public void registerUndo(UndoDelegate delegate, JTextPane textPane) {
        undoEngine.registerUndo(new Undo(undoEngine, delegate), textPane);
    }

    public void performUndo() {
        Undo undo = getCurrentUndo();
        if(undo != null) {
            undo.performUndo();
        }
    }

    public void performRedo() {
        Undo undo = getCurrentUndo();
        if(undo != null) {
            undo.performRedo();
        }
    }

    public Undo getUndo(JTextPane textPane) {
        return undoEngine.getUndo(textPane);
    }

    public Undo getCurrentUndo() {
        return undoEngine.getCurrentUndo();
    }

    public boolean shouldDisplayMainMenuBar() {
        return true;
    }

    public boolean shouldAppearsInWindowMenu() {
        return false;
    }

    public void windowActivated() {

    }

    public void windowDeactivated() {

    }

    public void customizeFileMenu(Menu menu) {

    }

    public void customizeEditMenu(Menu menu) {

    }

    public void customizeWindowMenu(Menu menu) {

    }

    public void customizeHelpMenu(Menu menu) {

    }

    public void customizeMenuBar(MainMenuBar menubar) {

    }

    public void menuItemState(MenuItem item) {
        switch(item.getTag()) {
            case MainMenuBar.MI_NEW:
                item.setTitle(XJLocalizable.getXJString("New")+((Application.shared().getDocumentExtensions().size()>1)?"...":""));
                break;
            case MainMenuBar.MI_UNDO:
            case MainMenuBar.MI_REDO:
                getMainMenuBar().menuUndoRedoItemState(undoEngine.getCurrentUndo());
                break;
        }
    }

    public void handleMenuEvent(Menu menu, MenuItem item) {
        switch(item.getTag()) {
            case MainMenuBar.MI_UNDO:
                performUndo();
                break;
            case MainMenuBar.MI_REDO:
                performRedo();
                break;
            case MainMenuBar.MI_CUT:
                performActionOnFocusedJComponent(DefaultEditorKit.cutAction);
                break;
            case MainMenuBar.MI_COPY:
                performActionOnFocusedJComponent(DefaultEditorKit.copyAction);
                break;
            case MainMenuBar.MI_PASTE:
                performActionOnFocusedJComponent(DefaultEditorKit.pasteAction);
                break;
            case MainMenuBar.MI_SELECT_ALL:
                performActionOnFocusedJComponent(DefaultEditorKit.selectAllAction);
                break;
        }
    }

    public void handleMenuSelected(Menu menu) {
    }

    public void performActionOnFocusedJComponent(String action) {
        JComponent c = getFocusedJComponent();
        if(c != null)
            c.getActionMap().get(action).actionPerformed(null);
    }

    public JComponent getFocusedJComponent() {
        Component c = getJFrame().getFocusOwner();
        if(c instanceof JComponent)
            return (JComponent)c;
        else
            return null;
    }

    public Container getJavaContainer() {
        return jFrame;
    }

    public JFrame getJFrame() {
        return jFrame;
    }

}
