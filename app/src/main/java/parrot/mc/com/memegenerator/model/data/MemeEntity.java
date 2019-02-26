package parrot.mc.com.memegenerator.model.data;

public class MemeEntity {
    private int imageID;
    private String displayName;
    private String imageUrl;
    private int totalVotesScore;
    private int ranking;

    public MemeEntity() {
    }

    public MemeEntity(int imageID, String displayName, String imageUrl, int totalVotesScore, int ranking) {
        this.imageID = imageID;
        this.displayName = displayName;
        this.imageUrl = imageUrl;
        this.totalVotesScore = totalVotesScore;
        this.ranking = ranking;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getTotalVotesScore() {
        return totalVotesScore;
    }

    public void setTotalVotesScore(int totalVotesScore) {
        this.totalVotesScore = totalVotesScore;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
}
