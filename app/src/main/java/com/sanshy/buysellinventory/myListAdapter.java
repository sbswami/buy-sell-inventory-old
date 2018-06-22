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

public class myListAdapter extends ArrayAdapter<String> {
    Activity context;
    String[] productName;
    String[] quantity;
    String[] buyPrice;
    String[] sellPrice;

    public myListAdapter(Activity context, String[] productName, String[] quantity, String[] buyPrice, String[] sellPrice) {
        super(context,R.layout.stock_list,productName);
        this.context = context;
        this.productName = productName;
        this.quantity = quantity;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.stock_list,null,true);
        TextView pName = (TextView) rowView.findViewById(R.id.productName);
        TextView q = rowView.findViewById(R.id.quantity);
        TextView bprice = rowView.findViewById(R.id.buyPrice);
        TextView sprice = rowView.findViewById(R.id.sellPrice);

        pName.setText(productName[position]);
        q.setText(quantity[position]);
        bprice.setText(buyPrice[position]);
        sprice.setText(sellPrice[position]);

        return rowView;

    }
}
