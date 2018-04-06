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

package com.spoonner.alex.appkit.core.menu;

import com.spoonner.alex.appkit.core.app.Application;
import com.spoonner.alex.appkit.core.document.Document;
import com.spoonner.alex.appkit.core.frame.AbstractWindow;
import com.spoonner.alex.appkit.core.undo.Undo;
import com.spoonner.alex.appkit.core.utils.XJLocalizable;
import com.spoonner.alex.appkit.misc.XJSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainMenuBar implements MenuItemDelegate {

    public static final int MI_NEW = 10000;
    public static final int MI_OPEN = 10001;
    public static final int MI_LOAD = 10002;
    public static final int MI_SAVE = 10003;
    public static final int MI_SAVEAS = 10004;
    public static final int MI_CLOSE = 10005;
    public static final int MI_QUIT = 10006;
    public static final int MI_HELP = 10020;
    public static final int MI_ABOUT = 10021;
    public static final int MI_PREFS = 10022;
    public static final int MI_UNDO = 10023;
    public static final int MI_REDO = 10024;
    public static final int MI_CUT = 10025;
    public static final int MI_COPY = 10026;
    public static final int MI_PASTE = 10027;
    public static final int MI_SELECT_ALL = 10028;

    public static final int MI_CLEAR_RECENT_FILES = 20000;
    public static final int MI_RECENT_FILES = 20001; // + any number of recent files!

    public static final int MI_NO_WINDOW = 21000;
    public static final int MI_WINDOW = 21001; // + any number of windows!

    // !!! Do not use any number above MI_WINDOW !!!

    protected JMenuBar menuBar = null;
    protected Menu menuFile = null;
    protected Menu menuRecentFiles = null;
    protected Menu menuEdit = null;
    protected Menu menuWindow = null;
    protected Menu menuHelp = null;

    protected MenuItem menuItemUndo = null;
    protected MenuItem menuItemRedo = null;

    protected MenuBarCustomizer customizer = null;
    protected MenuBarDelegate delegate = null;

    protected List customMenus = new ArrayList();

    protected static List mmbs = new ArrayList();

    public synchronized static MainMenuBar createInstance() {
        MainMenuBar mmb = new MainMenuBar();
        mmbs.add(mmb);
        return mmb;
    }

    public synchronized static void removeInstance(MainMenuBar mmb) {
        mmbs.remove(mmb);
    }

    public synchronized static void refreshAllRecentFilesMenu() {
        Iterator iterator = mmbs.iterator();
        while(iterator.hasNext()) {
            MainMenuBar mmb = (MainMenuBar)iterator.next();
            mmb.rebuildRecentFilesMenu();
        }
    }

    public synchronized static void refreshAllMenuBars() {
        Iterator iterator = mmbs.iterator();
        while(iterator.hasNext()) {
            MainMenuBar mmb = (MainMenuBar)iterator.next();
            mmb.refresh();
        }
    }

    public synchronized static void setActiveWindowToAllMenuBar(AbstractWindow window) {
        Iterator iterator = mmbs.iterator();
        while(iterator.hasNext()) {
            MainMenuBar mmb = (MainMenuBar)iterator.next();
            mmb.setActiveWindow(window);
        }
    }

    public static boolean isRecentFilesItem(MenuItem item) {
        return item.getTag() >= MainMenuBar.MI_CLEAR_RECENT_FILES && item.getTag() < MainMenuBar.MI_NO_WINDOW;
    }
    
    public MainMenuBar() {
    }

    public void setCustomizer(MenuBarCustomizer customizer) {
        this.customizer = customizer;
    }

    public void setDelegate(MenuBarDelegate delegate) {
        this.delegate = delegate;
    }

    public JMenuBar getJMenuBar() {
        return menuBar;
    }

    public Menu getFileMenu() {
        return menuFile;
    }

    public void refresh() {
        refreshContent();
        refreshState();
    }

    public void refreshState() {
        refreshMenuState(menuFile);
        refreshMenuState(menuEdit);
        refreshMenuState(menuHelp);

        Iterator iterator = customMenus.iterator();
        while(iterator.hasNext())
            refreshMenuState((Menu)iterator.next());
    }

    public void refreshMenuEditState() {
        refreshMenuState(menuEdit);
    }

    public void refreshMenuState(Menu menu) {
        for(int i=0; i<menu.getItemCount(); i++) {
            MenuItem item = menu.getItemAtIndex(i);
            if(item instanceof Menu)
                refreshMenuState((Menu)item);
            else
                refreshMenuItemState(item);
        }
    }

    public void refreshMenuItemState(MenuItem item) {
        if(delegate != null)
            delegate.menuItemState(item);
    }

    public void refreshContent() {
        rebuildRecentFilesMenu();
        rebuildWindowMenu();
    }

    public void menuUndoRedoItemState(Undo undo) {
        if(undo == null) {
            menuItemUndo.setEnabled(false);
            menuItemRedo.setEnabled(false);
        } else {
            menuItemUndo.setEnabled(undo.canUndo());
            menuItemRedo.setEnabled(undo.canRedo());
        }
    }

    public void setActiveWindow(AbstractWindow window) {
        if(menuWindow == null)
            return;

        int index = Application.shared().getWindows().indexOf(window);
        if(index>=0) {
            MenuItem item = menuWindow.getItemAtIndex(index);
            if(item != null)
                item.setSelected(true);
        }
    }

    public void setupMenuItem(MenuItem item, String name, int keystroke, int modifiers, int tag) {
        item.setTitle(name);
        item.setTag(tag);

        if(keystroke > 0 && modifiers > 0)
            item.setAccelerator(keystroke, modifiers);
        else if(keystroke >0)
            item.setAccelerator(keystroke);

        item.setDelegate(this);
    }

    public MenuItem buildMenuItem(String name, int keystroke, int modifiers, int tag) {
        MenuItem item = new MenuItem();
        setupMenuItem(item, name, keystroke, modifiers, tag);
        return item;
    }

    public MenuItem buildMenuItem(String name, int keystroke, int tag) {
        MenuItem item = new MenuItem();
        setupMenuItem(item, name, keystroke, -1, tag);
        return item;
    }

    public MenuItemCheck buildMenuItemCheck(String name, int keystroke, int tag) {
        MenuItemCheck item = new MenuItemCheck();
        setupMenuItem(item, name, keystroke, -1, tag);
        return item;
    }

    public MenuItem buildMenuItem(String name, int tag) {
        return buildMenuItem(name, -1, tag);
    }

    public void createMenuBar() {
        customMenus.clear();

        menuBar = new JMenuBar();

        addMenu(createFileMenu());
        addMenu(createEditMenu());

        // Customization between menu Edit and menu Help
        if(customizer != null)
            customizer.customizeMenuBar(this);

        addMenu(createWindowMenu());
        addMenu(createHelpMenu());
    }

    public Menu createFileMenu() {
        Menu menu = buildFileMenu();
        if(customizer != null)
            customizer.customizeFileMenu(menu);
        Application.getAppDelegate().customizeFileMenu(menu);
        return menu;
    }

    public Menu createEditMenu() {
        Menu menu = buildEditMenu();
        if(customizer != null)
            customizer.customizeEditMenu(menu);
        Application.getAppDelegate().customizeEditMenu(menu);
        return menu;
    }

    public Menu createWindowMenu() {
        Menu menu = buildWindowMenu();
        if(customizer != null)
            customizer.customizeWindowMenu(menu);
        Application.getAppDelegate().customizeWindowMenu(menu);
        return menu;
    }

    public Menu createHelpMenu() {
        Menu menu = buildHelpMenu();
        if(customizer != null)
            customizer.customizeHelpMenu(menu);
        Application.getAppDelegate().customizeHelpMenu(menu);
        return menu;
    }

    public void addCustomMenu(Menu menu) {
        if(menu == null)
            return;

        customMenus.add(menu);
        addMenu(menu);
    }

    private void addMenu(Menu menu) {
        menuBar.add(menu.getSwingComponent());
        menu.setMainMenuBar(this);
    }

    private Menu buildFileMenu() {
        boolean persistence = Application.shared().supportsPersistence();

        menuFile = new Menu();
        menuFile.setTitle(XJLocalizable.getXJString("File"));
        menuFile.addItem(buildMenuItem(XJLocalizable.getXJString("New"), KeyEvent.VK_N, MI_NEW));
        if(persistence) {
            menuFile.addItem(buildMenuItem(XJLocalizable.getXJString("Open"), KeyEvent.VK_O, MI_OPEN));
            menuFile.addItem(createRecentFilesMenu());
        }
        menuFile.addSeparator();
        menuFile.addItem(buildMenuItem(XJLocalizable.getXJString("Close"), KeyEvent.VK_W, MI_CLOSE));
        if(persistence) {
            menuFile.addItem(buildMenuItem(XJLocalizable.getXJString("Save"), KeyEvent.VK_S, MI_SAVE));
            menuFile.addItem(buildMenuItem(XJLocalizable.getXJString("SaveAs"), MI_SAVEAS));
        }

        if(!XJSystem.isMacOS()) {
            menuFile.addSeparator();
            if(Application.shared().hasPreferencesMenuItem()) {
                menuFile.addItem(buildMenuItem(XJLocalizable.getXJString("Preferences"), KeyEvent.VK_COMMA, MI_PREFS));
                menuFile.addSeparator();                
            }
            menuFile.addItem(buildMenuItem(XJLocalizable.getXJString("Quit"), KeyEvent.VK_Q, MI_QUIT));
        }
        return menuFile;
    }

    public Menu createRecentFilesMenu() {
        menuRecentFiles = new Menu();
        rebuildRecentFilesMenu();
        return menuRecentFiles;
    }

    public void rebuildRecentFilesMenu() {
        if(menuRecentFiles == null)
            return;

        menuRecentFiles.clear();
        menuRecentFiles.setTitle(XJLocalizable.getXJString("OpenRecent"));

        int f = 0;
        Iterator iterator = Application.shared().recentFiles().iterator();
        while(iterator.hasNext()) {
            menuRecentFiles.addItem(buildMenuItem((String)iterator.next(), MI_RECENT_FILES+f++));
        }
        menuRecentFiles.addSeparator();
        menuRecentFiles.addItem(buildMenuItem(XJLocalizable.getXJString("ClearMenu"), MI_CLEAR_RECENT_FILES));
    }

    private Menu buildEditMenu() {
        menuEdit = new Menu();
        menuEdit.setTitle(XJLocalizable.getXJString("Edit"));
        menuEdit.addItem(menuItemUndo = buildMenuItem(XJLocalizable.getXJString("Undo"), KeyEvent.VK_Z, MI_UNDO));
        menuEdit.addItem(menuItemRedo = buildMenuItem(XJLocalizable.getXJString("Redo"), KeyEvent.VK_Z, MenuItem.getKeyModifier() | Event.SHIFT_MASK, MI_REDO));
        menuEdit.addSeparator();
        menuEdit.addItem(buildMenuItem(XJLocalizable.getXJString("Cut"), KeyEvent.VK_X, MI_CUT));
        menuEdit.addItem(buildMenuItem(XJLocalizable.getXJString("Copy"), KeyEvent.VK_C, MI_COPY));
        menuEdit.addItem(buildMenuItem(XJLocalizable.getXJString("Paste"), KeyEvent.VK_V, MI_PASTE));
        menuEdit.addSeparator();
        menuEdit.addItem(buildMenuItem(XJLocalizable.getXJString("SelectAll"), KeyEvent.VK_A, MI_SELECT_ALL));
        return menuEdit;
    }

    private void buildWindowMenu_() {
        Iterator iterator = Application.shared().getWindows().iterator();
        int count = 0;
        while(iterator.hasNext()) {
            AbstractWindow window = (AbstractWindow)iterator.next();
            if(window.shouldAppearsInWindowMenu()) {
                MenuItemCheck item = buildMenuItemCheck(window.getTitle(), count<10?KeyEvent.VK_0+count:-1, MI_WINDOW+count);
                item.setSelected(window.isActive());
                menuWindow.addItem(item);
                count++;
            }
        }

        if(count == 0) {
            MenuItem item = new MenuItem(XJLocalizable.getXJString("NoWindows"), MI_NO_WINDOW, null);
            item.setEnabled(false);
            menuWindow.addItem(item);
        }
    }

    private Menu buildWindowMenu() {
        menuWindow = new Menu();
        menuWindow.setTitle(XJLocalizable.getXJString("Window"));
        buildWindowMenu_();
        return menuWindow;
    }

    private void rebuildWindowMenu() {
        if(menuWindow == null)
            return;

        for(int index = menuWindow.getItemCount()-1; index>=0; index--) {
            MenuItem item = menuWindow.getItemAtIndex(index);
            if(item != null && item.getTag() >= MI_NO_WINDOW)
                menuWindow.removeItem(index);
        }
        buildWindowMenu_();
    }

    private Menu buildHelpMenu() {
        menuHelp = new Menu();
        menuHelp.setTitle(XJLocalizable.getXJString("Help"));
        menuHelp.addItem(buildMenuItem(XJLocalizable.getXJString("Help"), MI_HELP));
        if(!XJSystem.isMacOS()) {
            menuHelp.addSeparator();
            menuHelp.addItem(buildMenuItem(XJLocalizable.getXJString("About"), MI_ABOUT));
        }
        return menuHelp;
    }

    public void handleMenuEvent(Menu menu, MenuItem item) {
        Document document = Application.shared().getActiveDocument();
        switch(item.tag) {
            case MI_NEW:
                Application.shared().newDocument();
                break;

            case MI_OPEN:
                Application.shared().openDocument();
                break;

            case MI_LOAD:
                if(document != null && document.performLoad())
                    document.changeReset();
                break;

            case MI_SAVE:
                if(document != null && document.performSave(false))
                    document.changeReset();
                break;

            case MI_SAVEAS:
                if(document != null && document.performSave(true))
                    document.changeReset();
                break;

            case MI_CLEAR_RECENT_FILES:
                Application.shared().clearRecentFiles();
                break;

            case MI_QUIT:
                Application.shared().performQuit();
                break;

            case MI_PREFS:
                Application.shared().displayPrefs();
                break;

            case MI_ABOUT:
                Application.shared().displayAbout();
                break;

            case MI_HELP:
                Application.shared().displayHelp();
                break;

            default:
                if(item.tag>=MI_WINDOW) {
                    AbstractWindow window = (AbstractWindow) Application.shared().getWindows().get(item.tag-MI_WINDOW);
                    window.bringToFront();
                    item.setSelected(true);
                } else if(item.tag>=MI_RECENT_FILES && item.tag<=MI_RECENT_FILES+ Application.MAX_RECENT_FILES) {
                    if(!Application.shared().openDocument(item.getTitle())) {
                        Application.shared().removeRecentFile(item.getTitle());
                    }
                } else if(delegate != null)
                    delegate.handleMenuEvent(menu, item);
                break;
        }
    }

    public void menuSelected(Menu menu) {
        if(delegate != null)
            delegate.handleMenuSelected(menu);
    }
}
