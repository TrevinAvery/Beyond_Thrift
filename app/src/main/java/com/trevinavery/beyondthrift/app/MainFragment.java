package com.trevinavery.beyondthrift.app;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.trevinavery.beyondthrift.R;

public class MainFragment extends Fragment {

//    private OnGetStartedListener onGetStartedListener;

    private Button getStarted;

    public MainFragment() {
        // Required default constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

//        getStarted = (Button) view.findViewById(R.id.getStartedButton);
//        getStarted.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (onGetStartedListener != null) {
//                    onGetStartedListener.onGetStarted();
//                }
//            }
//        });

        return view;
    }

//    public void setOnGetStartedListener(OnGetStartedListener onGetStartedListener) {
//        this.onGetStartedListener = onGetStartedListener;
//    }
//
//    public interface OnGetStartedListener {
//        public void onGetStarted();
//    }
}
