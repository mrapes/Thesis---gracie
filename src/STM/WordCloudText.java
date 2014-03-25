/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package STM;
import javax.swing.*;

public class WordCloudText
{
  // private instance variables
  private static String text;
  private static String[] words;
  
  public WordCloudText()
  {
    // gets user-entered text
    text = JOptionPane.showInputDialog("Please enter text:");
    // removes all spaces in the string
    words = text.split("\\s+");
  }
  
  // sets the words in the string array
  public void setWords(String[] strArray)
  {
    words = strArray;
  }
  
  // returns the words in the string array
  public String[] getWords()
  {
    return words;
  }
  
  // sets the text
  public void setText(String str)
  {
    text = str;
  }
  
  // returns the text
  public String getText()
  {
    return text;
  }
}