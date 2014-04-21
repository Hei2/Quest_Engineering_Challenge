package challenge.questboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

// Custom adapter for displaying the quests on the QuestBoardActivity's ListView
// Help for this class from: http://stackoverflow.com/questions/8166497/custom-adapter-for-list-view
public class QuestListAdapter extends ArrayAdapter {
    public QuestListAdapter(Context context, int resource, List<Quest> quests) {
        super(context, resource, quests);
    }

    // Populate the list's TextViews with the relevant information
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflatedView = LayoutInflater.from(getContext());
            view = inflatedView.inflate(R.layout.quest_list_item, null);
        }

        Quest quest = (Quest) getItem(position);

        if (quest != null) {
            TextView titleText = (TextView) view.findViewById(R.id.title_text);
            TextView giverText = (TextView) view.findViewById(R.id.giver_text);
            TextView rewardText = (TextView) view.findViewById(R.id.reward_text);

            if (titleText != null) {
                titleText.setText(quest.getTitle());
            }
            if (giverText != null) {
                giverText.setText("Posted by: " + quest.getGiver());
            }
            if (rewardText != null) {
                rewardText.setText("Reward: " + quest.getRewardGold() + " Gold, " +
                    quest.getRewardXP() + " XP");
            }
        }

        return view;
    }
}
