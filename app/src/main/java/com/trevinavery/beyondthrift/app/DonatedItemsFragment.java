package com.trevinavery.beyondthrift.app;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.trevinavery.beyondthrift.R;
import com.trevinavery.beyondthrift.adapter.DonatedItemsAdapter;
import com.trevinavery.beyondthrift.model.Donation;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Julio on 10/14/17.
 */

public class DonatedItemsFragment extends Fragment {
    ArrayList<Object> searchResults = new ArrayList<>();

    DonatedItemsAdapter adapter;
    public DonatedItemsFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Donation tempDonation = new Donation("Couch in great condition", "large", "Provo", "today", "tomorrow");
        searchResults.add(tempDonation);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donated_items, container, false);
        // ...
        // Lookup the recyclerview in activity layout



        RecyclerView rvSearch = (RecyclerView) view.findViewById(R.id.donated_items_list);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSearch.setLayoutManager(layoutManager);

        // Create adapter passing in the sample user data
        adapter = new DonatedItemsAdapter(getActivity().getBaseContext(), searchResults);

        // Attach the adapter to the recyclerview to populate items
        rvSearch.setAdapter(adapter);

        // Set layout manager to position the items
        rvSearch.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
