// Teddy Meeks
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }



    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.

    // Gets the permutqtions recursivelt
    public void permutations(String s, int left, int right) {
        if(left == right)
        {
            words.add(s);
        }
        else
        {
            for(int x = left; x<=right; x++)
            {
                char temp = s.charAt(x);
                s = swapChar(x,left,s);
                permutations(s,left+1,right);
                s = swapChar(x,left,s);
            }
        }
    }

    // CharSwapper
    public String swapChar(int p1, int p2, String s)
    {
        StringBuilder builder = new StringBuilder(s);
        char temp = builder.charAt(p1);
        builder.setCharAt(p1, builder.charAt(p2));
        builder.setCharAt(p2, temp);
        return builder.toString();
    }

    // Generates the permutations
    public void generate()
    {
        int subsequences = (int) (Math.pow(2,letters.length()) - 1);
        for(int x = 1; x <= subsequences; x++)
        {
            StringBuilder curr = new StringBuilder();
            int currPosi = letters.length() - 1;
            int y = x;
            while(y > 0)
            {
                if((y & 1) > 0)
                {
                    curr.append(letters.charAt(currPosi));
                }
                y/=2;
                currPosi--;
            }
            permutations(curr.toString(), 0,curr.length() - 1);
        }
    }

    // Called by sort to run recursively
    public static void mergeSort(ArrayList<String> list)
    {
        if (list.size() > 1)
        {
            int mid = list.size() / 2;
            ArrayList<String> left = new ArrayList<>(list.subList(0, mid));
            ArrayList<String> right = new ArrayList<>(list.subList(mid, list.size()));

            mergeSort(left);
            mergeSort(right);

            merge(list, left, right);
        }
    }

    // Merge part of mergeSort
    public static void merge(ArrayList<String> list, ArrayList<String> left, ArrayList<String> right)
    {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size())
        {
            if (left.get(i).compareTo(right.get(j)) < 0)
            {
                list.set(k++, left.get(i++));
            }
            else
            {
                list.set(k++, right.get(j++));
            }
        }
        while (i < left.size())
        {
            list.set(k++, left.get(i++));
        }
        while (j < right.size())
        {
            list.set(k++, right.get(j++));
        }
    }

    public void sort() {
        mergeSort(words);
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates()
    {
        int i = 0;
        while (i < words.size() - 1)
        {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    // Checks words using binarySearch
    public void checkWords()
    {
        for(int i = 0; i < words.size(); i++)
        {
            if(!binarySearch(0,DICTIONARY_SIZE,words.get(i)))
            {
                words.remove(i);
                i--;
            }
        }
    }

    // Basic BinarySearch
    public boolean binarySearch(int left, int right, String target)
    {
        if (left <= right)
        {
            int mid = left + (right - left) / 2;

            if (DICTIONARY[mid].compareTo(target) == 0)
                return true;
            else if (DICTIONARY[mid].compareTo(target) > 0)
                return binarySearch(left, mid - 1, target);
            else
                return binarySearch(mid + 1, right, target);
        }
        return false;
    }
    // Prints all valid words to wordList.txt
    public void printWords() throws IOException
    {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words)
        {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary()
    {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try
        {
            s = new Scanner(dictionaryFile);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine())
        {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args)
    {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do
        {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try
        {
            sb.printWords();
        }
        catch (IOException e)
        {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
