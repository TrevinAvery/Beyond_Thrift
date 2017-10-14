package com.trevinavery.beyondthrift.app;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.trevinavery.beyondthrift.R;
import com.trevinavery.beyondthrift.model.Event;
import com.trevinavery.beyondthrift.model.Family;
import com.trevinavery.beyondthrift.model.Model;
import com.trevinavery.beyondthrift.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This activity displays the details of a selected person, including a list
 * of all life events and family members. The personID must be stored in the
 * intent under {@link PersonActivity#EXTRA_PERSON_ID}.
 */
public class PersonActivity extends Activity {

    public static final String EXTRA_PERSON_ID = "EXTRA_PERSON_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        // get the id of the person to show
        String personID = getIntent().getStringExtra(EXTRA_PERSON_ID);

        // load the name and gender of the person
        Person person = Model.getPerson(personID);

        TextView firstName = (TextView) findViewById(R.id.firstNameTextView);
        firstName.setText(person.getFirstName());

        TextView lastName = (TextView) findViewById(R.id.lastNameTextView);
        lastName.setText(person.getLastName());

        TextView gender = (TextView) findViewById(R.id.genderTextView);
        if (person.getGender().equalsIgnoreCase("m")) {
            gender.setText(R.string.gender_male);
        } else {
            gender.setText(R.string.gender_female);
        }

        // load the life events and family of the person
        List<ExpandableList> expandableLists = new ArrayList<>();

        // life events
        List<ExpandableListItem> eventListItems = new ArrayList<>();

        Set<Event> events = Model.getFilteredEvents(personID);

        for (Event event : events) {
            eventListItems.add(new ExpandableListItem(
                    event.getEventID(),
                    event.getDescription(),
                    person.getName(),
                    ExpandableListItem.TYPE_EVENT
            ));
        }

        // if there is nothing in the list, don't add it
        if (eventListItems.size() > 0) {
            ExpandableList eventList = new ExpandableList(getString(R.string.life_events), eventListItems);
            expandableLists.add(eventList);
        }

        // family
        List<ExpandableListItem> personListItems = new ArrayList<>();

        Family family = Model.getFamily(personID);

        Person[] familyArray = {
                family.getSpouse(),
                family.getFather(),
                family.getMother(),
                family.getChild()
        };

        // relationships are the descriptions underneath each name
        String[] relationships = getResources().getStringArray(R.array.relationships);

        for (int i = 0; i < 4; ++i) {
            if (familyArray[i] != null) {
                Person familyMember = familyArray[i];

                int genderType = familyMember.getGender().equalsIgnoreCase("m") ?
                        ExpandableListItem.TYPE_PERSON_MALE : ExpandableListItem.TYPE_PERSON_FEMALE;

                personListItems.add(new ExpandableListItem(
                        familyMember.getPersonID(),
                        familyMember.getName(),
                        relationships[i],
                        genderType
                ));
            }
        }

        // if there is nothing in the list, don't add it
        if (personListItems.size() > 0) {
            ExpandableList personList = new ExpandableList(getString(R.string.family), personListItems);
            expandableLists.add(personList);
        }

        // set up the expandable list
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(expandableLists);
        recyclerView.setAdapter(expandableListAdapter);
    }

    private class ExpandableListItem {

        public static final int TYPE_PERSON_MALE = 0;
        public static final int TYPE_PERSON_FEMALE = 1;
        public static final int TYPE_EVENT = 2;

        String id;
        String title;
        String description;
        int type;

        public ExpandableListItem(String id, String title, String description, int type) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    private class ExpandableList implements Parent<ExpandableListItem> {

        String title;
        private List<ExpandableListItem> items;

        public ExpandableList(String title, List<ExpandableListItem> items) {
            this.title = title;
            this.items = items;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public List<ExpandableListItem> getChildList() {
            return items;
        }

        @Override
        public boolean isInitiallyExpanded() {
            return false;
        }
    }

    private class ExpandableListViewHolder extends ParentViewHolder {

        private TextView title;
        private ImageView icon;

        /**
         * Default constructor.
         *
         * @param itemView The {@link View} being hosted in this ViewHolder
         */
        public ExpandableListViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.titleTextView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }

        public void bind(ExpandableList expandableList) {
            title.setText(expandableList.getTitle());
        }

        @Override
        public void onExpansionToggled(boolean expanded) {
            super.onExpansionToggled(expanded);

            // animate rotation of the expand icon
            if (expanded) {
                icon.animate().rotation(0f);
            } else {
                icon.animate().rotation(180f);
            }
        }
    }

    private class ExpandableListItemViewHolder extends ChildViewHolder {

        private View itemView;
        private ImageView icon;
        private TextView title;
        private TextView description;

        /**
         * Default constructor.
         *
         * @param itemView The {@link View} being hosted in this ViewHolder
         */
        public ExpandableListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;

            title = (TextView) itemView.findViewById(R.id.titleTextView);
            description = (TextView) itemView.findViewById(R.id.descriptionTextView);

            icon = (ImageView) itemView.findViewById(R.id.icon);
        }

        public void bind(ExpandableListItem listItem) {
            title.setText(listItem.getTitle());
            description.setText(listItem.getDescription());

            int iconResource;
            final Intent intent;

            if (listItem.getType() == ExpandableListItem.TYPE_EVENT) {
                iconResource = R.drawable.icons8_marker_48;

                intent = new Intent(PersonActivity.this, MapActivity.class);
                intent.putExtra(MyMapFragment.EXTRA_EVENT_ID, listItem.getId());

            } else {
                if (listItem.getType() == ExpandableListItem.TYPE_PERSON_MALE) {
                    iconResource = R.drawable.icons8_male_48;
                } else {
                    iconResource = R.drawable.icons8_female_48;
                }

                intent = new Intent(PersonActivity.this, PersonActivity.class);
                intent.putExtra(PersonActivity.EXTRA_PERSON_ID, listItem.getId());
            }

            icon.setImageResource(iconResource);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(intent);
                }
            });
        }
    }

    private class ExpandableListAdapter extends ExpandableRecyclerAdapter
            <ExpandableList, ExpandableListItem, ExpandableListViewHolder, ExpandableListItemViewHolder> {

        private List<ExpandableList> parentList;

        /**
         * Primary constructor. Sets up {@link #mParentList} and {@link #mFlatItemList}.
         * <p>
         * Any changes to {@link #mParentList} should be made on the original instance, and notified via
         * {@link #notifyParentInserted(int)}
         * {@link #notifyParentRemoved(int)}
         * {@link #notifyParentChanged(int)}
         * {@link #notifyParentRangeInserted(int, int)}
         * {@link #notifyChildInserted(int, int)}
         * {@link #notifyChildRemoved(int, int)}
         * {@link #notifyChildChanged(int, int)}
         * methods and not the notify methods of RecyclerView.Adapter.
         *
         * @param parentList List of all parents to be displayed in the RecyclerView that this
         *                   adapter is linked to
         */
        public ExpandableListAdapter(@NonNull List<ExpandableList> parentList) {
            super(parentList);
            this.parentList = parentList;
        }

        @NonNull
        @Override
        public ExpandableListViewHolder onCreateParentViewHolder(
                @NonNull ViewGroup parentViewGroup, int viewType) {

            View inflatedView = LayoutInflater.from(parentViewGroup.getContext())
                    .inflate(R.layout.list_expandable, parentViewGroup, false);
            return new ExpandableListViewHolder(inflatedView);
        }

        @NonNull
        @Override
        public ExpandableListItemViewHolder onCreateChildViewHolder(
                @NonNull ViewGroup childViewGroup, int viewType) {

            View inflatedView = LayoutInflater.from(childViewGroup.getContext())
                    .inflate(R.layout.list_item_expandable, childViewGroup, false);
            return new ExpandableListItemViewHolder(inflatedView);
        }

        @Override
        public void onBindParentViewHolder(
                @NonNull ExpandableListViewHolder parentViewHolder,
                int parentPosition, @NonNull ExpandableList parent) {

            parentViewHolder.bind(parentList.get(parentPosition));
        }

        @Override
        public void onBindChildViewHolder(
                @NonNull ExpandableListItemViewHolder childViewHolder,
                int parentPosition, int childPosition, @NonNull ExpandableListItem child) {

            childViewHolder.bind(parentList.get(parentPosition).getChildList().get(childPosition));
        }
    }
}
