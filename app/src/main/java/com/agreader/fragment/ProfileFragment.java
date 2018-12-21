package com.agreader.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agreader.EditProfileActivity;
import com.agreader.R;
import com.agreader.User;
import com.agreader.screen.Dashboard;
import com.agreader.screen.LoginScreen;
import com.agreader.screen.SliderActivity;
import com.agreader.screen.TermEndService;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Button logout;
    private View v;

    TextView editProfile,name,email,phone,iconTerm,iconPrivacy;
    ImageView profilePicture;

    RelativeLayout pmenu1,pmenu2,pmenu3,pmenu4,pmenu5;

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
        name = v.findViewById(R.id.textFname);
        email = v.findViewById(R.id.textFEmail);
        phone = v.findViewById(R.id.textFPhone);
        pmenu1 = v.findViewById(R.id.pmenu1);
        pmenu2 = v.findViewById(R.id.pmenu2);
        pmenu3 = v.findViewById(R.id.pmenu3);
        pmenu4 = v.findViewById(R.id.pmenu4);
        pmenu5 = v.findViewById(R.id.pmenu5);


        profilePicture = v.findViewById(R.id.imageProfile);


        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference dbf = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());
        dbf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User us = dataSnapshot.getValue(User.class);
                if (us.getName().isEmpty()){
                    name.setText("NAME");
                }else {
                    name.setText(us.getName());
                }

                if (us.getEmail().isEmpty()){
                    email.setText("EMAIL");
                }else {
                    email.setText(us.getEmail());
                }

                if (us.getNumberPhone().isEmpty()){
                    phone.setText("PHONE");
                }else {
                    phone.setText(us.getNumberPhone());
                }
                Picasso.get().load(us.getGambar()).transform(new RoundedCornersTransformation(100,10)).fit().centerInside().into(profilePicture);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        pmenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Tahap Pengembangan", Toast.LENGTH_SHORT).show();
            }
        });


        pmenu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TermEndService.class);
                intent.putExtra("EXTRA_SESSION_ID", "https://www.authenticguards.com/term/");
                startActivity(intent);
            }
        });

        pmenu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TermEndService.class);
                intent.putExtra("EXTRA_SESSION_ID", "https://www.authenticguards.com/privacy-policy/");
                startActivity(intent);
            }
        });

        pmenu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = "com.agreader";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        pmenu5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Tahap Pengembangan", Toast.LENGTH_SHORT).show();
            }
        });

        logout = (Button)v.findViewById(R.id.logoutBtn);

        editProfile = (TextView)v.findViewById(R.id.textEditProfile);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
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
                FirebaseAuth.getInstance().signOut();
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

