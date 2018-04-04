package com.example.m.laylak.Activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m.laylak.ApiServices.Api;
import com.example.m.laylak.ApiServices.Services;
import com.example.m.laylak.Models.ResponsModel;
import com.example.m.laylak.R;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private EditText userName,password;
    private TextView register;
    private Button login;
    private ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();





    }

    private void initView() {


        login=findViewById(R.id.login);
        register = findViewById(R.id.register);
        userName = findViewById(R.id.user_name);
        password = findViewById(R.id.pass);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent=new Intent(LoginActivity.this,AlbumsActivity.class);
                startActivity(intent);*/
                SignIn();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,Signup.class);
                startActivity(intent);
            }
        });
        ProgressBar progressBar = new ProgressBar(this);
        Drawable drawable = progressBar.getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Login...");
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setIndeterminateDrawable(drawable);

    }

    private void SignIn() {
        String uname = userName.getText().toString();
        String upass = password.getText().toString();

        if (TextUtils.isEmpty(uname))
        {
            userName.setError("Enter user name");
        }else if (TextUtils.isEmpty(upass))
        {
            userName.setError(null);
            password.setError("Enter password");
        }else
            {
                password.setError(null);
                dialog.show();
                Map<String,String> map = new HashMap<>();
                map.put("user_name",uname);
                map.put("user_pass",upass);
                Retrofit retrofit = Api.getClient();
                Services services = retrofit.create(Services.class);
                Call<ResponsModel> call = services.Login(map);
                call.enqueue(new Callback<ResponsModel>() {
                    @Override
                    public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess()==1)
                            {
                                Intent intent = new Intent(LoginActivity.this,AlbumsActivity.class);
                                intent.putExtra("user_id",response.body().getUser_id());
                                dialog.dismiss();
                                startActivity(intent);
                                finish();
                            }else
                                {
                                    Toast.makeText(LoginActivity.this, "Check username or password", Toast.LENGTH_SHORT).show();
                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsModel> call, Throwable t) {

                    }
                });
            }
    }
}
