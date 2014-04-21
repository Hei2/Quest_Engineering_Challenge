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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.security.NoSuchAlgorithmException;
import java.util.List;


public class RegistrationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Populate the alignment_spinner
        Spinner spinner = (Spinner) findViewById(R.id.alignment_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.alignment_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Populate the Username and Password fields
        Intent intent = getIntent();
        String username = intent.getStringExtra(MainActivity.EXTRA_USERNAME);
        EditText usernameText = (EditText) findViewById(R.id.username_field);
        usernameText.setText(username);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registration, menu);
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

    // Get all the input information and insert it into the Parse database
    public void register(View view) {
        final EditText usernameText = (EditText) findViewById(R.id.username_field);
        EditText passwordText = (EditText) findViewById(R.id.password_field);
        EditText nameText = (EditText) findViewById(R.id.name_field);
        Spinner alignmentSpinner = (Spinner) findViewById(R.id.alignment_spinner);
        final String username = usernameText.getText().toString();
        final String password = passwordText.getText().toString();
        final String name = nameText.getText().toString();
        final String alignment = alignmentSpinner.getSelectedItem().toString();

        // Keep track of whether or not the username or password fields are empty
        boolean usernameEmpty = false;
        boolean passwordEmpty = false;

        // Display an error if either field is empty
        if (username.length() == 0) {
            usernameText.setError("Username required.");
            usernameEmpty = true;
        }
        if (password.length() == 0) {
            passwordText.setError("Password required.");
            passwordEmpty = true;
        }

        // Register if neither the username or password fields are empty
        if (!usernameEmpty && !passwordEmpty) {
            // Ensure the username is not already in the database
            ParseQuery<ParseObject> query = ParseQuery.getQuery("UserInfo");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        boolean alreadyRegistered = false;
                        for (ParseObject parseObject : objects) {
                            if (username.equals(parseObject.getString("username"))) {
                                alreadyRegistered = true;
                                break;
                            }
                        }

                        // Inform the user if the username is already in use
                        if (alreadyRegistered) {
                            usernameText.setError("Username already in use.");
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Username already in use.", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            try {
                                ParseObject userInfo = new ParseObject("UserInfo");
                                userInfo.put("username", username);
                                userInfo.put("password", MainActivity.hashPassword(password));
                                userInfo.put("name", name);
                                userInfo.put("alignment", alignment);
                                userInfo.saveInBackground();

                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Registered successfully!", Toast.LENGTH_SHORT);
                                toast.show();
                            } catch (NoSuchAlgorithmException ex) {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "An error occurred with your password. Please try again.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "An error occurred contacting the database. Please try again.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
        }
    }
}
