package com.trevinavery.familymap.app;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.trevinavery.familymap.R;
import com.trevinavery.familymap.model.Event;
import com.trevinavery.familymap.model.Model;
import com.trevinavery.familymap.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This fragment displays a map with markers representing visible events.
 * To load this fragment with an event preselected, the eventID must be
 * stored in the intent under {@link MyMapFragment#EXTRA_EVENT_ID}.
 */
public class MyMapFragment extends Fragment implements OnMapReadyCallback {

    public static final String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";

    private String eventID;
    private GoogleMap googleMap;
    private Map<String, Event> markerMap;
    private List<Polyline> polylines;

    private View eventInfo;
    private TextView title;
    private TextView description;
    private ImageView icon;

    public MyMapFragment() {
        // Required default constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            eventID = getArguments().getString(EXTRA_EVENT_ID);
        }
        if (eventID == null) {
            setHasOptionsMenu(true); // enable actionbar inflation
        }
        markerMap = new TreeMap<>();
        polylines = new ArrayList<>();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSearch:
                Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                startActivity(searchIntent);
                return true;
            case R.id.actionFilter:
                Intent filterIntent = new Intent(getActivity(), FilterActivity.class);
                // start for result to update the map on return
                startActivityForResult(filterIntent, 0);
                return true;
            case R.id.actionSettings:
                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                // start for result to update the map on return
                startActivityForResult(settingsIntent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateMapMarkers(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        MapFragment mapFragment = (MapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);

        mapFragment.getMapAsync(this);

        eventInfo = view.findViewById(R.id.eventInfo);
        title = (TextView) view.findViewById(R.id.titleTextView);
        description = (TextView) view.findViewById(R.id.descriptionTextView);
        icon = (ImageView) view.findViewById(R.id.icon);

        return view;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                updateMapSelection(marker, true);

                // return true to prevent the default behavior
                return true;
            }
        };

        googleMap.setOnMarkerClickListener(onMarkerClickListener);

        updateMapMarkers(true);
    }

    /**
     * Clears all existing markers and lines from the map, then loads all
     * visible events and draws new lines.
     *
     * @param doScroll whether or not to scroll the camera to the selected
     *                 event, if there is a selected event
     */
    private void updateMapMarkers(boolean doScroll) {

        if (googleMap == null) {
            return;
        }

        // clear out old markers
        googleMap.clear();
        markerMap.clear();

        // set type from settings, add 1 to fix index offset
        googleMap.setMapType(Model.getMapType() + 1);

        Set<Event> events = Model.getFilteredEvents();
        LatLng pos;
        Marker marker;

        Marker selectedMarker = null;

        for (Event event : events) {
            pos = new LatLng(event.getLatitude(), event.getLongitude());
            marker = googleMap.addMarker(new MarkerOptions().position(pos)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(Model.getEventTypeColor(event.getEventType())))
            );
            markerMap.put(marker.getId(), event);

            // save the marker for the selected event
            if (eventID != null && eventID.equals(event.getEventID())) {
                selectedMarker = marker;
            }
        }

        updateMapSelection(selectedMarker, doScroll);
    }

    /**
     * Draws the lines for the selected event, and updates the event info at
     * the bottom of the fragment.
     *
     * @param marker the marker for the event
     * @param doScroll whether or not to scroll the camera to the selected
     *                 event, if there is a selected event
     */
    private void updateMapSelection(Marker marker, boolean doScroll) {

        // check if a marker is selected
        if (marker != null) {
            // marker selected, update event info
            Event event = markerMap.get(marker.getId());
            eventID = event.getEventID();

            drawLines(event);

            final String personID = event.getPersonID();
            Person person = Model.getPerson(personID);

            title.setText(person.getName());
            title.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            description.setText(event.getDescription());

            icon.setVisibility(View.VISIBLE);
            if (person.getGender().equalsIgnoreCase("m")) {
                icon.setImageResource(R.drawable.icons8_male_48);
            } else {
                icon.setImageResource(R.drawable.icons8_female_48);
            }

            eventInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent personIntent = new Intent(getContext(), PersonActivity.class);
                    personIntent.putExtra(PersonActivity.EXTRA_PERSON_ID, personID);
                    startActivity(personIntent);
                }
            });

            if (doScroll) {
                googleMap.animateCamera(CameraUpdateFactory
                        .newLatLng(marker.getPosition()), 250, null);
            }
        } else {
            // no marker selected, clear event info
            eventID = null;
            drawLines(null);
            icon.setVisibility(View.GONE);
            title.setText(R.string.prompt_click_marker);
            title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            description.setText("");
            eventInfo.setOnClickListener(null);
        }
    }

    /**
     * Draws all enabled lines.
     *
     * @param rootEvent the event to draw all lines from
     */
    private void drawLines(Event rootEvent) {

        // remove old lines
        for (Polyline line : polylines) {
            line.remove();
        }

        // if no event, don't draw any lines
        if (rootEvent == null) {
            return;
        }

        if (Model.getLineSwitch(Model.LINE_COLOR_LIFE_STORY)) {
            drawLifeStoryLines(rootEvent);
        }

        if (Model.getLineSwitch(Model.LINE_COLOR_FAMILY_TREE)) {
            drawFamilyTreeLines(rootEvent);
        }

        if (Model.getLineSwitch(Model.LINE_COLOR_SPOUSE)) {
            drawSpouseLines(rootEvent);
        }
    }

    /**
     * Draw life story lines in chronological order.
     *
     * @param rootEvent an event in the life story to connect
     */
    private void drawLifeStoryLines(Event rootEvent) {

        int width = 25;

        Set<Event> personEvents = Model.getFilteredEvents(rootEvent.getPersonID());

        Event lastEvent = null;
        for (Event event : personEvents) {
            // skip first event
            if (lastEvent != null) {

                LatLng lastLatLng = new LatLng(
                        lastEvent.getLatitude(), lastEvent.getLongitude());
                LatLng thisLatLng = new LatLng(
                        event.getLatitude(), event.getLongitude());

                PolylineOptions options = new PolylineOptions()
                        .add(lastLatLng, thisLatLng)
                        .width(width)
                        .color(getResources().getColor(
                                Model.getLineColorResource(Model.LINE_COLOR_LIFE_STORY), null));

                polylines.add(googleMap.addPolyline(options));
            }
            lastEvent = event;
        }
    }

    /**
     * Draws lines connecting the rootEvent to earliest event for each parent
     * of the person associated with the rootEvent. This continues recursively
     * through grandparent and great-grandparents and so on until there are no
     * more connections. Each generation, the lines get progressively thinner.
     *
     * @param rootEvent the event to draw all the lines from
     */
    private void drawFamilyTreeLines(Event rootEvent) {

        int width = 25;

        Set<Event> generation = new TreeSet<>();
        generation.add(rootEvent);

        Set<Event> nextGeneration = new TreeSet<>();

        Person person;
        LatLng personLatLng;
        Event parentEvent;

        while (generation.size() > 0) {

            for (Event personEvent : generation) {

                person = Model.getPerson(personEvent.getPersonID());

                personLatLng = new LatLng(
                        personEvent.getLatitude(), personEvent.getLongitude());

                parentEvent = drawParentLine(person.getFather(), personLatLng, width);

                if (parentEvent != null) {
                    nextGeneration.add(parentEvent);
                }

                parentEvent = drawParentLine(person.getMother(), personLatLng, width);

                if (parentEvent != null) {
                    nextGeneration.add(parentEvent);
                }
            }

            generation = nextGeneration;
            nextGeneration = new TreeSet<>();

            // decrease width of line
            width = width - 8;
            if (width < 1) {
                width = 1;
            }
        }

    }

    /**
     * Draws a line from a location to the earliest event of the person associated
     * with parentID. This is a helper method for {@link #drawFamilyTreeLines(Event)}.
     *
     * @param parentID the id of the person to draw a line to
     * @param personLatLng the starting point of the line
     * @param width the width of the line to be drawn
     * @return the earliest event of the person associated with parentID
     */
    private Event drawParentLine(String parentID, LatLng personLatLng, int width) {

        Set<Event> parentEvents = Model.getFilteredEvents(parentID);

        if (parentEvents != null && parentEvents.size() > 0) {
            Event parentEvent = parentEvents.iterator().next();
            LatLng parentLatLng = new LatLng(
                    parentEvent.getLatitude(), parentEvent.getLongitude());

            PolylineOptions options = new PolylineOptions()
                    .add(personLatLng, parentLatLng)
                    .width(width)
                    .color(getResources().getColor(
                            Model.getLineColorResource(Model.LINE_COLOR_FAMILY_TREE), null));

            polylines.add(googleMap.addPolyline(options));
            return parentEvent;
        }
        return null;
    }

    /**
     * Draws a line connecting the rootEvent to the earliest event of the
     * spouse of the person associated with the rootEvent.
     *
     * @param rootEvent the event to connect with the spouse of the person
     *                  associated with the event
     */
    private void drawSpouseLines(Event rootEvent) {

        int width = 25;

        Person person = Model.getPerson(rootEvent.getPersonID());

        Set<Event> spouseEvents = Model.getFilteredEvents(person.getSpouse());

        if (spouseEvents != null && spouseEvents.size() > 0) {
            Event spouseEvent = spouseEvents.iterator().next();
            LatLng spouseLatLng = new LatLng(
                    spouseEvent.getLatitude(), spouseEvent.getLongitude());
            LatLng personLatLng = new LatLng(
                    rootEvent.getLatitude(), rootEvent.getLongitude());

            PolylineOptions options = new PolylineOptions()
                    .add(personLatLng, spouseLatLng)
                    .width(width)
                    .color(getResources().getColor(
                            Model.getLineColorResource(Model.LINE_COLOR_SPOUSE), null));

            polylines.add(googleMap.addPolyline(options));
        }
    }
}
