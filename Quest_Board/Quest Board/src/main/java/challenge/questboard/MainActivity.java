package challenge.questboard;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

// Display the login page to the user and handle the process of logging in
public class MainActivity extends Activity {
    // Username and password fields to be passed to the RegistrationActivity
    public final static String EXTRA_USERNAME = "challenge.questboard.USERNAME";
    public final static String EXTRA_PASSWORD = "challenge.questboard.PASSWORD";

    // Username field and checkbox to be saved in the preferences
    private static final String PREF_USERNAME = "username";
    private static final String PREF_REMEMBER = "remember";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText usernameText = (EditText) findViewById(R.id.username_field);

        // Determine if the "Remember Username" checkbox was checked, then fill in the username
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getBoolean(PREF_REMEMBER, false)) {
            CheckBox rememberUsername = (CheckBox) findViewById(R.id.remember_username_checkbox);
            rememberUsername.setChecked(true);
            String username = pref.getString(PREF_USERNAME, "");
            usernameText.setText(username);
        }

        // This listener is added to ensure the username gets saved
        usernameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (pref.getBoolean(PREF_REMEMBER, false)) {
                    pref.edit().putString(PREF_USERNAME, usernameText.getText().toString()).commit();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    // Retrieve the text in the username and password fields to verify the login attempt
    public void login(View view) {
        EditText usernameText = (EditText) findViewById(R.id.username_field);
        EditText passwordText = (EditText) findViewById(R.id.password_field);
        final String username = usernameText.getText().toString();
        final String password = passwordText.getText().toString();

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

        // Check the database if neither the username or password fields are empty
        if (!usernameEmpty && !passwordEmpty) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("UserInfo");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        // Keeps track of whether or not the user and password combination was
                        // found in the database
                        boolean foundUser = false;
                        for (ParseObject parseObject : objects) {
                            if (username.equals(parseObject.get("username")) &&
                                    password.equals(parseObject.get("password"))) {
                                foundUser = true;
                                break;
                            }
                        }
                        finishLogin(foundUser);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "An error occured. Please try again.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
        }
    }

    // Either move the user to the QuestBoardActivity or display the error on the username and
    // password fields
    public void finishLogin(boolean foundUser) {
        if (foundUser) {
            Intent intent = new Intent(this, QuestBoardActivity.class);
            startActivity(intent);
        } else {
            EditText usernameText = (EditText) findViewById(R.id.username_field);
            EditText passwordText = (EditText) findViewById(R.id.password_field);
            usernameText.setError("Username and/or Password incorrect.");
            passwordText.setError("Username and/or Password incorrect.");
        }
    }

    // Start the RegistrationActivity and pass the username and password fields for convenience
    public void register(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        EditText usernameText = (EditText) findViewById(R.id.username_field);
        EditText passwordText = (EditText) findViewById(R.id.password_field);
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        intent.putExtra(EXTRA_USERNAME, username);
        intent.putExtra(EXTRA_PASSWORD, password);
        startActivity(intent);
    }

    // Save the username and checkbox state to the preferences
    public void remember(View view) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        CheckBox remember = (CheckBox) findViewById(R.id.remember_username_checkbox);
        if (remember.isChecked()) {
            EditText usernameText = (EditText) findViewById(R.id.username_field);
            pref.edit().putString(PREF_USERNAME, usernameText.getText().toString()).putBoolean(PREF_REMEMBER, true).commit();
        } else {
            pref.edit().putBoolean(PREF_REMEMBER, false).commit();
        }
    }
}