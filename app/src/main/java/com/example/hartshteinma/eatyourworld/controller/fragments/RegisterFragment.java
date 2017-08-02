package com.example.hartshteinma.eatyourworld.controller.fragments;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hartshteinma.eatyourworld.R;
import com.example.hartshteinma.eatyourworld.model.User;
public class RegisterFragment extends Fragment {

    private EditText bDayEt, firstNameEt, lastNameEt, emailEt, passwordEt, userIdEt;
    private Button registerButton;
    private Delegate delegate;

    public void setDelegate(Delegate dlg) {
        this.delegate = dlg;
    }

    public interface Delegate {
        void onRegisterButtonClick(User user);
    }

    public RegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initWidgets(view);
        return view;
    }

    private void initWidgets(View view) {
        this.userIdEt = (EditText) view.findViewById(R.id.fragmemt_register_userId);
        this.firstNameEt = (EditText) view.findViewById(R.id.fragmemt_register_fname);
        this.lastNameEt = (EditText) view.findViewById(R.id.fragmemt_register_lname);
        this.emailEt = (EditText) view.findViewById(R.id.fragmemt_register_Email);
        this.passwordEt = (EditText) view.findViewById(R.id.fragmemt_register_Password);
        this.registerButton = (Button) view.findViewById(R.id.fragment_register_btn);
        this.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "REGISTER-BUTTON", Toast.LENGTH_SHORT).show();
                delegate.onRegisterButtonClick(getUserFromWidgets());
            }
        });
        this.bDayEt = (EditText) view.findViewById(R.id.fragment_register_Birthday);
        this.bDayEt.setInputType(0);
        this.bDayEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

    }

    private User getUserFromWidgets() {
        User user = new User();
        user.setCountry(this.bDayEt.getText().toString());
        user.setEmail(this.emailEt.getText().toString());
        user.setFirstName(this.firstNameEt.getText().toString());
        user.setLastName(this.lastNameEt.getText().toString());
        user.setPassword(this.passwordEt.getText().toString());
        user.setUserId(this.userIdEt.getText().toString());
        return user;
    }

    public String getRegisterBtnTag() {
        return registerButton.getTag().toString();
    }
}
