
package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ngram.NGramDriver;

/**
 *
 * @author Nancy
 */
public class tweetHandler {
    
    //Adds tweet to database
    
//    public ArrayList<DBtemp> temptable = new ArrayList<>(); 
    
    
        
    public static String addTweet(tweetModel tm){
        String message = "* Saving Failed.";
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("INSERT INTO `Tweets` "
                    + "(statusId, username, message, retweetcount, latitude, longhitude, date) VALUES (?,?,?,?,?,?,?)"); 
            
            ps.setString(1, tm.getStatusId());
            ps.setString(2, tm.getUsername());
            ps.setString(3, tm.getMessage());
            ps.setString(4, tm.getRetweetCount());
            ps.setDouble(5, tm.getLatitude());
            ps.setDouble(6, tm.getLonghitude());
            ps.setString(7, tm.getDate());
            
            int i = ps.executeUpdate();
            
            if (i == 1) {
                message = "* Saving successful.";
            }
            
            ps.close();
            c.close();
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return message;
        
    }
    
    //Rewrites tweet to text file in UTF-8 format
    public static String RewriteTweet(String tweet){
        String filePath = "C:\\Users\\JOY\\Desktop\\Twitter API\\Twitter4j\\twitter4j\\twitter4j-stream\\writetweet.txt";
        String tweetLine = tweet;
        
        //Rewrites tweet to text file
        try{
            Writer write = new Writer(filePath, false);
            write.writeToFile(tweet);
//            System.out.print("__! Rewrite Successful! __");
        }catch(IOException ex){
            System.out.println("__! Sorry, No Can Do!");
        }
      
        //Reads tweet as pure text
        Reader read = new Reader(filePath);
        read.OpenFile();
        tweetLine = read.ReadFile();
        
        return tweetLine;
    }
    
    //Normalizes tweet
    public static String normalizeTweet(String tweet){
        String tweetLine = tweet.toLowerCase();
        return tweetLine;
    }
    
    //Cleans tweet
    public static String cleanTweet(String tweet){
        tweet = normalizeTweet(tweet);
        
        while(tweet.contains("@")){
            String mention = "";
            int atindex = tweet.indexOf("@");
            
            while(tweet.charAt(atindex) != ' '){
                mention = mention.concat(tweet.charAt(atindex)+"");
                atindex++;
                if(atindex >= tweet.length())
                    break;
            }
//            System.out.println(mention);
            tweet = tweet.replace(mention, "").trim();
            
        }
        return tweet;
    }
    
    //Retrieves all Tweets
    public static ArrayList<tweetModel> getAllTweets(){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        tweetModel t;
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM tweets ");
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                t = new tweetModel();
                t.setIdTweets(rs.getInt("idTweets"));
                t.setStatusId(rs.getString("statusId"));
                t.setUsername(rs.getString("username"));
                t.setMessage(cleanTweet(rs.getString("message")));
                t.setRetweetCount(rs.getString("retweetCount"));
                t.setLatitude(rs.getLong("latitude"));
                t.setLonghitude(rs.getLong("longhitude"));
                t.setDate(rs.getString("date"));
                
                results.add(t);
            }
            
            rs.close();
            ps.close();
            c.close();
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return results;
    }
    public static ArrayList<tweetModel> getAllTweetsByKeywordAndDate(String keywords, String startDate, String endDate){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        
        String[] start = startDate.split(" ");
        String[] end = endDate.split(" ");
        tweetModel t;
        
          
       String tablename = keywords + "(" + start[1]
                        + "-" + end[1] + " "
                        + start[2] + ")";
       tablename = tablename.replaceAll(",", "");
       tablename = tablename.replaceAll(";", "");
       tablename = tablename.replaceAll(" ", "");
        System.out.println(tablename);
         keywords = keywords.replaceAll(",", "%\' and message like \'%");   
          System.out.println(keywords);
       keywords = keywords.replaceAll(";", "%\' or message like \'%"); 
          System.out.println(keywords);
 

//        System.out.println(start[0] + " " + end[0]);
//        startDate= start[1]+" "+start[0]+" "+start[2]+"";
//        endDate= end[1]+" "+end[0]+" "+end[2];
        System.out.println(start[0] + " " + end[0]);
        String whereCondition = "'" + start[1] + " " + start[0] + " " + start[2] + "%'";
        System.out.println("[1] "+whereCondition);
        
        int year = Integer.parseInt(start[2]);
//        for(int year = Integer.parseInt(start[2]); year <= Integer.parseInt(end[2]); year++){
            for(int month = monthNumber(start[0]); month <= monthNumber(end[0]); month++){
                int currentday = 1;
                if(month == monthNumber(start[0]))
                    currentday = Integer.parseInt(start[1]);
                
                System.out.println("[2.5] "+whereCondition);
                for(int day = currentday; day <= numDaysinMonth(month); day++){
                    if(month == monthNumber(end[0]) && day > Integer.parseInt(end[1]))
                        break;
                    whereCondition = whereCondition.concat(" or date like '" + day + " " + monthName(month) + " " + year +"%'");
                    System.out.println("[2] "+whereCondition);
                }
            }
            System.out.println(whereCondition);
          try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("CREATE TEMPORARY TABLE "+ tablename
                    + " SELECT * FROM tweets " 
                    + "WHERE (message like '%" + keywords + "%')"
                    + "and (date like "+whereCondition+" )" 
                    );
            
            System.out.println("hiiii :D "+ps);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                t = new tweetModel();
                t.setIdTweets(rs.getInt("idTweets"));
                t.setStatusId(rs.getString("statusId"));
                t.setUsername(rs.getString("username"));
                t.setMessage(cleanTweet(rs.getString("message")));
                t.setRetweetCount(rs.getString("retweetCount"));
                t.setLatitude(rs.getLong("latitude"));
                t.setLonghitude(rs.getLong("longhitude"));
                t.setDate(rs.getString("date"));
                
                results.add(t);
            }
            
            rs.close();
            ps.close();
            c.close();
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return results;
    }
     public static String select(String tablename){

        
                    String ps = " SELECT * FROM" + tablename  ;
           
        return ps;
    }
    //Retrieves all Tweets via Keywords
    public static ArrayList<tweetModel> getAllTweetsByKeyword(String keywords){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        tweetModel t;
       // ArrayList<Integer> conditionals = new ArrayList<Integer>();
        
        
//       while(keywords.contains(",") || keywords.contains(";")){
//            int commaindex = keywords.indexOf(",");
//            int colonindex = keywords.indexOf(";");
//            
//            if(commaindex != -1)
//                conditionals.add(commaindex);
//            if(colonindex != -1)
//                conditionals.add(colonindex);
//            
////            if(commaindex < colonindex && commaindex != -1)
////                conditionals.add(',');
////            else
////                conditionals.add(';');
//        }
          
       String tablename = keywords;
       tablename = tablename.replaceAll(",", "");
       tablename = tablename.replaceAll(";", "");
       tablename = tablename.replaceAll(" ", "");
        System.out.println("hiiiii");
       keywords = keywords.replaceAll(",", "%\' and message like \'%");   
          System.out.println(keywords);
       keywords = keywords.replaceAll(";", "%\' or message like \'%"); 
          System.out.println(keywords);
          
//        for(int index=0; index<=keywords.length();index++ ){
//        
//            if(index==conditionals.get(index))
//                if(keywords.charAt(index) == ',')
//                    
//                else
//                   
//            
//        }
        System.out.println(keywords);
//        if(keywords.contains(","))
//            keywords = keywords.replace(",", "AND");
//        else if(keywords.contains(";"))
//            keywords = keywords.replace(";", "OR");
         
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                    "CREATE TABLE "+ tablename
                    + " SELECT * FROM tweets " 
                    + "WHERE message like '%" + keywords + "%'" 
                    );
            System.out.println(ps);
             ps.executeUpdate();
            PreparedStatement pss = c.prepareStatement(
                     " SELECT * FROM " + tablename 
            );
            ResultSet rs = pss.executeQuery();
            while(rs.next()){
                t = new tweetModel();
                t.setIdTweets(rs.getInt("idTweets"));
                t.setStatusId(rs.getString("statusId"));
                t.setUsername(rs.getString("username"));
                t.setMessage(cleanTweet(rs.getString("message")));
                t.setRetweetCount(rs.getString("retweetCount"));
                t.setLatitude(rs.getLong("latitude"));
                t.setLonghitude(rs.getLong("longhitude"));
                t.setDate(rs.getString("date"));
                
                results.add(t);
            }
            
            rs.close();
            ps.close();
            c.close();
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return results;
    }
    
    //Retrieves all Tweets via Date
    public static ArrayList<tweetModel> getAllTweetsByDate(String startDate, String endDate){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        String[] start = startDate.split(" ");  //[0] month, [1] day, [2] year
        String[] end = endDate.split(" ");
        tweetModel t;
        
        System.out.println(start[0] + " " + end[0]);
        String whereCondition = "'" + start[1] + " " + start[0] + " " + start[2] + "%'";
        System.out.println("[1] "+whereCondition);
        
        int year = Integer.parseInt(start[2]);
//        for(int year = Integer.parseInt(start[2]); year <= Integer.parseInt(end[2]); year++){
            for(int month = monthNumber(start[0]); month <= monthNumber(end[0]); month++){
                int currentday = 1;
                if(month == monthNumber(start[0]))
                    currentday = Integer.parseInt(start[1]);
                
                System.out.println("[2.5] "+whereCondition);
                for(int day = currentday; day <= numDaysinMonth(month); day++){
                    if(month == monthNumber(end[0]) && day > Integer.parseInt(end[1]))
                        break;
                    whereCondition = whereCondition.concat(" or date like '" + day + " " + monthName(month) + " " + year +"%'");
                    System.out.println("[2] "+whereCondition);
                }
            }
//        }
        System.out.println("[3] " + whereCondition);
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM tweets " 
                    + "WHERE date like " + whereCondition);
            //SELECT * FROM `Seasons` WHERE (date_field BETWEEN '2010-01-30 14:15:55' AND '2010-09-29 10:15:55')
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                t = new tweetModel();
                t.setIdTweets(rs.getInt("idTweets"));
                t.setStatusId(rs.getString("statusId"));
                t.setUsername(rs.getString("username"));
                t.setMessage(cleanTweet(rs.getString("message")));
                t.setRetweetCount(rs.getString("retweetCount"));
                t.setLatitude(rs.getLong("latitude"));
                t.setLonghitude(rs.getLong("longhitude"));
                t.setDate(rs.getString("date"));
                
                
                results.add(t);
            }
            
            rs.close();
            ps.close();
            c.close();
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        return results;
    }
    
    //Converts Month to its Number Equivalent
    private static int monthNumber(String month){
        int monthnum = 0;
        
        switch(month){
            case "Jan": return 1;
            case "Feb":return 2;
            case "Mar":return 3;
            case "Apr":return 4;
            case "May":return 5;
            case "Jun":return 6;
            case "Jul":return 7;
            case "Aug":return 8;
            case "Sep":return 9;
            case "Oct":return 10;
            case "Nov":return 11;
            case "Dec":return 12;
            default: return monthnum;
        }
    }
    
    //Converts Month Number to its name equivalent
    private static String monthName(int month){
        String name = " ";
        
        switch(month){
            case 1: return "Jan";
            case 2: return "Feb";
            case 3: return "Mar";
            case 4: return "Apr";
            case 5: return "May";
            case 6: return "Jun";
            case 7: return "Jul";
            case 8: return "Aug";
            case 9: return "Sep";
            case 10: return "Oct";
            case 11: return "Nov";
            case 12: return "Dec";
            default: return name;
        }
    }
    
    //Returns number of days in a month
    private static int numDaysinMonth(int month){
        int numdays = 30;
        
        switch(month){
            case 1: return 31;
            case 2: return 28;
            case 3: return 31;
            case 4: return 30;
            case 5: return 31;
            case 6: return 30;
            case 7: return 31;
            case 8: return 31;
            case 9: return 30;
            case 10: return 31;
            case 11: return 30;
            case 12: return 31;
            default: return numdays;
        }
    }
    
    public static ArrayList<tweetModel> getAllRetweets(){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        tweetModel t;
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM tweets "
                    + "WHERE message like 'RT%' "
                    + "LIMIT 0,10");
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                t = new tweetModel();
                t.setIdTweets(rs.getInt("idTweets"));
                t.setStatusId(rs.getString("statusId"));
                t.setUsername(rs.getString("username"));
                t.setMessage(cleanTweet(rs.getString("message")));
                t.setRetweetCount(rs.getString("retweetCount"));
                t.setLatitude(rs.getLong("latitude"));
                t.setLonghitude(rs.getLong("longhitude"));
                t.setDate(rs.getString("date"));
                
                results.add(t);
            }
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return results;
    }
    
    public static Boolean checkTweet(String username, String message){
        tweetModel tw = null;
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT username, message FROM tweets "+
                    "WHERE username = ? and message = ?");
            ps.setString(1, username);
            ps.setString(2, message);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                tw = new tweetModel();
                tw.setUsername(rs.getString(1));
                tw.setMessage(rs.getString(2));
            }
            
            if(tw == null){
                return true;    //tweet doesn't exist
            }
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public static String getEarliestDate(){
        String date = "";
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT date FROM tweets "
                    + "ORDER BY idtweets ASC "
                    + "LIMIT 1;");
            
            ResultSet rs = ps.executeQuery();
            rs.next();
            date = rs.getString("date").substring(0, 11).trim();
        
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return date;
    }
    
    public static String getLatestDate(){
        String date = "";
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT date FROM tweets "
                    + "ORDER BY idtweets DESC "
                    + "LIMIT 1;");
            
            ResultSet rs = ps.executeQuery();
            rs.next();
            date = rs.getString("date").substring(0, 11).trim();
        
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return date;
    }
    
     public static ArrayList<tweetModel> TempGetAllTweetsByKeyword(String keywords, String tempParent){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        tweetModel t;
        
       String tablename = keywords;
       tablename = tablename.replaceAll(",", "");
       tablename = tablename.replaceAll(";", "");
       tablename = tablename.replaceAll(" ", "");
       tempParent = tempParent.replaceAll("-", "");
       tempParent = tempParent.replaceAll("LM", "");
       tempParent = tempParent.replaceAll(" ", "");
            keywords = keywords.replaceAll(",", "%\' and message like \'%");   
         
            keywords = keywords.replaceAll(";", "%\' or message like \'%"); 
       
       
          try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("CREATE TABLE "+ tablename
                    + " SELECT * FROM "
                    + tempParent
                    + " WHERE message like '%" + keywords + "%'" );
            System.out.println(ps);
            ps.executeUpdate();
            ps = c.prepareStatement(
                    " SELECT * FROM "
                    + tablename );
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                t = new tweetModel();
                t.setIdTweets(rs.getInt("idTweets"));
                t.setStatusId(rs.getString("statusId"));
                t.setUsername(rs.getString("username"));
                t.setMessage(cleanTweet(rs.getString("message")));
                t.setRetweetCount(rs.getString("retweetCount"));
                t.setLatitude(rs.getLong("latitude"));
                t.setLonghitude(rs.getLong("longhitude"));
                t.setDate(rs.getString("date"));
                
                results.add(t);
            }
            
            rs.close();
            ps.close();
            c.close();
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }
    
    
    
    
    
    
}
