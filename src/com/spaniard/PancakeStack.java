package com.spaniard;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.*;


public class PancakeStack {

    private ArrayList<String> originalStack;
    private ArrayList<String> sortedStack;
    private ArrayList<String> reversedStack;


    // Constructor creates a pancake stack object and initialises the value of the original
    // stack and the sorted stack, i.e. the solution.
    public PancakeStack(){
        getOriginalStack();
        SortStack();
        //reverseList();
    }

    // Read the second line of text file to gather the original pancake stack and returning to the constructor
    public ArrayList<String> getOriginalStack(){
        File textFile = new File("/home/israelconesa/Development/PancakeSorter/src/myfile.txt");
        try {
            String line2 = Files.readAllLines(Paths.get(String.valueOf(textFile))).get(1);
            originalStack = new ArrayList<String>(Arrays.asList(line2.split(" ")));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return this.originalStack;
    }

    // Sort arraylist for solution
    public ArrayList<String> SortStack(){
        sortedStack = new ArrayList<>(originalStack);
        Collections.sort(sortedStack);
        return this.sortedStack;
    }

    public ArrayList<String> reverseList() {
        reversedStack = new ArrayList<>(originalStack);
        Collections.sort(this.reversedStack, Collections.reverseOrder());
        return this.reversedStack;
    }

    // Compare a stack with original stack for equality
    public boolean areStacksEqual(ArrayList<String> listToCompare) {
        if (listToCompare.equals(originalStack)){
            return true;
        }else {
            return false;
        }
    }


    // Creates sublist from index[0] to index[max], where max is the highest
    // value
    public ArrayList<String> identifySublistToFlip() {
        Collections

    }

    // Compare arrays to get solution

    public static void main(String[] args) {

        PancakeStack pancakeStack = new PancakeStack();


        System.out.println(pancakeStack.originalStack);


        System.out.println(pancakeStack.sortedStack);


        //System.out.println(pancakeStack.reversedStack);

        ArrayList<String> list = new ArrayList<String>();
        list.add("1");
        list.add("3");
        list.add("5");
        list.add("4");
        list.add("2");
        System.out.println(pancakeStack.areStacksEqual(list));

    }
}
