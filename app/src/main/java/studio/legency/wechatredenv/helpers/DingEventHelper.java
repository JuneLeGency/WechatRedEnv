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

import studio.legency.wechatredenv.configs.DingInfo;
import studio.legency.wechatredenv.configs.Setting_;
import studio.legency.wechatredenv.data.WechatRedEnvHis;
import studio.legency.wechatredenv.services.AccessService;

/**
 * Created by Administrator on 2015/9/30.
 */
@EBean
public class DingEventHelper {

    @RootContext
    AccessService wechatAccessService;

    @Bean
    NodeFinder nodeFinder;

    @Bean
    Common common;

    @StringRes
    String wechat_notification_symbol;

    @Pref
    Setting_ setting_;

    public void handleEvent(AccessibilityEvent event) {
        LogUtils.d(event);

        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
//            handleNotificationChange(event);
        } else if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            AccessibilityNodeInfo nodeInfo = event.getSource();
            if (nodeInfo == null) return;
            CharSequence currentActivityName = event.getClassName();
            if (DingInfo.chat_msg_page.equals(currentActivityName)) {
                // 聊天以及主页
                LogUtils.d("钉钉聊天页面");
                handleChatPage(nodeInfo);
            } else if (DingInfo.open_page.equals(currentActivityName)) {
                //打开红包主页
                LogUtils.d("钉钉打开红包页面");
                handleToOpenPage(nodeInfo);
            } else if (DingInfo.redpacket_detail_page.equals(currentActivityName)) {
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
        AccessibilityNodeInfo backButton = NodeFindUtil.with(node).id(DingInfo.back_btn_id).findFirst().getParent();
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

    private void handleToOpenPage(AccessibilityNodeInfo nodeInfo) {
        nodeFinder.debugNode(nodeInfo);
        AccessibilityNodeInfo openDetail = NodeFindUtil.with(nodeInfo).id(DingInfo.see_detail_btn_id).findFirst();
        if (openDetail != null) {// the red envelope already opened
            LogUtils.d("手慢了,开过了群红包");
            clickNode(openDetail);
        } else {
            LogUtils.d("新的红包");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(NodeFindUtil.with(nodeInfo).id(DingInfo.sender_id).firstText());
            stringBuilder.append(NodeFindUtil.with(nodeInfo).id(DingInfo.sender_tip_id).firstText());
            stringBuilder.append(NodeFindUtil.with(nodeInfo).id(DingInfo.sender_desc_id).firstText());
            LogUtils.d("发来红包" + stringBuilder.toString());
            AccessibilityNodeInfo nodeDetail = NodeFindUtil.with(nodeInfo).clazz("android.widget.ImageButton").useCustom().findFirst();
            clickNode(nodeDetail);
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
        AccessibilityNodeInfo title = NodeFindUtil.with(node).id(DingInfo.back_btn_id).clazz("android.widget.TextView").findFirst();
//        AccessibilityNodeInfo title = NodeFindUtil.with(node).text("cici~ honey").clazz("android.widget.TextView").findFirst();
        String name = "";
        if (title == null) {
            LogUtils.d("可能不在聊天详情页面");
        } else {
            name = title.getText().toString();
            LogUtils.d("当前在" + name + "页面");
        }
        List<AccessibilityNodeInfo> envs = getRedEnvelopeNodes(node, name);
        if (envs != null) {
            LogUtils.d("发现" + envs.size() + "个新红包");
            //逆序打开
            for (int i = envs.size() - 1; i >= 0; i--) {
                AccessibilityNodeInfo env = envs.get(i);
                openEnv(env, name);
            }
            envs.clear();
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
            LogUtils.d("点击" + success);
            nodeInfo.recycle();
            return success;
        } else {// this page is loading red envelope data, no action
            return false;
        }
    }

    /**
     * 获取微信红包的节点信息
     *
     * @param node
     * @param name
     * @return
     */
    public List<AccessibilityNodeInfo> getRedEnvelopeNodes(AccessibilityNodeInfo node, String name) {
        if (node == null)
            return null;

        List<AccessibilityNodeInfo> red_env_all = NodeFindUtil.with(node).id(DingInfo.chat_red_env_id).clazz("android.widget.TextView").find();
//        List<AccessibilityNodeInfo> result = new ArrayList<>();
        //更改逻辑发现有新的就全部打开  因为在list view 中 按位置 hash 值不变
        if (red_env_all != null && !red_env_all.isEmpty()) {
            LogUtils.d("发现" + red_env_all.size() + "个红包");
            for (AccessibilityNodeInfo nodeInfo : red_env_all) {
                AccessibilityNodeInfo node_env = nodeInfo;
                LogUtils.d("find hashCode:" + name + node_env.hashCode());
                if (!isOpened(name + node_env.hashCode())) {
//                    result.add(node_env);
                    return red_env_all;
                } else {
//                    LogUtils.d("发现hash" + node_env.hashCode() + "已经被开过了");
//                    nodeInfo.recycle();
                }
            }
        }
        return null;
    }

    /**
     * TODO  如何在所有的hash 都相同的情况下判断当前的红包有没有被打开过 寻找标志位
     *
     * @param hash
     * @return
     */
    boolean isOpened(String hash) {
        if (setting_.use_His().get()) {
            List<WechatRedEnvHis> a = WechatRedEnvHis.find(WechatRedEnvHis.class, "hash = ?", "" + hash);
            return (a != null && !a.isEmpty());
        } else {
            return false;
        }
    }
}
