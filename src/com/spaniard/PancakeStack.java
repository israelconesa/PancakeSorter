package com.spaniard;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.*;


public class PancakeStack {

    private ArrayList<String> originalStack;
    private ArrayList<String> sortedStack;
    private ArrayList<String> flippedStack;
    private ArrayList<String> subStack;
    private ArrayList<String> indexOfBiggestPancake;
    private List<Object> stackToFlip;


    // Constructor creates a pancake stack object and initialises the value of the original
    // stack and the sorted stack, i.e. the solution.
    public PancakeStack(){
        getOriginalStack();
        sortStack();
        //reverseStack();
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
    public ArrayList<String> sortStack(){
        sortedStack = new ArrayList<>(originalStack);
        Collections.sort(sortedStack);
        return this.sortedStack;
    }

    // Flip any stack
    public ArrayList<String> flipStack(ArrayList<String> stackToFlip) {
        flippedStack = new ArrayList<>(stackToFlip);
        Collections.reverse(flippedStack);
        return this.flippedStack;
    }

    // Compare a stack with original stack for equality
    public boolean areStacksEqual(ArrayList<String> listToCompare) {
        return listToCompare.equals(originalStack);
    }

    // Creates substack from index[0] to index[max], where max is the highest
    // value on said list and flips it
    public ArrayList<String> subStackToLargest(ArrayList<String> subStackToFlip) {
        String biggestPancake = Collections.max(subStackToFlip); // find largest pancake on stack
        int sizeOfSubStack = subStackToFlip.size(); // find size of stack
        int indexOfBiggestPancake = subStackToFlip.indexOf(biggestPancake); //find index of largest pancake on stack
        if (indexOfBiggestPancake !=  sizeOfSubStack) {
            ArrayList<String> subStack = new ArrayList<String>(subStackToFlip.subList(0, indexOfBiggestPancake + 1));
            flipStack(subStack);
        }else {
            ArrayList<String> subStack = new ArrayList<String>(subStackToFlip.subList(0, indexOfBiggestPancake));
            flipStack(subStack);
        }
        return this.subStack;
    }

    // Compare arrays to get solution

    public static void main(String[] args) {

        PancakeStack pancakeStack = new PancakeStack();


        System.out.println("The original stack is : " + pancakeStack.originalStack);

        System.out.println("The sorted stack is : " + pancakeStack.sortedStack);

        System.out.println("Flipping the original stack : " + pancakeStack.flipStack(pancakeStack.originalStack));

        System.out.println(pancakeStack.subStackToLargest(pancakeStack.originalStack));


    }
}
