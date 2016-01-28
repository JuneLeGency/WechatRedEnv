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
 * api 18 以上 可以用ID搜索
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
    public AccessibilityNodeInfo getWechatRedEnvelopeOpenDetailNode(AccessibilityNodeInfo info) {
        if (info == null)
            return null;
        NodeFindUtil.with(info).text("手慢了").text("红包已失效").hasResult();
        AccessibilityNodeInfo opend_empty = findNodeInfoOneByText(info, "手慢了", true);
        return opend_empty;
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
     * 获取微信红包的节点信息
     *
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
                if (!isOpened(node_env)) {
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
     * @param node
     * @return
     */
    boolean isOpened(AccessibilityNodeInfo node) {
        if (setting_.use_His().get()) {
            List<WechatRedEnvHis> a = WechatRedEnvHis.find(WechatRedEnvHis.class, "hash = ?", "" + nodeToHash(node));
            return (a != null && !a.isEmpty());
        } else {
            return false;
        }
    }

    public String nodeToHash(AccessibilityNodeInfo nodeInfo) {
        String dialog = "unknown dialog";

        try {
            dialog = nodeInfo.getParent().getParent().getParent().getParent().getParent().getParent().getContentDescription().toString();
        } catch (Exception e) {

        }

        String time = "unknown time";

        String head_desc = "unknown head";
        try {
            AccessibilityNodeInfo relative = nodeInfo.getParent().getParent();
            time = relative.getChild(0).getText().toString();
            head_desc = relative.getChild(1).getContentDescription().toString();
        } catch (Exception e) {

        }
        String r = dialog + " | " + time + " | " + head_desc + " | " + nodeInfo.hashCode();
        LogUtils.d("红包标示:" + r);
        return r;
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
     * 查找 并返回第一个节点信息
     *
     * @param node  被查找的节点
     * @param id    使用 node id 检索
     * @param text  使用 字符检索
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
        boolean id_failed = false;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !TextUtils.isEmpty(id)) {
            resultList = node.findAccessibilityNodeInfosByViewId(id);
            id_failed = NodeFindUtil.isEmptyCollection(resultList);
        }
//
        if (resultList == null || resultList.isEmpty() && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && !TextUtils.isEmpty(text)) {
            resultList = node.findAccessibilityNodeInfosByText(text);
            if (id_failed && !NodeFindUtil.isEmptyCollection(resultList)) {
                LogUtils.d("id：" + id + " 已经失效 但是 " + text + "可用");
            }
        }
        return resultList;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void debugNode(AccessibilityNodeInfo info) {
        if (info.getChildCount() == 0) {
            showInfo(info);
        } else {
            LogUtils.d("parent:");
            showInfo(info);
            for (int i = 0; i < info.getChildCount(); i++) {
                if (info.getChild(i) != null) {
                    debugNode(info.getChild(i));
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void showInfo(AccessibilityNodeInfo info) {
        LogUtils.d("------------------------");
        LogUtils.d("id:" + info.getViewIdResourceName());
        LogUtils.d("class:" + info.getClassName());
        LogUtils.d("text:" + info.getText());
        LogUtils.d("hash:" + info.hashCode());
        LogUtils.d("------------------------");
    }

    public AccessibilityNodeInfo getWechatRedEnvelopeCloseNode(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null)
            return null;
        AccessibilityNodeInfo opend_empty = findNodeInfoOneById(nodeInfo, WechatInfo.close_btn_id, true);
        return opend_empty;
    }
}
