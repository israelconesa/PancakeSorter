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
        File textFile = new File("/home/ilerma/Projects/PancakeSorter/src/myfile.txt");
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

        /* Work your way from the bottom to the top of the stack */
        for (int i = interimStack.size()-1; i>=0; i--) {
            System.out.println("----------------------------------------------");
            System.out.println("INDEX IS NOW : " + i);
            System.out.println( !interimStack.get(i).equals(sortedStackNumbers.get(i)) && !interimStack.get(0).equals(sortedStackNumbers.get(i)));

            if (
                !interimStack.get(i).equals(sortedStackNumbers.get(i)) && //pancake under test is NOT at the bottom of the interim stack
                !interimStack.get(0).equals(sortedStackNumbers.get(i))    //AND NOT at the top already
                ) {


                /* Identify a substack starting from the top and ending on the largest pancake.
                 * Largest is identifiable by its position on the sorted stack */
                subStack = new ArrayList<>(interimStack.subList(0, interimStack.lastIndexOf(sortedStackNumbers.get(i)) + 1));

                /* Create another substack from that point to the bottom of the pile */
                subStack2 = new ArrayList<>(interimStack.subList(interimStack.lastIndexOf(sortedStackNumbers.get(i)) + 1, interimStack.size()));

                System.out.println(subStack);
                System.out.println(subStack2);

                /* Flip the subStack to bring the largest pancake to the top */
                Collections.reverse(subStack);

                /* Put together the stacks */
                subStack.addAll(subStack2);

                /* This is how the whole stack looks like now after bringing the largest pancake AT THE TOP */
                interimStack = new ArrayList<>(subStack);

                flipCounter = flipCounter + 1;

                System.out.println("Flip " + flipCounter + " is " + interimStack);

                /* Break the loop if the right sorting has been achieved
                * by comparing with the solution. I do it here as I want to break
                * as early as possible to make it faster... and carrying on
                * would break the sorting :)*/
                if (sortedStackNumbers.equals(interimStack)) break;

                /* Ensure that pancake under test is NOT at the top */
//                if (interimStack.get(0).equals(sortedStackNumbers.get(i))) {

                    /* Identify a substack starting from the top and ending on the largest pancake */
                    subStack = new ArrayList<>(interimStack.subList(0, i + 1));

                    /* Create another substack from that point to the bottom of the pile */
                    subStack2 = new ArrayList<>(interimStack.subList(i + 1, interimStack.size()));

                    System.out.println(subStack);
                    System.out.println(subStack2);

                    Collections.reverse(subStack);

                    /* Put together the stacks */
                    subStack.addAll(subStack2);

                    /* This is how the whole stack looks like now after bringing the largest pancake AT THE BOTTOM */
                    interimStack = new ArrayList<>(subStack);

                    flipCounter = flipCounter + 1;

                    System.out.println("Flip " + flipCounter + " is -----" + interimStack);

                    /* Break the loop if the right sorting has been achieved
                    * by comparing with the solution */
                    if (sortedStackNumbers.equals(interimStack)) break;

//                }else {
//
//                }

            } else {
                /* Pancake under test is already at the bottom of the interim
                stack so start the iteration again to find next one to go over */
                interimStack = new ArrayList<>(interimStack);
                System.out.println(interimStack);
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
