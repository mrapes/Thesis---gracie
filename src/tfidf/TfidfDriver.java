/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tfidf;

/**
 *
 * @author Matt
 */

import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.lang.Math;
import database.tweetModel;
import ngram.NGram;
import ngram.NGramDriver;

public class TfidfDriver {
  //  private static ArrayList<ngram.NGram> ngrammy; //contains the list of the ngrams and the frequency counts
    private static ArrayList <Tfidf> toplist; // contains the list of the top ngrams given tf-idf scores
    
    public static void idfchecker(ArrayList<tweetModel> newList, ArrayList <NGram> ngrammy)//gets the idf element by checkin the ngram results against the filtered corpus
    {
        int count=0;
        //ngrammy = ngrammy;    //list of ngrams
            System.out.println("*****>>> " + ngrammy + "\n\t " + newList.size());
        toplist = new ArrayList<>();
        String tweet = "";
        
        for (int i = 0; i< ngrammy.size(); i++)
        {
            for(int j=0; j < newList.size(); j++)
            {
                tweet = newList.get(j).getMessage().replaceAll("[^a-zA-Z0-9]", " ");
                tweet = tweet.replaceAll("\\s+", " ");
//                    System.out.println("$$$ " + tweet);
                    
                if(tweet.contains(ngrammy.get(i).getTweet()))
                {
                        System.out.println("%%%%%%%%%%%%% " + tweet);
                    count++;
                        System.out.println("_______>>> " + count + "\n\t[" + ngrammy.get(i).getTweet() +"]" +
                            "\n\t " + newList.get(j).getMessage());
                }
            }
            tfidfscore(i, count,newList.size(), ngrammy);
            count = 0;
        }
        printTopList();
    }
    
    public static void tfidfscore(int ngramindex, int count, int tweetListCount, ArrayList<NGram> ngrammy) //compute for the tf-idf scores
    {
//        tf * log(idf)
        ngrammy = ngram.NGramDriver.getNgramlist();    //list of ngrams
        String tweet = NGramDriver.cleanFunctionWordsFromTweet(ngrammy.get(ngramindex).getTweet());
        
        if(tweet.length()==0);
        else{
            double tfscore = 0;
            if(count == 0) count = 1;
        
            //System.out.println("\t\t___tweetlistcount______ "+tweetListCount);

            tfscore = ngrammy.get(ngramindex).getFrequency()*java.lang.Math.log10(tweetListCount/count);
                System.out.println("\t\t[["+ngrammy.get(ngramindex).getTweet()+"]] has "+count);
                System.out.println("\t\t_frequency_ "+ngrammy.get(ngramindex).getFrequency());
                System.out.println("\t\t___tfscore___ "+ngrammy.get(ngramindex).getFrequency()*java.lang.Math.log10(tweetListCount/count));

            Tfidf newtf = new Tfidf(tweet, tfscore);
            getToplist().add(newtf);
        }
    }
    
    private static void printTopList(){
        sorttoplist(getToplist());
        
        for(Tfidf tf : getToplist()){
            System.out.println("\t\t\t[[" + tf.getTweet() +"]] == " + tf.getScore());
        }
    }
    
    public static void sorttoplist(ArrayList<Tfidf> list){

        Collections.sort(list, new MyComparator());

    }

    /**
     * @return the toplist
     */
    public static ArrayList <Tfidf> getToplist() {
        return toplist;
    }
    
    public static class MyComparator implements Comparator<Tfidf> {
   
        @Override
        public int compare(Tfidf o1, Tfidf o2) {
          
            try{
                if (o1.getScore() > o2.getScore()) {
                    return -1;
                } else if (o1.getScore() < o2.getScore()) {
                    return 1;
                }
            }catch(Exception e){
                System.err.println(e.toString());
        }
            return 0;
        }
    }
}
