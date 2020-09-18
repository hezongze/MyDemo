package com.hezongze.privatedemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.hezongze.privatedemo.R;

import java.util.ArrayList;
import java.util.List;


public class MyListAdapter extends BaseAdapter {


    private Context context;
    private List<String> myDemoTitleList = new ArrayList<>();

    public MyListAdapter(Context context, List<String> myDemoTitleList) {
        this.context = context;
        this.myDemoTitleList = myDemoTitleList;
    }

    @Override
    public int getCount() {
        return myDemoTitleList != null?myDemoTitleList.size():0;
    }

    @Override
    public Object getItem(int i) {
        return myDemoTitleList != null?myDemoTitleList.get(i):"";
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ListItemView listItemView;
        if(view == null){
            listItemView = new ListItemView();
            view = View.inflate(context, R.layout.my_list_item,null);
            listItemView.setMyTitleView((Button) view.findViewById(R.id.myTitleText));
            view.setTag(listItemView);
        }else{
            listItemView = (ListItemView) view.getTag();
        }

        listItemView.getMyTitleView().setText(myDemoTitleList.get(i));

        return view;
    }

    class ListItemView{
        private Button myTitleView;

        public Button getMyTitleView() {
            return myTitleView;
        }

        public void setMyTitleView(Button myTitleView) {
            this.myTitleView = myTitleView;
        }
    }
}
