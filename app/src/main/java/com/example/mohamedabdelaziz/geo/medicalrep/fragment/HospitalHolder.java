package com.example.mohamedabdelaziz.geo.medicalrep.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mohamedabdelaziz.geo.R;

/**
 * Created by Mohamed Abd Elaziz on 3/8/2018.
 */

public class HospitalHolder extends RecyclerView.ViewHolder {

    TextView id, content;
    TextView textView_close_open;

    public HospitalHolder(final View itemView) {
        super(itemView);
        id = itemView.findViewById(R.id.id);
        content = itemView.findViewById(R.id.content);
        textView_close_open = itemView.findViewById(R.id.close_open);
    }
}
