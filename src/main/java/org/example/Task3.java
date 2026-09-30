package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Task3 {
    private static class Employee {
        private String fullName;
        private Integer age;
        private String department;
        private Double salary;

        Employee(String fullName, Integer age, String department, Double salary) {
            this.fullName = fullName;
            this.age = age;
            this.department = department;
            this.salary = salary;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public Double getSalary() {
            return salary;
        }

        public void setSalary(Double salary) {
            this.salary = salary;
        }

        @Override
        public String toString() {
            return "Employee {fullName=" + fullName + ", age=" + age + ", department=" + department + ", salary=" + salary + "}";
        }
    }

    private static class SalaryComparator implements Comparator<Employee> {
        @Override
        public int compare(Employee o1, Employee o2) {
            return o1.getSalary().compareTo(o2.getSalary());
        }
    }

    public static void CompleteTask() {
        new ArrayList<>(List.of(
                new Employee(
                        "Фёдор Иванович Тютчев",
                        45,
                        "Отдел разработки",
                        450000.0),
                new Employee(
                        "Лев Николаевич Чайковский",
                        33,
                        "Отдел маркетинга",
                        70000.0),
                new Employee(
                        "Александр Сергеевич Пушкин",
                        37,
                        "Отдел дизайна",
                        30000.0),
                new Employee(
                        "Михаил Юрьевич Лермонтов",
                        26,
                        "Отдел аналитики",
                        87000.0),
                new Employee(
                        "Фёдор Михайлович Достоевский",
                        54,
                        "Отдел Data Science",
                        120000.0)
        )).stream()
                .sorted(new SalaryComparator())
                .forEach(System.out::println);
    }
}
