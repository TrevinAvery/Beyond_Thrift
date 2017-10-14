package com.trevinavery.beyondthrift.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.trevinavery.beyondthrift.R;
import com.trevinavery.beyondthrift.model.Model;

/**
 * This is the main activity. It will show the LoginFragment if the user is not
 * logged in, or the MapFragment if the user is logged in.
 */
public class MainActivity extends Activity {

    private OnboardingFragment onboardingFragment;
    private LoginFragment loginFragment;
    private MyMapFragment myMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // fragments are preserved in the savedInstanceState
        // only create one if one doesn't already exist
        if (savedInstanceState == null) {

            // create all fragments

            onboardingFragment = new OnboardingFragment();
            onboardingFragment.setOnGetStartedListener(new OnboardingFragment.OnGetStartedListener() {
                @Override
                public void onGetStarted() {
                    // user has logged in, switch to map fragment
                    FragmentTransaction ft = getFragmentManager().beginTransaction();

//                    ft.setCustomAnimations(0, R.animator.slide_out_down);
                    ft.replace(R.id.fragmentContainer, loginFragment);
                    ft.commit();
                }
            });

            loginFragment = new LoginFragment();
            loginFragment.setOnLoginListener(new LoginFragment.OnLoginListener() {
                @Override
                public void onLogin() {
                    // user has logged in, switch to map fragment
                    FragmentTransaction ft = getFragmentManager().beginTransaction();

//                    ft.setCustomAnimations(0, R.animator.slide_out_down);
                    ft.replace(R.id.fragmentContainer, myMapFragment);
                    ft.commit();
                }
            });



            // if there is an auth token, show map, else show login
            Fragment fragment = (Model.getAuthToken() != null) ? myMapFragment : loginFragment;

            // if first time opening, show onboarding fragment
            fragment = onboardingFragment;

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragmentContainer, fragment);
            ft.commit();
        }
    }
}
