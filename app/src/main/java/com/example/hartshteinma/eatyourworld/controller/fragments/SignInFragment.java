package com.example.hartshteinma.eatyourworld.controller.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.hartshteinma.eatyourworld.R;
import com.example.hartshteinma.eatyourworld.dialogs.MyProgressBar;

public class SignInFragment extends Fragment {

    private EditText emailEt, passwordEt;
    private Button signInButton;
    private Delegate delegate;
    private MyProgressBar progressBar;

    public SignInFragment() {
    }

    public interface Delegate {
        void onSignInPressed(String email, String password);
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initWidgets(view);
        return view;
    }

    private void initWidgets(View view) {
        this.emailEt = (EditText) view.findViewById(R.id.fragmemt_signIn_emailEdit);
        this.passwordEt = (EditText) view.findViewById(R.id.fragmemt_signIn_passwordEdit);
        this.signInButton = (Button) view.findViewById(R.id.fragment_signIn_signInButton);
        this.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.onSignInPressed(emailEt.getText().toString(), passwordEt.getText().toString());

            }
        });
    }
}
