// imports

package STM;

import gui.DD_Level2;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.Rectangle;
import tfidf.Tfidf;
import tweets.TweetCleaner;

public class WordCloud
{
  // string array
  private static String[] words;
  // integer arrays to hold the occurences of each word
  private static Integer[] times, timesOld;
  // creates new Random object
  private static Random r = new Random();
  // four ints with a value of 0, used in the code to determine when to perform certain actions
  private static int fontSize = 0;
  private static int cont = 0;
  private static int normalizer = 0;
  private static int first = 0;
  // new array list of Rectangle objects
  private static ArrayList<Rectangle> rect = new ArrayList<Rectangle>();
  // new array list of String objects
  private static ArrayList<String> wordsList = new ArrayList<String>();
  // random rgb values to create a random color
  private static int red = r.nextInt(256);
  private static int green = r.nextInt(256);
  private static int blue = r.nextInt(256);
  // gets the graphics environment and uses it to retrieve all of the fonts avaliable (stored in a Font array)
  private static GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
  private static Font[] fonts = e.getAllFonts();
   private static String font;

    /**
     * @return the font
     */
    public static String getFont() {
        return font;
    }
  public WordCloud(ArrayList<Tfidf> tweets)
  { 
    // new random number created within the amount of fonts avaliable
    int num = r.nextInt((fonts.length)+1);
    // get the string name of the randomly selected font
    font = fonts[num].getName();
    // array of bad fonts
    String[] badFonts = {"Wingdings", "Wingdings 2", "Wingdings 3", "Webdings", "WP Arabic Sihafa", "WP ArabicScript Sihafa",
      "WP BoxDrawing", "WP CyrillicA", "WP CyrillicB", "WP Greek Century", "WP Greek Courier", "WP Greek Helve",
      "WP Hebrew David", "WP IconicSymbolsA", "WP IconicSymbolsB", "WP Japanese", "WP MathA", "WP MathB", 
      "WP MathExtendedA", "WP MathExtendedB", "WP MultinationalA Courier", "WP MultinationalA Helve", "WP MultinationalA Roman",
      "WP MultinationalB Courier", "WP MultinationalB Helve", "WP MultinationalB Roman", "WP Phonetic", "WP TypographicSymbols",
      "ZWAdobeF", "TI84EmuKeys", "AIGDT", "AMGDT", "AcadEref", "Euclid", "Euclid Bold", "Euclid Bold Italic", "Euclid Extra",
      "Euclid Extra Bold", "Euclid Fraktur", "Euclid Fraktur Bold", "Euclid Italic", "Euclid Math One", "Euclid Math One Bold",
      "Euclid Math Two", "Euclid Math Two Bold", "Euclid Symbol", "Euclid Symbol Bold", "Euclid Symbol Bold Italic", 
      "Euclid Symbol Italic", "CityBlueprint"};
    // creates an array list from the bad fonts array
    ArrayList<String> badFontsList = new ArrayList<String>(Arrays.asList(badFonts));
    // boolean which keeps the loop running
    boolean b = true;
    // will make sure that none of the bad fonts can be selected
    while(b == true)
    {
      // if the random font is one of the bad fonts then generate a new one
      if(badFontsList.contains(getFont()) == true)
      {
        font = fonts[num].getName();
      }
      // break from the loop when the font is a good font
      else
      {
        b = false;
      }
    }
     for(int i=0;i<tweets.size();i++){                        
                     words[i] = tweets.get(i).getTweet();
                    // ,tm.getScore()
               }
    
    
    
    // new JFrame
    JFrame frame = new JFrame("Word Cloud Generator 2013 Kieran Harrigan");
    // when "X" is pressed, the program terminates
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // new panel
    //JPanel panel = new JPanel();
    // null layout is used so that the JLabels can be set to a random coordinate
   // panel.setLayout(null);
    // the panel color is set to white
    //panel.setBackground(Color.white);
    // sets the dimensions of the panel
    //panel.setPreferredSize(new Dimension(745, 265));
    // gets and delimits input
    //WordCloudText t = new WordCloudText();
   // words = 
    // determines the frequencies of each word
    WordCloud.textSize();
    // sorts the frequencies in descending order
    WordCloud.sortSize();
    // displays everything
   // WordCloud.display(DD_Level2.wordcloud, font);
    // adds panel to the frame
   // frame.getContentPane().add(panel);
    // packs everything together
    //frame.pack();
    // sets frame visible
    //frame.setVisible(true);
  }
  
  public static void textSize()
  {
    // new array list from the string array words, because array lists are easier to work with
    ArrayList<String> wordsListAll = new ArrayList<String>(Arrays.asList(words)); 
    // creates a new hash set from the array list of entered words, because hash set does not allow for duplicates
    HashSet<String> h = new HashSet<String>(wordsListAll);
    // takes all the values from the newly created hash set and copies it back to an array list
    wordsList.addAll(h);
    // since every word occurs at least once, this sets all default number for times to 1
    times = new Integer[wordsList.size()];
    for(int k = 0; k < wordsList.size(); k++)
    {
      times[k] = 1;
    }
    // gets the amount of times each word occurs and saves it to the corresponding slot in times
    for(int i = 0; i < wordsList.size(); i++)
    {  
      times[i] = Collections.frequency(wordsListAll, wordsList.get(i));
    }
  }
  
  public static void sortSize()
  {
    // copies the integer array times to a new array
    timesOld = Arrays.copyOf(times, times.length);
    // because Arrays.sort() generally sorts an array in ascending order, we use Collections.reverseOrder()
    // in order to reverse the sorting process, so instead it sorts in decending order
    Arrays.sort(times, Collections.reverseOrder());
  }
  
  public static void display(JLayeredPane p, String font)
  {
    // loops for the length of how many non-repeating words there are
    for(int q = 0; q < wordsList.size(); q++)
    {
      // default multiplier
      int multi = 14;
      // default bounds values
      int x = 0;
      int y = 0;
      int height, width;
      // array list of the times each word occurs
      ArrayList<Integer> timesList = new ArrayList<Integer>(Arrays.asList(times));
      // creates a new integer to hold the position of the word that corresponds to its correct frequency
      // because there isn't a way to sort the word array based on the integer array
      int wordsListPosition = Arrays.asList(timesOld).indexOf(times[q]);
      // number of words that only occur once
      int singleFreq = Collections.frequency(timesList, new Integer(1));
      // corrects the multiplier based upon how many total words there are to be displayed or if the single word count
      // is greater than 100
      if((wordsList.size() >= 500) && (wordsList.size() < 1000) || (singleFreq > 100))
      {
        multi = 8;
      }
      if((wordsList.size() > 1000))
      {
        multi = 6;
      }
      // the font size is equal to the frequency at q position in the array multiplied by the multiplier
      fontSize = times[q] * multi;
      // new color from random values
      Color background = new Color(red, green, blue);
      // new JLabel created from the word at the corresponding position based upon the times array
      JLabel word = new JLabel(wordsList.get(wordsListPosition));
      // sets font color
      word.setForeground(background);
      // sets font, style, and size
      word.setFont(new Font(font, Font.PLAIN, fontSize));
      // creates new FontMetrics object to determine the bounds of the word
      FontMetrics fm = word.getFontMetrics(word.getFont());
      // gets the height of the word
      height = fm.getHeight();
      // gets the width of the word
      width = fm.stringWidth(wordsList.get(wordsListPosition));
      // if an exception has already been found, resize the rest of the words
      if((cont == 1) || (wordsList.size() >= 200))
      { 
        // creates a rectangle for the word that has been rescaled
        Rectangle rescaled = WordCloud.rescaleFont(p, fontSize, wordsListPosition, word, font);
        // new height, width, x value, and y value for the word
        height = (int)rescaled.getHeight();
        width = (int)rescaled.getWidth();
        x = (int)rescaled.getX(); 
        y = (int)rescaled.getY(); 
        // new rectangle created from the new "rescaled" parameters
        Rectangle correctRect = WordCloud.checkRect(q, x, y, width, height);
        // new height, width, x value, and y value for the word
        height = (int)correctRect.getHeight();
        width = (int)correctRect.getWidth();
        x = (int)correctRect.getX(); 
        y = (int)correctRect.getY(); 
        // sets the bounds for the JLabel
        word.setBounds(x, y, width, height);
        // turns the frequency of the just displayed word to zero so it can't be found again if there are multiples
        // words with the same frequency. zero is used because each word occurs at least once.
        timesOld[wordsListPosition] = 0;
        // displays the word
        p.add(word);
      }
      // if an exception hasn't been found yet continue
      if(cont == 0)
      {
        // tries for an IllegalArgumentException
        try
        {
          // generates random coordinates within the bounds of the GUI
          x = r.nextInt(746-width);
          y = r.nextInt(266-height);
          Rectangle correctRect = WordCloud.checkRect(q, x, y, width, height);
          height = (int)correctRect.getHeight();
          width = (int)correctRect.getWidth();
          x = (int)correctRect.getX(); 
          y = (int)correctRect.getY(); 
          word.setBounds(x, y, width, height);
          timesOld[wordsListPosition] = 0;
          p.add(word);
        }
        // catches an IllegalArgumentException, in this program a "n is not a positive integer" is thrown when
        // the font size is too large
        catch(IllegalArgumentException e)
        {
          Rectangle rescaled = WordCloud.rescaleFont(p, fontSize, wordsListPosition, word, font);
          height = (int)rescaled.getHeight();
          width = (int)rescaled.getWidth();
          x = (int)rescaled.getX(); 
          y = (int)rescaled.getY(); 
          Rectangle correctRect = WordCloud.checkRect(q, x, y, width, height);
          height = (int)correctRect.getHeight();
          width = (int)correctRect.getWidth();
          x = (int)correctRect.getX(); 
          y = (int)correctRect.getY(); 
          word.setBounds(x, y, width, height);
          timesOld[wordsListPosition] = 0;
          p.add(word);
        }
      }
    }
  }
  
  public static Rectangle rescaleFont(JLayeredPane p, int f, int pos, JLabel w, String font)
  {
    int scale = f;
    // new temporary rectangle
    Rectangle temp = new Rectangle();
    int height, width;
    // number to divide by
    int divisor = 300;
    // if the exception has been found for one word already this will continue it for the rest of them
    if(cont == 1)
    {
      // the new font size is set in proportion to the scale
      scale /= normalizer;
      w.setFont(new Font(font, Font.PLAIN, scale));
      FontMetrics fm = w.getFontMetrics(w.getFont());
      height = fm.getHeight();
      width = fm.stringWidth(wordsList.get(pos));
      int x = r.nextInt(746-width);
      int y = r.nextInt(266-height);
      temp = new Rectangle(x, y, width, height);
    } 
    // if it is the first time running this method
    if(cont == 0)
    {
      // sets the maximum font size accordingly
      if((wordsList.size() >= 400) && (wordsList.size() <= 1000))
      {
        divisor = 200;
      }
      if(wordsList.size() > 1000)
      {
        divisor = 100;
      }
      // tries for an ArithmeticException
      try
      {
        normalizer = scale/divisor;
        scale /= normalizer;
      }
      // catches an ArithmeticException, in this program a "/ by zero" is thrown when
      // the most frequent occuring word is not frequent enough compared to the total amount of words
      catch(ArithmeticException e)
      {
        // keeps the maximum font size the same
        normalizer = 1;
        scale /= normalizer;
      }
      w.setFont(new Font(font, Font.PLAIN, scale));
      FontMetrics fm = w.getFontMetrics(w.getFont());
      height = fm.getHeight();
      width = fm.stringWidth(wordsList.get(pos));
      int x = r.nextInt(746-width);
      int y = r.nextInt(266-height);
      temp = new Rectangle(x, y, width, height);
      // ensures this part of the method won't occur twice
      cont = 1;
    }
    // returns the temporary rectangle object
    return temp;
  }
  
  public static Rectangle checkRect(int q, int x, int y, int width, int height)
  {
    int count = 0;
    Rectangle temp = new Rectangle(x, y, width, height);
    rect.add(q, temp);
    // boolean to keep the loop running
    boolean b = true;
    while(b == true)
    {
      // loops for the length of the rectangle array
      for(int r = 0; r < rect.size(); r++)
      {
        // because there are no other words displayed yet, the position does not need to be altered
        if(first == 0)
        {
          first = 1;
          return temp;
        }
        // checks if any other the words intersect the temporary rectangle
        if(rect.get(r).intersects(temp))
        {
          count++;
        }
      }
      // if any of the rectangles intersect, new coordinates are generated
      if(count > 0)
      {
        int x1 = r.nextInt(746-width);
        int y1 = r.nextInt(266-height);
        temp = new Rectangle(x1, y1, width, height);
        count = 0;
      }
      // if none of the rectangles intersect, break from the loop
      else
      {
        b = false;
      }
    }
    // adds the temporary rectangle to the end of the array
    rect.set(q, temp);
    return temp;
  }
}