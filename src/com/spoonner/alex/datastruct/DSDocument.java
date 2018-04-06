package com.spoonner.alex.datastruct;


import com.spoonner.alex.appkit.appkit.document.DataXML;
import com.spoonner.alex.appkit.appkit.document.Document;

public class DSDocument extends Document {

    protected static final String DATA_KEY = "data";

    public void documentWillWriteData() {
        DSWindow w = (DSWindow)getWindow();
        DataXML data = (DataXML)getDocumentData();
        data.setDataForKey(this, DATA_KEY, w.getData());
    }

    public void documentDidReadData() {
        DSWindow w = (DSWindow)getWindow();
        DataXML data = (DataXML)getDocumentData();
        w.setData(data.getDataForKey(DATA_KEY));
    }

}
