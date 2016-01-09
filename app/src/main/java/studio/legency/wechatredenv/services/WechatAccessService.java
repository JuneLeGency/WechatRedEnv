package studio.legency.wechatredenv.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.view.accessibility.AccessibilityEvent;

import com.apkfuns.logutils.LogUtils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;

import studio.legency.wechatredenv.configs.WechatInfo;
import studio.legency.wechatredenv.data.WechatRedEnvHis;
import studio.legency.wechatredenv.helpers.WechatEventHelper;

/**
 * Created by Administrator on 2015/9/30.
 */
@EService
public class WechatAccessService extends AccessibilityService {

    @Bean
    WechatEventHelper wechatEventHelper;

    private PowerManager.WakeLock lock;

    @Override
    protected void onServiceConnected() {
        LogUtils.d("微信服务已连接");
        keepScreenOn();
        cleanData();
        configService();
        super.onServiceConnected();
    }

    private void keepScreenOn() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        lock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "LOCK");

        //请求常亮
        lock.acquire();
    }

    private void cleanData() {
        LogUtils.d("清空数据库");
        WechatRedEnvHis.deleteAll(WechatRedEnvHis.class);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void configService() {
        AccessibilityServiceInfo accessibilityServiceInfo = getServiceInfo();
        if (accessibilityServiceInfo == null)
            accessibilityServiceInfo = new AccessibilityServiceInfo();
//        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED |
                AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED |
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_VIEW_LONG_CLICKED |
                AccessibilityEvent.TYPE_VIEW_CLICKED;
        accessibilityServiceInfo.flags |= AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        accessibilityServiceInfo.packageNames = new String[]{WechatInfo.package_name};
        accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        accessibilityServiceInfo.notificationTimeout = 10;
        setServiceInfo(accessibilityServiceInfo);
        // 4.0之后可通过xml进行配置,以下加入到Service里面
        /*
         * <meta-data android:name="android.accessibilityservice"
		 * android:resource="@xml/accessibility" />
		 */
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        wechatEventHelper.handleEvent(event);
    }

    @Override
    public void onInterrupt() {
        LogUtils.e("服务意外关闭");
//        lock.release();
    }
}
