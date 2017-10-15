package com.trevinavery.beyondthrift.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.trevinavery.beyondthrift.R;
import com.trevinavery.beyondthrift.model.Model;

/**
 * This is the main activity. It will show the LoginFragment if the user is not
 * logged in, or the MapFragment if the user is logged in.
 */
public class MainActivity extends Activity {

    private OnboardingFragment onboardingFragment;
    private LoginFragment loginFragment;
    private MainFragment mainFragment;
    private MyMapFragment myMapFragment;


    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

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

                    ft.setCustomAnimations(0, R.animator.slide_out_down);
                    ft.replace(R.id.fragmentContainer, loginFragment);
                    ft.commit();
                }
            });

            loginFragment = new LoginFragment();
            loginFragment.setOnLoginListener(new LoginFragment.OnLoginListener() {
                @Override
                public void onLogin(LoginType loginType) {
                    // user has logged in, switch to map fragment
                    FragmentTransaction ft = getFragmentManager().beginTransaction();

                    ft.setCustomAnimations(0, R.animator.slide_out_down);
                    ft.replace(R.id.fragmentContainer, new PromotionsFragment());
                    ft.commit();
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    mDrawerLayout.openDrawer(Gravity.START);
                }
            });

//            mainFragment = new MainFragment();



            // if there is an auth token, show map, else show login
            Fragment fragment = (Model.getAuthToken() != null) ? myMapFragment : loginFragment;

            // if first time opening, show onboarding fragment
            fragment = onboardingFragment;

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragmentContainer, fragment);
            ft.commit();


            // load drawer items

            mPlanetTitles = getResources().getStringArray(R.array.main_drawer_items);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerList = (ListView) findViewById(R.id.left_drawer);

            // Set the adapter for the list view
            mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, mPlanetTitles));
            // Set the list's click listener
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = null;
        Intent intent = null;

        switch (position) {
            case 0:
                fragment = new PromotionsFragment();
                break;
            case 1:
                fragment = new MyMapFragment();
                break;
            case 2:
                fragment = new DonateFragment();
                break;
            case 3:
                fragment = new DonatedItemsFragment();
                break;
            case 5:
                fragment = onboardingFragment;
                break;
            case 6:
                intent = new Intent(this, SettingsActivity.class);
                break;
            default:
                fragment = new MainFragment();
                break;
        }

        if (intent != null) {
            this.startActivity(intent);
        } else if (fragment != null) {

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();

            // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(mPlanetTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
//        mTitle = title;
//        getActionBar().setTitle(mTitle);
//        getActionBar().setTitle(title);
    }

}
