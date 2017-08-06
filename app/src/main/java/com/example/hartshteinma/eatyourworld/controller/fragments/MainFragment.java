package com.example.hartshteinma.eatyourworld.controller.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hartshteinma.eatyourworld.R;

public class MainFragment extends Fragment {
    private Button signInButton, registerButton;
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
        this.signInButton = (Button) view.findViewById(R.id.sign_in_button);
        this.registerButton = (Button) view.findViewById(R.id.register_button);
        this.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.onSignInPressed();
            }
        });
        this.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.onRegisterPressed();
            }
        });
    }
}
