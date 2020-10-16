package com.hezongze.privatedemo.jetpack;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hezongze.privatedemo.R;
import com.hezongze.privatedemo.databinding.ActivityJetpackBinding;

public class JetPackActivity extends AppCompatActivity {


    //databinding的后续函数的名字，是根据布局里面指定的标记变化而变化
    //例如：setAaaaaaa
    private ActivityJetpackBinding binding;
    private JetpackViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_jetpack);
        viewModel = new ViewModelProvider.Factory(){
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                JetpackViewModel viewModel = new JetpackViewModel((Application) JetPackActivity.this.getApplicationContext());
                return (T) viewModel;
            }
        }.create(JetpackViewModel.class);
        binding.setData(viewModel);
        //如何建立感应model改变----》观察者（布局改变）
        binding.setLifecycleOwner(this);
    }
}
