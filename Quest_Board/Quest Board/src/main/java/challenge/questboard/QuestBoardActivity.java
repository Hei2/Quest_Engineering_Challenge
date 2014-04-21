package challenge.questboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

// Display individual quest details to the user
public class QuestBoardActivity extends Activity {
    private String userID;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_quest_board);

            Intent intent = getIntent();
            userID = intent.getStringExtra(MainActivity.EXTRA_USERID);

            getQuests();
        }

    // Populate the ListView with the quests from the database
    public void populateList(List<Quest> questList) {
        // Populate the ListView with the quests
        ListView listView = (ListView) findViewById(R.id.quest_listView);
        QuestListAdapter questListAdapter = new QuestListAdapter(this, R.layout.quest_list_item, questList);
        listView.setAdapter(questListAdapter);
    }

    // Retrieve the quests from the database before passing them to populateList()
    public void getQuests() {
        // Retrieve the user's alignment from the database
        ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("UserInfo");
        userQuery.getInBackground(userID, new GetCallback<ParseObject>() {
            public void done(ParseObject userInfo, ParseException e) {
                if (e == null) {
                    ParseQuery<ParseObject> questQuery = ParseQuery.getQuery("Quests");
                    try {
                        List<Quest> questList = new ArrayList<Quest>();

                        List<ParseObject> objects = questQuery.find();
                        for (ParseObject parseObject : objects) {
                            // Display all quests if the user's alignment is set to "Neutral"
                            if (userInfo.getString("alignment").equals("Neutral")) {
                                String title = parseObject.getString("title");
                                String giver = parseObject.getString("questGiver");
                                int rewardGold = parseObject.getInt("rewardGold");
                                int rewardXP = parseObject.getInt("rewardXP");
                                questList.add(new Quest(title, giver, rewardGold, rewardXP));
                            } else if (userInfo.getString("alignment").equals(parseObject
                                    .getString("alignment"))) {
                                String title = parseObject.getString("title");
                                String giver = parseObject.getString("questGiver");
                                int rewardGold = parseObject.getInt("rewardGold");
                                int rewardXP = parseObject.getInt("rewardXP");
                                questList.add(new Quest(title, giver, rewardGold, rewardXP));
                            }

                            populateList(questList);
                        }
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "An error occurred. Please try again.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quest_board, menu);
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
