package com.example.hartshteinma.eatyourworld.controller.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hartshteinma.eatyourworld.R;

public class MainFragment extends Fragment {
    private Button signInBtn, registerBtn;
    private Delegate delegate;

    public MainFragment() {
    }

    public interface Delegate {
        void onSignInPressed();

        void onRegisterPressed();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initWidgets(view);
        return view;
    }

    private void initWidgets(View view) {
        this.signInBtn = (Button) view.findViewById(R.id.fragment_main_signInButton);
        this.registerBtn = (Button) view.findViewById(R.id.fragment_main_registerButton);
        this.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.onSignInPressed();
            }
        });
        this.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.onRegisterPressed();
            }
        });
    }
}
