package studio.legency.wechatredenv.workflow.wechat;

import android.view.accessibility.AccessibilityEvent;

import studio.legency.wechatredenv.workflow.WorkFlow;

/**
 *
 *  监听通知-> 发现红包->点击通知
 *  进入聊天页-> 寻找红包->点击红包
 *  进入红包领取页->点击领取
 *  看到详情页-> 点击返回 （点击HOME）
 * Created by lichen:) on 2016/9/26.
 */
public class WeChatWorkFlow implements WorkFlow {

    @Override
    public String getPackageName() {
        return "com.tencent.mm";
    }

    @Override
    public void handleEvent(AccessibilityEvent event) {
        getMatchedProcessor(event);
    }

    private void getMatchedProcessor(AccessibilityEvent event) {
        
    }
}
