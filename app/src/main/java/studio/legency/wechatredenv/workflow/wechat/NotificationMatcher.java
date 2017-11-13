package studio.legency.wechatredenv.workflow.wechat;

import android.app.Notification;
import android.view.accessibility.AccessibilityEvent;

import com.apkfuns.logutils.LogUtils;

import studio.legency.wechatredenv.workflow.EventMatcher;

/**
 * Created by lichen:) on 2016/9/27.
 */

public class NotificationMatcher extends EventMatcher {
    public NotificationMatcher(AccessibilityEvent event) {
        super(event);
        eventType = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
    }


    @Override
    public boolean match() {
        return typeMatch() & notificationMatch();
    }

    @Override
    public boolean go() {
        return false;
    }

    boolean notificationMatch() {
        if (event.getParcelableData() instanceof Notification) {
            Notification notification = (Notification) event
                    .getParcelableData();
            LogUtils.d("收到通知" + notification.tickerText.toString());
            if (notification.tickerText != null
                    && notification.tickerText.toString().contains(": [微信红包]")) {
                return true;
            }
        }
        return false;
    }
}
