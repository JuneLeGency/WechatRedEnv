package studio.legency.wechatredenv.configs;

import io.paperdb.Paper;

/**
 * Created by Administrator on 2015/9/30.
 */
public class WechatInfo {

    /**
     * 微信包名
     */
    static public String package_name = "com.tencent.mm";

    /**
     * 微信启动ui
     */
    static public String main_page = "com.tencent.mm.ui.LauncherUI";

    /**
     * 微信红包
     */
    static public String env_page = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";

    /**
     * 红包详情页
     */
    static public String env_detail_page = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";

    /**
     * ids
     */

//    /**
//     * 聊天页 红包id
//     */
//    static public String chat_red_env_id = "com.tencent.mm:id/ys1";
//
//    /**
//     * 打开红包按钮id
//     */
//    static public String open_env_button_id = "com.tencent.mm:id/aww1";
//
//    /**
//     * 红包金额信息
//     */
//    static public String money_info_id = "com.tencent.mm:id/aub1";
//
//    /**
//     * 微信 红包详情页 返回按钮ID
//     */
//    static public String back_button_id = "com.tencent.mm:id/fb1";
//
//    /**
//     * 聊天页标题
//     */
//    static public String chat_title_id = "com.tencent.mm:id/ew1";
//
//    static public String close_btn_id = "com.tencent.mm:id/awr1";

    /**
     * 聊天页 红包id
     */
    static public String chat_red_env_id = "com.tencent.mm:id/dq";

    /**
     * 打开红包按钮id
     */
    static public String open_env_button_id = "com.tencent.mm:id/b2c";


    /**
     * 红包金额信息 
     */
    static public String money_info_id = "com.tencent.mm:id/b02";

    /**
     * 微信 红包详情页 返回按钮ID
     */
    static public String back_button_id = "com.tencent.mm:id/c2m";

    /**
     * 聊天页标题
     */
    static public String chat_title_id = "com.tencent.mm:id/ew";

    static public String close_btn_id = "com.tencent.mm:id/b2g";

    void init(){

    }

}
