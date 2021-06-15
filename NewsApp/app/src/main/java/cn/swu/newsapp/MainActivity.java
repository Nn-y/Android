package cn.swu.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecycler;
    private ItemAdapter mAdapter;
    public static LayoutInflater mLayoutInflater;

    private String mJsonString;
    private List<Item> mRows;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {

        getNews();

    }

    private void getNews() {
        String url = "http://c.m.163.com/nc/article/headline/T1348647853363/0-40.html";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = okHttpClient.newCall(request);

        new Thread(new Runnable() {//创建线程
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    mJsonString = response.body().string();
                    //传递消息
                    Message message = new Message();
                    message.what = 0;
                    message.obj = mJsonString;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private final Handler handler = new Handler(new Handler.Callback() {
        @SuppressLint("HandlerLeak")
        @Override
        public boolean handleMessage(Message msg) {
            int flag1 = msg.what;
            mJsonString = (String) msg.obj;
            ItemArr itemObj = JSON.parseObject(mJsonString, ItemArr.class);
            mRows = itemObj.getT1348647853363();
            mRecycler = findViewById(R.id.recycler_view);

            mAdapter = new ItemAdapter(MainActivity.this, mRows);
            mLayoutInflater = getLayoutInflater();

            mRecycler.setAdapter(mAdapter);
            LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);

            mRecycler.setLayoutManager(manager);

            mAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View itemView, int position) {
                    Intent intent = new Intent(MainActivity.this, newspage.class);
                    intent.putExtra("url", mRows.get(position).getUrl());
                    MainActivity.this.startActivity(intent);
                }
            });
            return false;
            }
        });


    }