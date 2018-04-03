package com.spaniard;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.Math.abs;

public class PancakeStack {

    private ArrayList<String> originalStack;
    private ArrayList<Integer> subStack;
    private ArrayList<Integer> interimStack;
    private ArrayList<Integer> originalStackNumbers;
    private ArrayList<Integer> sortedStackNumbers;
    private ArrayList<Integer> burntStatuses;
    private ArrayList<Integer> subStackBurntStatus;
    private ArrayList<Integer> subStack2;

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
     * I had cast the arraylist to integers as the numbers, i.e. pancake sizes, could be two digits and
     * the sorting with strings would not provide the required results.
    */
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
     * At the start we only know and care about -1. A value of 2 is only achieved from the burnt pancakes
     * after turning them over.
     *
     * E.g; original list [3, 2, -1, -4, 5] => burntStatuses [1, 1, -1, -1, 1]
     * After flipping [4, 1, 2, 3, 5] => burntStatuses [2, 2, 1, 1, 1]
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
            System.out.println(interimStack);
//            System.out.println(interimStack.get(i));
//            System.out.println(sortedStackNumbers.get(i));

            /*
             * Pancake under test is NOT at the bottom of the interim stack.
             * If it is, do nothing and start a new iteration
            */
            if (!interimStack.get(i).equals(sortedStackNumbers.get(i))) {

                /* Pancake under test is NOT the burnt version of its match on the sorted stack.
                 * If it is, it must be flipped to the top, flipped over (burnt face up) and back
                 * to the bottom of the stack (burnt face down) */
                if (abs(interimStack.get(i)) != sortedStackNumbers.get(i)) {

                    /* Pancake under test is NOT at the TOP of the interim stack.
                     * If it is, flip it internally to bring it to the top */
                    if (abs(interimStack.get(0)) != sortedStackNumbers.get(i)) {

                        /* Identify a substack starting from the top and ending on current index (inclusive) */
                        subStack = new ArrayList<>(interimStack.subList(0, i + 1));

//                        System.out.println(subStack);

                        /* Trim further this substack to find the index of the LAST largest pancake, in case that there
                         * are duplicates. Largest is defined by its position in the sorted stack.
                         * If the trimmed substack is empty that means that the pancake which I'm after cannot be found,
                         * i.e. it is negative in size (has a burnt side facing up */

                        subStack2 = new ArrayList<>(subStack.subList(0, subStack.lastIndexOf(sortedStackNumbers.get(i)) + 1));

                        if (subStack2.isEmpty()) {
                            int negativeVersion2 = sortedStackNumbers.get(i)*(-1);

                            subStack2 = new ArrayList<>(subStack.subList(0, subStack.lastIndexOf(negativeVersion2) + 1));

                        }

                        subStackBurntStatus = new ArrayList<>(burntStatuses.subList(0, subStack2.size()));

                        Collections.reverse(subStack2);
                        Collections.reverse(subStackBurntStatus);

                        /* Update the interim stack once the substack has been flipped internally */
                        for (int j = 0; j < subStack2.size(); j++) {
                            if (subStackBurntStatus.get(j) < 0) {
                                interimStack.set(j, subStack2.get(j)*(-1));
                                burntStatuses.set(j, 2);
                            }else if (subStackBurntStatus.get(j) == 2){
                                interimStack.set(j, subStack2.get(j));
                                burntStatuses.set(j, -1);
                            }else {
                                interimStack.set(j, subStack2.get(j));
                                burntStatuses.set(j, 1);
                            }
                        }

                        flipCounter = flipCounter + 1;

                        /* This is how the whole stack looks like now after bringing the largest pancake AT THE TOP */
                        System.out.println("Flip " + flipCounter + " is ******" + interimStack);

                        System.out.println("Burn status are now : " + burntStatuses);

                        /* Break the loop if the right sorting has been achieved by comparing with the solution.
                         * I do it here as I want to break as early as possible to make it faster... and carrying on
                         * may break the sorting :)*/
                        if (sortedStackNumbers.equals(interimStack)) break;


                        if (burntStatuses.get(0) == 2) {
                            interimStack.set(0, subStack2.get(0));
                            burntStatuses.set(0, -1);

//                            System.out.println(subStack);

                            flipCounter = flipCounter + 1;

                            System.out.println("Flip " + flipCounter + " is >>>>>>" + interimStack);
                            System.out.println("Burn status are now : " + burntStatuses);
                        }


                        /* New substack that assumes that the largest pancake is already at the top */
                        subStack = new ArrayList<>(interimStack.subList(0, i + 1));
                        subStackBurntStatus = new ArrayList<>(subStack.size());

                        Collections.reverse(subStack);
                        Collections.reverse(subStackBurntStatus);

                        System.out.println(subStack);
                        System.out.println(subStackBurntStatus);

                        for (int j = 0; j < subStack.size(); j++) {
                            if (subStackBurntStatus.get(j) < 0) {
                                interimStack.set(j, subStack.get(j));
                                burntStatuses.set(j, 2);
                            }else if (subStackBurntStatus.get(j) == 2){
                                interimStack.set(j, subStack.get(j)*(-1));
                                burntStatuses.set(j, -1);
                            }else {
                                interimStack.set(j, subStack.get(j));
                                burntStatuses.set(j, 1);
                            }
                        }

                        flipCounter = flipCounter + 1;
                        System.out.println("Flip " + flipCounter + " is ++++++" + interimStack);

                        System.out.println("Burn status are now : " + burntStatuses);

                        if (sortedStackNumbers.equals(interimStack)) break;

                    } else {

                        /* This substack assumes that the largest pancake under test is at the top of the stack already.
                         * It only requires flipping to the bottom and on top of previous largest pancake.
                         *
                         * E.g. [-4, 1, 2, 3, 5] becomes [3, 2, 1, 4, 5]
                         */
                        subStack = new ArrayList<>(interimStack.subList(0, i + 1));
                        subStackBurntStatus = new ArrayList<>(burntStatuses.subList(0, i + 1));

                        Collections.reverse(subStack);
                        Collections.reverse(subStackBurntStatus);

                        System.out.println(subStack);
                        System.out.println(subStackBurntStatus);

                        for (int j = 0; j < subStack.size(); j++) {
                            if (subStackBurntStatus.get(j) < 0) {
                                interimStack.set(j, subStack.get(j)*(-1));
                                burntStatuses.set(j, 2);
                            }else if (subStackBurntStatus.get(j) == 2){
                                interimStack.set(j, subStack.get(j));
                                burntStatuses.set(j, -1);
                            }else {
                                interimStack.set(j, subStack.get(j));
                                burntStatuses.set(j, 1);
                            }
                        }

                        flipCounter = flipCounter + 1;
                        System.out.println("Flip " + flipCounter + " is ------" + interimStack);

                        System.out.println("Burn status are now : " + burntStatuses);

                        if (sortedStackNumbers.equals(interimStack)) break;
                    }

                } else {

                    /* Update the interim stack once the substack has been flipped internally.
                     * Burnt pancakes originally facing up will be now facing down, i.e. status = 2.
                     *
                     * E.g. [3, 2, -1, -4, 5] becomes [4, 1, 2, 3, 5]
                     * Burnt statuses: [1, 1, -1, -1, 1] becomes [2, 2, 1, 1, 1]
                     */
                    subStack = new ArrayList<>(interimStack.subList(0, i + 1));

                    Collections.reverse(subStack);

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

                    System.out.println("Flip " + flipCounter + " is 1^^^^^" + interimStack);

                    if (sortedStackNumbers.equals(interimStack)) break;


                    /* Just flip the top pancake to have the burnt side up.
                     *
                     * E.g. [4, 1, 2, 3, 5] becomes [-4, 1, 2, 3, 5]
                     * Burnt statuses: [2, 2, 1, 1, 1] becomes [-1, 2, 1, 1, 1]
                     */
                    interimStack.set(0, subStack.get(0));
                    burntStatuses.set(0, -1);

                    System.out.println("Burn status are now : " + burntStatuses);
                    System.out.println(subStack);

                    flipCounter = flipCounter + 1;

                    System.out.println("Flip " + flipCounter + " is 2^^^^^" + interimStack);

                    if (sortedStackNumbers.equals(interimStack)) break;


                    /* Flip the whole stack back so the top pancake comes down to the bottom with the
                     * burnt side facing down.
                     *
                     * E.g. [-4, 1, 2, 3, 5] becomes [3, 2, -1, 4, 5]
                     * Burnt statuses: [-1, 2, 1, 1, 1] becomes [1, 1, -1, 2, 1]
                     */
                    subStackBurntStatus = new ArrayList<>(burntStatuses.subList(0, i + 1));

                    Collections.reverse(subStack);
                    Collections.reverse(subStackBurntStatus);

                    System.out.println(subStack);
                    System.out.println(subStackBurntStatus);

                    for (int j = 0; j < subStack.size(); j++) {
                        if (subStackBurntStatus.get(j) < 0) {
                            interimStack.set(j, subStack.get(j)*(-1));
                            burntStatuses.set(j, 2);
                        }else if (subStackBurntStatus.get(j) == 2){
                            interimStack.set(j, subStack.get(j));
                            burntStatuses.set(j, -1);
                        }else {
                            interimStack.set(j, subStack.get(j));
                            burntStatuses.set(j, 1);
                        }
                    }

                    flipCounter = flipCounter + 1;

                    System.out.println("Burn status are now : " + burntStatuses);

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
