package com.trevinavery.beyondthrift.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.trevinavery.beyondthrift.R;
import com.trevinavery.beyondthrift.model.Event;
import com.trevinavery.beyondthrift.model.Model;
import com.trevinavery.beyondthrift.model.Person;

/**
 * This activity allows the user to enter a string and search through all
 * people and events. It displays a dynamic list of of results, first matching
 * people, then matching events (excluding those that are filtered).
 */
public class SearchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        final SearchAdapter searchAdapter = new SearchAdapter();
        recyclerView.setAdapter(searchAdapter);


        final EditText search = (EditText) findViewById(R.id.searchEditText);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchStr = search.getText().toString();
                    searchAdapter.setData(
                            Model.searchPersons(searchStr),
                            Model.searchEvents(searchStr)
                    );
                    return true;
                }
                return false;
            }
        });
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

        private Person[] persons;
        private Event[] events;

        public SearchAdapter() {
            setData(null, null);
        }

        public void setData(Person[] persons, Event[] events) {
            if (persons == null) {
                persons = new Person[]{};
            }
            this.persons = persons;

            if (events == null) {
                events = new Event[]{};
            }
            this.events = events;

            // tell the list to update
            notifyDataSetChanged();
        }

        @Override
        public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflatedView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_expandable, parent, false);
            return new SearchViewHolder(inflatedView);
        }

        @Override
        public void onBindViewHolder(SearchViewHolder holder, int position) {
            int relPosition = position - persons.length;
            if (relPosition < 0) {
                holder.bind(persons[position]);
            } else {
                holder.bind(events[relPosition]);
            }
        }

        @Override
        public int getItemCount() {
            return persons.length + events.length;
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView titleTextView;
        private TextView descriptionTextView;
        private ImageView icon;

        public SearchViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }

        public void bind(Person person) {
            titleTextView.setText(person.getName());
            descriptionTextView.setText("");

            if (person.getGender().equalsIgnoreCase("m")) {
                icon.setImageResource(R.drawable.icons8_male_48);
            } else {
                icon.setImageResource(R.drawable.icons8_female_48);
            }

            final Intent personIntent = new Intent(SearchActivity.this, PersonActivity.class);
            personIntent.putExtra(PersonActivity.EXTRA_PERSON_ID, person.getPersonID());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(personIntent);
                }
            });
        }

        public void bind(Event event) {
            Person person = Model.getPerson(event.getPersonID());

            titleTextView.setText(event.getDescription());
            descriptionTextView.setText(person.getName());

            icon.setImageResource(R.drawable.icons8_marker_48);

            final Intent eventIntent = new Intent(SearchActivity.this, MapActivity.class);
            eventIntent.putExtra(MyMapFragment.EXTRA_EVENT_ID, event.getEventID());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(eventIntent);
                }
            });
        }
    }
}
