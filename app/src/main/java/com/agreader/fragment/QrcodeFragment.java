package com.agreader.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.agreader.screen.QRcodeActivity;
import com.agreader.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class QrcodeFragment extends Fragment {

    private Button button;

    public QrcodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qrcode, container, false);

        button = view.findViewById(R.id.button_scan_QRCode);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), QRcodeActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
