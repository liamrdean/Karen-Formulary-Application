import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

//package search_function;

public class SearchHelper {
    /**
     * Trie storage for dictionary
     * Make trie from list 
     * 
     * Return autofill
     * Return clostest by steps away
     * 
     */

   
    private TrieNode root;
    private File inputFile;
    private String[] dictionary;

    public Search(){
        inputFile = new File("inputFile.txt");
    }
    
    public void buildFromFile(){
        try{
            Scanner input = new Scanner(inputFile);
            String entry;
            root = new TrieNode("\0",false);
            dictionary = new String[4]; // number would need to be changed for this, but we do know it
            int counter = 0;
            TrieNode current;
            while(input.hasNextLine()){
                entry = input.nextLine();
                entry.toLowerCase();
                current = root; 
                dictionary[counter] = entry;
                counter++;
                for(int i =0; i < entry.length(); i++){
                    // making sure we get the right index 
                    int newIndex = (int)(entry.charAt(i)) - (int)('a');
                    if (entry.charAt(i) == ' '){
                        newIndex = 26;
                    } else if (entry.charAt(i) == '\n'){
                        newIndex = 27;
                    }
                    if(current.getChild(newIndex) == null){
                        if (i == entry.length() -1 ){
                            TrieNode newNode = new TrieNode(entry,true);
                            //System.out.println( entry.charAt(i)+ ", new entry");
                            current.setChild(newIndex, newNode);
                            current = newNode;
                        } else {
                            TrieNode newNode = new TrieNode(null,false);
                            //System.out.println( entry.charAt(i));
                            current.setChild(newIndex, newNode);
                            current = newNode;
                        }
                    } else{
                        current = current.getChild(newIndex);
                    }
                }

            }
        }
        catch (Exception FileNotFoundException){
            System.out.println("you screwed up");
        }
        
    }

    public boolean search(String guess){
        boolean found = false;
        TrieNode current;
        current = root;
        for(int i =0; i< guess.length(); i++){
            //System.out.println(guess.charAt(i));
            int index = (int)(guess.charAt(i)) - (int)('a');
            if (guess.charAt(i) == ' '){
                index = 26;
            } else if (guess.charAt(i) == '\n'){
                index = 27;
            }
            if(current.getChild(index) == null){
                //System.out.println("null");
                break;
            } else{
                if (i == guess.length() -1){
                    if(current.getChild(index).getWord() == true){
                        //System.out.println("found");
                        found = true;
                    } else {
                        //System.out.println("end and no");
                    }
                } else {
                    current = current.getChild(index);
                    //System.out.println("next");
                }  
            }
        }
        return found;
    }

    /**
     * Autofill will search for the inital guess and return everything that follows 
     * will go by the shortest word close to it
     */
    public String[] autofill(String guess){
        String[] results = new String[10]; // could change the number of results easily 
        int resultsIndex = 0;
        TrieNode current;
        current = root;
        for(int i =0; i< guess.length(); i++){
            //System.out.println(guess.charAt(i));
            int index = (int)(guess.charAt(i)) - (int)('a');
            if (guess.charAt(i) == ' '){
                index = 26;
            } else if (guess.charAt(i) == '\n'){
                index = 27;
            }
            if(current.getChild(index) == null){
                //System.out.println("null");
                break;
                //System.out.println("bad guess");
            } else{
                current = current.getChild(index);
            }
        }
        //at current node 
        // make a list of current valid children nodes 
        TrieNode[] tempchildren = current.getAllChildren();
        ArrayList<TrieNode> currentChildren;
        currentChildren = new ArrayList<TrieNode>();
        for (int i = 0; i < tempchildren.length; i++){
            if(tempchildren[i] != null){
                currentChildren.add(tempchildren[i]);
            }
        }
        
        // while results isn't full
            // search all children nodes for valid words
            // add any words to results 
            // create new list of valid children nodes from existing
            // replace list
            // if list == null break 
        while(resultsIndex<results.length){
            if(!currentChildren.isEmpty()) {
                TrieNode temp = currentChildren.remove(0);
                if(temp.getWord()){
                    results[resultsIndex] = temp.getValue();
                    resultsIndex++;
                }
                tempchildren = temp.getAllChildren();
                for(int i = 0; i < tempchildren.length; i++){
                    if(tempchildren[i] != null){
                        currentChildren.add(tempchildren[i]);
                    }
                }
            } else{
                resultsIndex = results.length;
                break;
            }
            
        }


        return results;
    }

    // return any that are one letter misplesses 
    public String[] closest( String guess){
        //String[] results = new String[10]; // could change the number of results easily 
        //int resultsIndex = 0;
        for(int i = 0; i < dictionary.length; i++){
            int distance = levenshtein (guess, dictionary[i], guess.length(), dictionary[i].length());
            System.out.println(guess + " : "+ dictionary[i] + " : " + distance);
        }
       // for each in dicitonary perform the levenstien calculation 

        return null; 
    }

    public int levenshtein (String guess, String entry, int m, int n){
        if (m == 0) {
            return n;
        }
        if (n == 0) {
            return m;
        }
        if (guess.charAt(m - 1) == entry.charAt(n - 1)) {
            return levenshtein(guess, entry, m - 1, n - 1);
        }
        return 1 + Math.min(
            levenshtein(guess, entry, m, n - 1),
            Math.min(
                levenshtein(guess, entry, m - 1, n),
                levenshtein(guess, entry, m - 1, n - 1)
            )
        );
    }

    
    class TrieNode {
        private String value;
        private boolean word;
        private TrieNode children[];

        //Constructor

        public TrieNode(String newValue, boolean newWord){
            this.value = newValue;
            this.word = newWord;
            //0-25 are a-z, 26 = ' ', 27 = '\n'
            this.children = new TrieNode[28];
            for (int i = 0; i < 28; i++){
                children[i]=null;
            }
        }

        //Setters and Getters

        public void setValue(String newValue){
            value = newValue;
        }
        public void setWord (boolean newWord){
            word = newWord;
        }
        public void setAllChildren (TrieNode newChildren[]){
            children = newChildren;
        }
        public void setChild (int index, TrieNode newChild){
            children[index] = newChild;
        }

        public String getValue(){
            return value;
        }
        public boolean getWord(){
            return word;
        }
        public TrieNode[] getAllChildren(){
            return children;
        }
        public TrieNode getChild(int index){
            return children[index];
        }

    }
}

