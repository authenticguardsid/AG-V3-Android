package com.agreader.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agreader.R;
import com.agreader.screen.Dashboard;
import com.agreader.screen.LoginScreen;
import com.agreader.screen.SliderActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Button logout;
    private View v;
    private TextView editProfile;
    private RelativeLayout connectFacebook;

    private GoogleApiClient googleApiClient;
    private GoogleApiClient mGoogleSignInClient;
    private FirebaseAuth mAuth;


    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        logout = (Button)v.findViewById(R.id.logout);
        editProfile = (TextView)v.findViewById(R.id.editProfile);
        connectFacebook = (RelativeLayout)v.findViewById(R.id.connectFacebook);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Masih Dalam Tahap Pengembangan", Toast.LENGTH_SHORT).show();
            }
        });

        connectFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Masih Dalam Tahap Pengembangan", Toast.LENGTH_SHORT).show();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        if (mGoogleSignInClient == null){
            mGoogleSignInClient = new GoogleApiClient.Builder(getContext())
                    .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            Toast.makeText(getActivity(), "Something Error", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                getActivity().finish();
                if (mGoogleSignInClient != null){
                    Auth.GoogleSignInApi.signOut(mGoogleSignInClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {

                        }
                    });
                }
                startActivity(new Intent(getActivity(), LoginScreen.class));
            }
        });
        return v;
    }



}
