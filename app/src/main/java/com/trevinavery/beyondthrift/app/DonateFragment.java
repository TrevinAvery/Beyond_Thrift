package com.trevinavery.beyondthrift.app;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.trevinavery.beyondthrift.R;
import com.trevinavery.beyondthrift.model.Donation;
import com.trevinavery.beyondthrift.proxy.Proxy;
import com.trevinavery.beyondthrift.result.DonationResult;

import static android.app.Activity.RESULT_OK;

public class DonateFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView image;

    private EditText description;
    private EditText size;
    private EditText location;
    private EditText start;
    private EditText end;


    public DonateFragment() {
        // Required default constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donate, container, false);

        description = (EditText) view.findViewById(R.id.description);
        size = (EditText) view.findViewById(R.id.size);
        location = (EditText) view.findViewById(R.id.location);
        start = (EditText) view.findViewById(R.id.start);
        end = (EditText) view.findViewById(R.id.end);

        image = (ImageView) view.findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        Button donateButton = (Button) view.findViewById(R.id.donate);
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DonateTask().execute();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
        }
    }

//    private void updateButtons() {
//
//        // only change state of buttons if no task is running
//        if (!runningTask) {
//
//            // check host, port, username, and password
//            if (serverHost.getText().length() > 0
//                    && serverPort.getText().length() > 0
//                    && username.getText().length() > 0
//                    && password.getText().length() > 0) {
//
//                // enable sign in button
//                signIn.setEnabled(true);
//
//                // check firstName, lastName, and email
//                if (firstName.getText().length() > 0
//                        && lastName.getText().length() > 0
//                        && email.getText().length() > 0) {
//
//                    // enable register button
//                    register.setEnabled(true);
//
//                } else {
//                    // disable register button
//                    register.setEnabled(false);
//                }
//
//            } else {
//                // disable both buttons
//                signIn.setEnabled(false);
//                register.setEnabled(false);
//            }
//        }
//    }


    private class DonateTask extends AsyncTask<Void, Void, String> {

        private String serverHost = "cesarjcm.com";
        private String serverPort = "80";

        protected Proxy proxy;
        protected Donation donation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            proxy = new Proxy(serverHost, serverPort);

            donation = new Donation(
                    description.getText().toString(),
                    size.getText().toString(),
                    location.getText().toString(),
                    start.getText().toString(),
                    end.getText().toString()
            );
        }

        @Override
        protected String doInBackground(Void... params) {

            DonationResult result = proxy.donate(donation);

            String message;

            if (result == null) {
                message = getString(R.string.connection_failed);
            } else {
                message = result.getMessage();
            }

            return message;
        }

        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);

            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

        }
    }

}
