
package tweets;

import database.Writer;
import database.tweetHandler;
import database.tweetModel;
import java.io.IOException;
import java.util.ArrayList;
import ngram.NGram;
import static ngram.NGramDriver.NGramfinal;
import static ngram.NGramDriver.sortNgramAndRemoveOutliers;
import tfidf.Tfidf;
import tfidf.TfidfDriver;
import static tfidf.TfidfDriver.getToplist;
import static tweets.TweetCleaner2.getNgrams;
import static tweets.TweetCleaner2.getTweets;
import static tweets.TweetCleaner2.setNgrams;
import static tweets.TweetCleaner2.setTfngrams;

/**
 *
 * @author Nancy
 */
public class TweetCleaner {
    private static ArrayList<tweetModel> tweets;
    private static ArrayList<NGram> Ngrams;
    private static ArrayList<Tfidf> Tfngrams;

    /**
     * @return the Tfngrams
     */
    public static ArrayList<Tfidf> getTfngrams() {
        return Tfngrams;
    }

    /**
     * @param aTfngrams the Tfngrams to set
     */
    public static void setTfngrams(ArrayList<Tfidf> aTfngrams) {
        Tfngrams = aTfngrams;
    }
    public ArrayList<tweetModel> cleanByKeyword(String keyword){
//        tweets = tweetHandler.getAllTweets();
//        System.out.println(tweets.get(9).getMessage());
        
//        tweets = tweetHandler.getAllTweetsByDate("Oct 8 2013", "Nov 10 2013");
        
        setTweets(tweetHandler.getAllTweetsByKeyword(keyword));
        writeTweets(getTweets());
         setNgrams(NGramfinal(getTweets()));
         TweetCleaner2.setNgrams(NGramfinal(getTweets()));
         
          writeTweets(getTweets());
        setNgrams(NGramfinal(getTweets()));
        setNgrams(sortNgramAndRemoveOutliers(getNgrams()));
        
            TfidfDriver.idfchecker(getTweets(),getNgrams() );
        setTfngrams(getToplist());
        TweetCleaner2.setTfngrams(getToplist());
         TweetCleaner2.setNgrams(NGramfinal(getTweets()));
         
//        for(tweetModel tm : tweets)
//            System.out.println(tm.getMessage());
        
//            System.out.println(tweetHandler.cleanTweet("@moontwink shshhaGAHSAx @djsdsd @mmplusr"));
        return getTweets();
    }
    
    public ArrayList<tweetModel> cleanByDate(String start, String end){
        setTweets(tweetHandler.getAllTweetsByDate(start, end));
        writeTweets(getTweets());
        return getTweets();
    }
    
    public ArrayList<tweetModel> cleanByKeywordsAndDate(String keywords, String start, String end){
        setTweets(tweetHandler.getAllTweetsByKeywordAndDate(keywords, start, end));
        writeTweets(getTweets());
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
        TweetCleaner.tweets = tweets;
    }

    /**
     * @return the Ngrams
     */
    public static ArrayList<NGram> getNgrams() {
        return Ngrams;
    }

    /**
     * @param Ngrams the Ngrams to set
     */
    public static void setNgrams(ArrayList<NGram> Ngrams) {
        TweetCleaner.Ngrams = Ngrams;
    }
}
