package challenge.questboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

// Display the quests to the user and handle displaying the individual quest details to the user
public class QuestDetailsActivity extends Activity {

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
                    descriptionText.setText(questInfo.getString("description"));
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
        return super.onOptionsItemSelected(item);
    }

}
