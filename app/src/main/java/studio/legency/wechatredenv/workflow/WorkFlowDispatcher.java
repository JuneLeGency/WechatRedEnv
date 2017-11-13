package studio.legency.wechatredenv.workflow;

import java.util.HashMap;

import android.view.accessibility.AccessibilityEvent;
import studio.legency.wechatredenv.workflow.ding.DingWorkFlow;
import studio.legency.wechatredenv.workflow.wechat.WeChatWorkFlow;

/**
 * @author legency
 */

public class WorkFlowDispatcher {

    HashMap<String, WorkFlow> mAppWorkFlows = new HashMap<>();

    public void init() {
        register(new WeChatWorkFlow());
        register(new DingWorkFlow());
    }

    private void register(WorkFlow flow) {
        register(flow, false);
    }

    private void register(WorkFlow flow, boolean replaceIfNeed) {
        if (replaceIfNeed || !mAppWorkFlows.containsKey(flow.getPackageName())) {
            mAppWorkFlows.put(flow.getPackageName(), flow);
        }
    }

    void dispatch(AccessibilityEvent event) {
        String packageName = event.getPackageName().toString();
        WorkFlow workFlow = mAppWorkFlows.get(packageName);
        workFlow.handleEvent(event);
    }
}
