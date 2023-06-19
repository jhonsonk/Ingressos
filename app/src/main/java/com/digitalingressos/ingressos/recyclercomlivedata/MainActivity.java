package com.digitalingressos.ingressos.recyclercomlivedata;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalingressos.ingressos.R;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LifecycleOwner {

    MainActivity context;
    MainViewModel viewModel;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        //recyclerView = findViewById(R.id.rv_main);
        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        viewModel = new ViewModelProvider(context).get(MainViewModel.class);
        viewModel.getUserMutableLiveData().observe(context, userListUpdateObserver);
    }
    Observer<List<User>> userListUpdateObserver = new Observer<List<User>>() {
        @Override
        public void onChanged(List<User> userArrayList) {
            recyclerViewAdapter.updateUserList(userArrayList);
        }
    };

}

