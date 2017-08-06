package com.example.hartshteinma.eatyourworld.controller.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.hartshteinma.eatyourworld.R;

public class SignInFragment extends Fragment
{

    private EditText emailEt, passwordEt;
    private Button signInButton;
    private Delegate delegate;

    public SignInFragment()
    {
    }

    public interface Delegate
    {
        void onSignInPressed(String email, String password);
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initWidgets(view);
        return view;
    }

    private void initWidgets(View view)
    {
        this.emailEt = (EditText) view.findViewById(R.id.email_editText);
        this.passwordEt = (EditText) view.findViewById(R.id.password_editText);
        this.signInButton = (Button) view.findViewById(R.id.sign_in_button);
        this.signInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                delegate.onSignInPressed(emailEt.getText().toString(), passwordEt.getText().toString());
            }
        });
    }
}
