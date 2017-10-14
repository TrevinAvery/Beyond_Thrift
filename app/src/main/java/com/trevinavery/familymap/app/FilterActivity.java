package com.trevinavery.familymap.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.trevinavery.familymap.R;
import com.trevinavery.familymap.model.Model;

import java.util.Iterator;
import java.util.Set;

/**
 * This activity shows a list of all filterable events categories (each event type,
 * father's side, mother's side, male events, and female events). Each item can be
 * enabled or disabled. It updates the model automatically.
 */
public class FilterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        // set up list
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        FilterAdapter filterAdapter = new FilterAdapter();
        recyclerView.setAdapter(filterAdapter);
    }

    private class FilterAdapter extends RecyclerView.Adapter<FilterViewHolder> {

        @Override
        public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflatedView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_filter, parent, false);
            return new FilterViewHolder(inflatedView);
        }

        @Override
        public void onBindViewHolder(FilterViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            // add 4 to account for the family side and gender filters
            return Model.getEventTypes().size() + 4;
        }
    }

    private class FilterViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView titleTextView;
        private TextView descriptionTextView;
        private Switch onOffSwitch;

        public FilterViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
            onOffSwitch = (Switch) itemView.findViewById(R.id.onOffSwitch);
        }

        public void bind(int position) {
            // if position is within the size of eventTypes,
            // then load the dynamic filter,
            // else, assume it is a family side or gender filter

            Set<String> eventTypes = Model.getEventTypes();
            if (position < eventTypes.size()) {
                // because a set cannot be case to an array,
                // iterate through the set to the position given
                Iterator<String> iter = eventTypes.iterator();
                for (int i = 0; i < position; ++i) {
                    iter.next();
                }
                final String eventType = iter.next();
                titleTextView.setText(String.format(getString(R.string.filter_title), eventType));
                descriptionTextView.setText(String.format(getString(R.string.filter_description), eventType));

                onOffSwitch.setChecked(Model.isEventTypeEnabled(eventType));
                onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Model.setEventTypeEnabled(eventType, isChecked);
                    }
                });
            } else {
                int relPosition = position - eventTypes.size();

                String title = null;
                String description = null;
                boolean isEnabled = true;
                CompoundButton.OnCheckedChangeListener onChecked = null;

                switch (relPosition) {
                    case 0: // father's side
                        title = getString(R.string.filter_title_fathers_side);
                        description = getString(R.string.filter_description_fathers_side);
                        isEnabled = Model.isFathersSideEventsEnabled();
                        onChecked = new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                Model.setFathersSideEventsEnabled(isChecked);
                            }
                        };
                        break;
                    case 1: // mother's side
                        title = getString(R.string.filter_title_mothers_side);
                        description = getString(R.string.filter_description_mothers_side);
                        isEnabled = Model.isMothersSideEventsEnabled();
                        onChecked = new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                Model.setMothersSideEventsEnabled(isChecked);
                            }
                        };
                        break;
                    case 2: // male
                        title = getString(R.string.filter_title_male_events);
                        description = getString(R.string.filter_description_male_events);
                        isEnabled = Model.isMaleEventsEnabled();
                        onChecked = new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                Model.setMaleEventsEnabled(isChecked);
                            }
                        };
                        break;
                    case 3: // female
                        title = getString(R.string.filter_title_female_events);
                        description = getString(R.string.filter_description_female_events);
                        isEnabled = Model.isFemaleEventsEnabled();
                        onChecked = new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                Model.setFemaleEventsEnabled(isChecked);
                            }
                        };
                        break;
                }

                titleTextView.setText(title);
                descriptionTextView.setText(description);
                onOffSwitch.setChecked(isEnabled);
                onOffSwitch.setOnCheckedChangeListener(onChecked);
            }
        }
    }
}
