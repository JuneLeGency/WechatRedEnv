package studio.legency.wechatredenv.helpers;

import android.os.Build;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

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
        List<AccessibilityNodeInfo> nodeInfoList = null;
        if (nodeInfo != null) {

            if (!TextUtils.isEmpty(id)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    nodeInfoList = nodeInfo.findAccessibilityNodeInfosByViewId(id);
                }
                filter(nodeInfoList);
            }

            if (isEmptyCollection(nodeInfoList)) {
                if (!TextUtils.isEmpty(text)) {
                    nodeInfoList = nodeInfo.findAccessibilityNodeInfosByText(text);
                    filter(nodeInfoList);
                }
            }
        }
        return nodeInfoList;
    }

    public AccessibilityNodeInfo findFirst() {
        List<AccessibilityNodeInfo> nodeInfoList = find();
        if (isEmptyCollection(nodeInfoList)) return null;
        else return
                nodeInfoList.get(0);
    }

    void filter(List<AccessibilityNodeInfo> nodes) {
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
}
