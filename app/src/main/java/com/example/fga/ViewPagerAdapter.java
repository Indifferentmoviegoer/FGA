package com.example.fga;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class ViewPagerAdapter extends PagerAdapter {

    private final Context context;

    private final Integer[] vertical = {
            R.drawable.african,
            R.drawable.aker,
            R.drawable.alexandr,
            R.drawable.sef,
            R.drawable.sies,
            R.drawable.ssiasd,
            R.drawable.narok,
            R.drawable.desert,
            R.drawable.rain,
            R.drawable.sanm
    };

    private final Integer[] horizontal = {
            R.drawable.alberta,
            R.drawable.amer,
            R.drawable.dest,
            R.drawable.hate,
            R.drawable.kenya,
            R.drawable.london,
            R.drawable.newzel,
            R.drawable.pght,
            R.drawable.picrt,
            R.drawable.picrt
    };

    TextView textAdvice;

    private static final String TAG = "myLogs";

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public @NotNull Object instantiateItem(@NotNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.fragment_one, container,
                false);
        textAdvice = view.findViewById(R.id.textAdvice);

        ImageView backgroundImage = view.findViewById(R.id.backgroundImage);

        if (context.getResources().getConfiguration().orientation == 1) {

            Glide.with(this.context)
                    .load(vertical[(int) (Math.random() * 10.0d)])
                    .thumbnail(0.5f)
                    .into(backgroundImage);
        } else if (context.getResources().getConfiguration().orientation == 2) {

            Glide.with(this.context)
                    .load(horizontal[(int) (Math.random() * 10.0d)])
                    .thumbnail(0.5f)
                    .into(backgroundImage);
        }

        ViewPager vp = (ViewPager) container;
        vp.addView(view);

        if (isNetworkOnline(context)) {
            getAdvice();
        } else {
            textAdvice.setText("Нет инета");
        }

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

    public static boolean isNetworkOnline(Context context) {
        if (context == null) {
            return true;
        }
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void getAdvice() {

        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .get()
                .url("http://fucking-great-advice.ru/api/random")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                if (call.request().body() != null) {
                    Log.d(TAG, Objects.requireNonNull(call.request().body()).toString());
                }

                ((Activity) context).runOnUiThread(() -> {
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) {
                ((Activity) context).runOnUiThread(() -> {
                    try {

                        String jsonData = null;
                        if (response.body() != null) {
                            jsonData = response.body().string();
                        }

                        JSONObject Jobject = new JSONObject(Objects.requireNonNull(jsonData));

                        Log.d(TAG, Jobject.getString("text"));

                        textAdvice.setText(Jobject.getString("text"));

                    } catch (IOException | JSONException e) {

                        Log.d(TAG, "Ошибка: " + e);
                    }
                });
            }
        });
    }
}
