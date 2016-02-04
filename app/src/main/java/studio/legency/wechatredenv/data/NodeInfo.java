package studio.legency.wechatredenv.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lichen:) on 2016/2/4.
 */
public class NodeInfo implements Serializable {

    String text;

    String id;

    String clazz;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    List<NodeInfo> nodeInfos = new ArrayList<>();

    public List<NodeInfo> getNodeInfos() {
        return nodeInfos;
    }

    public void setNodeInfos(List<NodeInfo> nodeInfos) {
        this.nodeInfos = nodeInfos;
    }

    public void addChild(NodeInfo node) {
        nodeInfos.add(node);
    }

    public int getChildCount() {
        return nodeInfos.size();
    }

    public NodeInfo getChild(int i) {
        return nodeInfos.get(i);
    }
}
