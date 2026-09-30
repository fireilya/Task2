package org.example;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() throws InterruptedException {
        System.out.println("Task 1 demonstration:");
        Task1.CompleteTask(10 /*Enter your size of array here*/);
        System.out.println("================================");
        System.out.println("Task 2 demonstration:");
        Task2.CompleteTask(10 /*Enter your size of array here*/);
        System.out.println("================================");
        System.out.println("Task 3 demonstration:");
        Task3.CompleteTask();
        System.out.println("================================");
        System.out.println("Task 4 demonstration:");
        Task4.CompleteTask();
        System.out.println("==============================");
        System.out.println("Task 5 demonstration:");
        Task5 t5 = new Task5("" /*Enter your master-folder path here*/, "" /*Enter your slave-folder path here*/);
        t5.start();
        Thread.sleep(5000);
        t5.stop();
    }
}
