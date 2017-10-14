package com.trevinavery.beyondthrift.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.trevinavery.beyondthrift.R;
import com.trevinavery.beyondthrift.model.Donation;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Julio on 10/14/17.
 */
// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class DonatedItemsAdapter extends RecyclerView.Adapter<DonatedItemsAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView iconImage;
        public TextView objectDetails;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            iconImage = (ImageView) itemView.findViewById(R.id.list_view_icon);
            objectDetails = (TextView) itemView.findViewById(R.id.expandedListItem);
        }
    }

    // Store a member variable for the contacts
    private List<Object> mResultObjects;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public DonatedItemsAdapter(Context context, ArrayList<Object> objects) {
        mResultObjects = objects;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public DonatedItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.list_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(DonatedItemsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Object searchResult = mResultObjects.get(position);

        if(searchResult instanceof Donation){
            final Donation tempDonation = (Donation) searchResult;
            // Set item views based on your views and data model
            TextView textView = viewHolder.objectDetails;
            textView.setText(tempDonation.getDescription().toLowerCase());

            ImageView imageView = viewHolder.iconImage;
            imageView.setBackgroundResource(R.drawable.default_photo);


        }

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mResultObjects.size();
    }
}