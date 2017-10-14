package com.trevinavery.beyondthrift.app;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.trevinavery.beyondthrift.R;
import com.trevinavery.beyondthrift.adapter.DonatedItemsAdapter;
import com.trevinavery.beyondthrift.model.Donation;
import com.trevinavery.beyondthrift.model.DonationData;
import com.trevinavery.beyondthrift.proxy.Proxy;
import com.trevinavery.beyondthrift.result.DonationResult;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Julio on 10/14/17.
 */

public class DonatedItemsFragment extends Fragment {
    ArrayList<Object> searchResults = new ArrayList<>();
    View view;

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

        new DonateItemListTask().execute();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_donated_items, container, false);
        // ...
        // Lookup the recyclerview in activity layout




        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }



    private class DonateItemListTask extends AsyncTask<Void, Void, Void> {

        private String serverHost = "cesarjcm.com";
        private String serverPort = "80";

        protected Proxy proxy;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            proxy = new Proxy(serverHost, serverPort);

        }

        @Override
        protected Void doInBackground(Void... params) {

            DonationData data = proxy.getDonations();

            Donation[] donations = data.getData();

            searchResults.add(donations);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            RecyclerView rvSearch = (RecyclerView) view.findViewById(R.id.donated_items_list);


            final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rvSearch.setLayoutManager(layoutManager);

            // Create adapter passing in the sample user data
            adapter = new DonatedItemsAdapter(getActivity().getBaseContext(), searchResults);

            // Attach the adapter to the recyclerview to populate items
            rvSearch.setAdapter(adapter);

            // Set layout manager to position the items
            rvSearch.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }
}
