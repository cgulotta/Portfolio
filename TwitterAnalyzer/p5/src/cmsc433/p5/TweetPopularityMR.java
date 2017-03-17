package cmsc433.p5;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


/**
 * Map reduce which takes in a CSV file with tweets as input and output
 * key/value pairs.</br>
 * </br>
 * The key for the map reduce depends on the specified {@link TrendingParameter}
 * , <code>trendingOn</code> passed to
 * {@link #score(Job, String, String, TrendingParameter)}).
 */
public class TweetPopularityMR {

  // For your convenience...
  public static final int          TWEET_SCORE   = 1;
  public static final int          RETWEET_SCORE = 2;
  public static final int          MENTION_SCORE = 1;

  // Is either USER, TWEET, or HASHTAG. Set for you before call to map()
  private static TrendingParameter trendingOn;

  public static class TweetMapper
      extends Mapper<Object, Text, Text, IntWritable> {
//	  	private final static IntWritable one = new IntWritable(1);
//	  	private Text word = new Text();

    @Override
    public void map(Object key, Text value, Context context)
        throws IOException, InterruptedException {
      // Converts the CSV line into a tweet object
      Tweet tweet = Tweet.createTweet(value.toString());

      if (trendingOn == TrendingParameter.USER){
    	  //score = (# tweets by user) + 2*(# times retweeted) + (# times mentioned)
    	  String username = tweet.getUserScreenName();
    	  List<String> mentionList = tweet.getMentionedUsers();
    	  context.write(new Text(username), new IntWritable(TWEET_SCORE));
    	  if (tweet.wasRetweetOfUser()){
    		  String retweetedUser = tweet.getRetweetedUser();
    		  context.write(new Text(retweetedUser),new IntWritable(RETWEET_SCORE));
    	  }
    	  for (String user : mentionList){
    		  context.write(new Text(user), new IntWritable(MENTION_SCORE));
    	  }
    	  
      }else if (trendingOn == TrendingParameter.TWEET){
    	  //score = 1 + 2*(# times retweeted)
    	  String tweetID = tweet.getId().toString();
    	  context.write(new Text(tweetID), new IntWritable(TWEET_SCORE));
    	  if (tweet.wasRetweetOfTweet()){
    		  String retweetedTweet = tweet.getRetweetedTweet().toString();
    		  context.write(new Text(retweetedTweet),new IntWritable(RETWEET_SCORE));
    	  }
      }if (trendingOn == TrendingParameter.HASHTAG){
    	  //score = (# times hashtags used)
    	  List<String> hashtagList = tweet.getHashtags();
    	  for (String user : hashtagList){
    		  context.write(new Text(user), new IntWritable(MENTION_SCORE));
    	  }
      }

      
      
      
      
    }
  }

  public static class PopularityReducer
      extends Reducer<Text, IntWritable, Text, IntWritable> {

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context)
        throws IOException, InterruptedException {
    	Iterator<IntWritable> iter = values.iterator();
    	int sum = 0;
    	while (iter.hasNext()){
    		sum += iter.next().get();
    	}
    	context.write(key, new IntWritable(sum));
    }
  }

  /**
   * Method which performs a map reduce on a specified input CSV file and
   * outputs the scored tweets, users, or hashtags.</br>
   * </br>
   * 
   * @param job
   * @param input
   *          The CSV file containing tweets
   * @param output
   *          The output file with the scores
   * @param trendingOn
   *          The parameter on which to score
   * @return true if the map reduce was successful, false otherwise.
   * @throws Exception
   */
  public static boolean score(Job MRjob, String input, String output,
      TrendingParameter trendingOn) throws Exception {

    TweetPopularityMR.trendingOn = trendingOn;

    MRjob.setJarByClass(TweetPopularityMR.class);

    // TODO: Set up map-reduce...
    	// Set up and configure MapReduce job.

    			// Set key, output classes for the job.
    			MRjob.setOutputKeyClass(Text.class);  
    			MRjob.setOutputValueClass(IntWritable.class);  

    			// Set Mapper and Reducer classes for the job.
    			MRjob.setMapperClass(TweetMapper.class);  
    			MRjob.setReducerClass(PopularityReducer.class);  

    			// Sets format of input files.  "TextInputFormat" views files as
    			// a sequence of lines.
    			
    			MRjob.setInputFormatClass(TextInputFormat.class);  
    			
    			// Sets format of output files:  here, lines of text.
    			MRjob.setOutputFormatClass(TextOutputFormat.class);  
    
    
    
    
    // End

    FileInputFormat.addInputPath(MRjob, new Path(input));
    FileOutputFormat.setOutputPath(MRjob, new Path(output));

    return MRjob.waitForCompletion(true);
  }
}
