package studio.legency.wechatredenv.helpers;

import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by lichen:) on 2016/1/9.
 */
public class NodeFindUtil {

    public static final String TAG = "NodeFindUtil";

    private String text = "";

    private String id;

    private AccessibilityNodeInfo nodeInfo;

    private CharSequence className;

    private boolean useCustom;

    public NodeFindUtil(AccessibilityNodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
    }


    public static NodeFindUtil with(AccessibilityNodeInfo nodeInfo) {
        return new NodeFindUtil(nodeInfo);
    }

    public NodeFindUtil text(String text) {
        this.text = text;
        return this;
    }

    public NodeFindUtil id(String id) {
        this.id = id;
        return this;
    }

    NodeFindUtil clazz(String className) {
        this.className = className;
        return this;
    }

    public List<AccessibilityNodeInfo> find() {
        if (useCustom) {
            return findCChildren();
        } else {
            return findByApi();
        }
    }

    @Nullable
    private List<AccessibilityNodeInfo> findByApi() {
        List<AccessibilityNodeInfo> nodeInfoList = null;
        if (nodeInfo != null) {

            if (!TextUtils.isEmpty(id)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    nodeInfoList = nodeInfo.findAccessibilityNodeInfosByViewId(id);
                }
                filterResult(nodeInfoList);
            }

            if (isEmptyCollection(nodeInfoList)) {
                if (!TextUtils.isEmpty(text)) {
                    nodeInfoList = nodeInfo.findAccessibilityNodeInfosByText(text);
                    filterResult(nodeInfoList);
                }
            }
        }
        return nodeInfoList;
    }

    public AccessibilityNodeInfo findFirst() {
        List<AccessibilityNodeInfo> nodeInfoList = find();
        if (isEmptyCollection(nodeInfoList)) {
            return null;
        } else {
            return nodeInfoList.get(0);
        }
    }

    public String firstText() {
        AccessibilityNodeInfo node = findFirst();
        if (node == null || TextUtils.isEmpty(node.getText())) return "";
        else return (String) node.getText();
    }

    void filterResult(List<AccessibilityNodeInfo> nodes) {
        if (isEmptyCollection(nodes)) return;
        for (int i = nodes.size() - 1; i >= 0; i--) {
            AccessibilityNodeInfo node = nodes.get(i);
            if (!isAvailableNode(node)) nodes.remove(node);
        }

    }

    private boolean isAvailableNode(AccessibilityNodeInfo node) {
        if (!TextUtils.isEmpty(className) && !className.equals(node.getClassName())) {
            return false;
        }
        return true;
    }

    public static boolean isEmptyCollection(Collection e) {
        return e == null || e.size() == 0;
    }

    private List<AccessibilityNodeInfo> findCChildren() {
        List<AccessibilityNodeInfo> accessibilityNodeInfos = new ArrayList<>();
        doFilterChildren(nodeInfo, accessibilityNodeInfos);
        return accessibilityNodeInfos;
    }

    public NodeFindUtil useCustom() {
        useCustom = true;
        return this;
    }

    private void doFilterChildren(AccessibilityNodeInfo nodeInfo, List<AccessibilityNodeInfo> nodeInfos) {
        int count = nodeInfo.getChildCount();
        for (int i = 0; i < count; i++) {
            AccessibilityNodeInfo n = nodeInfo.getChild(i);
            if (n == null) continue;
            if (isAvailableNode(n)) {
                nodeInfos.add(n);
            }
            doFilterChildren(n, nodeInfos);
        }
    }
}
