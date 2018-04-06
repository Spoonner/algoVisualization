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

package com.spoonner.alex.appkit.appkit.document;

import com.spoonner.alex.appkit.appkit.app.Application;
import com.spoonner.alex.appkit.appkit.frame.AbstractWindow;
import com.spoonner.alex.appkit.appkit.utils.XJAlert;
import com.spoonner.alex.appkit.appkit.utils.XJFileChooser;
import com.spoonner.alex.appkit.appkit.utils.XJLocalizable;
import com.spoonner.alex.appkit.misc.XJObject;
import com.spoonner.alex.appkit.misc.XJUtils;

import java.awt.*;
import java.io.*;

public class Document extends XJObject {

    protected Data documentData = null;
    protected AbstractWindow documentWindow = null;
    protected String documentTitle = XJLocalizable.getXJString("DocUntitled");
    protected String documentPath = null;

    protected String documentFileExt = null;
    protected String documentFileExtDescription = null;

    protected boolean dirty = false;
    protected boolean firstDocument = false;
    protected boolean writing = false;

    protected Component javaContainer = null;

    protected static int absoluteCounter = 0;

    public Document() {
        Application.shared().addDocument(this);
        this.firstDocument = absoluteCounter == 0;
        absoluteCounter++;
    }

    // *** Public methods

    public boolean isFirstDocument() {
        return firstDocument;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void changeDone() {
        if(dirty)
            return;

        dirty = true;
        if(documentWindow != null)
            documentWindow.setDirty();
    }

    public void changeReset() {
        dirty = false;
        if(documentWindow != null)
            documentWindow.resetDirty();
    }

    public void setTitle(String title) {
        documentTitle = title;
        if(documentWindow != null)
            documentWindow.setTitle(documentTitle);
    }

    public void setWindow(AbstractWindow window) {
        documentWindow = window;
        if(documentWindow != null) {
            documentWindow.setDocument(this);
            documentWindow.setTitle(documentTitle);
        }
    }

    public AbstractWindow getWindow() {
        return documentWindow;
    }

    public void setJavaContainer(Component container) {
        this.javaContainer = container;
    }

    public Component getJavaContainer() {
        if(javaContainer == null)
            return getWindow() == null ? null : getWindow().getJavaContainer();
        else
            return javaContainer;
    }

    public void showWindow() {
        if(documentWindow != null)
            documentWindow.show();
    }

    public void setDocumentData(Data data) {
        this.documentData = data;
        if(documentData != null) {
            documentData.addObserver(this);
        }
    }

    public Data getDocumentData() {
        return documentData;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public String getDocumentName() {
        return XJUtils.getLastPathComponent(documentPath);
    }

    public void setDocumentFileType(String ext, String description) {
        documentFileExt = ext;
        documentFileExtDescription = description;
    }

    public void observeValueForKey(Object sender, String key, Object object) {
        if(!writing)
            changeDone();
    }

    /** Automatically handle external modification detection
     *
     */

    protected long lastModifiedOnDisk = 0;

    public long getDateOfModificationOnDisk() {
        if(getDocumentPath() == null)
            return 0;

        File f = null;
        try {
            f = new File(getDocumentPath());
        } catch(Exception e) {
            // ignore excepton
        }

        if(f == null)
            return 0;
        else
            return f.lastModified();
    }

    public void synchronizeLastModifiedDate() {
        lastModifiedOnDisk = getDateOfModificationOnDisk();
    }

    public boolean isModifiedOnDisk() {
        return lastModifiedOnDisk != getDateOfModificationOnDisk();
    }

    /** General methods
     *
     */

    private boolean performLoad_() {
        if(!dirty)
            return Application.YES;


        int r = XJAlert.displayAlertYESNOCANCEL(getJavaContainer(),
                                                XJLocalizable.getXJString("DocLoad"),
                                                XJLocalizable.getStringFormat("DocSaveChanges",
                                                documentTitle));
        switch(r) {
            case XJAlert.YES:
                return performSave(false);

            case XJAlert.NO:
                return Application.YES;

            case XJAlert.CANCEL:
                return Application.NO;
        }

        return Application.YES;
    }

    public boolean performLoad(String file) {
        documentPath = file;
        try {
            readDocument(documentPath);
        } catch(Exception e) {
            e.printStackTrace();
            XJAlert.display(getJavaContainer(), XJLocalizable.getXJString("DocError"), XJLocalizable.getXJString("DocLoadError")+" "+e.toString());
            return Application.NO;
        }

        setTitle(documentPath);
        changeReset();

        return Application.YES;
    }

    public boolean performLoad() {
        if(!performLoad_())
            return Application.NO;

        if(!XJFileChooser.shared().displayOpenDialog(getJavaContainer(), documentFileExt, documentFileExtDescription, false))
            return Application.NO;

        String path = XJFileChooser.shared().getSelectedFilePath();
        Document document = Application.shared().getDocumentForPath(path);
        if(document != null && document != this) {
            XJAlert.display(getJavaContainer(), XJLocalizable.getXJString("DocError"), XJLocalizable.getXJString("DocLoadExists"));
            return Application.NO;
        } else {
            Application.shared().addRecentFile(path);
            return performLoad(XJFileChooser.shared().getSelectedFilePath());
        }
    }

    public boolean reload() {
        try {
            readDocument(documentPath);
        } catch(Exception e) {
            XJAlert.display(getJavaContainer(), XJLocalizable.getXJString("DocError"), XJLocalizable.getXJString("DocLoadError")+" "+e.toString());
            return Application.NO;
        }
        return Application.YES;
    }

    public boolean performAutoSave() {
        if(getDocumentPath() != null && isDirty())
            return performSave(false);
        else
            return true;
    }

    public boolean performSave(boolean saveAs) {
        if(documentPath == null || saveAs) {
            if(!XJFileChooser.shared().displaySaveDialog(getJavaContainer(), documentFileExt, documentFileExtDescription, true))
                return Application.NO;

            documentPath = XJFileChooser.shared().getSelectedFilePath();
            Application.shared().addRecentFile(documentPath);
        }

        try {
            writeDocument(documentPath);
        } catch(Exception e) {
            e.printStackTrace();
            XJAlert.display(getJavaContainer(), XJLocalizable.getXJString("DocError"), XJLocalizable.getXJString("DocSaveError")+" "+e.toString());
            return Application.NO;
        }

        setTitle(documentPath);        
        changeReset();
        return Application.YES;
    }

    protected boolean performClose_() {
        if(!dirty || !Application.shared().supportsPersistence())
            return Application.YES;

        if(documentWindow != null)
            documentWindow.bringToFront();

        int r = XJAlert.displayAlertYESNOCANCEL(getJavaContainer(), XJLocalizable.getXJString("DocCloseTitle"), XJLocalizable.getStringFormat("DocCloseMessage", documentTitle));
        switch(r) {
            case XJAlert.YES:
                return performSave(false);

            case XJAlert.NO:
                return Application.YES;

            case XJAlert.CANCEL:
                return Application.NO;
        }

        return Application.YES;
    }

    public boolean performClose(boolean force) {
        boolean r = force? Application.YES:performClose_();
        if(r) {
            Application.shared().removeDocument(this);
            if(documentWindow != null)
                documentWindow.close();
        }
        return r;
    }

    public boolean performClose() {
        return performClose(false);
    }

    private void beginWrite() {
        writing = true;
    }

    private void endWrite() {
        writing = false;
    }

    private void writeDocument(String file) throws IOException {
        beginWrite();
        try {
            documentWillWriteData();
            documentData.setFile(file);
            switch(documentData.dataType()) {
                case Data.DATA_INPUTSTREAM: {
                    OutputStream os = new FileOutputStream(file);
                    documentData.writeData(os);
                    os.close();
                    break;
                }

                case Data.DATA_OBJECTINPUTSTREAM: {
                    OutputStream os = new FileOutputStream(file);
                    documentData.writeData(new ObjectOutputStream(os));
                    os.close();
                    break;
                }

                case Data.DATA_PLAINTEXT:
                    documentData.writeData();
                    break;

                case Data.DATA_XML:
                    documentData.writeData();
                    break;
            }
            synchronizeLastModifiedDate();
        } finally {
            endWrite();            
        }
    }

    private void readDocument(String file) throws IOException, ClassNotFoundException {
        documentWillReadData();
        documentData.setFile(file);
        switch(documentData.dataType()) {
            case Data.DATA_INPUTSTREAM: {
                InputStream is = new FileInputStream(file);
                documentData.readData(is);
                is.close();
                break;
            }

            case Data.DATA_OBJECTINPUTSTREAM: {
                InputStream is = new FileInputStream(file);
                documentData.readData(new ObjectInputStream(is));
                is.close();
                break;
            }

            case Data.DATA_PLAINTEXT:
                documentData.readData();
                break;

            case Data.DATA_XML:
                documentData.readData();
                break;
        }
        documentDidReadData();
        synchronizeLastModifiedDate();        
    }

    // Subclasses only

    public void documentWillWriteData() {

    }

    public void documentWillReadData() {

    }

    public void documentDidReadData() {
        
    }

}
