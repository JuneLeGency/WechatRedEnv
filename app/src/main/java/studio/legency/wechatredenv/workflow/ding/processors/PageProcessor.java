package studio.legency.wechatredenv.workflow.ding.processors;

import android.view.accessibility.AccessibilityEvent;

import studio.legency.wechatredenv.workflow.EventProcessor;

/**
 * Created by lichen:) on 2016/9/26.
 */

public class PageProcessor extends EventProcessor {

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void process() {
        
    }

    int eventType() {
        return AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED | AccessibilityEvent.TYPE_ANNOUNCEMENT;
    }

}
