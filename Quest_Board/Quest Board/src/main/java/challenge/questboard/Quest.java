package challenge.questboard;

// This object contains the information pertaining to a quest
public class Quest {
    private String title, giver;
    private int rewardGold, rewardXP;

    public Quest(String title, String giver, int rewardGold, int rewardXP) {
        this.title = title;
        this.giver = giver;
        this.rewardGold = rewardGold;
        this.rewardXP = rewardXP;
    }

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
