package com.google.test.json;

import org.json.JSONObject;

/**
 * Created by 15119 on 2015/10/14.
 */
public abstract class BaseJSONParser {

    protected JSONObject mJSONObject;

    public BaseJSONParser(JSONObject jsonObject) {
        mJSONObject = jsonObject;
    }

    public int parseJSONForCode() {
        try {
            return mJSONObject.getInt("code");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public abstract void parseJSONForDetail();
}
