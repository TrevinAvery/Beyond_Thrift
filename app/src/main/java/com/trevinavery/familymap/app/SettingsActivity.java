package com.trevinavery.familymap.app;

import android.app.Activity;
import android.content.Intent;
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

import com.trevinavery.familymap.R;
import com.trevinavery.familymap.model.Event;
import com.trevinavery.familymap.model.Model;
import com.trevinavery.familymap.model.Person;
import com.trevinavery.familymap.proxy.Proxy;
import com.trevinavery.familymap.result.EventResult;
import com.trevinavery.familymap.result.PersonResult;

public class SettingsActivity extends Activity {

    private String[] titles;
    private String[] descriptions;
    private String[] lineColorNames;
    private String[] mapTypes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // grab resources
        titles = getResources().getStringArray(R.array.settings_titles);
        descriptions = getResources().getStringArray(R.array.settings_descriptions);
        lineColorNames = getResources().getStringArray(R.array.settings_line_color_names);
        mapTypes = getResources().getStringArray(R.array.settings_map_types);

        // set up list
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        SettingsAdapter settingsAdapter = new SettingsAdapter();
        recyclerView.setAdapter(settingsAdapter);
    }

    private class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder> {

        private final int TYPE_NORMAL = 0;
        private final int TYPE_MAP_SETTING = 1;
        private final int TYPE_LINE_SETTING = 2;

        @Override
        public int getItemViewType(int position) {
            switch (position) {
                case 0:
                case 1:
                case 2:
                    return TYPE_LINE_SETTING;
                case 3:
                    return TYPE_MAP_SETTING;
                default:
                    return TYPE_NORMAL;
            }
        }

        @Override
        public SettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            int layout;

            switch (viewType) {
                case TYPE_LINE_SETTING:
                    layout = R.layout.list_item_settings_lines;
                    break;
                case TYPE_MAP_SETTING:
                    layout = R.layout.list_item_settings_map;
                    break;
                default:
                    layout = R.layout.list_item_settings_normal;
                    break;
            }

            View inflatedView = LayoutInflater.from(parent.getContext())
                    .inflate(layout, parent, false);
            return new SettingsViewHolder(inflatedView, viewType);
        }

        @Override
        public void onBindViewHolder(SettingsViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return 6;
        }


        public class SettingsViewHolder extends RecyclerView.ViewHolder {

            private View itemView;
            private TextView titleTextView;
            private TextView descriptionTextView;
            private Spinner spinner;
            private Switch onOffSwitch;

            public SettingsViewHolder(View itemView, int viewType) {
                super(itemView);

                // save references to each element in the layout to be used later

                this.itemView = itemView;

                titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
                descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);

                if (viewType == TYPE_MAP_SETTING) {
                    spinner = (Spinner) itemView.findViewById(R.id.typeSpinner);

                } else if (viewType == TYPE_LINE_SETTING) {
                    spinner = (Spinner) itemView.findViewById(R.id.colorSpinner);
                    onOffSwitch = (Switch) itemView.findViewById(R.id.onOffSwitch);
                }
            }

            public void bind(final int position) {
                int viewType = getItemViewType();

                titleTextView.setText(titles[position]);
                descriptionTextView.setText(descriptions[position]);

                if (viewType == TYPE_NORMAL) {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (position == 4) {
                                // re-sync
                                new AsyncTask<Void, Void, Boolean>() {

                                    @Override
                                    protected Boolean doInBackground(Void... params) {

                                        String authToken = Model.getAuthToken();
                                        Proxy proxy = Model.getProxy();

                                        PersonResult personResult = proxy.getPersons(authToken);
                                        EventResult eventResult = proxy.getEvents(authToken);

                                        if (personResult != null
                                                && eventResult != null) {
                                            Person[] persons = personResult.getData();
                                            Event[] events = eventResult.getData();

                                            if (persons != null && events != null) {
                                                Model.load(persons, events);
                                                return true;
                                            }
                                        }

                                        return false;
                                    }

                                    @Override
                                    protected void onPostExecute(Boolean success) {
                                        super.onPostExecute(success);
                                        if (success) {
                                            Intent mainIntent = new Intent(SettingsActivity.this, MainActivity.class);
                                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(mainIntent);
                                            finish();
                                        } else {
                                            Toast.makeText(getBaseContext(), R.string.toast_resync_failed,
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }.execute();
                            } else {
                                // Logout
                                Toast.makeText(getBaseContext(), R.string.toast_logout, Toast.LENGTH_SHORT).show();
                                Model.logout();
                                Intent logoutIntent = new Intent(SettingsActivity.this, MainActivity.class);
                                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(logoutIntent);
                                finish();
                            }
                        }
                    });
                } else { // if (viewType == TYPE_MAP_SETTING || viewType == TYPE_LINE_SETTING) {

                    // get list of items for spinner
                    String[] items;
                    int selected;
                    AdapterView.OnItemSelectedListener listener;

                    if (viewType == TYPE_MAP_SETTING) {
                        items = mapTypes;
                        selected = Model.getMapType();
                        listener = new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Model.setMapType(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        };
                    } else {
                        items = lineColorNames;
                        selected = Model.getLineColor(position);
                        listener = new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int itemPosition, long id) {
                                Model.setLineColor(position, itemPosition);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        };
                    }

                    ArrayAdapter<String> spinnerAdapter =
                            new ArrayAdapter<>(getBaseContext(),
                                    android.R.layout.simple_spinner_item, items);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(spinnerAdapter);
                    spinner.setSelection(selected);
                    spinner.setOnItemSelectedListener(listener);


                    // initialize the switch
                    if (viewType == TYPE_LINE_SETTING) {
                        onOffSwitch.setChecked(Model.getLineSwitch(position));
                        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                Model.setLineSwitch(position, isChecked);
                            }
                        });
                    }

                }
            }
        }
    }
}
