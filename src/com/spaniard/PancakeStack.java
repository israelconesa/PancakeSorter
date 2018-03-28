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

    /*
    Constructor creates a pancake stack object and initialises the value of the original
    stack and the sorted stack, i.e. the solution.
    */
    public PancakeStack(){
        getOriginalStack();
        sortStack();
    }

    /* Read the second line of text file to gather the original pancake stack and return it to the constructor */
    private ArrayList<String> getOriginalStack(){
        File textFile = new File("/home/israelconesa/Development/PancakeSorter/src/myfile.txt");
        try {
            String line2 = Files.readAllLines(Paths.get(String.valueOf(textFile))).get(1);
            originalStack = new ArrayList<String>(Arrays.asList(line2.split(" ")));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return this.originalStack;
    }

    /* Sort stack to learn the solution */
    private ArrayList<String> sortStack(){
        sortedStack = new ArrayList<>(originalStack);
        Collections.sort(sortedStack);
        return this.sortedStack;
    }

    public void letTheFlippingBegin() {

        interimStack = new ArrayList<>(getOriginalStack());

        /* Work your way from the bottom to the top of the stack */
        for (int i = interimStack.size()-1; i>=0; i--) {
            System.out.println(interimStack);

            if ( //pancake under test is NOT at the bottom of the interim stack
                !interimStack.get(i).equals(sortedStack.get(i))
             ) {
                /* identify a substack starting from the top and ending on the largest pancake */
                subStack = new ArrayList<String>(interimStack.subList(0, interimStack.lastIndexOf(sortedStack.get(i)) + 1));

                /* create another substack from that point to the bottom of the pile */
                subStack2 = new ArrayList<String>(interimStack.subList(interimStack.lastIndexOf(sortedStack.get(i)) + 1, interimStack.size()));

                Collections.reverse(subStack);

                /* put together the stacks */
                subStack.addAll(subStack2);

                /* this is how the whole stack looks like now after bringing the largest pancake to the bottom */
                interimStack = new ArrayList<>(subStack);

                System.out.println("Flip " + i + " is " + interimStack);

                /* Break the loop if the right sorting has been achieved */
                if (sortedStack.equals(interimStack)) break;

                //            if (!interimStack.get(i).equals(interimStack.get(i-1))) {
                /* identify a substack starting from the bottom and ending on the largest pancake */
                subStack = new ArrayList<String>(interimStack.subList(0, i + 1));

                /* create another substack from that point to the top of the pile */
                subStack2 = new ArrayList<String>(interimStack.subList(i + 1, interimStack.size()));

                Collections.reverse(subStack);

                /* put together the stacks */
                subStack.addAll(subStack2);

                interimStack = new ArrayList<>(subStack);

                System.out.println("Flip " + i + " is ---- " + interimStack);

                /* Break the loop if the right sorting has been achieved */
                if (sortedStack.equals(interimStack)) break;
//            }
            } else {
                /* Pancake under test is already at the bottom of the interim
                stack so start the iteration again to find next one to go over */
                interimStack = new ArrayList<>(interimStack);
            }

        }
    }

    public static void main(String[] args) {

        PancakeStack pancakeStack = new PancakeStack();

        /* Ensure that the original stack is not already ordered before start flipping pancakes */
        if (!pancakeStack.sortedStack.equals(pancakeStack.originalStack)) {
            pancakeStack.letTheFlippingBegin();
        }else {
            System.out.println("Don't be cheeky, the pancake stack is already sorted");
        }
    }
}
