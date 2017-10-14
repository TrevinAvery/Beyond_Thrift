package com.trevinavery.beyondthrift.app;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.trevinavery.beyondthrift.R;

import static android.app.Activity.RESULT_OK;

public class DonateFragment extends Fragment {

//    private OnGetStartedListener onGetStartedListener;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView image;

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

//        getStarted = (Button) view.findViewById(R.id.getStartedButton);
//        getStarted.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (onGetStartedListener != null) {
//                    onGetStartedListener.onGetStarted();
//                }
//            }
//        });

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

//    public void setOnGetStartedListener(OnGetStartedListener onGetStartedListener) {
//        this.onGetStartedListener = onGetStartedListener;
//    }
//
//    public interface OnGetStartedListener {
//        public void onGetStarted();
//    }
}
