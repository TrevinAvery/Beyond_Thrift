package com.trevinavery.familymap.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.trevinavery.familymap.R;
import com.trevinavery.familymap.model.Model;

/**
 * This is the main activity. It will show the LoginFragment if the user is not
 * logged in, or the MapFragment if the user is logged in.
 */
public class MainActivity extends Activity {

    private LoginFragment loginFragment;
    private MyMapFragment myMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // fragments are preserved in the savedInstanceState
        // only create one if one doesn't already exist
        if (savedInstanceState == null) {

            myMapFragment = new MyMapFragment();

            loginFragment = new LoginFragment();
            loginFragment.setOnLoginListener(new LoginFragment.OnLoginListener() {
                @Override
                public void onLogin() {
                    // user has logged in, switch to map fragment
                    FragmentTransaction ft = getFragmentManager().beginTransaction();

                    ft.setCustomAnimations(0, R.animator.slide_out_down);
                    ft.replace(R.id.fragmentContainer, myMapFragment);
                    ft.commit();
                }
            });

            // if there is an auth token, show map, else show login
            Fragment fragment = (Model.getAuthToken() != null) ? myMapFragment : loginFragment;

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragmentContainer, fragment);
            ft.commit();
        }
    }
}
