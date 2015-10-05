package studio.legency.wechatredenv.activities;

import android.app.Activity;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import studio.legency.wechatredenv.R;
import studio.legency.wechatredenv.helpers.AccessibilityServiceHelper;

/**
 * Created by Administrator on 2015/9/30.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @ViewById
    TextView service_state;

    @AfterViews
    void init(){
        if (AccessibilityServiceHelper.isAccessibilitySettingsOn(this)) {
            service_state.setText("服务运行中");
        }else {
            service_state.setText("服务已停止");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Click
    void openServiceClicked(){
        AccessibilityServiceHelper.startService(this);
    }
}
