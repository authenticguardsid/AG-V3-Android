package com.agreader.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agreader.R;
import com.agreader.adapter.MyProductAdapter;
import com.agreader.base.BaseFragment;
import com.agreader.model.ProductModel;
import com.agreader.screen.MyProductDetail;
import com.agreader.utils.CustomItemClickListener;
import com.agreader.utils.DataRequest;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.text.Line;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends BaseFragment {

    FirebaseUser firebaseUser;
    private LinearLayout emptyView,emptyViewLogin;
    View rootView;
    private ArrayList<ProductModel> mData;
    private MyProductAdapter mAdapter;
    private ActionMode mActionMode;
    private String token = "", finalImage = "", name_product = "", name_brand = "", date_claim_product = "", image_product = "", status = "";
    private String size, color, material, price, distributor, expiredDate, alamat, imageBrand, finalImage2;
    private RecyclerView recyclerView;

    public ProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_product, container, false);

        setUpView(rootView);
        generateView(rootView);

        return rootView;
    }

    private void getProduct(String tokenlol) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, "https://admin.authenticguards.com/api/myproduct_?token=" + tokenlol + "&appid=003", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() > 0) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("result");
                        Log.d("ciee", "onResponse: " + jsonObject);
                        JSONArray results = (JSONArray) jsonObject.get("data");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject data = results.getJSONObject(i);
                            final String tanggal = data.getString("created_at");
                            date_claim_product = tanggal.substring(0, 10);
                            status = data.getString("statusClaim");
                            JSONObject produk = (JSONObject) data.get("product");
                            name_product = produk.getString("nama");

                            size = produk.getString("size");
                            color = produk.getString("color");
                            material = produk.getString("material");
                            price = produk.getString("price");
                            distributor = produk.getString("distributedOn");
                            expiredDate = produk.getString("expireDate");
                            image_product = produk.getString("image");

                            finalImage = "https://admin.authenticguards.com/product/" + image_product + ".jpg";


                            JSONObject brand = (JSONObject) produk.get("brand");
                            name_brand = brand.getString("Name");
                            alamat = brand.getString("addressOfficeOrStore");
                            imageBrand = brand.getString("image");
                            finalImage2 = "https://admin.authenticguards.com/storage/app/public/" + imageBrand + ".jpg";
                            mData.add(new ProductModel(finalImage, name_product, name_brand, date_claim_product, status, size, color, material, price, distributor, expiredDate, alamat, finalImage2));
                        }
                        if (mData.size() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            emptyView.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }

    @Override
    public void setUpView(View view) {

        mData = new ArrayList<>();

        emptyView = (LinearLayout) view.findViewById(R.id.emptyView);
        emptyViewLogin = (LinearLayout) view.findViewById(R.id.empty_view_login);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = view.findViewById(R.id.listProduct);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void generateView(View view) {
        if(firebaseUser != null ){
            emptyViewLogin.setVisibility(View.GONE);
            try {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                assert firebaseUser != null;
                firebaseUser.getIdToken(true)
                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                            @Override
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                try{
                                    token = Objects.requireNonNull(task.getResult()).getToken();
                                    Log.d("tokenproduct", "onComplete: " + token);
                                    getProduct(token);
                                }catch (NullPointerException ignored){
                                    Toast.makeText(getContext(), "Check your connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }catch (Exception e){
                Toast.makeText(getContext(), "Check your connection", Toast.LENGTH_SHORT).show();
            }
        }
        else{

            emptyViewLogin.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);

        }

        mAdapter = new MyProductAdapter(getContext(), mData, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                final ProductModel productModel = mData.get(position);
                Intent detail_intent = new Intent(getContext(), MyProductDetail.class);
                detail_intent.putExtra("namaProduk", mData.get(position).getNameProduct());
                detail_intent.putExtra("size", productModel.getSize());
                detail_intent.putExtra("color", productModel.getColor());
                detail_intent.putExtra("material", productModel.getMaterial());
                detail_intent.putExtra("price", productModel.getPrice());
                detail_intent.putExtra("distributor", productModel.getDistributor());
                detail_intent.putExtra("expiredDate", productModel.getExpiredDate());
                detail_intent.putExtra("image", mData.get(position).getImageProduct());
                detail_intent.putExtra("nama_brand", mData.get(position).getBrand());
                detail_intent.putExtra("alamat_brand", productModel.getAlamatBrand());
                detail_intent.putExtra("logo_brand", productModel.getLogoBrand());
                startActivity(detail_intent);
            }
        });
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void setupListener(View view) {

    }
}
