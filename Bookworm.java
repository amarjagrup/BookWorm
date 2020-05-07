import java.io.File;
import java.util.Arrays;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
/**
 * This class implements a board game called book worms. The primary purpose of the game is to find words
 * on the boards. In this modified version there is an input file containing the charchters of the board 
 * and the words to look for on the board. The class will then return wheter the word was found on the
 * board or not found. This call uses recursion on the board to search for the word. 
 *
 * @author (Amar Jagrup)
 * @version (2/28/19)
 */
public class Bookworm 
{
    public  ArrayList<String> arrList; // list of words in the file
    public  char[][] charArr; // the board
    boolean contains;
    public ArrayList<String> wordsFound;// arrlist that stores all the words found in the board
    public ArrayList<String> allWords; // arraylist that stores all the words in the text file
    public boolean visit[][];
    /**
     * This method constures a bookworm object. The method reads in the information from the file and
     * sets up the board. Stores all the words to be found in the arraylist and stores the first seven lines
     * of text as the tiles on the grid. 
     *
     * @param words the name of the file to be read
     */
    public Bookworm(String words) throws IOException
    {
        allWords = new ArrayList<String>();
        wordsFound = new ArrayList<String>();
        FileReader file = new FileReader(words +".txt");
        BufferedReader buff = new BufferedReader(file);
        String line = buff.readLine();
        arrList = new ArrayList<String>();
        line = line.replaceAll("\\s",""); 

        char[] cArr = new char[8];
        charArr = new char[7][8];  
        visit = new boolean[7][8];

        // creates the board alternating by length of 7 then 8. also creates the boolean 2d array
        for(int i=0; i<7;i++)
        {
            if(i%2 ==0)
            {
                charArr[i] = new char[7];
                visit[i] = new boolean[7];
            }
            else{
                charArr[i] = new char[8];
                visit[i] = new boolean[8];
            }
        }

        // reads in from the file
        while( line!= null )
        {
            line = line.replaceAll("\\s",""); 
            cArr = line.toCharArray();
            int c = 0; 
            for(int i = 0;i< charArr[0].length;i++)
            {
                for(int j =0; j< line.length();j++)
                {
                    charArr[i][j] = cArr[j];
                }

                c= 0;
                line = buff.readLine();
                cArr = line.toCharArray();
                line = line.replaceAll("\\s","");
                // if we see a blank line break and go to next line
                if(line.equals(""))
                {
                    line = buff.readLine();
                    break;
                }
            }

            // after the blank line there is a list of words, which should be added to the arrayList,arrList
            while( line!= null  )
            {

                line = line.replaceAll("\\s",""); 
                arrList.add(line.toUpperCase());
                allWords.add(line.toUpperCase());
                line = buff.readLine();
            }
        }
        buff.close();

    }

    /**
     * this method finds all words of length 3 or greater on the board. if the word can be made on the board it is found.
     * Uses a for loop that loops over the words in the list, then within that loop there
     * are two for loops that loop through the board.starting with each column, then processing the charchters 
     * row by row for that column. Uses a recusive hepler method to navigate through the board. 
     *
     * 
     */
    public void findWord() throws IOException
    {
        String str = "";
        char[] wordChars = arrList.get(0).toCharArray();
        int count = 0;

        for(int k =arrList.size(); k>0; k--)   
        {
            wordChars = arrList.get(0).toCharArray(); 
            contains = false;
            str = "";
            if(wordChars.length >=3) // only check words of length 3  or greater
            {
                for(int i=0; i<charArr[0].length&& contains ==false; i++) 
                {
                    for(int j = 0; j < charArr.length && contains ==false; j++)
                    {
                        if(wordChars[count] == charArr[i][j])
                        {
                            visit[i][j] = true; // find a match mark that spot in the visit array as true
                            rec(charArr,visit,i,j,str,count); // use recusrsive helper method
                            visit[i][j] = false; // when finished when the search reset the spot in the visit array
                        }
                        str = "";
                    }

                }

            }
            arrList.remove(0); // remove the word from the list
        }

    }

    /**
     * this method finds words on the board by recusively checking if adjacent cells contain the next letter in the word.
     * if the word is found store it in the wordsFound arraylist else return. 12 recursive calls checking the adjacent cells
     * Since the board is not a perfect n x n grid the postion of adjacent cells of the odd columns 
     * differ from the even columns. six calls for the column that is even and six for the odd columns. 
     * @param charArr the board
     * @param visit the board of boolean values used to mark a spot in the board so that it is not visited
     * twice. Marked true if it is a match. marked back false when done searching for word. 
     * @param col the column of the board
     * @param row the row of the board
     * @param w the string that forms the word
     * @param count a counter that is used to check the next letter in the word to be looked at
     */
    public void rec(char[][] charArr, boolean visit[][],int col, int row,String w,int count)
    {
        w= w + charArr[col][row];
        ArrayList<String> l = new ArrayList<String>();
        contains = false;
        int j =count;
        char[] wordChars = arrList.get(0).toCharArray();
        String qu = "QU"; 

        // if the current letter is a q then the next letter has to be a u 
        if( wordChars[count] == 'Q' && wordChars[count+1] == 'U')
        {
            w =  qu;
            j = j+1;
            count = j ;
        }
        else if(wordChars.length > count+1 && wordChars[count+1] == 'Q' && wordChars[count + 2] == 'U')
        {
            w = w+  qu;
        }

        //base case: if we find a match return and add the word to the wordsfound arraylist
        //else the rec method will return nothing 
        if(w.equals(arrList.get(0)))
        {

            contains = true;
            wordsFound.add(w);       
            return;
        }

        // these if statements checks the adjacent cells to find out if the word is found on the board
        if(col %2 ==0 )
        {

            // checks the tile  below it
            if( row <6 && charArr[col][row+1] == wordChars[count+1]&& !visit[col][row+1] ) 
            {
                
                visit[col][row+1] = true;
                rec(charArr,visit,col,row+1,w,count+1); 
                visit[col][row+1]  = false;
            }

            // checks the tile  above it
            if( row > 0 && charArr[col][row-1] == wordChars[count+1]&& !visit[col][row-1]  )  
            {

                visit[col][row-1] = true;
                rec(charArr,visit,col,row-1,w,count+1);
                visit[col][row-1]  = false;
            }

            // checks the tile  to the upper left.
            if( col > 0 && charArr[col-1][row] == wordChars[count+1]&& !visit[col-1][row] ) 
            {
                visit[col-1][row] = true;
                rec(charArr,visit,col-1,row,w,count+1); 
                visit[col-1][row]  = false;
            }

            // if the column does not equal six check the upper right, lower left, and lower right
            else if(  col != 6 )
            {
                // checks the tile  in the upper right
                if(charArr[col+1][row] == wordChars[count+1] && col <6&& !visit[row][col]  )  
                {
                    visit[col+1][row] = true;
                    rec(charArr,visit,col+1,row,w,count+1); 
                    visit[col+1][row]  = false;
                }

                // checks the tile  in the lower left 
                if(col > 0 &&charArr[col-1][row+1] == wordChars[count+1]&& !visit[col-1][row+1] )  
                {
                    visit[col-1][row+1] = true;
                    rec(charArr,visit,col-1,row+1,w,count+1); 
                    visit[col-1][row+1]  = false;
                }

                // checks the tile  in the lower right
                if(charArr[col+1][row+1] == wordChars[count+1] && col < 6&& !visit[col+1][row+1] )  
                {
                    visit[col+1][row+1] = true;
                    rec(charArr,visit,col+1,row+1,w,count+1); 
                    visit[col+1][row+1]  = false;
                }
            }

        }

        else{

            // checks the tile  below it
            if( row <7 && charArr[col][row+1] == wordChars[count+1]&& !visit[col][row+1] )  
            {
             
                visit[col][row+1] = true;
                rec(charArr,visit,col,row+1,w,count+1); 
                visit[col][row+1]  = false;
            }

            // checks the tile  above it
            if(  row > 0 && charArr[col][row-1] == wordChars[count+1]&& !visit[col][row-1] )  
            {
               
                visit[col][row-1] = true;
                rec(charArr,visit,col,row-1,w,count+1); 
                visit[col][row-1]  = false;

            }

            // checks the tile  to the upper left.
            if(  row >0 && col >0 &&charArr[col-1][row-1] == wordChars[count+1] && !visit[col-1][row-1] )  
            {
               
                visit[col-1][row-1] = true;
                rec(charArr,visit,col-1,row-1,w,count+1); 
                visit[col-1][row-1]  = false;
            }

            // checks the tile  in the lower left 
            if( row <7 &&col > 0 && charArr[col-1][row] == wordChars[count+1] && !visit[col-1][row] )  
            {
                
                visit[col-1][row] = true;
                rec(charArr,visit,col-1,row,w,count+1); 
                visit[col-1][row]  = false;

            }

            // checks the tile  in the upper right
            if( row > 0 && col < 6 &&charArr[col+1][row-1] == wordChars[count+1] && !visit[col+1][row-1] )  
            {              
               
                visit[col+1][row-1] = true;
                rec(charArr,visit,col+1,row-1,w,count+1); 
                visit[col+1][row-1]  = false;

            }

            // checks the tile  in the lower right
            if( row < 7 && col < 6 &&charArr[col+1][row] == wordChars[count+1]&& !visit[col+1][row] ) 
            {
              
                visit[col+1][row]  = true;
                rec(charArr,visit,col+1,row,w,count+1); 
                visit[col+1][row]  = false;
            }
        }

    }

    /**
     * prompts the user for the file name and displays wheter the words in the file was found or not 
     *
     * @param args an array of String 
     */
    public static void main(String[]args) throws IOException
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("enter name of text file don't include .txt");
        String line = sc.nextLine();
        Bookworm b = new Bookworm(line);
        char bookWorm[][] = new char[0][0];
        char[][]c = b.charArr;

        b.findWord();

        // for all words in the allWords arraylist, if the word is in wordsFound arraylist print the word is found, else
        // print the word is not found
        for(String w: b.allWords)
        {
            if(b.wordsFound.contains(w))
            {
                System.out.println(w + " is found");
            }
            else
            {
                System.out.println(w + " is not found" );
            }
        }

    }
}
