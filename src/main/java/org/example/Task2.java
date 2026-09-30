package org.example;

import java.util.ArrayList;
import java.util.Random;

public class Task2 {
    private static final Random rand = new Random();

    private static ArrayList<Double> GenerateArray(int count) {
        var numbers = new ArrayList<Double>();
        for (var i = 0; i < count; i++)
            numbers.add(rand.nextDouble());
        return numbers;
    }

    private static void BubbleSortArray(ArrayList<Double> arr) {
        boolean swapped;
        for (int i = 0; i < arr.size() - 1; i++) {
            swapped = false;
            for (int j = 0; j < arr.size() - i - 1; j++) {
                if (arr.get(j) > arr.get(j + 1)) {
                    double temp = arr.get(j);
                    arr.set(j, arr.get(j + 1));
                    arr.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped)
                break;
        }
    }

    public static void CompleteTask(int numbersCount) {
        var numbers = GenerateArray(numbersCount);
        System.out.println(numbers);
        BubbleSortArray(numbers);
        System.out.println(numbers);
    }
}
