package com.hezongze.privatedemo;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.hezongze.privatedemo.adapter.MyListAdapter;

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
    }

    /**
     * 初始化控件
     */
    private void initView() {

        myListView = (ListView) findViewById(R.id.myListView);
        myListView.setAdapter(new MyListAdapter(this,myTitleList));
    }
}
