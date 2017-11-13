package studio.legency.wechatredenv.activities;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import studio.legency.wechatredenv.R;
import studio.legency.wechatredenv.adapters.RecyclerViewAdapterBase;

/**
 * @author
 */
@EActivity(R.layout.activity_package)
public class PackageActivity extends Activity {

    @ViewById
    RecyclerView recyclerView;

    @Bean
    AppAdapter mAdapter;

    @AfterViews
    void init() {
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @AfterInject
    @Background
    void getPackage() {
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA);
        mAdapter.setItems(packages);
        changes();
    }

    @UiThread
    void changes() {
        mAdapter.notifyDataSetChanged();
    }
}

@EBean
class AppAdapter extends RecyclerViewAdapterBase<PackageInfo, AppView> {

    @RootContext
    Context context;

    @Override
    protected AppView onCreateItemView(ViewGroup parent, int viewType) {
        return AppView_.build(context);
    }

    @Override
    protected void bindData(AppView view, PackageInfo packageInfo) {
        view.bind(packageInfo);
    }

}

@EViewGroup(R.layout.item_app)
class AppView extends RelativeLayout {

    @ViewById
    ImageView app_icon;

    @ViewById
    TextView app_name;

    @ViewById
    TextView app_package;

    public AppView(Context context) {
        super(context);
    }

    public AppView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void bind(PackageInfo packageInfo) {
        app_icon.setImageDrawable(getContext().getPackageManager().getApplicationIcon(packageInfo.applicationInfo));
        app_name.setText(getContext().getPackageManager().getApplicationLabel(packageInfo.applicationInfo));
        app_package.setText(packageInfo.packageName);
    }
}
