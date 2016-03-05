package studio.legency.wechatredenv.services;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import studio.legency.wechatredenv.data.NodeInfo;

/**
 * Created by lichen:) on 2016/3/5.
 */
@EBean
public class NodeInfoWindow {

    @RootContext
    Context context;

    private RelativeLayout relativeLayout;

    public void showWindow(NodeInfo nodeInfo) {
        showWindowInner(nodeInfo);
    }

    public void showWindow(AccessibilityNodeInfo nodeInfo) {
        showWindowInner(nodeInfo);
    }

    public void showWindowInner(Object nodeInfo) {
        if (nodeInfo == null) return;
        int type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        type = WindowManager.LayoutParams.TYPE_TOAST;
//                    } else {
//                        type = WindowManager.LayoutParams.TYPE_PHONE;
//                    }
//                    Log.d("ttttt", type + "asd");
        if (relativeLayout == null) {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                    type
                    ,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, PixelFormat.TRANSLUCENT);
            params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            relativeLayout = new RelativeLayout(context);
            relativeLayout.setBackgroundColor(Color.argb(55, 255, 255, 255));
            wm.addView(relativeLayout, params);
        } else {
            relativeLayout.removeAllViews();
        }
        if (nodeInfo instanceof AccessibilityNodeInfo) {
            addViewToLayout((AccessibilityNodeInfo) nodeInfo);
        }
        if (nodeInfo instanceof NodeInfo) {
            addViewToLayout((NodeInfo) nodeInfo);
        }
    }

    public void addViewToLayout(AccessibilityNodeInfo info) {
        if (info == null) return;
        addView(info, relativeLayout);
        if (info.getChildCount() != 0) {
            for (int i = 0; i < info.getChildCount(); i++) {
                if (info.getChild(i) != null) {
                    addViewToLayout(info.getChild(i));
                }
            }
        }
    }


    private void addView(AccessibilityNodeInfo info, RelativeLayout relativeLayout) {
        Rect rect = new Rect();
        info.getBoundsInScreen(rect);
        int w = rect.right - rect.left;
        int h = rect.bottom - rect.top;
        View view = new View(context);
        relativeLayout.addView(view);
        RelativeLayout.LayoutParams l = (RelativeLayout.LayoutParams) view.getLayoutParams();
        l.leftMargin = rect.left;
        l.topMargin = rect.top;
        l.width = w;
        l.height = h;
        view.setBackgroundColor(Color.argb(55, 100, 100, 200));
    }

    public void addViewToLayout(NodeInfo info) {
        if (info == null) return;
        addView(info, relativeLayout);
        if (info.getChildCount() != 0) {
            for (int i = 0; i < info.getChildCount(); i++) {
                if (info.getChild(i) != null) {
                    addViewToLayout(info.getChild(i));
                }
            }
        }
    }

    private void addView(NodeInfo info, RelativeLayout relativeLayout) {
        View view = new View(context);
        relativeLayout.addView(view);
        RelativeLayout.LayoutParams l = (RelativeLayout.LayoutParams) view.getLayoutParams();
        l.leftMargin = info.left;
        l.topMargin = info.top;
        l.width = info.width;
        l.height = info.height;
        info.setView(view);
        view.setBackgroundColor(Color.argb(55, 100, 100, 200));
    }

}
