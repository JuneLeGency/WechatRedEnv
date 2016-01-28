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

    private List<String> texts = new ArrayList<>();

    private List<String> ids = new ArrayList<>();

    private AccessibilityNodeInfo nodeInfo;

    private List<CharSequence> className = new ArrayList<>();

    private boolean useCustom;

    public NodeFindUtil(AccessibilityNodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
    }


    public static NodeFindUtil with(AccessibilityNodeInfo nodeInfo) {
        return new NodeFindUtil(nodeInfo);
    }

    public NodeFindUtil text(String text) {
        this.texts.add(text);
        return this;
    }

    public NodeFindUtil id(String id) {
        this.ids.add(id);
        return this;
    }

    NodeFindUtil clazz(String className) {
        this.className.add(className);
        return this;
    }

    public List<AccessibilityNodeInfo> find() {
        if (useCustom) {
            return findCChildren();
        } else {
            return findByApi();
        }
    }

    public boolean hasResult() {
        return !isEmptyCollection(find());
    }

    @Nullable
    private List<AccessibilityNodeInfo> findByApi() {
        List<AccessibilityNodeInfo> nodeInfoList = new ArrayList<>();
        if (nodeInfo != null) {

            if (!isEmptyCollection(ids)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    for (String id : ids) {
                        if (!TextUtils.isEmpty(id)) {
                            nodeInfoList.addAll(filterResult(nodeInfo.findAccessibilityNodeInfosByViewId(id)));
                        }
                    }
                }
            }

            if (isEmptyCollection(nodeInfoList) && !isEmptyCollection(texts)) {
                for (String text : texts) {
                    if (!TextUtils.isEmpty(text)) {
                        nodeInfoList.addAll(filterResult(nodeInfo.findAccessibilityNodeInfosByText(text)));
                    }
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

    List<AccessibilityNodeInfo> filterResult(List<AccessibilityNodeInfo> nodes) {
        if (isEmptyCollection(nodes)) return nodes;
        for (int i = nodes.size() - 1; i >= 0; i--) {
            AccessibilityNodeInfo node = nodes.get(i);
            if (!isAvailableNode(node)) nodes.remove(node);
        }
        return nodes;
    }

    private boolean isAvailableNode(AccessibilityNodeInfo node) {
        if (!isEmptyCollection(className) && !className.contains(node.getClassName())) {
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
