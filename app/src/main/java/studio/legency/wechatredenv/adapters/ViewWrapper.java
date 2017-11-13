package studio.legency.wechatredenv.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lichen:) on 2016/9/23.
 */

public class ViewWrapper<V extends View> extends RecyclerView.ViewHolder {

    private V view;

    public ViewWrapper(V itemView) {
        super(itemView);
        view = itemView;
    }

    public V getView() {
        return view;
    }
}