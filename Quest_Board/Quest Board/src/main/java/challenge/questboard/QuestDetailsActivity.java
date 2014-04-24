package challenge.questboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Locale;

// Display the quests to the user and handle displaying the individual quest details to the user
public class QuestDetailsActivity extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_details);

        Intent intent = getIntent();
        String questID = intent.getStringExtra(QuestBoardActivity.EXTRA_QUESTID);
        String title = intent.getStringExtra(QuestBoardActivity.EXTRA_TITLE);
        String giver = intent.getStringExtra(QuestBoardActivity.EXTRA_GIVER);
        int rewardGold = intent.getIntExtra(QuestBoardActivity.EXTRA_REWARDGOLD, 0);
        int rewardXP = intent.getIntExtra(QuestBoardActivity.EXTRA_REWARDXP, 0);

        // Populate the Title, Giver, and Reward TextViews
        TextView titleText = (TextView) findViewById(R.id.title_text);
        TextView giverText = (TextView) findViewById(R.id.giver_text);
        TextView rewardText = (TextView) findViewById(R.id.reward_text);
        titleText.setText(title);
        giverText.setText("Posted by: " + giver);
        rewardText.setText("Reward: " + rewardGold + " Gold, " + rewardXP + " XP");

        ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("Quests");
        userQuery.getInBackground(questID, new GetCallback<ParseObject>() {
            public void done(ParseObject questInfo, ParseException e) {
                if (e == null) {
                    TextView descriptionText = (TextView) findViewById(R.id.description_text);
                    GoogleMap questMap = ((MapFragment) getFragmentManager()
                            .findFragmentById(R.id.quest_map)).getMap();
                    descriptionText.setText(questInfo.getString("description"));

                    questMap.setOnInfoWindowClickListener(QuestDetailsActivity.this);

                    LatLng questLocation = new LatLng(questInfo.getParseGeoPoint("location").getLatitude(),
                            questInfo.getParseGeoPoint("location").getLongitude());
                    LatLng questGiverLocation = new LatLng(questInfo.getParseGeoPoint("questGiverLocation").getLatitude(),
                            questInfo.getParseGeoPoint("questGiverLocation").getLongitude());

                    LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
                    boundsBuilder.include(questLocation);
                    boundsBuilder.include(questGiverLocation);
                    LatLngBounds bounds = boundsBuilder.build();

                    //questMap.moveCamera(CameraUpdateFactory.newLatLngZoom(questLocation, 13));
                    questMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200, 200, 0));
                    // Place the markers for the quest's and the quest giver's locations
                    questMap.addMarker(new MarkerOptions().title("Quest Location")
                            .snippet("Go here to complete your quest")
                            .position(questLocation));
                    questMap.addMarker(new MarkerOptions().title("Quest Giver's Location")
                            .snippet("You can find the quest giver here")
                            .position(questGiverLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "An error occurred. Please open this quest again.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quest_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", marker.getPosition().latitude, marker.getPosition().longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

}
