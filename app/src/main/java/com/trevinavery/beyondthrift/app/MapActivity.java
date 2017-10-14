package com.trevinavery.beyondthrift.app;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.trevinavery.beyondthrift.R;

/**
 * This activity simple shows the map fragment with a preselected event.
 * The eventID must be stored in the intent under {@link MyMapFragment#EXTRA_EVENT_ID}.
 */
public class MapActivity extends Activity {

    private MyMapFragment myMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // fragments are preserved in the savedInstanceState
        // only create one if one doesn't already exist
        if (savedInstanceState == null) {
            myMapFragment = new MyMapFragment();

            String eventID = getIntent().getStringExtra(MyMapFragment.EXTRA_EVENT_ID);

            if (eventID != null) {
                Bundle args = new Bundle();
                args.putString(MyMapFragment.EXTRA_EVENT_ID, eventID);
                myMapFragment.setArguments(args);
            }

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragmentContainer, myMapFragment);
            ft.commit();
        }
    }
}