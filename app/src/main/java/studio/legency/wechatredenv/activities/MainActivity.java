package studio.legency.wechatredenv.activities;

import android.app.Activity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import studio.legency.wechatredenv.R;
import studio.legency.wechatredenv.helpers.AccessibilityServiceHelper;

/**
 * Created by Administrator on 2015/9/30.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @Click
    void openServiceClicked(){
        AccessibilityServiceHelper.startService(this);
    }
}
