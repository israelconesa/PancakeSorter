package com.spaniard;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class PancakeStack {

    private ArrayList<String> originalStack;
    private ArrayList<String> sortedStack;
    private ArrayList<String> subStack;
    private ArrayList<String> subStack2;
    private ArrayList<String> interimStack;

    // Constructor creates a pancake stack object and initialises the value of the original
    // stack and the sorted stack, i.e. the solution.
    public PancakeStack(){
        getOriginalStack();
        sortStack();
    }

    // Read the second line of text file to gather the original pancake stack and return it to the constructor
    private ArrayList<String> getOriginalStack(){
        File textFile = new File("/home/ilerma/Projects/PancakeSorter/src/myfile.txt");
        try {
            String line2 = Files.readAllLines(Paths.get(String.valueOf(textFile))).get(1);
            originalStack = new ArrayList<String>(Arrays.asList(line2.split(" ")));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return this.originalStack;
    }

    // Sort stack to learn the solution
    private ArrayList<String> sortStack(){
        sortedStack = new ArrayList<>(originalStack);
        Collections.sort(sortedStack);
        return this.sortedStack;
    }

    // Compare a stack with original stack for equality
//    private boolean areStacksEqual(ArrayList<String> listToCompare) {
//        return listToCompare.equals(originalStack);
//    }

    public void letTheFlippingBegin() {

        interimStack = new ArrayList<>(getOriginalStack());

        // Work your way from the bottom to the top of the stack
        for (int i = interimStack.size()-1; i>=0; i--) {

            System.out.println("Current Index value : " +i);

            System.out.println("interim stack is now " +interimStack);

            //identify a substack starting from the top and ending on the largest pancake
            subStack = new ArrayList<String>(interimStack.subList(0, interimStack.indexOf(sortedStack.get(i))+1));

            //create another substack from that point to the bottom of the pile
            subStack2 = new ArrayList<String>(interimStack.subList(interimStack.indexOf(sortedStack.get(i))+1, interimStack.size()));

            System.out.println(subStack);
            System.out.println(subStack2);

            //Collections.reverse(subStack);

            Collections.reverse(subStack);
            subStack.addAll(subStack2);

            interimStack = new ArrayList<>(subStack);

            System.out.println("Flip " + i + " is " + interimStack);

            //flip to bottom
            //identify a substack starting from the bottom and ending on the largest pancake
            subStack = new ArrayList<String>(interimStack.subList(0, i+1));

            //create another substack from that point to the bottom of the pile
            subStack2 = new ArrayList<String>(interimStack.subList(i+1, interimStack.size()));

            System.out.println(subStack);
            System.out.println(subStack2);

            Collections.reverse(subStack);

            subStack.addAll(subStack2);

            interimStack = new ArrayList<>(subStack);

            //create another substack from that point to the top of the pile
//            subStack2 = new ArrayList<String>(interimStack.subList(interimStack.indexOf(sortedStack.get(i))+1, interimStack.size()));


            System.out.println("Flip " + i + " is " + interimStack);

            System.out.println("------------------------------------");
        }

    }

    public static void main(String[] args) {

        PancakeStack pancakeStack = new PancakeStack();

        pancakeStack.letTheFlippingBegin();

    }
}
