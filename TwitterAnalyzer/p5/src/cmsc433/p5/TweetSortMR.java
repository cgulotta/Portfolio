package cmsc433.p5;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import cmsc433.p5.TweetPopularityMR.PopularityReducer;
import cmsc433.p5.TweetPopularityMR.TweetMapper;

/**
 * Map reduce which sorts the output of {@link TweetPopularityMR}.
 * The input will either be in the form of: </br>
 * 
 * <code></br>
 * &nbsp;(screen_name,  score)</br>
 * &nbsp;(hashtag, score)</br>
 * &nbsp;(tweet_id, score)</br></br>
 * </code>
 * 
 * The output will be in the same form, but with results sorted on the score.
 * 
 */
public class TweetSortMR {

  /**
   * Minimum <code>int</code> value for a pair to be included in the output.
   * Pairs with an <code>int</code> less than this value are omitted.
   */
  private static int CUTOFF = 10;

  public static class SwapMapper
      extends Mapper<Object, Text, IntWritable, Text> {

    String      id;
    int         score;

    @Override
    public void map(Object key, Text value, Context context)
        throws IOException, InterruptedException {
      String[] columns = value.toString().split("\t");
      Text id = new Text(columns[0]);
      IntWritable score = new IntWritable(-1 * Integer.valueOf(columns[1]));

      // TODO: Your code goes here
      if(Integer.valueOf(columns[1]) >= CUTOFF){
    	  context.write(score, id);
    }	
      
      
      
      
    }
  }

  public static class SwapReducer
      extends Reducer<IntWritable, Text, Text, IntWritable> {

    @Override
    public void reduce(IntWritable key, Iterable<Text> values, Context context)
        throws IOException, InterruptedException {
    	Iterator<Text> iter = values.iterator();
      // TODO: Your code goes here
    	while(iter.hasNext()){
    		IntWritable newKey = new IntWritable(-1 * key.get());
    		context.write(iter.next(), newKey);
    	}
      
      
      
      
    }
  }

  /**
   * This method performs value-based sorting on the given input by configuring
   * the job as appropriate and using Hadoop.
   * 
   * @param job
   *          Job created for this function
   * @param input
   *          String representing location of input directory
   * @param output
   *          String representing location of output directory
   * @return True if successful, false otherwise
   * @throws Exception
   */
  public static boolean sort(Job MRjob, String input, String output, int cutoff)
      throws Exception {

    CUTOFF = cutoff;
    
    MRjob.setJarByClass(TweetSortMR.class);

    // TODO: Set up map-reduce...
    
	// Set key, output classes for the job.
	MRjob.setOutputKeyClass(Text.class);  
	MRjob.setOutputValueClass(IntWritable.class);  

	// Set Mapper and Reducer classes for the job.
	MRjob.setMapperClass(SwapMapper.class);  
	MRjob.setReducerClass(SwapReducer.class);  

	// Sets format of input files.  "TextInputFormat" views files as
	// a sequence of lines.
	
	MRjob.setInputFormatClass(TextInputFormat.class);  
	
	// Sets format of output files:  here, lines of text.
	MRjob.setOutputFormatClass(TextOutputFormat.class);  

	MRjob.setMapOutputKeyClass(IntWritable.class);
	MRjob.setMapOutputValueClass(Text.class);


    
    
    // End

    FileInputFormat.addInputPath(MRjob, new Path(input));
    FileOutputFormat.setOutputPath(MRjob, new Path(output));

    return MRjob.waitForCompletion(true);
  }

}
