package com.trevinavery.beyondthrift.app;


import android.content.Intent;
import android.graphics.PorterDuff;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.trevinavery.beyondthrift.R;
import com.trevinavery.beyondthrift.model.Event;
import com.trevinavery.beyondthrift.model.Location;
import com.trevinavery.beyondthrift.model.Model;
import com.trevinavery.beyondthrift.model.Person;

import org.w3c.dom.Text;

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
    private Map<String, Location> markerMap;
//    private List<Polyline> polylines;

    private View eventInfo;
//    private TextView title;
    private TextView description;
    private ImageView icon;

    public MyMapFragment() {
        // Required default constructor
    }

    private Location[] dropOffLocations = {
            new Location("store", "Orem Store", "165 North State St", "Orem", "UT", "84057", "40.3003298", "-111.7664602", "801-555-5555", "Mon-Fri: 8am-5pm"),
            new Location("store", "West Valley Store", "3749 S Constitution Blvd", "West Valley City", "UT", "84119", "40.6908489", "-111.9592429", "801-555-5555", "Mon-Fri: 8am-5pm"),

            new Location("bin", "Granite School District Office", "165 W 7200 S", "Midvale", "UT", "84047", "40.619596", "-111.8981467", "801-555-5555", "Mon-Fri: 8am-5pm"),
            new Location("bin", "Cyprus High School", "8739 W 3000 S", "Magna", "UT", "84044", "40.7057", "-112.1026787", "801-555-5555", "Mon-Fri: 8am-5pm"),
            new Location("bin", "Millcreek Elementary School", "3761 S 1100 E", "Salt Lake City", "UT", "84106", "40.6893034", "-111.8607833", "801-555-5555", "Mon-Fri: 8am-5pm"),
            new Location("bin", "Wasatch Jr High School", "3750 S 3100 E", "Salt Lake City", "UT", "84109", "40.690719", "-111.8086177", "801-555-5555", "Mon-Fri: 8am-5pm"),
            new Location("bin", "Cottonwood Elementary School", "5205 Holladay Blvd", "Holladay", "UT", "84117", "40.6566013", "-111.8180488", "801-555-5555", "Mon-Fri: 8am-5pm"),
            new Location("bin", "Jolley’s Compounding Pharmacy", "1702 South 1100 East", "Salt Lake City", "UT", "84105", "40.7333838", "-111.8621177", "801-555-5555", "Mon-Fri: 8am-5pm"),
            new Location("bin", "Wendy’s in Highland", "10969 N Town Center Blvd", "Highland", "UT", "84003", "40.431139", "-111.7917937", "801-555-5555", "Mon-Fri: 8am-5pm"),
            new Location("bin", "Kohl’s American Fork", "634 Pacific Dr", "American Fork", "UT", "84003", "40.3826808", "-111.8194143", "801-555-5555", "Mon-Fri: 8am-5pm"),
            new Location("bin", "Vasa Fitness Orem", "15 E 700 N", "Orem", "UT", "84057", "40.310244", "-111.6976168", "801-555-5555", "Mon-Fri: 8am-5pm"),
            new Location("bin", "Sam’s Club Provo", "1225 S University Ave", "Provo", "UT", "84606", "40.217361", "-111.6605686", "801-555-5555", "Mon-Fri: 8am-5pm")
    };


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
//        polylines = new ArrayList<>();
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_fragment_map, menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.actionSearch:
//                Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
//                startActivity(searchIntent);
//                return true;
//            case R.id.actionFilter:
//                Intent filterIntent = new Intent(getActivity(), FilterActivity.class);
//                // start for result to update the map on return
//                startActivityForResult(filterIntent, 0);
//                return true;
//            case R.id.actionSettings:
//                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
//                // start for result to update the map on return
//                startActivityForResult(settingsIntent, 0);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

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

//        eventInfo = view.findViewById(R.id.eventInfo);
//        title = (TextView) view.findViewById(R.id.titleTextView);
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

//                updateMapSelection(marker, true);

                // return true to prevent the default behavior
                return false;
            }
        };

        googleMap.setOnMarkerClickListener(onMarkerClickListener);

        googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

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
//        boolean first = true;

        for (Location location : dropOffLocations) {
            pos = new LatLng(Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongitude()));
            marker = googleMap.addMarker(new MarkerOptions().position(pos)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(location.getType().equals("bin")
                                    ? BitmapDescriptorFactory.HUE_AZURE : BitmapDescriptorFactory.HUE_RED))
                    .title(location.getTitle())
                    .snippet(location.getAddress()));
            markerMap.put(marker.getId(), location);

            // save the marker for the selected event
//            if (first) {
//                first = false;
//                selectedMarker = marker;
//            }
        }
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f), 250, null);

//        LatLngBounds utah = new LatLngBounds(
//                new LatLng(-44, 113), new LatLng(-10, 154));

        LatLng center = new LatLng(40.488444, -111.853867);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 9.5f));
//        updateMapSelection(selectedMarker, doScroll);
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
            Location location = markerMap.get(marker.getId());
//            eventID = location.getEventID();

//            drawLines(event);

//            final String personID = location.getPersonID();
//            Person person = Model.getPerson(personID);

//            title.setText(location.getType());
//            description.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
//            description.setText(location.getAddress());
//
//            icon.setVisibility(View.VISIBLE);
//            if (location.getType().equals("bin")) {
//                icon.setImageResource(R.mipmap.bin_icon);
//            } else {
//                icon.setImageResource(R.mipmap.store_icon);
//            }

//            eventInfo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent personIntent = new Intent(getContext(), PersonActivity.class);
//                    personIntent.putExtra(PersonActivity.EXTRA_PERSON_ID, personID);
//                    startActivity(personIntent);
//                }
//            });

            if (doScroll) {
                googleMap.animateCamera(CameraUpdateFactory
                        .newLatLng(marker.getPosition()), 250, null);
            }
        } else {
            // no marker selected, clear event info
//            eventID = null;
////            drawLines(null);
//            icon.setVisibility(View.GONE);
//            description.setText(R.string.prompt_click_marker);
//            description.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
////            description.setText("");
//            eventInfo.setOnClickListener(null);
        }
    }

    private class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.map_info_window, null);

            Location location = markerMap.get(marker.getId());

            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            if (location.getType().equals("bin")) {
                icon.setImageResource(R.mipmap.bin_icon);
            } else {
                icon.setImageResource(R.mipmap.store_icon);
            }

            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(location.getTitle());

            TextView address = (TextView) view.findViewById(R.id.address);
            address.setText(location.getAddress() + "\n" + location.getHours() + "\n" + location.getPhone());

            return view;
        }
    }

//
//    /**
//     * Draws all enabled lines.
//     *
//     * @param rootEvent the event to draw all lines from
//     */
//    private void drawLines(Event rootEvent) {
//
//        // remove old lines
//        for (Polyline line : polylines) {
//            line.remove();
//        }
//
//        // if no event, don't draw any lines
//        if (rootEvent == null) {
//            return;
//        }
//
//        if (Model.getLineSwitch(Model.LINE_COLOR_LIFE_STORY)) {
//            drawLifeStoryLines(rootEvent);
//        }
//
//        if (Model.getLineSwitch(Model.LINE_COLOR_FAMILY_TREE)) {
//            drawFamilyTreeLines(rootEvent);
//        }
//
//        if (Model.getLineSwitch(Model.LINE_COLOR_SPOUSE)) {
//            drawSpouseLines(rootEvent);
//        }
//    }
//
//    /**
//     * Draw life story lines in chronological order.
//     *
//     * @param rootEvent an event in the life story to connect
//     */
//    private void drawLifeStoryLines(Event rootEvent) {
//
//        int width = 25;
//
//        Set<Event> personEvents = Model.getFilteredEvents(rootEvent.getPersonID());
//
//        Event lastEvent = null;
//        for (Event event : personEvents) {
//            // skip first event
//            if (lastEvent != null) {
//
//                LatLng lastLatLng = new LatLng(
//                        lastEvent.getLatitude(), lastEvent.getLongitude());
//                LatLng thisLatLng = new LatLng(
//                        event.getLatitude(), event.getLongitude());
//
//                PolylineOptions options = new PolylineOptions()
//                        .add(lastLatLng, thisLatLng)
//                        .width(width)
//                        .color(getResources().getColor(
//                                Model.getLineColorResource(Model.LINE_COLOR_LIFE_STORY), null));
//
//                polylines.add(googleMap.addPolyline(options));
//            }
//            lastEvent = event;
//        }
//    }
//
//    /**
//     * Draws lines connecting the rootEvent to earliest event for each parent
//     * of the person associated with the rootEvent. This continues recursively
//     * through grandparent and great-grandparents and so on until there are no
//     * more connections. Each generation, the lines get progressively thinner.
//     *
//     * @param rootEvent the event to draw all the lines from
//     */
//    private void drawFamilyTreeLines(Event rootEvent) {
//
//        int width = 25;
//
//        Set<Event> generation = new TreeSet<>();
//        generation.add(rootEvent);
//
//        Set<Event> nextGeneration = new TreeSet<>();
//
//        Person person;
//        LatLng personLatLng;
//        Event parentEvent;
//
//        while (generation.size() > 0) {
//
//            for (Event personEvent : generation) {
//
//                person = Model.getPerson(personEvent.getPersonID());
//
//                personLatLng = new LatLng(
//                        personEvent.getLatitude(), personEvent.getLongitude());
//
//                parentEvent = drawParentLine(person.getFather(), personLatLng, width);
//
//                if (parentEvent != null) {
//                    nextGeneration.add(parentEvent);
//                }
//
//                parentEvent = drawParentLine(person.getMother(), personLatLng, width);
//
//                if (parentEvent != null) {
//                    nextGeneration.add(parentEvent);
//                }
//            }
//
//            generation = nextGeneration;
//            nextGeneration = new TreeSet<>();
//
//            // decrease width of line
//            width = width - 8;
//            if (width < 1) {
//                width = 1;
//            }
//        }
//
//    }
//
//    /**
//     * Draws a line from a location to the earliest event of the person associated
//     * with parentID. This is a helper method for {@link #drawFamilyTreeLines(Event)}.
//     *
//     * @param parentID the id of the person to draw a line to
//     * @param personLatLng the starting point of the line
//     * @param width the width of the line to be drawn
//     * @return the earliest event of the person associated with parentID
//     */
//    private Event drawParentLine(String parentID, LatLng personLatLng, int width) {
//
//        Set<Event> parentEvents = Model.getFilteredEvents(parentID);
//
//        if (parentEvents != null && parentEvents.size() > 0) {
//            Event parentEvent = parentEvents.iterator().next();
//            LatLng parentLatLng = new LatLng(
//                    parentEvent.getLatitude(), parentEvent.getLongitude());
//
//            PolylineOptions options = new PolylineOptions()
//                    .add(personLatLng, parentLatLng)
//                    .width(width)
//                    .color(getResources().getColor(
//                            Model.getLineColorResource(Model.LINE_COLOR_FAMILY_TREE), null));
//
//            polylines.add(googleMap.addPolyline(options));
//            return parentEvent;
//        }
//        return null;
//    }
//
//    /**
//     * Draws a line connecting the rootEvent to the earliest event of the
//     * spouse of the person associated with the rootEvent.
//     *
//     * @param rootEvent the event to connect with the spouse of the person
//     *                  associated with the event
//     */
//    private void drawSpouseLines(Event rootEvent) {
//
//        int width = 25;
//
//        Person person = Model.getPerson(rootEvent.getPersonID());
//
//        Set<Event> spouseEvents = Model.getFilteredEvents(person.getSpouse());
//
//        if (spouseEvents != null && spouseEvents.size() > 0) {
//            Event spouseEvent = spouseEvents.iterator().next();
//            LatLng spouseLatLng = new LatLng(
//                    spouseEvent.getLatitude(), spouseEvent.getLongitude());
//            LatLng personLatLng = new LatLng(
//                    rootEvent.getLatitude(), rootEvent.getLongitude());
//
//            PolylineOptions options = new PolylineOptions()
//                    .add(personLatLng, spouseLatLng)
//                    .width(width)
//                    .color(getResources().getColor(
//                            Model.getLineColorResource(Model.LINE_COLOR_SPOUSE), null));
//
//            polylines.add(googleMap.addPolyline(options));
//        }
//    }
}
