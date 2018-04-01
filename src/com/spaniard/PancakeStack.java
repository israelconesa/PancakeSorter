package com.spaniard;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PancakeStack {

    private ArrayList<String> originalStack;
    private ArrayList<Integer> subStack;
    private ArrayList<Integer> subStack2;
    private ArrayList<Integer> interimStack;
    private ArrayList<Integer> originalStackNumbers;
    private ArrayList<Integer> sortedStackNumbers;

    /*
    Constructor creates a pancake stack object and initialises the value of the original
    stack and the sorted stack, i.e. the solution.
    */
    public PancakeStack() throws Exception {
        getOriginalStackNumbers();
        sortStackNumbers();
    }

    /* Read the second line of text file to gather the original pancake stack and return it to the constructor.
     * I had cast the arraylist as the numbers, i.e. pancake sizes, could be two digits and
     * the sorting when strings would not provide the required results */
    private ArrayList<Integer> getOriginalStackNumbers() throws Exception {
        File textFile = new File("/home/israelconesa/Development/PancakeSorter/src/myfile.txt");
        try {
            String line2 = Files.readAllLines(Paths.get(String.valueOf(textFile))).get(1);
            originalStack = new ArrayList<>(Arrays.asList(line2.split(" ")));
            originalStackNumbers = new ArrayList<>();
            for ( String s : originalStack) {
                originalStackNumbers.add(Integer.parseInt(s));
            }
        } catch (java.io.IOException | NumberFormatException e) {
            /* Putting together errors regarding IO and issues with junk on the file */
            throw new Exception("I can't find the file or the file contains non-numeric charachers");
        }
        return this.originalStackNumbers;
    }

    /* Sort stack to learn the solution */
    private ArrayList<Integer> sortStackNumbers(){
        sortedStackNumbers = new ArrayList<>(originalStackNumbers);
        Collections.sort(sortedStackNumbers);
        return this.sortedStackNumbers;
    }

    public void letTheFlippingBegin() throws Exception {

        interimStack = new ArrayList<>(getOriginalStackNumbers());

        int flipCounter = 0;

        /* The interim stack begins as a copy of the original and is updated as pancakes are rearranged by size.
         * Its size drives the number of iterations through the stack starting from the bottom.*/
        for (int i = interimStack.size() - 1; i >= 0; i--) {

            /* Pancake under test is NOT at the bottom of the interim stack.
             * If it is, do nothing and start a new iteration */
            if (!interimStack.get(i).equals(sortedStackNumbers.get(i))) {

                /* Pancake under test is NOT at the TOP of the interim stack.
                 * If it is, flip it internally to bring it to the bottom */
                if (!interimStack.get(0).equals(sortedStackNumbers.get(i))) {

                    /* Identify a substack starting from the top and ending on current index (inclusive) */
                    subStack = new ArrayList<>(interimStack.subList(0, i + 1));

                    /* Trim further this substack to find the index of the LAST largest pancake, in case that there
                    * are duplicates. Largest is defined by its position in the sorted stack on current iteration.*/
                    subStack = new ArrayList<>(subStack.subList(0, subStack.lastIndexOf(sortedStackNumbers.get(i))+1));

                    Collections.reverse(subStack);

                    /* Update the interim stack once the substack has been flipped internally */
                    for (int j = 0; j < subStack.size(); j++) {
                        interimStack.set(j, subStack.get(j));
                    }

                    flipCounter = flipCounter + 1;

                    /* This is how the whole stack looks like now after bringing the largest pancake AT THE TOP */
                    System.out.println("Flip " + flipCounter + " is " + interimStack);

                    /* Break the loop if the right sorting has been achievedd by comparing with the solution.
                    * I do it here as I want to break as early as possible to make it faster... and carrying on
                    * may break the sorting :)*/
                    if (sortedStackNumbers.equals(interimStack)) break;

                    /* New substack that assumes that the largest pancake is already at the top */
                    subStack2 = new ArrayList<>(interimStack.subList(0, i + 1));

                    Collections.reverse(subStack2);

                    for (int j = 0; j < subStack2.size() ; j++) {
                        interimStack.set(j, subStack2.get(j));
                    }

                    flipCounter = flipCounter + 1;

                    /* This is how the whole stack looks like now after bringing the largest pancake AT THE BOTTOM
                     * and on top of previous largest pancake (if any) */
                    System.out.println("Flip " + flipCounter + " is " + interimStack);

                    /* Break the loop if the right sorting has been achieved
                     * by comparing with the solution */
                    if (sortedStackNumbers.equals(interimStack)) break;

                } else {

                    /* This substack assumes that the largest pancake under test is at the top of the stack*/
                    subStack2 = new ArrayList<>(interimStack.subList(0, i + 1));

                    Collections.reverse(subStack2);

                    for (int j = 0; j <= i ; j++) {

                        interimStack.set(j, subStack2.get(j));

                    }

                    flipCounter = flipCounter + 1;
                    System.out.println("Flip " + flipCounter + " is " + interimStack);

                    /* Break the loop if the right sorting has been achieved
                    * by comparing with the solution */
                    if (sortedStackNumbers.equals(interimStack)) break;
                }
            }
        }
    }


    public static void main(String[] args) throws Exception {

        PancakeStack pancakeStack = new PancakeStack();

        System.out.println(pancakeStack.originalStackNumbers);
        System.out.println(pancakeStack.sortedStackNumbers);
        System.out.println("------------------------------");

        /* Ensure that the original stack is not already ordered before start flipping pancakes */
        if (!pancakeStack.sortedStackNumbers.equals(pancakeStack.originalStackNumbers)) {
            pancakeStack.letTheFlippingBegin();
        }else {
            System.out.println("Don't be cheeky, the pancake stack is already sorted");
        }
    }
}
