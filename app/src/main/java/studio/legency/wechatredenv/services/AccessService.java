package studio.legency.wechatredenv.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.RelativeLayout;

import com.apkfuns.logutils.LogUtils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;

import studio.legency.wechatredenv.activities.TestActivity_;
import studio.legency.wechatredenv.configs.DingInfo;
import studio.legency.wechatredenv.configs.WechatInfo;
import studio.legency.wechatredenv.data.NodeInfo;
import studio.legency.wechatredenv.data.WechatRedEnvHis;
import studio.legency.wechatredenv.helpers.Common;
import studio.legency.wechatredenv.helpers.DingEventHelper;
import studio.legency.wechatredenv.helpers.NodeFinder;
import studio.legency.wechatredenv.helpers.WechatEventHelper;

/**
 * Created by Administrator on 2015/9/30.
 */
@EService
public class AccessService extends AccessibilityService {

    @Bean
    WechatEventHelper wechatEventHelper;

    @Bean
    NodeInfoWindow nodeInfoWindow;

    @Bean
    DingEventHelper dingEventHelper;

    private PowerManager.WakeLock lock;

    private RelativeLayout relativeLayout;

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
        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
//        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED |
//                AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED |
//                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_VIEW_LONG_CLICKED |
//                AccessibilityEvent.TYPE_VIEW_CLICKED;
        accessibilityServiceInfo.flags |= AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        accessibilityServiceInfo.packageNames = new String[]{WechatInfo.package_name, DingInfo.package_name};
        accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        accessibilityServiceInfo.notificationTimeout = 10;
        setServiceInfo(accessibilityServiceInfo);
        // 4.0之后可通过xml进行配置,以下加入到Service里面
        /*
         * <meta-data android:name="android.accessibilityservice"
		 * android:resource="@xml/accessibility" />
		 */
    }

    @Bean
    NodeFinder nodeFinder;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        try {
            if (event == null) {
                return;
            }

            if (Common.is_view_test()) {
                int[] types = {AccessibilityEvent.TYPE_VIEW_LONG_CLICKED, AccessibilityEvent.TYPE_VIEW_CLICKED, AccessibilityEvent.TYPE_TOUCH_INTERACTION_START};
//                int[] types = {AccessibilityEvent.TYPE_TOUCH_INTERACTION_START};
                if (inType(types, event.getEventType())) {
                    AccessibilityNodeInfo accessibilityNodeInfo = event.getSource();
                    if (accessibilityNodeInfo == null) return;
                    nodeFinder.debugNode(accessibilityNodeInfo);
                    AccessibilityNodeInfo rootNode = getRootParent(accessibilityNodeInfo);
                    NodeInfo node = getNode(rootNode);
                    TestActivity_.intent(this).nodeInfo(node).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
//                    nodeInfoWindow.showWindow(node);
                }
                return;
            }

            if (WechatInfo.package_name.equals(event.getPackageName())) {
                wechatEventHelper.handleEvent(event);
            } else {
                dingEventHelper.handleEvent(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    NodeInfo getNode(AccessibilityNodeInfo nodeInfo) {
        NodeInfo node = new NodeInfo();
        Rect rect = new Rect();
        nodeInfo.getBoundsInScreen(rect);
        node.left = rect.left;
        node.top = rect.top;
        node.width = rect.right - rect.left;
        node.height = rect.bottom - rect.top;
        node.setClazz(nodeInfo.getClassName().toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            node.setId(nodeInfo.getViewIdResourceName());
        }
        if (!TextUtils.isEmpty(nodeInfo.getText()))
            node.setText(nodeInfo.getText().toString());
        int count = nodeInfo.getChildCount();
        for (int i = 0; i < count; i++) {
            AccessibilityNodeInfo n = nodeInfo.getChild(i);
            if (n != null) {
                node.addChild(getNode(n));
            }
        }
        return node;
    }

    private AccessibilityNodeInfo getRootParent(AccessibilityNodeInfo nodeInfo) {
        AccessibilityNodeInfo parent = nodeInfo.getParent();
        if (parent != null)
            return getRootParent(parent);
        else
            return nodeInfo;
    }

    boolean inType(int[] types, int type) {
        for (int type1 : types) {
            if (type == type1) return true;
        }
        return false;
    }

    @Override
    public void onInterrupt() {
        LogUtils.e("服务意外关闭");
//        lock.release();
    }
}
