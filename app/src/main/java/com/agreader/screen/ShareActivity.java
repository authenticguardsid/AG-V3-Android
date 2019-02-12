package com.agreader.screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.agreader.R;

public class ShareActivity extends AppCompatActivity {

    TextView shareText;
    Button buttonHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        shareText = (TextView) findViewById(R.id.share);
        buttonHome = (Button) findViewById(R.id.go_home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShareActivity.this, MasterActivity.class);
                startActivity(intent);
            }
        });
        shareText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);

                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.doaApp");
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });
    }

}
