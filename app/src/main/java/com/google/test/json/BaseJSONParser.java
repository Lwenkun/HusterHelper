package com.google.test.json;

import org.json.JSONObject;

/**
 * Created by 15119 on 2015/10/14.
 */
public abstract class BaseJSONParser {

    protected JSONObject mJSONObject;

    public BaseJSONParser(String sJSON) {

        try {
            mJSONObject = new JSONObject(sJSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public abstract void parseJSONForDetail();
}
