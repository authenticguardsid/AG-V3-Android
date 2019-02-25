package com.agreader.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class DataRequest {
    static final String RESULT_JSON = "resultJSON";
    static final String RESULT_TOKEN = "resultTOKEN";
    public static JSONObject result;
    public static String resultJSON;
    public static String token_new = "";
    static String rvalid;
    static String GCODE;
    String brand;
    String company;
    String address;
    String phone;
    String email;
    String web;

    public static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static void setResultToken(Context context, String json) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(RESULT_JSON, json);
        editor.apply();
    }

    public static String getResultToken(Context context) {
        return getSharedPreference(context).getString(RESULT_JSON, token_new);
    }

    public static void setUser(final Context context, final String token) {
        Log.d("lol", "setUser: " + token);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://admin.authenticguards.com/api/getuser?token=" + token + "&appid=003", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                token_new = token;
                setResultToken(context, token_new);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

}