package com.spaniard;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.Math.abs;

public class PancakeStack {

    private ArrayList<String> originalStack;
    private ArrayList<Integer> subStack;
    private ArrayList<Integer> subStack2;
    private ArrayList<Integer> interimStack;
    private ArrayList<Integer> originalStackNumbers;
    private ArrayList<Integer> sortedStackNumbers;
    private ArrayList<Integer> burntStatuses;
    private ArrayList<Integer> subStackBurntStatus;

    /*
    Constructor creates a pancake stack object and initialises the value of the original
    stack, the sorted stack, i.e. the solution, and the burnt statuses.
    */
    public PancakeStack() throws Exception {
        getOriginalStackNumbers();
        sortStackNumbers();
        getburntStatuses();
    }

    /*
     * Read the second line of text file to gather the original pancake stack and return it to the constructor.
     * I had cast the arraylist as the numbers, i.e. pancake sizes, could be two digits and
     * the sorting when strings would not provide the required results
    */
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
        /* The sorted stack has no burnt pancakes facing upwards, i.e. negative numbers.
         * So I must get the absolute values before sorting */
        for (int i = 0; i < sortedStackNumbers.size(); i++){
            sortedStackNumbers.set(i, abs(sortedStackNumbers.get(i)));
        }
        Collections.sort(sortedStackNumbers);
        return this.sortedStackNumbers;
    }

    /*
     * List to keep track of burnt statuses;
     * -1 is burnt side sticking up
     * 2 is burnt side sticking down
     * and 1 is not burnt on either side
     * At the start we only know and care about -1
    */
    private ArrayList<Integer> getburntStatuses(){
        burntStatuses = new ArrayList<>(originalStackNumbers);
        /* The sorted stack has no burnt pancakes facing upwards, i.e. negative numbers.
         * So I must get the absolute values before sorting */
        for (int i = 0; i < burntStatuses.size(); i++){
            if (burntStatuses.get(i) < 0) {
                burntStatuses.set(i, -1);
            }else {
                burntStatuses.set(i, 1);
            }
        }

        return this.burntStatuses;
    }

    public void letTheFlippingBegin() throws Exception {

        interimStack = new ArrayList<>(getOriginalStackNumbers());

        int flipCounter = 0;

        /* The interim stack begins as a copy of the original and is updated as pancakes are rearranged by size.
         * Its size drives the number of iterations through the stack starting from the bottom.*/
        for (int i = interimStack.size() - 1; i >= 0; i--) {

            System.out.println("----------------------------------------");
            System.out.println("INDEX IS NOW : " + i);

            /* Pancake under test is NOT at the bottom of the interim stack.
             * If it is, do nothing and start a new iteration */
            if (!interimStack.get(i).equals(sortedStackNumbers.get(i))) {

                System.out.println("absolute value interim at i : " + abs(interimStack.get(i)));
                System.out.println("value sorted at i : " + sortedStackNumbers.get(i));
                System.out.println("Interim stack: " + interimStack);

                /* Pancake under test is NOT the burnt version of the equivalent on the sorted stack.
                 * If it is, it must be flipped to the top, flipped over (burnt face up) and back
                 * to the bottom of the stack (burnt face down) */
                if (abs(interimStack.get(i)) != sortedStackNumbers.get(i)) {

                    /* Pancake under test is NOT at the TOP of the interim stack.
                     * If it is, flip it internally to bring it to the top */
                    if (abs(interimStack.get(0)) != sortedStackNumbers.get(i)) {


                        /* Identify a substack starting from the top and ending on current index (inclusive) */
                        subStack = new ArrayList<>(interimStack.subList(0, i + 1));

                        /* Trim further this substack to find the index of the LAST largest pancake, in case that there
                         * are duplicates. Largest is defined by its position in the sorted stack on current iteration.*/
                        subStack = new ArrayList<>(subStack.subList(0, abs(subStack.lastIndexOf(sortedStackNumbers.get(i)) + 1)));

                        System.out.println(subStack);

                        Collections.reverse(subStack);

                        /* Update the interim stack once the substack has been flipped internally */
                        for (int j = 0; j < subStack.size(); j++) {
                            interimStack.set(j, subStack.get(j));
                        }

                        flipCounter = flipCounter + 1;

                        /* This is how the whole stack looks like now after bringing the largest pancake AT THE TOP */
                        System.out.println("Flip " + flipCounter + " is ******" + interimStack);

                        /* Break the loop if the right sorting has been achievedd by comparing with the solution.
                         * I do it here as I want to break as early as possible to make it faster... and carrying on
                         * may break the sorting :)*/
                        if (sortedStackNumbers.equals(interimStack)) break;

                        /* New substack that assumes that the largest pancake is already at the top */
                        subStack2 = new ArrayList<>(interimStack.subList(0, i + 1));

                        System.out.println(subStack2);

                        Collections.reverse(subStack2);

                        for (int j = 0; j < subStack2.size(); j++) {
                            interimStack.set(j, subStack2.get(j));
                        }

                        flipCounter = flipCounter + 1;

                        /* This is how the whole stack looks like now after bringing the largest pancake AT THE BOTTOM
                         * and on top of previous largest pancake (if any) */
                        System.out.println("Flip " + flipCounter + " is ++++++" + interimStack);

                        if (sortedStackNumbers.equals(interimStack)) break;

                    } else {

                        /* This substack assumes that the largest pancake under test is at the top of the stack already.
                         * It only requires flipping to the bottom and on top of previous largest pancake.
                         * This works even if the pancake is burnt as it gets flipped over and to the bottom of the stack */
                        subStack2 = new ArrayList<>(interimStack.subList(0, i + 1));

                        System.out.println(subStack2);

                        Collections.reverse(subStack2);

                        for (int j = 0; j < subStack2.size(); j++) {
                            interimStack.set(j, abs(subStack2.get(j)));
                        }

                        flipCounter = flipCounter + 1;
                        System.out.println("Flip " + flipCounter + " is ------" + interimStack);

                        if (sortedStackNumbers.equals(interimStack)) break;
                    }

                } else {

                    /* Identify a substack starting from the top and ending on current index (inclusive) */
                    subStack = new ArrayList<>(interimStack.subList(0, i + 1));

                    /* Flip it to the top. */
                    Collections.reverse(subStack);

//                    System.out.println(subStack);

                    /* Update the interim stack once the substack has been flipped internally.
                    * Burnt pancakes originally facing up will be now facing down, i.e. status = 2 */
                    for (int j = 0; j < subStack.size(); j++) {
                        if (subStack.get(j) < 0) {
                            interimStack.set(j, subStack.get(j)*(-1));
                            burntStatuses.set(j, 2);
                        }else {
                            interimStack.set(j, subStack.get(j));
                            burntStatuses.set(j, 1);
                        }
                    }

                    System.out.println("Burn status are now : " + burntStatuses);

                    flipCounter = flipCounter + 1;

                    /* This is how the whole stack looks like now after bringing the largest pancake AT THE TOP,
                     * burnt side is down */
                    System.out.println("Flip " + flipCounter + " is 1^^^^^" + interimStack);

                    if (sortedStackNumbers.equals(interimStack)) break;


                    subStack2 = new ArrayList<>(subStack);

                    interimStack.set(0, subStack2.get(0));
                    burntStatuses.set(0, -1);

                    System.out.println("Burn status are now : " + burntStatuses);
                    System.out.println(subStack2);

                    flipCounter = flipCounter + 1;

                    /* This is how the whole stack looks like now after flipping over ONLY the largest pancake,
                     * burnt side is up */
                    System.out.println("Flip " + flipCounter + " is 2^^^^^" + interimStack);

                    if (sortedStackNumbers.equals(interimStack)) break;

                    subStackBurntStatus = new ArrayList<>(burntStatuses.subList(0, i + 1));

                    Collections.reverse(subStack2);

                    Collections.reverse(subStackBurntStatus);

                    System.out.println(subStack2);
                    System.out.println(subStackBurntStatus);

                    for (int j = 0; j < subStack2.size(); j++) {
                        if (subStack2.get(j) < 0) {
                            interimStack.set(j, subStack2.get(j)*(-1));
                            burntStatuses.set(j, 2);
                        }else if (subStack2.get(j) == 2){
                            interimStack.set(j, subStack2.get(j));
                            burntStatuses.set(j, -1);
                        }
                    }

                    //interimStack.set(subStack2.size()-1, (subStack2.get(subStack2.size()-1))*(-1));
                    flipCounter = flipCounter + 1;

                    System.out.println("Burn status are now : " + burntStatuses);

                    /* This is how the whole stack looks like now after bringing the largest pancake AT THE TOP */
                    System.out.println("Flip " + flipCounter + " is 3^^^^^" + interimStack);

                    if (sortedStackNumbers.equals(interimStack)) break;

                }
            }
        }
    }


    public static void main(String[] args) throws Exception {

        PancakeStack pancakeStack = new PancakeStack();

        System.out.println("The original stack is : " + pancakeStack.originalStackNumbers);
        System.out.println("The sorted stack is : " + pancakeStack.sortedStackNumbers);
        System.out.println("Burnt statuses are : " + pancakeStack.burntStatuses);
        System.out.println("------------------------------");

        /* Ensure that the original stack is not already ordered before start flipping pancakes */
        if (!pancakeStack.sortedStackNumbers.equals(pancakeStack.originalStackNumbers)) {
            pancakeStack.letTheFlippingBegin();
        }else {
            System.out.println("Don't be cheeky, the pancake stack is already sorted");
        }
    }
}
