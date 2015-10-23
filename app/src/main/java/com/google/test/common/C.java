package com.google.test.common;

/**
 * Created by 15119 on 2015/10/23.
 */
public class C {

    public static final class url{

        public static final String URL_GET_ELECTRICITY = "http://api.hustonline.net/dianfei?";

        public static final String URL_POST_EMAIL = "http://api.hustonline.net/dianfei/notify";
    }

    public static final class notice{

        public static final String EMAIL_NO_EMPTY = "邮箱不可以为空哦";

        public static final String INPUT_ERROR = "输入错误，请重试";

        public static final String EMAIL_BIND_FAILED = "邮箱绑定失败，请重试";

        public static final String EMAIL_ALREADY_BINDED = "亲，该邮箱已经绑定过了哦~";

        public static final String EMAIL_HASNOT_BINDED = "亲，邮箱还未绑定呢~";

        public static final String EMAIL_BEING_SENT = "邮箱正在发送，请稍后";

        public static final String EMAIL_BINDED_SENT = "已经发送了一封绑定确认邮件到你的邮箱中";

        public static final String EMAIL_UNBINDED_SENT = "已经发送了一封解绑确认邮件到你的邮箱中";

        public static final String I_KNOW = "我知道了";

        public static final String BUILD_NUM_NO_EMPTY = "楼栋号不可以为空哦~";

        public static final String ROOM_NUM_NO_EMPTY = "寝室号不可以为空哦~";
    }

    public static final String[] AREA_LIST = {"东区", "西区" , "韵苑", "紫菘"};
}
