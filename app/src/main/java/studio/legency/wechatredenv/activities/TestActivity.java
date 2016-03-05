package studio.legency.wechatredenv.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import studio.legency.wechatredenv.R;
import studio.legency.wechatredenv.data.NodeInfo;
import studio.legency.wechatredenv.services.NodeInfoWindow;

/**
 * Created by lichen:) on 2016/2/4.
 */
@EActivity(R.layout.activity_test)
public class TestActivity extends Activity {

    @ViewById
    RelativeLayout container;

    @Extra
    NodeInfo nodeInfo;

    @Bean
    NodeInfoWindow nodeInfoWindow;

    @AfterViews
    void init() {
        TreeNode treeNode = TreeNode.root();
        treeNode.addChild(getNode(nodeInfo, 0));
        AndroidTreeView tView = new AndroidTreeView(this, treeNode);
        container.addView(tView.getView());
        nodeInfoWindow.showWindow(nodeInfo);
    }

    TreeNode getNode(NodeInfo nodeInfo, int level) {
        TreeNode node = new TreeNode(nodeInfo).setViewHolder(new MyHolder(this, level));
        int count = nodeInfo.getChildCount();
        for (int i = 0; i < count; i++) {
            NodeInfo n = nodeInfo.getChild(i);
            if (n != null) {
                node.addChild(getNode(n, level + 1));
            }
        }
        return node;
    }

    public class MyHolder extends TreeNode.BaseNodeViewHolder<NodeInfo> {

        private final int level;

        public MyHolder(Context context, int level) {
            super(context);
            this.level = level;
        }

        @Override
        public View createNodeView(TreeNode node, final NodeInfo value) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.node_info, null, false);
            View content = view.findViewById(R.id.content);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) content.getLayoutParams();
            lp.leftMargin = level * 40;
            TextView id = (TextView) view.findViewById(R.id.id);
            TextView text = (TextView) view.findViewById(R.id.text);
            TextView count = (TextView) view.findViewById(R.id.count);
            view.findViewById(R.id.find).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    value.highLight(TestActivity.this);
                }
            });
            if (value.getChildCount() == 0) {
                count.setVisibility(View.INVISIBLE);
            } else {
                count.setVisibility(View.VISIBLE);
                count.setText(String.valueOf(value.getChildCount()));
            }
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
