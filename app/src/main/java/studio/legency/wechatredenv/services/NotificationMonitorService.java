package studio.legency.wechatredenv.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.apkfuns.logutils.LogUtils;

import org.androidannotations.annotations.EService;

/**
 * Created by lichen:) on 2016/1/29.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
@EService
public class NotificationMonitorService extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification n = sbn.getNotification();
        PendingIntent i = n.contentIntent;
//        try {
//            i.send();
//        } catch (PendingIntent.CanceledException e) {
//            e.printStackTrace();
//        }
        LogUtils.d("notification received ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LogUtils.d(n.extras);
        }
        super.onNotificationPosted(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
