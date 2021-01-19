package com.example.campusmarket.cart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.campusmarket.R;

public class CheckOutActivity extends AppCompatActivity {

    private String sellers = "";
    private String itemNames = "";
    private String total;
    private TextView tvSellers,tvItemNames, tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        Intent intent = getIntent();
        String sellers = intent.getStringExtra("sellers");
        String itemNames = intent.getStringExtra("itemNames");
        String total= intent.getStringExtra("total");
        if (sellers != null)
        {
            this.sellers = sellers;
        }
        if (itemNames != null)
        {
            this.itemNames = itemNames;
        }
        if(total!=null)
        {
            this.total = total;
        }

        tvTotal = findViewById(R.id.tvTotalCheckout);
        tvSellers = findViewById(R.id.tvSellersCheckout);
        tvItemNames = findViewById(R.id.tvItemNamesCheckout);

        displayCheckOutInfo();

    }

    /**
     * Displays the items names and corresponding seller names that the user checked out.
     */
    private void displayCheckOutInfo() {
        tvSellers.setText(sellers);
        tvItemNames.setText(itemNames);
        String moneyTotal = "$"+total;
        tvTotal.setText(moneyTotal);
    }
}
