package com.Alatheer.Projects.lailaky.Activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.Alatheer.Projects.lailaky.R;

public class Confirm_SignUPActivity extends AppCompatActivity {
 private Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm__sign_up);
        initView();

    }

    private void initView() {
        confirm=findViewById(R.id.confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Confirm_SignUPActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
