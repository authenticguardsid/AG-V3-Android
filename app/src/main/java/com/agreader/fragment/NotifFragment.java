package com.agreader.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.agreader.R;
import com.agreader.adapter.NotifAdapter;
import com.agreader.model.Notif;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotifFragment extends Fragment {

    private RecyclerView mList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Notif> notifList;
    private RecyclerView.Adapter adapter;

    public NotifFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notif, container, false);
        changeStatusBarColor();

        mList = view.findViewById(R.id.recycler_notif);

        notifList = new ArrayList<>();
        adapter = new NotifAdapter(getApplicationContext(),notifList);

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        getData();

        return view;
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimary));
        }
    }

    private void getData(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://admin.authenticguards.com/api/notification/?appid=003", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response.length() > 0){
                    try {
                        JSONObject jsonObject = response.getJSONObject("result");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int j = 0; j < jsonArray.length() ; j++) {
                            JSONObject data = jsonArray.getJSONObject(j);

                            Notif notif = new Notif();
                            notif.setId(data.getString("id"));
                            notif.setTitle(data.getString("title"));
                            notif.setMessage(data.getString("message"));
                            notif.setImage("http://admin.authenticguards.com/storage/app/public/notif/"+data.getString("image")+".jpg");
                            notif.setDate(data.getString("created_at"));

                            notifList.add(notif);

                            Log.e("lol", "asdasdasd : " + data.getString("title") + data.getString("message") + data.getString("image") + data.getString("created_at"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }

}
