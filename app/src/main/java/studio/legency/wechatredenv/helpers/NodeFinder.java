package studio.legency.wechatredenv.helpers;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.apkfuns.logutils.LogUtils;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import studio.legency.wechatredenv.configs.Setting_;
import studio.legency.wechatredenv.configs.WechatInfo;
import studio.legency.wechatredenv.data.WechatRedEnvHis;

/**
 * 18 以上 可以用ID搜索
 * 14 以上可以用 名称搜索
 * Created by Administrator on 2015/9/30.
 */
@EBean
public class NodeFinder {

    @RootContext
    Context context;
    @StringRes
    String wechat_chat_env_symbol;
    @Pref
    Setting_ setting_;
    @StringRes
    String wechat_open_env_button_symbol;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static AccessibilityNodeInfo getWechatRedEnvelopeOpenDetailNode(AccessibilityNodeInfo info) {
        if (info == null)
            return null;
        List<AccessibilityNodeInfo> list = info.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/aqx");
        AccessibilityNodeInfo tempNode = null;
        for (int i = 0; i < list.size(); i++) {
            tempNode = list.get(i);
            if ("android.widget.TextView".equals(tempNode.getClassName()) && tempNode.isVisibleToUser()) {
                return tempNode;
            }
        }
        return null;
    }

    /**
     * 返回按钮上方的layout
     *
     * @param node
     * @return
     */
    public AccessibilityNodeInfo getWechatRedBack(AccessibilityNodeInfo node) {
        List<AccessibilityNodeInfo> resultList = findNodeInfo(node, WechatInfo.back_button_id, null);
        if (resultList != null && !resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;
    }

    public CharSequence getWechatRedEnvelopeMoney(AccessibilityNodeInfo node) {
        AccessibilityNodeInfo result = findNodeInfoOneById(node, WechatInfo.money_info_id, true);

        if (result != null) {
            CharSequence money = result.getText();
            LogUtils.d("恭喜你得到了红包" + money + "元");
            return money;
        } else {
            return null;
        }
    }

    /**
     * @param node
     * @param name
     * @return
     */
    public List<AccessibilityNodeInfo> getWechatRedEnvelopeNodes(AccessibilityNodeInfo node, String name) {
        if (node == null)
            return null;

        List<AccessibilityNodeInfo> red_env_all = findNodeInfo(node, WechatInfo.chat_red_env_id, wechat_chat_env_symbol);
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public AccessibilityNodeInfo getWechatRedEnvelopeOpenNode(AccessibilityNodeInfo info) {
        if (info == null)
            return null;
        List<AccessibilityNodeInfo> list = findNodeInfo(info, WechatInfo.open_env_button_id, wechat_open_env_button_symbol);
        AccessibilityNodeInfo tempNode;
        for (int i = 0; i < list.size(); i++) {
            tempNode = list.get(i);
            if ("android.widget.Button".equals(tempNode.getClassName()) && tempNode.isVisibleToUser()) {
                return tempNode;
            }
        }
        return null;
    }

    public AccessibilityNodeInfo findNodeInfoOneById(AccessibilityNodeInfo node, String id, boolean first) {
        return findNodeInfoOne(node, id, null, first);
    }

    public AccessibilityNodeInfo findNodeInfoOneByText(AccessibilityNodeInfo node, String text, boolean first) {
        return findNodeInfoOne(node, null, text, first);
    }

    /**
     * @param node
     * @param id
     * @param text
     * @param first true first false last
     * @return
     */
    public AccessibilityNodeInfo findNodeInfoOne(AccessibilityNodeInfo node, String id, String text, boolean first) {
        List<AccessibilityNodeInfo> result = findNodeInfo(node, id, text);
        if (result != null && !result.isEmpty()) {
            for (int i = 0; i < result.size(); i++) {
                if ((first && i == 0) || (!first && i == result.size() - 1)) {

                } else {
                    result.get(i).recycle();
                }
            }
            if (first)
                return result.get(0);
            else
                return result.get(result.size() - 1);
        }
        return null;
    }

    public List<AccessibilityNodeInfo> findNodeInfo(AccessibilityNodeInfo node, String id, String text) {
        List<AccessibilityNodeInfo> resultList = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !TextUtils.isEmpty(id)) {
            resultList = node.findAccessibilityNodeInfosByViewId(id);
        }
//
        if (resultList == null || resultList.isEmpty() && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && !TextUtils.isEmpty(text)) {
            resultList = node.findAccessibilityNodeInfosByText(text);
        }
        return resultList;
    }
}
