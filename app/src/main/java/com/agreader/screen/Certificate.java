package com.agreader.screen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.agreader.R;

import org.w3c.dom.Text;

public class Certificate extends AppCompatActivity {

    TextView codeText, nameText, companyText, instanceText, inText, outText, pointText, jobText;
    String code, name, company, in, out, point, instance, job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);

        codeText = (TextView) findViewById(R.id.certificate_code);
        nameText = (TextView) findViewById(R.id.name_intern);
        companyText = (TextView) findViewById(R.id.certificate_instance);
        instanceText = (TextView) findViewById(R.id.name_instance);
        inText = (TextView) findViewById(R.id.time_intern);
        outText = (TextView) findViewById(R.id.out_intern);
        pointText = (TextView) findViewById(R.id.point_intern);
        jobText = (TextView) findViewById(R.id.job_intern);

        code = getIntent().getStringExtra("Code");
        name = getIntent().getStringExtra("Name");
        company = getIntent().getStringExtra("company");
        in = getIntent().getStringExtra("in");
        out = getIntent().getStringExtra("out");
        point = getIntent().getStringExtra("nilai");
        instance = getIntent().getStringExtra("instance");
        job = getIntent().getStringExtra("job");

        codeText.setText(code);
        nameText.setText(name);
        companyText.setText(company);
        instanceText.setText(instance);
        inText.setText(in);
        outText.setText(out);
        pointText.setText(point);
        jobText.setText(job);
        pointText.setText(point);

    }
}
