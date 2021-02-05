package com.cairocartt.Notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LifecycleRegistry;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    String title,message,productName,productId,banner_url,notificationType,brandId,brandName,categoryId,categoryName;
    MyNotification mNotificationManager;
    Intent intent;
    Context context;
    SharedPreferences DataSaver ;
    private LifecycleRegistry lifecycleRegistry;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        context=getApplicationContext();
        DataSaver= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        EventBus.getDefault().postSticky(new MessageEvent("notify"));
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
//            getNewNotification();
            try {
                Map<String, String> params = remoteMessage.getData();
                JSONObject json = new JSONObject(params);
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            String status = NetworkUtil.getConnectivityStatusString(context);
            Log.e("Receiver ", "" + status);
            if (status.equals("Not connected to Internet")) {
                Log.e("Receiver ", "not connction");// your code when internet lost
            } else {
                Log.e("Receiver ", "connected to internet");//your code when internet connection come back
            }
        }
    }

    private void sendPushNotification(JSONObject json) {
        mNotificationManager = new MyNotification(getApplicationContext());
        intent = new Intent(getApplicationContext(), BottomNavigateFragment.class);


        Log.e(TAG, "Notification JSON " + json.toString());
        try {
//            JSONObject data = json.getJSONObject("data");

            title = json.getString("title");
            message = json.getString("message");
            banner_url = json.getString("banner_url");
            notificationType = json.getString("notificationType");
            intent.putExtra("notificationType",notificationType);

            if(notificationType.equals("product")){
                productId = json.getString("productId");
                productName = json.getString("productName");
                intent.putExtra("id",productId);
                intent.putExtra("name",productName);

            }
            else if (notificationType.equals("category")){
                categoryId = json.getString("categoryId");
                categoryName = json.getString("categoryName");
                intent.putExtra("id",categoryId);
                intent.putExtra("name",categoryName);

            }
            else if (notificationType.equals("brand")){
                brandId = json.getString("brandId");
                brandName = json.getString("brandName");
                intent.putExtra("id",brandId);
                intent.putExtra("name",brandName);

            }

//            if(address.equals("null")) {
            mNotificationManager.getBitmapAsyncAndDoWork(title, message, intent,banner_url);

//            }

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }





}
