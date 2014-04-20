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

import com.parse.ParseObject;


public class RegistrationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Populate the alignment_spinner
        Spinner spinner = (Spinner) findViewById(R.id.alignment_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.alignment_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Populate the Username and Password fields
        Intent intent = getIntent();
        String username = intent.getStringExtra(MainActivity.EXTRA_USERNAME);
        String password = intent.getStringExtra(MainActivity.EXTRA_PASSWORD);
        EditText usernameText = (EditText) findViewById(R.id.username_field);
        EditText passwordText = (EditText) findViewById(R.id.password_field);
        usernameText.setText(username);
        passwordText.setText(password);
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
        EditText usernameText = (EditText) findViewById(R.id.username_field);
        EditText passwordText = (EditText) findViewById(R.id.password_field);
        EditText nameText = (EditText) findViewById(R.id.name_field);
        Spinner alignmentSpinner = (Spinner) findViewById(R.id.alignment_spinner);
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String name = nameText.getText().toString();
        String alignment = alignmentSpinner.getSelectedItem().toString();

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
            ParseObject userInfo = new ParseObject("UserInfo");
            userInfo.put("username", username);
            userInfo.put("password", password);
            userInfo.put("name", name);
            userInfo.put("alignment", alignment);
            userInfo.saveInBackground();
        }
    }
}
