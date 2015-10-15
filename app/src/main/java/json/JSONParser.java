package json;

import org.json.JSONObject;

/**
 * Created by 15119 on 2015/10/14.
 */
public class JSONParser {

    public static int parseJSONForCode(JSONObject jsonObject) {
        int code = 0;
        try {
            code = jsonObject.getInt("code");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public void parseJSONForDetail() {
    }
}
