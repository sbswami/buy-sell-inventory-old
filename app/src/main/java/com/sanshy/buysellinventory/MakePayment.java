package com.sanshy.buysellinventory;

import android.app.Activity;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class MakePayment implements PaymentResultListener {

    public static Activity mActivity;

    public static void makePayment(Activity activity){
        mActivity = activity;
        Checkout.preload(activity.getApplicationContext());
        Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", "100");

            JSONObject preFill = new JSONObject();
            preFill.put("email", "test@razorpay.com");
            preFill.put("contact", "9876543210");

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }
    @Override
    public void onPaymentSuccess(String s) {
        MyDialogBox.ShowDialog(mActivity,"Work Done");
    }

    @Override
    public void onPaymentError(int i, String s) {
        MyDialogBox.ShowDialog(mActivity,"Failed To Done");
    }
}
