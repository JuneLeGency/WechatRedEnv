package studio.legency.wechatredenv.workflow;

import android.view.accessibility.AccessibilityEvent;

/**
 * Created by lichen:) on 2016/9/26.
 */
public interface WorkFlow {
    String getPackageName();

    void handleEvent(AccessibilityEvent event);
}
