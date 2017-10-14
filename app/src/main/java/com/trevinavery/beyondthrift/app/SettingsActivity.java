package com.trevinavery.beyondthrift.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.trevinavery.beyondthrift.R;
import com.trevinavery.beyondthrift.model.Event;
import com.trevinavery.beyondthrift.model.Model;
import com.trevinavery.beyondthrift.model.Person;
import com.trevinavery.beyondthrift.proxy.Proxy;
import com.trevinavery.beyondthrift.result.EventResult;
import com.trevinavery.beyondthrift.result.PersonResult;

public class SettingsActivity extends Activity {
    private String BEYOND_THRIFT_DB = "beyondThriftDb";
    private String[] titles;
    private String[] descriptions;
    private String[] lineColorNames;
    private String[] mapTypes;

    private String[] carTypes = new String[] { "Compact Car", "Mid-Size Car", "Small Truck", "Medium Truck", "Large Truck" };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setSavedDropDownButtons();
        setSavedSwitchesStates();
        setUpSwitchListeners();
        addDropDownButton();
        setButtonListeners();

    }

    private void setSavedDropDownButtons(){
        SharedPreferences prefs = getSharedPreferences(BEYOND_THRIFT_DB, MODE_PRIVATE);


        Spinner vehicleTypeSpinner = (Spinner) findViewById(R.id.vehicle_type_spinner);
        vehicleTypeSpinner.setSelection(prefs.getInt("vehicle_type",0));
    }

    private void setSavedSwitchesStates(){
        //open database
        SharedPreferences prefs = getSharedPreferences(BEYOND_THRIFT_DB, MODE_PRIVATE);

        //email receipts switch
        boolean switchState = prefs.getBoolean("email_receipts", true);
        Switch emailReceiptsSwitch = (Switch) findViewById(R.id.id_email_receipts_switch);
        emailReceiptsSwitch.setChecked(switchState);

        //notifications switch
        switchState = prefs.getBoolean("notifications_switch", true);
        Switch notificationsSwitch = (Switch) findViewById(R.id.id_notifications_switch);
        notificationsSwitch.setChecked(switchState);
    }

    private void setUpSwitchListeners(){
        //email receipts switch
        Switch emailReceiptsSwitch = (Switch) findViewById(R.id.id_email_receipts_switch);

        emailReceiptsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                buttonView.setChecked(isChecked);

                SharedPreferences.Editor editor = getSharedPreferences(BEYOND_THRIFT_DB, MODE_PRIVATE).edit();
                editor.putBoolean("email_receipts", isChecked);
                editor.commit();
            }
        });

        //baptism switch
        Switch notificationsSwitch = (Switch) findViewById(R.id.id_notifications_switch);

        notificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                buttonView.setChecked(isChecked);

                SharedPreferences.Editor editor = getSharedPreferences(BEYOND_THRIFT_DB, MODE_PRIVATE).edit();
                editor.putBoolean("notifications_switch", isChecked);
                editor.commit();
            }
        });
    }
    private void addDropDownButton(){
        ArrayAdapter<String> vehicleTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, carTypes);

        Spinner vehicleTypeSpinner = (Spinner) findViewById(R.id.vehicle_type_spinner);
        vehicleTypeSpinner.setAdapter(vehicleTypeAdapter);

        vehicleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //Log.v("item", (String) parent.getItemAtPosition(position));
                //Log.v("index", Integer.toString(position));

                setUpSpinnerValue("vehicle_type", position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void setUpSpinnerValue(String spinner, int position){
        SharedPreferences.Editor editor = getSharedPreferences(BEYOND_THRIFT_DB, MODE_PRIVATE).edit();
        editor.putInt(spinner, position);
        editor.commit();
    }

    private void setButtonListeners(){

        //address buttons
        TextView homeAddressButton = (TextView) findViewById(R.id.id_home_address_setting);
        homeAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pop up dialog to change address

            }
        });

        TextView homeAddressStringButton = (TextView) findViewById(R.id.id_user_home_address);
        homeAddressStringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pop up dialog to change address

            }
        });

        //email buttons
        TextView emailAddressButton = (TextView) findViewById(R.id.id_email_address_setting);
        emailAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pop up dialog to change email address
            }
        });

        TextView emailAddressStringButton = (TextView) findViewById(R.id.id_user_email_address);
        emailAddressStringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pop up dialog to change email address
            }
        });

        TextView logoutButton = (TextView) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
