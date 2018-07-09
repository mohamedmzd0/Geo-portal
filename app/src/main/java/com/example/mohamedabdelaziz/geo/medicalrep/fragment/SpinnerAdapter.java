package com.example.mohamedabdelaziz.geo.medicalrep.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mohamedabdelaziz.geo.GovernatesResponse;
import com.example.mohamedabdelaziz.geo.R;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    List<GovernatesResponse.Governate> governates;
    Context context;

    public SpinnerAdapter(Context context, List<GovernatesResponse.Governate> governates) {
        this.context = context;
        this.governates = governates;
    }

    @Override
    public int getCount() {
        if (governates == null)
            return 0;
        return governates.size();
    }

    @Override
    public Object getItem(int i) {
        return governates.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_spinner_tv, null);
        TextView tv_item_title = view.findViewById(R.id.tv_item_title);
        tv_item_title.setText(governates.get(i).getName());
        return view;
    }
}
