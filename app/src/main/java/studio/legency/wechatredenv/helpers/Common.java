package studio.legency.wechatredenv.helpers;

import android.content.Context;
import android.content.Intent;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import studio.legency.wechatredenv.BuildConfig;
import studio.legency.wechatredenv.configs.WechatInfo;

/**
 * Created by Administrator on 2015/9/30.
 */
@EBean
public class Common {

    @RootContext
    Context context;

    static public boolean is_view_test(){
        return BuildConfig.BUILD_TYPE.equals("view_test");
    }

    public void goHome() {
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(mHomeIntent);
    }

    public void goChat() {
        //无效
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(WechatInfo.package_name, WechatInfo.main_page);
        context.startActivity(intent);
    }
}
