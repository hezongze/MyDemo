package com.hezongze.privatedemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.hezongze.privatedemo.adapter.MyListAdapter;
import com.hezongze.privatedemo.memory.MemoryActivity;
import com.hezongze.privatedemo.rxJavaAndRetrofit.RetrofitView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {



    private List<String> myTitleList;
    private ListView myListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
               setContentView(R.layout.activity_main);

        initData();

        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        myTitleList = new ArrayList<>();
        //https://www.jianshu.com/p/1fb294ec7e3b  参考简书完成编写
        myTitleList.add("RxJava + Retrofit");
        myTitleList.add("Memory(内存相关)");


    }

    /**
     * 初始化控件
     */
    private void initView() {

        myListView = (ListView) findViewById(R.id.myListView);
        myListView.setAdapter(new MyListAdapter(this,myTitleList));

        myListView.setOnItemClickListener(

                new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Log.i("111","position--->"+position);
                intent.setClass(MainActivity.this, MemoryActivity.class);
                switch (position){
                    case 0:
                        intent.setClass(MainActivity.this, RetrofitView.class);
                        break;
                    case 1:
                        intent.setClass(MainActivity.this, MemoryActivity.class);
                        break;
                }
                startActivity(intent);
            }
        });
    }
}
