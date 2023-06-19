package com.digitalingressos.ingressos.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.digitalingressos.ingressos.R;
import com.digitalingressos.ingressos.application.IngressosApplication;
import com.digitalingressos.ingressos.data.database.auth.Auth;
import com.digitalingressos.ingressos.ui.fragment.EventoFragment;
import com.digitalingressos.ingressos.ui.fragment.LoginFragment;
import com.digitalingressos.ingressos.util.NavigationHost;


public class MainActivity extends AppCompatActivity implements NavigationHost {

    //private ItemViewModel viewModel;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Aguarde...");
        mProgressDialog.show();

        Auth login = IngressosApplication.getAppAuth();
        if (savedInstanceState == null) {

            if (login != null) {
                mProgressDialog.dismiss();
                getSupportFragmentManager().beginTransaction().add(R.id.container, new EventoFragment()).commit();

            } else {
                mProgressDialog.dismiss();
                getSupportFragmentManager().beginTransaction().add(R.id.container, new LoginFragment()).commit();
            }
        }
    }


    /**
     * Navigate to the given fragment.
     *
     * @param fragment       Fragment to navigate to.
     * @param addToBackstack Whether or not the current fragment should be added to the backstack.
     */
    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}