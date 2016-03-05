package studio.legency.wechatredenv.data;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import studio.legency.wechatredenv.R;

/**
 * Created by lichen:) on 2016/2/4.
 */
public class NodeInfo implements Serializable {

    public String text;

    public String id;

    public String clazz;

    public int left;

    public int top;

    public int width;

    public int height;

    private View view;


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

    public void setView(View view) {
        this.view = view;
    }


    public View getView() {
        return view;
    }

    public void highLight(Context context) {
        if (view != null) {
            view.setBackgroundColor(Color.argb(255, 0, 0, 0));
            AnimationSet shake = (AnimationSet) AnimationUtils.loadAnimation(
                    context, R.anim.shake);
            view.setAnimation(shake);
            shake.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setBackgroundColor(Color.argb(55, 100, 100, 200));
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.animate();

        }
    }
}
