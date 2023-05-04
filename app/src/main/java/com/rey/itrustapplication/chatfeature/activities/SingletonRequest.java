package com.rey.itrustapplication.chatfeature.activities;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SingletonRequest {

    private static SingletonRequest mInstance;
    private RequestQueue mRequestQueue;
    private final Context mContext;

    private SingletonRequest(Context context) {
        mContext = context.getApplicationContext();
        mRequestQueue = getRequestQueue();
    }

    public static synchronized SingletonRequest getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SingletonRequest(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

}
