package com.trevinavery.beyondthrift.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trevinavery.beyondthrift.R;

/**
 * Created by Julio on 10/14/17.
 */

public class PromotionsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotions, container, false);


        return view;
    }
}
