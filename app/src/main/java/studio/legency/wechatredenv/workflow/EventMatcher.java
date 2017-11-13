package studio.legency.wechatredenv.workflow;

import android.view.accessibility.AccessibilityEvent;

/**
 * Created by lichen:) on 2016/9/27.
 */

public abstract  class EventMatcher {

    protected int eventType;

    protected AccessibilityEvent event;

    private String clazz = "";

    public EventMatcher(AccessibilityEvent event) {
        this.event = event;
    }

    protected boolean typeMatch() {
        return (event.getEventType() & eventType) != 0;
    }

    boolean classMatch() {
        return clazz.equals(event.getClassName());
    }

    boolean nodeMatch(){
        return false;
    }
    public abstract boolean match();
    public abstract boolean go();
}
