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

public class statementAdapter extends ArrayAdapter<String> {
    Activity context;
    String[] productName;
    String[] quantity;
    String[] buyPrice;
    String[] sellPrice;
    String[] sellPrice2;
    String[] sellPrice3;

    public statementAdapter(Activity context, String[] productName, String[] quantity, String[] buyPrice, String[] sellPrice, String[] sellPrice2, String[] sellPrice3) {
        super(context,R.layout.stock_list,productName);
        this.context = context;
        this.productName = productName;
        this.quantity = quantity;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.sellPrice2 = sellPrice2;
        this.sellPrice3 = sellPrice3;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.statement_list,null,true);
        TextView pName = (TextView) rowView.findViewById(R.id.productName);
        TextView q = rowView.findViewById(R.id.quantity);
        TextView bprice = rowView.findViewById(R.id.buyPrice);
        TextView sprice = rowView.findViewById(R.id.sellPrice);
        TextView sprice2 = rowView.findViewById(R.id.sellPrice2);
        TextView sprice3 = rowView.findViewById(R.id.sellPrice3);

        pName.setText(productName[position]);
        q.setText(quantity[position]);
        bprice.setText(buyPrice[position]);
        sprice.setText(sellPrice[position]);
        sprice2.setText(sellPrice2[position]);
        sprice3.setText(sellPrice3[position]);

        return rowView;

    }
}
