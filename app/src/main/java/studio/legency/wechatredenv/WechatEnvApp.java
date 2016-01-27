package studio.legency.wechatredenv;

import android.util.Log;
import android.widget.Toast;

import com.bugtags.library.Bugtags;
import com.orm.SugarApp;

import org.androidannotations.annotations.EApplication;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;
import io.paperdb.Paper;

/**
 * Created by Administrator on 2015/9/30.
 */
@EApplication
public class WechatEnvApp extends SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
        FIR.init(this);
        Bugtags.start("bbe1e5f34900d32dcd17678d91682d97", this, Bugtags.BTGInvocationEventBubble);
        FIR.checkForUpdateInFIR("acc0748415b87fa329d4c149f3a01a04", new VersionCheckCallback() {
            @Override
            public void onSuccess(String versionJson) {
                Log.i("fir", "check from fir.im success! " + "\n" + versionJson);
            }

            @Override
            public void onFail(Exception exception) {
                Log.i("fir", "check fir.im fail! " + "\n" + exception.getMessage());
            }

            @Override
            public void onStart() {
                Toast.makeText(getApplicationContext(), "正在获取", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "获取完成", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
