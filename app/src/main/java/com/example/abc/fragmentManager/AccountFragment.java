package com.example.abc.fragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abc.ManagerActivity;
import com.example.abc.R;
import com.example.abc.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {
    private TextView edtAdminName, edtAdminEmail;
    private AppCompatButton btnLogOut;
    ManagerActivity managerActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        managerActivity = (ManagerActivity) getActivity();
        assert managerActivity != null;
        String email = managerActivity.getEmail();

        edtAdminEmail = view.findViewById(R.id.edtAdminEmail);
        edtAdminName = view.findViewById(R.id.edtAdminName);
        btnLogOut = view.findViewById(R.id.btnLogOut);

        edtAdminEmail.setText(email);
        if (email.equals("admin1@gmail.com")) {
            edtAdminName.setText("admin1");
        } else if (email.equals("admin2@gmail.com")) {
            edtAdminName.setText("admin2");
        } else if (email.equals("admin3@gmail.com")) {
            edtAdminName.setText("admin3");
        }
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder myDialog = new AlertDialog.Builder(getContext());
                myDialog.setTitle("Log out confirmation");
                myDialog.setMessage("Are you sure you want to log out?");
                myDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    }
                });
                myDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                myDialog.create().show();
            }
        });
        return view;
    }
}