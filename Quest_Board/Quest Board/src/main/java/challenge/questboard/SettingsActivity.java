package challenge.questboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class SettingsActivity extends Activity {
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        userID = intent.getStringExtra(MainActivity.EXTRA_USERID);

        // Populate the alignment_spinner
        Spinner spinner = (Spinner) findViewById(R.id.alignment_spinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.alignment_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Retrieve the user's alignment and location of origin from the database
        ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("UserInfo");
        userQuery.getInBackground(userID, new GetCallback<ParseObject>() {
            public void done(ParseObject userInfo, ParseException e) {
                if (e == null) {
                    String name = userInfo.getString("name");
                    String location = userInfo.getString("locationOfOrigin");
                    int alignmentIndex = adapter.getPosition(userInfo.getString("alignment"));
                    populateFields(name, location, alignmentIndex);
                } else {
                    populateFields("", "", 0);

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "An error occurred retrieving your information.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    // Populate the Name, Location of Origin, and Alignment fields/spinner
    public void populateFields(String name, String location, int alignmentIndex) {
        EditText nameText = (EditText) findViewById(R.id.name_field);
        EditText locationText = (EditText) findViewById(R.id.location_field);
        Spinner alignmentSpinner = (Spinner) findViewById(R.id.alignment_spinner);
        nameText.setText(name);
        locationText.setText(location);
        alignmentSpinner.setSelection(alignmentIndex, true);
    }

    // Save the Name, Location of Origin, and Alignment to the database
    public void updateFields(View view) {
        EditText nameText = (EditText) findViewById(R.id.name_field);
        EditText locationText = (EditText) findViewById(R.id.location_field);
        Spinner alignmentSpinner = (Spinner) findViewById(R.id.alignment_spinner);
        final String name = nameText.getText().toString();
        final String location = locationText.getText().toString();
        final String alignment = alignmentSpinner.getSelectedItem().toString();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserInfo");
        query.getInBackground(userID, new GetCallback<ParseObject>() {
            public void done(ParseObject userInfo, ParseException e) {
                if (e == null) {
                    userInfo.put("name", name);
                    userInfo.put("locationOfOrigin", location);
                    userInfo.put("alignment", alignment);
                    userInfo.saveInBackground();

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Updated your information successfully.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "An error occurred updating your information. Please try again",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
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
        return super.onOptionsItemSelected(item);
    }
}
