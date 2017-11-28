package com.spoonner.alex.ds;

import com.spoonner.alex.xj.appkit.app.XJApplication;
import com.spoonner.alex.xj.appkit.app.XJApplicationDelegate;
import com.spoonner.alex.xj.appkit.document.XJDataXML;
import com.spoonner.alex.xj.appkit.update.XJUpdateManager;
import com.spoonner.alex.xj.appkit.utils.BrowserLauncher;
import com.spoonner.alex.xj.appkit.utils.XJAlert;
import com.spoonner.alex.xj.appkit.utils.XJLocalizable;

import javax.swing.*;
import java.io.IOException;

public class DSApplication extends XJApplicationDelegate {

    public static void main(String[] args) {
        XJApplication.run(new DSApplication(), args, "Visualization");
    }

    public void appDidLaunch(String[] args) {

        XJApplication.setPropertiesPath("com/spoonner/alex/ds/properties/");
        XJApplication.addDocumentType(DSDocument.class, DSWindow.class, XJDataXML.class, "ds", XJLocalizable.getString("strings", "DSDocumentType"));

        if (XJApplication.shared().getDocuments().size() == 0) {
            if (!XJApplication.shared().openLastUsedDocument())
                XJApplication.shared().newDocument();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //checkForUpdates(true);
            }
        });
    }

    public void appShowHelp() {
        String url = "http://www.cs.usfca.edu/~galles/visualization/";
        try {
            BrowserLauncher.openURL(url);
        } catch (IOException e) {
            XJAlert.display(null, "Cannot access the online resource", "Browse "+url+" for more information.");
        }
    }

    public Class appPreferencesClass() {
        return DSApplication.class;
    }

    public boolean supportsPersistence() {
        return false;
    }

    public static void checkForUpdates(boolean automatic) {
        XJUpdateManager um = new XJUpdateManager(null, null);
        um.checkForUpdates("1.04",
                           "http://www.cs.usfca.edu/~galles/visualization/update.xml",
                           System.getProperty("user.home"),
                           automatic);
    }

}
