package com.spoonner.alex.appkit.core.undo;


import com.spoonner.alex.appkit.core.menu.MainMenuBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.Map;


public class UndoEngine {

    protected MainMenuBar mainMenuBar;
    protected Map undos = new HashMap();

    public UndoEngine() {
    }

    public void setMainMenuBar(MainMenuBar mainMenuBar) {
        this.mainMenuBar = mainMenuBar;
    }

    public void registerUndo(Undo undo, JTextPane component) {
        undo.bindTo(component);
        component.addFocusListener(new EditorFocusListener());
        undos.put(component, undo);
    }

    public Undo getCurrentUndo() {
        // Use the permanent focus owner because on Windows/Linux, an opened menu become
        // the current focus owner (non-permanent).
        return (Undo)undos.get(KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner());
    }

    public Undo getUndo(Object object) {
        return (Undo)undos.get(object);
    }

    public void updateUndoRedo(Object source) {
        Undo undo = getUndo(source);
        updateUndoRedo(undo);
    }

    public void updateUndoRedo(Undo undo) {
        if(mainMenuBar != null)
            mainMenuBar.menuUndoRedoItemState(getCurrentUndo());
    }

    public void undoStateDidChange(Undo undo) {
        updateUndoRedo(undo);
    }

    protected class EditorFocusListener implements FocusListener {

        public void focusGained(FocusEvent event) {
            updateUndoRedo(event.getSource());
        }

        public void focusLost(FocusEvent event) {
            // Update the menu only if the event is not temporary. Temporary
            // focus lost can be, for example, when opening a menu on Windows/Linux.
            if(!event.isTemporary())
                updateUndoRedo(null);
        }
    }

}
