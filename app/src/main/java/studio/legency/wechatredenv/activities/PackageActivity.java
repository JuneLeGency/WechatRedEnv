package studio.legency.wechatredenv.activities;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import studio.legency.wechatredenv.R;

/**
 * Created by lichen:) on 2016/2/4.
 */
@EActivity(R.layout.activity_package)
public class PackageActivity extends Activity {

    @ViewById
    ListView list;

    @AfterViews
    void init() {
        List<PackageInfo> p = getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES);

    }
}

@EBean
class AppAdapter extends BaseAdapter {

    List<PackageInfo> packageInfos = new ArrayList<>();

    public void setPackageInfos(List<PackageInfo> packageInfos) {
        this.packageInfos = packageInfos;
    }

    @Override
    public int getCount() {
        return packageInfos.size();
    }

    @Override
    public PackageInfo getItem(int position) {
        return packageInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PackageInfo packageInfo = getItem(position);
        return null;
    }
}