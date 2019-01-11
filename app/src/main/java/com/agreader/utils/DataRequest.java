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
    public static JSONObject result;
    public static String resultJSON;
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

    public static void setResultJSON(Context context, String json) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(RESULT_JSON, json);
        editor.apply();
    }

    public static String getResultJSON(Context context) {
        return getSharedPreference(context).getString(RESULT_JSON, resultJSON);
    }

    public static void getData(final Context context, String link, String token) {
        Log.d("lol", "getData: masuk 0" + link + token);
        Log.d("lol", "getData: masuk 1");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://admin.authenticguards.com/api/" + link + "?token=" + token + "&appid=003&loclang=a&loclong=a", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("lol", "getData: masuk 2");
                if (response.length() > 0) {
                    Log.d("lol", "getData: masuk 3");
                    resultJSON = response.toString();
                    setResultJSON(context, resultJSON);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(context).add(jsonObjectRequest);

    }
}

