package com.example.m.laylak.Activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.m.laylak.R;

public class DetailOffer extends AppCompatActivity {
TextView title,desc,price;
ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_offer);

        title=findViewById(R.id.detail_title);
        desc=findViewById(R.id.detail_desc);
        price=findViewById(R.id.detail_pric);

        img=findViewById(R.id.detail_img);

        Intent u = getIntent();
       String titlee = u.getStringExtra("title");
        String desce = u.getStringExtra("detail");
        String pricee = u.getStringExtra("price");

        int imgg =u.getIntExtra("img",0);

        title.setText(titlee);
        desc.setText(desce);
        price.setText(pricee);

        img.setImageResource(imgg);

    }
}