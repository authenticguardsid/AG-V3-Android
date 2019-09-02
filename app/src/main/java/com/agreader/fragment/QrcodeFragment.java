package com.agreader.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.agreader.base.BaseFragment;
import com.agreader.screen.LoginScreenActivity;
import com.agreader.screen.QRCodeBaruActivity;
import com.agreader.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class QrcodeFragment extends BaseFragment {

    private Button button;

    final int REQUEST_CODE_CAMERA = 999;

    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;

    public QrcodeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qrcode, container, false);

        setUpView(view);
        generateView(view);

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_CAMERA){
            if(grantResults.length <0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getContext(), "You don't have permission to access camera!", Toast.LENGTH_SHORT).show();
            }else {
                mFirebaseAuth =FirebaseAuth.getInstance();
                mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser != null){
                    Intent intent = new Intent(getActivity(), QRCodeBaruActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), LoginScreenActivity.class);
                    startActivity(intent);
                }
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void setUpView(View view) {
        button = view.findViewById(R.id.button_scan_QRCode);

        mFirebaseAuth= FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

    }

    @Override
    public void generateView(View view) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
                }else {
                    if(mFirebaseUser != null){
                        Intent intent = new Intent(getActivity(), QRCodeBaruActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getActivity(), LoginScreenActivity.class);
                        startActivity(intent);
                    }
                }

            }
        });

    }

    @Override
    public void setupListener(View view) {

    }
}