package studio.legency.wechatredenv.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import studio.legency.wechatredenv.R;
import studio.legency.wechatredenv.configs.Setting_;
import studio.legency.wechatredenv.data.WechatRedEnvHis;
import studio.legency.wechatredenv.helpers.AccessibilityServiceHelper;

/**
 * Created by Administrator on 2015/9/30.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);

        setTranslucentStatus(true);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @ViewById
    TextView service_state;

    @Pref
    Setting_ setting_;

    @ViewById
    Button enable_test;

    @AfterViews
    void init() {
        if (AccessibilityServiceHelper.isAccessibilitySettingsOn(this)) {
            service_state.setText("服务运行中");
        } else {
            service_state.setText("服务已停止");
        }
        if (setting_.testMode().get()) {
            enable_test.setText("关闭调试");
        } else {
            enable_test.setText("开启调试");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Click
    void openServiceClicked() {
        AccessibilityServiceHelper.startService(this);
    }

    @Click
    void enable_test() {
        Boolean a = setting_.testMode().get();
        setting_.edit().testMode().put(!a).apply();
        init();
    }

    @Click
    void clear_data() {
        WechatRedEnvHis.deleteAll(WechatRedEnvHis.class);
    }
}
