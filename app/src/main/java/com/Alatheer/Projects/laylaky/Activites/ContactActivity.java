package com.Alatheer.Projects.laylaky.Activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Alatheer.Projects.laylaky.ApiServices.Api;
import com.Alatheer.Projects.laylaky.ApiServices.Services;
import com.Alatheer.Projects.laylaky.Models.ContactModel;
import com.Alatheer.Projects.laylaky.Models.ModelContactUs;
import com.Alatheer.Projects.laylaky.R;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener{
    Shimmer shimmer;
    ShimmerTextView offer_txt;
    EditText name,phone,email,message;
    TextView mail,web,mob,map;
    Button send;

    double lat,lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        initView();
        getdata();

    }

    private void getdata() {

        Services services=Api.getClient().create(Services.class);
        Call<ModelContactUs> call=services.GetContactUs();
        call.enqueue(new Callback<ModelContactUs>() {
            @Override
            public void onResponse(Call<ModelContactUs> call, Response<ModelContactUs> response) {

                mail.setText(response.body().getEmail_info());
                web.setText(response.body().getWeb_info());
                mob.setText(response.body().getTele_info());

                lat=response.body().getLocation_google_lat();
                lang=response.body().getLocation_google_long();


            }

            @Override
            public void onFailure(Call<ModelContactUs> call, Throwable t) {

            }
        });
    }

    private void SendDataToServer() {

        String uname = name.getText().toString();
        String uemail= email.getText().toString();
        String uphone= phone.getText().toString();
        String umessage = message.getText().toString();

        if (TextUtils.isEmpty(uname))
        {
            name.setError("Enter uer name");
        }else if (TextUtils.isEmpty(uemail))
        {
            name.setError(null);
            email.setError("Enter email address");
        }else if (!Patterns.EMAIL_ADDRESS.matcher(uphone).matches())
        {
            name.setError(null);
            email.setError("Invalid email address");

        }else if (TextUtils.isEmpty(uphone))
        {
            email.setError(null);
            phone.setError("Enter phone number");
        }else if (!Patterns.PHONE.matcher(uphone).matches())
        {
            email.setError(null);
            phone.setError("Invalid phone number");

        }else if (TextUtils.isEmpty(umessage))
        {
            phone.setError(null);
            message.setError("Enter your message");
        }else
            {
            message.setError(null);
        Map<String,String> map = new HashMap<>();

        map.put("full_name",name.getText().toString());
        map.put("email",email.getText().toString());
        map.put("phone_number",phone.getText().toString());
        map.put("message",message.getText().toString());

        Services services= Api.getClient().create(Services.class);
        Call<ContactModel> call=services.ContactUs(map);
        call.enqueue(new Callback<ContactModel>() {
            @Override
            public void onResponse(Call<ContactModel> call, Response<ContactModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getSuccess()==1){

                        Toast.makeText(ContactActivity.this, "success", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

            }

            @Override
            public void onFailure(Call<ContactModel> call, Throwable t) {

            }
        });

    }
    }

    private void initView() {

        offer_txt=findViewById(R.id.shimmer);
        shimmer =new Shimmer();
        shimmer.setStartDelay(500);
        shimmer.setDuration(500);
        shimmer.start(offer_txt);
        name=findViewById(R.id.edt_name);
        phone=findViewById(R.id.edt_phone);
        email=findViewById(R.id.edt_mail);
        message=findViewById(R.id.edt_message);
        send=findViewById(R.id.btn_contact);

        mail=findViewById(R.id.email);
        mob=findViewById(R.id.phone);
        web=findViewById(R.id.web);
        map=findViewById(R.id.mapk);

        send.setOnClickListener(this);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ContactActivity.this, "dasdssdsdsss", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ContactActivity.this,MapsActivity.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lang",lang);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_contact:
                SendDataToServer();
                break;

        }

    }
}
