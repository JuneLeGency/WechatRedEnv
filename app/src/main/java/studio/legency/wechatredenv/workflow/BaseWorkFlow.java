package studio.legency.wechatredenv.workflow;

import java.util.HashMap;

import android.view.accessibility.AccessibilityEvent;

/**
 * Created by lichen:) on 2016/9/26.
 */

public abstract class BaseWorkFlow implements WorkFlow {

    HashMap<Integer, EventProcessor> eventProcessors = new HashMap<>();

    public BaseWorkFlow() {
        addProcessors();
    }

    protected abstract void addProcessors();

    protected abstract void getProcessor();

    void addProcessor(EventProcessor eventProcessor) {
    }

    EventProcessor getProcessorOfEvent(AccessibilityEvent event) {
        return null;
    }

    @Override
    public void handleEvent(AccessibilityEvent event) {
        EventProcessor processor = getProcessorOfEvent(event);
        processor.process();
    }
}
