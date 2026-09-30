package org.example;

import java.util.ArrayList;
import java.util.Random;

public class Task1 {
    private static final Random rand = new Random();
    public static void CompleteTask(int numbersCount)
    {
        var numbers = new ArrayList<Integer>();
        for (var i = 0; i < numbersCount; i++)
            numbers.add(rand.nextInt());

        System.out.println(numbers);
        System.out.println(numbers.stream()
                .mapToDouble(x -> x)
                .average()
                .orElse(0.0));
    }
}
