/**
 * DO NOT MODIFY!
 */

package cmsc433.p5;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Class representing a tweet. It contains all of the necessary fields for the
 * completion of the project.
 *
 */
public class Tweet {

  private static final DateFormat FORMAT = new SimpleDateFormat(
      "EEE MMM d HH:mm:ss Z yyyy", Locale.ENGLISH);

  private Long                    id;
  private Date                    timestamp;
  private String                  userScreenName;
  private List<String>            hashtags;
  private List<String>            userMentions;
  private String                  retweetedUser;
  private Long                    retweetedStatus;
  private String                  textContent;

  private Tweet() {}

  public Long getId() {
    return id;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public String getUserScreenName() {
    return userScreenName;
  }

  public List<String> getHashtags() {
    return hashtags;
  }

  public List<String> getMentionedUsers() {
    return userMentions;
  }

  public boolean wasRetweetOfUser() {
    return retweetedUser != null;
  }

  public String getRetweetedUser() {
    return retweetedUser;
  }

  public boolean wasRetweetOfTweet() {
    return retweetedStatus != null;
  }

  public Long getRetweetedTweet() {
    return retweetedStatus;
  }

  public String getTextContent() {
    return textContent;
  }

  /**
   * Creates a tweet object from a csv row.
   * 
   * @param csvLine
   *          A line from the csv file specified in the MR input.
   * @return A <code>Tweet</code> object representing the input.
   */
  public static Tweet createTweet(String csvLine) {
    Tweet tweet = new Tweet();

    String[] words = csvLine.split(",");
    tweet.id = Long.valueOf(words[0].replaceAll("\"", ""));
    try {
      tweet.timestamp = FORMAT.parse(words[1].replaceAll("\"", ""));
    } catch (ParseException e) {
      tweet.timestamp = null;
      System.err.println("Uh oh!");
      e.printStackTrace();
    }
    tweet.userScreenName = words[2].replaceAll("\"", "");
    if (words[3].equals("\"\"")) {
      tweet.hashtags = new ArrayList<>();
    } else {
      tweet.hashtags = Arrays.asList(words[3].replaceAll("\"", "").split("&"));
    }
    tweet.userMentions = new ArrayList<>();
    if (!words[4].equals("\"\"")) {
      for (String userId : words[4].replaceAll("\"", "").split("&")) {
        tweet.userMentions.add(userId);
      }
    }
    tweet.retweetedUser = words[5].equals("\"\"") ? null
        : words[5].replaceAll("\"", "");
    tweet.retweetedStatus = words[6].equals("\"\"") ? null
        : Long.valueOf(words[6].replaceAll("\"", ""));
    tweet.textContent = words[7].replaceAll("\"", "");

    return tweet;
  }

  @Override
  public String toString() {
    return "Tweet [id=" + id + ", timestamp=" + timestamp + ", userScreenName="
        + userScreenName + ", hashtags=" + hashtags + ", userMentions="
        + userMentions + ", retweetedUser=" + retweetedUser
        + ", retweetedStatus=" + retweetedStatus + ", textContent="
        + textContent + "]";
  }

//  public static void main(String[] args) {
//    String tweetText1 = "\"366076445623197697\",\"Sat Aug 10 06:00:00 +0000 2013\",\"Itsyogirlchy15\",\"\",\"baileperra_&marcotrejo1994\",\"baileperra_\",\"366076245001248768\",\"@baileperra_ @marcotrejo1994 definitely staples!\"";
//    String tweetText2 = "\"366076445593837568\",\"Sat Aug 10 06:00:00 +0000 2013\",\"Ilovethe_xfacto\",\"5HoniTunes&HarmonizeSanAntonio\",\"Luis_Harmonizer&FifthHarmony\",\"\",\"\",\"RT @Luis_Harmonizer: @FifthHarmony #5HoniTunes #HarmonizeSanAntonio http://t.co/qbBWynqQDp\"";
//    String tweetText3 = "\"366076445614800896\",\"Sat Aug 10 06:00:00 +0000 2013\",\"chalky_\",\"\",\"elbeefster&fatfelix00&clubazteca34\",\"elbeefster\",\"366061430098763776\",\"@elbeefster @fatfelix00 @clubazteca34 make him say Uncle!! ( or Tio)..\"";
//
//    System.out.println(Tweet.createTweet(tweetText1));
//    System.out.println(Tweet.createTweet(tweetText2));
//    System.out.println(Tweet.createTweet(tweetText3));
//  }
}
