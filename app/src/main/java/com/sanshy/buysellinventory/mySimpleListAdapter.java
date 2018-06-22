package com.sanshy.buysellinventory;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by sbswami on 3/4/2018.
 */

public class mySimpleListAdapter extends ArrayAdapter<String> {
    Activity context;
    String[] productName;

    public mySimpleListAdapter(Activity context, String[] productName) {
        super(context,R.layout.my_simple_list,productName);
        this.context = context;
        this.productName = productName;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.my_simple_list,null,true);
        TextView pName = (TextView) rowView.findViewById(R.id.textView);

        pName.setText(productName[position]);

        return rowView;

    }
}
