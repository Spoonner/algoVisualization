package com.spoonner.alex.ds;


import com.spoonner.alex.xj.appkit.document.XJDataXML;
import com.spoonner.alex.xj.appkit.document.XJDocument;

public class DSDocument extends XJDocument {

    protected static final String DATA_KEY = "data";

    public void documentWillWriteData() {
        DSWindow w = (DSWindow)getWindow();
        XJDataXML data = (XJDataXML)getDocumentData();
        data.setDataForKey(this, DATA_KEY, w.getData());
    }

    public void documentDidReadData() {
        DSWindow w = (DSWindow)getWindow();
        XJDataXML data = (XJDataXML)getDocumentData();
        w.setData(data.getDataForKey(DATA_KEY));
    }

}
