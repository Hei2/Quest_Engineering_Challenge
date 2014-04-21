package challenge.questboard;

import android.os.Parcelable;

// This object contains the information pertaining to a quest
public class Quest {
    private String objectID, title, giver;
    private int rewardGold, rewardXP;

    public Quest(String objectID, String title, String giver, int rewardGold, int rewardXP) {
        this.objectID = objectID;
        this.title = title;
        this.giver = giver;
        this.rewardGold = rewardGold;
        this.rewardXP = rewardXP;
    }

    public String getObjectID() { return objectID; }

    public String getTitle() {
        return title;
    }

    public String getGiver() {
        return giver;
    }

    public int getRewardGold() {
        return rewardGold;
    }

    public int getRewardXP() {
        return rewardXP;
    }
}
