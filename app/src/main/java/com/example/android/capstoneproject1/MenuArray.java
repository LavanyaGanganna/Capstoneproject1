package com.example.android.capstoneproject1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lavanya on 11/12/16.
 */

public class MenuArray extends ArrayAdapter {

    public MenuArray(Context context, ArrayList<MenuList> menuLists) {
        super(context, 0, menuLists);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuList menuList = (MenuList) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_array_item, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        TextView textView = (TextView) convertView.findViewById(R.id.imagetext);
        boolean tabletsize = getContext().getResources().getBoolean(R.bool.isTablet);
        if (tabletsize) {
            ViewGroup.LayoutParams params = convertView.getLayoutParams();
            params.height = 180;
            convertView.setLayoutParams(params);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);

        }
        imageView.setImageResource(menuList.getIcons());
        textView.setText(menuList.getItem());

        return convertView;
    }
}
