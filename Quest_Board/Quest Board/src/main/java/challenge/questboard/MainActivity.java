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
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        if (username.length() == 0) {
            usernameText.setError("Username required.");
        } else if (password.length() == 0) {
            passwordText.setError("Password required.");
        } else {
            if (username.equals("Lancelot") && password.equals("arthurDoesntKnow")) {
                Intent intent = new Intent(this, QuestBoardActivity.class);
                startActivity(intent);
            } else {
                usernameText.setError("Username and/or Password incorrect.");
                passwordText.setError("Username and/or Password incorrect.");
            }
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