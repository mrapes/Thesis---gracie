
package tweets;

import database.Writer;
import database.tweetHandler;
import database.tweetModel;
import java.io.IOException;
import java.util.ArrayList;
import ngram.NGram;
import ngram.NGramDriver;
import static ngram.NGramDriver.NGramfinal;
import static ngram.NGramDriver.sortNgramAndRemoveOutliers;
import tfidf.Tfidf;
import tfidf.TfidfDriver;
import static tfidf.TfidfDriver.getToplist;
import static tweets.TweetCleaner.getTweets;

/**
 *
 * @author Nancy
 */
public class TweetCleaner2 {
    private static ArrayList<tweetModel> tweets;
     private static ArrayList<NGram> ngrams;
     private static ArrayList<Tfidf> tfngrams;

    /**
     * @return the tfngrams
     */
    public static ArrayList<Tfidf> getTfngrams() {
        return tfngrams;
    }

    /**
     * @param aTfngrams the tfngrams to set
     */
    public static void setTfngrams(ArrayList<Tfidf> aTfngrams) {
        tfngrams = aTfngrams;
    }
    public ArrayList<tweetModel> cleanByKeyword(String keyword, String tempParent){
//        tweets = tweetHandler.getAllTweets();
//        System.out.println(tweets.get(9).getMessage());
        
//        tweets = tweetHandler.getAllTweetsByDate("Oct 8 2013", "Nov 10 2013");
        
        setTweets(tweetHandler.TempGetAllTweetsByKeyword(keyword, tempParent));
        writeTweets(getTweets());
        setNgrams(NGramfinal(getTweets()));
        setNgrams(sortNgramAndRemoveOutliers(getNgrams()));
        
            TfidfDriver.idfchecker(getTweets(),getNgrams() );
        setTfngrams(getToplist());
        TweetCleaner.setTfngrams(getToplist());
            TweetCleaner.setNgrams(NGramfinal(getTweets()));
//        for(tweetModel tm : tweets)
//            System.out.println(tm.getMessage());
        
//            System.out.println(tweetHandler.cleanTweet("@moontwink shshhaGAHSAx @djsdsd @mmplusr"));
        return getTweets();
    }
   
    private void writeTweets(ArrayList<tweetModel> tweets){
        String filePath = "src\\tweets.txt";
        
        //Rewrites tweet to text file
        try{
            Writer write = new Writer(filePath, false);
            write.writeToFile("");
            Writer write2 = new Writer(filePath, true);
            for(tweetModel tm : tweets)
                write2.writeToFile(tm.getMessage()+"\n");
//            System.out.print("__! Rewrite Successful! __");
        }catch(IOException ex){
            System.out.println("__! Sorry, No Can Do!");
        }
    }

    /**
     * @return the tweets
     */
    public static ArrayList<tweetModel> getTweets() {
        return tweets;
    }

    /**
     * @param tweets the tweets to set
     */
    public static void setTweets(ArrayList<tweetModel> tweets) {
        TweetCleaner2.tweets = tweets;
    }

    /**
     * @return the ngrams
     */
    public static ArrayList<NGram> getNgrams() {
        return ngrams;
    }

    /**
     * @param ngrams the ngrams to set
     */
    public static void setNgrams(ArrayList<NGram> ngrams) {
        TweetCleaner2.ngrams = ngrams;
    }
}
