package studio.legency.wechatredenv.helpers;

import android.app.Notification;
import android.app.PendingIntent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.apkfuns.logutils.LogUtils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.List;

import studio.legency.wechatredenv.configs.Setting_;
import studio.legency.wechatredenv.configs.WechatInfo;
import studio.legency.wechatredenv.data.WechatRedEnvHis;
import studio.legency.wechatredenv.services.WechatAccessService;

/**
 * Created by Administrator on 2015/9/30.
 */
@EBean
public class WechatEventHelper {

    @RootContext
    WechatAccessService wechatAccessService;

    @Bean
    NodeFinder nodeFinder;

    @Bean
    Common common;

    @StringRes
    String wechat_notification_symbol;

    @Pref
    Setting_ setting_;

    public void handleEvent(AccessibilityEvent event) {
//        LogUtils.d(event);
        if (Common.is_view_test()) {
            if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_LONG_CLICKED ||
                    event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
                AccessibilityNodeInfo nodeInfo = event.getSource();
                if (nodeInfo == null) return;
                nodeFinder.debugNode(nodeInfo);
            }
            return;
        }

        if (event == null)
            return;
        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            handleNotificationChange(event);
        } else if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            AccessibilityNodeInfo nodeInfo = event.getSource();
            if (nodeInfo == null) return;
            CharSequence currentActivityName = event.getClassName();
            if (WechatInfo.main_page.equals(currentActivityName)) {
                // 聊天以及主页
                handleChatPage(nodeInfo);
            } else if (WechatInfo.env_page.equals(currentActivityName)) {
                //打开红包主页
                LogUtils.d("红包外页");
                handleLuckyMoneyReceivePage(nodeInfo);
            } else if (WechatInfo.env_detail_page.equals(currentActivityName)) {
                LogUtils.d("红包详情页");
                handleLuckyMoneyDetailPage(nodeInfo);
            } else {
                handleChatPage(nodeInfo);
            }
        } else if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            AccessibilityNodeInfo nodeInfo = event.getSource();
            if (nodeInfo == null) return;
            handleChatPage(nodeInfo);
        }

    }

    public void handleLuckyMoneyDetailPage(AccessibilityNodeInfo node) {
        if (node == null)
            return;
        CharSequence money = nodeFinder.getWechatRedEnvelopeMoney(node);
        if (setting_.use_His().get()) {
            LogUtils.d("详情页 返回");
            goBack(node);
        } else
            common.goHome();
    }

    public void goBack(AccessibilityNodeInfo node) {
        AccessibilityNodeInfo backButton = nodeFinder.getWechatRedBack(node);
        clickNode(backButton);
        if (backButton == null) {
            LogUtils.d("无法找到详情页的返回按钮 进入主界面");
            common.goHome();
        }
    }

    public void goMessage(AccessibilityNodeInfo node) {
        AccessibilityNodeInfo messageButton = nodeFinder.findNodeInfoOneByText(node, "留言", true);
        clickNode(messageButton);
    }

    private void handleLuckyMoneyReceivePage(AccessibilityNodeInfo nodeInfo) {
        nodeFinder.debugNode(nodeInfo);
        AccessibilityNodeInfo nodeDetail = nodeFinder
                .getWechatRedEnvelopeOpenDetailNode(nodeInfo);
        if (nodeDetail != null) {// the red envelope already opened
            LogUtils.d("手慢了,开过了群红包");
            AccessibilityNodeInfo close_btn = nodeFinder
                    .getWechatRedEnvelopeCloseNode(nodeInfo);
            clickNode(close_btn);
        } else {
            LogUtils.d("新的红包");
            AccessibilityNodeInfo nodeOpen = nodeFinder
                    .getWechatRedEnvelopeOpenNode(nodeInfo);
            clickNode(nodeOpen);
        }
    }


    private void handleNotificationChange(AccessibilityEvent event) {

        if (event.getParcelableData() instanceof Notification) {
            Notification notification = (Notification) event
                    .getParcelableData();
            LogUtils.d("收到通知" + notification.tickerText.toString());
            if (notification.tickerText != null
                    && notification.tickerText.toString().contains(wechat_notification_symbol)) {
                LogUtils.d("收到红包通知");
                openNotification(event);
            }
        }
    }

    /**
     * 未知事件  都假设在 聊天页  尝试获取 红包数据
     *
     * @param node
     */
    public void handleChatPage(AccessibilityNodeInfo node) {
        if (node == null)
            return;
        AccessibilityNodeInfo title = NodeFindUtil.with(node).id(WechatInfo.chat_title_id).clazz("android.widget.TextView").findFirst();
//        AccessibilityNodeInfo title = NodeFindUtil.with(node).text("cici~ honey").clazz("android.widget.TextView").findFirst();
        String name = "";
        if (title == null) {
            LogUtils.d("可能不在聊天详情页面");
        } else {
            name = title.getText().toString();
            LogUtils.d("当前在" + name + "页面");
        }
        List<AccessibilityNodeInfo> close_envs = nodeFinder.getWechatRedEnvelopeNodes(node, name);
        if (close_envs != null) {
            LogUtils.d("发现" + close_envs.size() + "个新红包");
            //逆序打开
            for (int i = close_envs.size() - 1; i >= 0; i--) {
                AccessibilityNodeInfo env = close_envs.get(i);
                openEnv(env, name);
            }
            close_envs.clear();
        }
//        for (WechatRedEnvHis env :close_envs){
//            openEnv(env);
//        }
    }

    void openEnv(AccessibilityNodeInfo env, String name) {
        int hash = env.hashCode();
        if (clickNode(env.getParent()) && setting_.use_His().get()) {
            LogUtils.d("save hashCode:" + name + hash);
            new WechatRedEnvHis(name + hash).save();
        }

    }

    public void openNotification(AccessibilityEvent event) {
        if (!(event.getParcelableData() instanceof Notification)) {
            return;
        }
        Notification notification = (Notification) event.getParcelableData();
        PendingIntent pendingIntent = notification.contentIntent;
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    boolean clickNode(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            boolean success = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            nodeInfo.recycle();
            return success;
        } else {// this page is loading red envelope data, no action
            return false;
        }
    }
}
