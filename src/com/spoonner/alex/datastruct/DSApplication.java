package com.spoonner.alex.datastruct;

import com.spoonner.alex.appkit.core.app.Application;
import com.spoonner.alex.appkit.core.app.XJApplicationDelegate;
import com.spoonner.alex.appkit.core.document.DataXML;
import com.spoonner.alex.appkit.core.utils.XJLocalizable;

import javax.swing.*;

public class DSApplication extends XJApplicationDelegate {

    public static void main(String[] args) {
        Application.run(new DSApplication(), args, "Visualization");
    }

    public void appDidLaunch(String[] args) {

        Application.setPropertiesPath("com/spoonner/alex/datastruct/properties/");
        Application.addDocumentType(DSDocument.class, DSWindow.class, DataXML.class, "ds", XJLocalizable.getString("strings", "DSDocumentType"));

        if (Application.shared().getDocuments().size() == 0) {
            if (!Application.shared().openLastUsedDocument())
                Application.shared().newDocument();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //checkForUpdates(true);
            }
        });
    }
}
