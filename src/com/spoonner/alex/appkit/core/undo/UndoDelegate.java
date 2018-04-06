package com.spoonner.alex.appkit.core.undo;

public interface UndoDelegate {
    public void undoManagerWillUndo(boolean redo);
    public void undoManagerDidUndo(boolean redo);
}
