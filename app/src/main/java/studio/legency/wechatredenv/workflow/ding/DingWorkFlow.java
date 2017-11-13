package studio.legency.wechatredenv.workflow.ding;

import android.view.accessibility.AccessibilityEvent;

import studio.legency.wechatredenv.workflow.WorkFlow;

/**
 * Created by lichen:) on 2016/9/26.
 */
public class DingWorkFlow implements WorkFlow {

    @Override
    public String getPackageName() {
        return "com.alibaba.android.rimet";
    }

    @Override
    public void handleEvent(AccessibilityEvent event) {
    }
}
