package com.jnu.student.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jnu.student.R;

public class MyFragment extends Fragment {
    public MyFragment(){
        // Required empty public constructor
    }

    public  MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
        Bundle T = new Bundle();
        fragment.setArguments(T);
        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my, container, false);
    }
}
