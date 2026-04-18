package BruteForce;

import java.util.Scanner;

public class Main {
        
        public static void main(String[] args) {
            // Example: Find if a target number exists in an array
            int[] arr = {3, 7, 2, 9, 1, 5};
            
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the target number to search for: ");
            int target = scanner.nextInt();
            
            if (linearSearch(arr, target)) {
                System.out.println("Element found!");
            } else {
                System.out.println("Element not found!");
            }
            
            scanner.close();
        }
        
        // Simple brute force linear search
        public static boolean linearSearch(int[] arr, int target) {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == target) {
                    return true;
                }
            }
            return false;
        }
    }
