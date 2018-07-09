package com.example.mohamedabdelaziz.geo.medicalrep.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mohamedabdelaziz.geo.R;

/**
 * Created by Mohamed Abd Elaziz on 3/8/2018.
 */

public class TVHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView tv_item_detail;

    public TVHolder(final View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.tv_item_title);
        tv_item_detail = itemView.findViewById(R.id.tv_item_detail);

    }
}
