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
    private ArrayList<String> interimStack;
    private List<Object> stackToFlip;


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

    // Flip any stack
    private ArrayList<String> flipStack(ArrayList<String> stackToFlip) {
        flippedStack = new ArrayList<>(stackToFlip);
        Collections.reverse(flippedStack);
        return this.flippedStack;
    }

    // Compare a stack with original stack for equality
    private boolean areStacksEqual(ArrayList<String> listToCompare) {
        return listToCompare.equals(originalStack);
    }

    public void letTheFlippingBegin() {

        interimStack = new ArrayList<>(getOriginalStack());

        //String biggestPancake = Collections.max(interimStack); // find largest pancake on stack

        //int sizeOfSubStack = interimStack.size(); // find size of stack

        //int indexOfBiggestPancake = interimStack.indexOf(biggestPancake); //find index of largest pancake on stack

        // Work your way from the bottom to the top of the stack
        for (int i = interimStack.size()-1; i>=0; i--) {

            System.out.println("Current Index value : " +i);

            System.out.println("interim stack is now " +interimStack);

            //if (!sortedStack.get(i).equals(interimStack.get(i))) {

                //identify a substack starting from the top and ending on the largest pancake
                subStack = new ArrayList<String>(interimStack.subList(0, interimStack.indexOf(sortedStack.get(i))+1));

            System.out.println(subStack);

//                System.out.println("substack before flipping "+subStack);

                //flip the substack
                subStack = flipStack(subStack); // bring the biggest pancake to the top

//                System.out.println("substack after flipping "+flippedStack);

                //update the interim stack with the values of substack after they've been flipped
                for (int j = 0; j<subStack.size(); j++) {

                    interimStack.set(j, subStack.get(j));
                }

                // bring the biggest pancake to the bottom
                interimStack = flipStack(interimStack);

            //}else {



                //flip the whole interim stack
            //}
        }

    }

    public static void main(String[] args) {

        PancakeStack pancakeStack = new PancakeStack();


//        System.out.println("The original stack is : " + pancakeStack.originalStack);
//
//        System.out.println("The sorted stack is : " + pancakeStack.sortedStack);
//
//        System.out.println("Flipping the original stack : " + pancakeStack.flipStack(pancakeStack.originalStack));

        pancakeStack.letTheFlippingBegin();


    }
}
