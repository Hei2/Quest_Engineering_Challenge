package challenge.questboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.lang.reflect.Field;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        if (id == R.id.action_settings)
        {
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
}
