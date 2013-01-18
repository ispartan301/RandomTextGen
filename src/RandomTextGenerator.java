import java.io.*;

//*********************************************************************
//RandomTextGenerator class: creates output based on a source text.
//    source text filename is the only command line argument.
//    first build up a repository of three-word chains in the source text.
//    then generate a random sentence out of the chains combonation
//@author: Victor Lu
//@date: March 10, 2008
//@usage: java RandomTextGenerator filename

public class RandomTextGenerator
{
    //global variables as simple enum to represent different cases
    private static final int NONTEXT = 0;
    private static final int FRAGMENT = 1;
    private static final int SENTENCE = 2;

    //*******************************************************************
    //main function: read in source file then generate a random sentence
    //@param: args (string[]) only require one param args[0] as source filename
    public static void main(String[] args)
    {
        //check the command argument, require proper filename
        if (args.length != 1)
        {
            System.err.println("usage: java RandomTextGenerator (file_name)");
            System.exit(1);
        }

        //step 1: read the source text file line by line,
        //step 2: manipulate the line into sentence in proper format,
        //step 3: load the sentence into chain data structure
        //step 4: output a random sentence generated from the chain data structure
        BufferedReader input = null;
        try
        {
            input = new BufferedReader(new FileReader(args[0]));
            String line;
            StringBuffer sb = new StringBuffer();
            Chain chain = new Chain();

            while((line = input.readLine()) != null) //step 1
            {
                switch(checkLine(line))
                {
                    //step 2
                    case NONTEXT:
                        break;
                    case FRAGMENT:
                        sb.append(' '); //add gap between two fragments
                        sb.append(line);
                        break;
                    case SENTENCE:
                        sb.append(' '); //add gap between two fragments
                        sb.append(line);
                        chain.loadChain(formatLine(sb)); //step 3
                        sb = new StringBuffer();
                        break;
                }
            }
            System.out.println(formatLine(chain.getRandomSentence())); //step 4
        }
        catch(FileNotFoundException fnfe)
        {
            System.err.println("File not found: " + args[0]);
            System.exit(2);
        }
        catch(IllegalArgumentException iae)
        {
            System.err.println("An illegal argument has been passed");
            System.exit(3);
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    //********************************************************************
    //checkLine function: determine the input line string as
    //    NONTEXT: not part of a sentence
    //    FRAGMENT: part of a sentence, but not complete
    //    SENTENCE: complete sentence
    //@param: line (String) read from the source file
    //@return: result (int) as NONTEXT, FRAGMENT, or SENTENCE
    private static int checkLine(String line)
    {
        if (line.matches("[0-9]*"))
            return NONTEXT;
        else if ((line.codePointAt(line.length()-1)==33)|| //ASCII code for ! exclamation point
                 (line.codePointAt(line.length()-1)==46)|| //ASCII code for . period
                 (line.codePointAt(line.length()-1)==63))  //ASCII code for ? question mark
            return SENTENCE;
        else
            return FRAGMENT;
    }

    //*******************************************************************
    //formatLine function: remove unnecessary characters like double quote
    //    and add white space around characters like comma, semicolon, etc...
    //    so the return string is ready for splitting by white spaces
    //@param: sb (StringBuffer) is the complete sentence read from file
    //@return: formated complete sentence (String)
    private static String formatLine(StringBuffer sb)
    {
        for(int i=0; i<sb.length(); i++)
        {
            if (sb.codePointAt(i) == 34) //ASCII code for " double quote
            {
                sb = sb.deleteCharAt(i--);
            }
            else if ((sb.codePointAt(i) == 44)|| //ASCII code for , comma
                     (sb.codePointAt(i) == 58)|| //ASCII code for : colon
                     (sb.codePointAt(i) == 59)|| //ASCII code for ; semicolon
                     (sb.codePointAt(i) == 63)|| //ASCII code for ? question mark
                     (sb.codePointAt(i) == 46)|| //ASCII code for . period
                     (sb.codePointAt(i) == 33))  //ASCII code for ! exclamation point
            {
            	if ((i>0)&&(sb.codePointAt(i-1) != 32)) //ASCII code for white space
            	    sb = sb.insert(i++, ' ');
            	if ((i<sb.length()-1)&&(sb.codePointAt(i+1) != 32)) //ASCII code for white space
                    sb = sb.insert(++i, ' ');
            }
        }
        return sb.toString().trim();
    }

    //*******************************************************************
    //formatLine function: remove unnecessary white spaces before some
    //    characters like comma, semicolon, etc...
    //    also make the first character upper case
    //@param: str (String) is the complete sentence generated from chains
    //@return: formated complete sentence (String)
    private static String formatLine(String str)
    {
        StringBuffer sb = new StringBuffer(str);
        for(int i=0; i<sb.length(); i++)
        {
            if (i==0)
            {
                sb = sb.replace(i, i+1, sb.substring(i, i+1).toUpperCase());
            }
            if ((sb.codePointAt(i) == 44)|| //ASCII code for , comma
                (sb.codePointAt(i) == 58)|| //ASCII code for : colon
                (sb.codePointAt(i) == 59)|| //ASCII code for ; semicolon
                (sb.codePointAt(i) == 63)|| //ASCII code for ? question mark
                (sb.codePointAt(i) == 46)|| //ASCII code for . period
                (sb.codePointAt(i) == 33))  //ASCII code for ! exclamation point
            {
            	if ((i>0)&&(sb.codePointAt(i-1) == 32)) //ASCII code for white space
            	    sb = sb.deleteCharAt(--i);
            }
        }
        return sb.toString().trim();
    }

}