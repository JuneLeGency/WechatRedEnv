package studio.legency.wechatredenv.configs;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by Administrator on 2015/9/30.
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface Setting {

    @DefaultBoolean(true)
    boolean use_His();

    @DefaultBoolean(false)
    boolean testMode();
}
