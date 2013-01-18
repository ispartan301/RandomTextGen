import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;

//*********************************************************************
//Chain class: main data structure for creating the 3-words chains repository
//    public method returns the random text sentence picked out of the chains
//    repository.
//    HashTable<key>: String of "1st 2nd"
//             <value>: LinkedList of Strings of "3rd"
//@author: Victor Lu
//@date: March 10, 2008

public class Chain
{
    //local variables: Hashtable is fast for matching the key(1st & 2nd words)
    private Hashtable<String, LinkedList<String>> chain_db;
    //seperate structure to store the keys which contain the start code
    //which is "0start" in this program, this is for the randomization purpose
    private LinkedList<String> start_keys;

    //*******************************************************************
    //Chain constructor: initialize a empty chain repository
    //@param: none
    //@return: Chain
    public Chain()
    {
        chain_db = new Hashtable<String, LinkedList<String>>();
        start_keys = new LinkedList<String>();
    }

    //*******************************************************************
    //loadChain function: convert the input sentence string into chains
    //    and add the chain into the chains repository.
    //@param: sentence (String)
    //@return: void
    public void loadChain(String sentence)
    {
        String[] tokens = sentence.split(" +"); //split sentence into words
        String ch_key, ch_val;
        LinkedList<String> ch_vlist;

        for(int i=0;i<tokens.length-1;i++) //look into every words
        {
            ch_key = "";
            ch_val = "";
            ch_vlist = new LinkedList<String>();
            if (i == 0) //store the beginning of the sentence
            {
                ch_key = "0start" + " " + tokens[0];
                ch_val = tokens[1].toLowerCase();
                //starting combo store in data structure other than the main
                //chain_db (HashTable), so the program can randomly pick the
                //starting combo of a sentence (1st: "0start" 2nd: any_word)
                start_keys.add(ch_key.toLowerCase());
            }
            else //get key & value for the rest of the sequence in the sentence
            {
                ch_key = tokens[i-1] + " " + tokens[i];
                ch_val = tokens[i+1];
            }

            //start matching the key with existing key in the chain_db
            //if key (1st & 2nd word) already exist, add the 3rd word to
            //the correspoding linked list (so it can be randomly selected later
            if (chain_db.containsKey(ch_key))
            {
                ch_vlist = (LinkedList<String>) chain_db.get(ch_key);

                //check if this 3rd word already in the 3rd word linked list
                //this step may not be necessary
                if (!ch_vlist.contains(ch_val))
                {
                    ch_vlist.add(ch_val.toLowerCase());
                    chain_db.put(ch_key.toLowerCase(), ch_vlist);
                }
            }
            else //whole new 3-words chain, just add
            {
                ch_vlist.add(ch_val.toLowerCase());
                chain_db.put(ch_key.toLowerCase(), ch_vlist);
            }

        }
    }

    //*******************************************************************
    //getRandomSentence function: generate random sentence out of the chain
    //    data structure by appending value string of 3 word chain
    //@param: none
    //@return: sentence (String) random and not yet nicely formatted
    public String getRandomSentence()
    {
        StringBuffer sb = new StringBuffer();
        Random rn = new Random();
        //randomly pick out start combo of 1st '0start' and 2nd word
        String ch_key = (String) start_keys.get(rn.nextInt(start_keys.size()));
        String ch_val;
        boolean isFirstTime = true; //indicator for appending the first 2 words

        do
        {
            //get the collection of possible 3rd words in the chain
            LinkedList<String> ch_vlist = (LinkedList<String>) chain_db.get(ch_key);
            //randomly pick the 3rd word out of the previous collection
            ch_val = (String) ch_vlist.get(rn.nextInt(ch_vlist.size()));
            //rebuild a new key to look up in the chains repository
            ch_key = ch_key.split(" ")[1] + " " + ch_val;
            if (isFirstTime)
            {
                sb.append(ch_key); //append the very first 2 words of a sentence
                isFirstTime = false;
            }
            else
                sb.append(ch_val); //append the random 3rd word to the sentence
            sb.append(" "); //add gap between two words
        } while (!isEOL(ch_val)); //repeat if the 3rd word is not End Of Line.?!

        return sb.toString().trim();
    }

    //*******************************************************************
    //isEOL function: determine if param string is "End Of Line"
    //    EOL is one character out of 3 (period, question mark, or exclamation)
    //@param: val (String) is the value string from 3 word chain
    //@return: boolean
    private boolean isEOL(String val)
    {
        if ((val.length()==1)&&(
            (val.codePointAt(0)==33)|| //ASCII code for ! exclamation point
            (val.codePointAt(0)==46)|| //ASCII code for . period
            (val.codePointAt(0)==63)))  //ASCII code for ? question mark
            return true;
        return false;
    }
}