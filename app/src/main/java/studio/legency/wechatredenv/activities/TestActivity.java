package studio.legency.wechatredenv.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import studio.legency.wechatredenv.R;
import studio.legency.wechatredenv.data.NodeInfo;

/**
 * Created by lichen:) on 2016/2/4.
 */
@EActivity(R.layout.activity_test)
public class TestActivity extends Activity {

    @ViewById
    RelativeLayout container;

    @Extra
    NodeInfo nodeInfo;

    @AfterViews
    void init() {
        TreeNode treeNode = TreeNode.root();
        treeNode.addChild(getNode(nodeInfo));
        AndroidTreeView tView = new AndroidTreeView(this, treeNode);
        container.addView(tView.getView());
    }

    TreeNode getNode(NodeInfo nodeInfo) {
        TreeNode node = new TreeNode(nodeInfo).setViewHolder(new MyHolder(this));
        int count = nodeInfo.getChildCount();
        for (int i = 0; i < count; i++) {
            NodeInfo n = nodeInfo.getChild(i);
            if (n != null) {
                node.addChild(getNode(n));
            }
        }
        return node;
    }

    public class MyHolder extends TreeNode.BaseNodeViewHolder<NodeInfo> {

        public MyHolder(Context context) {
            super(context);
        }

        @Override
        public View createNodeView(TreeNode node, NodeInfo value) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.node_info, null, false);
            TextView id = (TextView) view.findViewById(R.id.id);
            TextView text = (TextView) view.findViewById(R.id.text);
            TextView classname = (TextView) view.findViewById(R.id.class_name);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                id.setText(value.getId());
            }
            text.setText(value.getText());
            classname.setText(value.getClazz());
            return view;
        }
    }
}
