package studio.legency.wechatredenv.data;

import com.orm.SugarRecord;

/**
 * Created by Administrator on 2015/9/30.
 */
public class WechatRedEnvHis extends SugarRecord<WechatRedEnvHis> {

    String hash;


    public WechatRedEnvHis() {

    }

    public WechatRedEnvHis(String hash) {
        this.hash = hash;
    }


}
