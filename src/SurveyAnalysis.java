import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * This program takes an x number of text files,
 * counts the number of occurrences of each line,
 * outputs the number of occurrences for each file
 * in respective descending order.
 * 
 * Written for student body survey analysis in
 * Spring 2020.
 *
 * @author Shaun Loftin
 * 		loftin.12@osu.edu
 *
 */
public class SurveyAnalysis {
	
	/**
	 * Main method to prompt for user input, pass to relevant data entry method,
	 * then pass list of file names to scanFiles method.
	 */
	public static void main(String[] args) throws IOException {
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Enter 1 to get all files in folder");
		System.out.print("Enter 2 to input individual file names....");
		String selection = keyboard.nextLine();
		
		while (!selection.equals("1") && !selection.equals("2")) {
			System.err.println("ERROR: Please enter either 1 or 2.");
			System.out.println("Enter 1 to get all files in folder");
			System.out.print("Enter 2 to input individual file names....");
			selection = keyboard.nextLine();
		}
		
		
		// Branches method calls based on user decision
		ArrayList<String> fileNames;
		if (selection.equals("1")) {
			fileNames = scanFileFolder(keyboard);
		} else {
			fileNames = promptFileNames(keyboard);
		}
		
		// Passes ArrayList of all file names to a method that will analyze/print
		scanFiles(fileNames);
		System.out.println("Done!");
 	}
	
	/**
	 * Prompts the user for the name of the directory to scan all of the data
	 * files are in.
	 * 
	 * @param keyboard
	 * 		{@code Scanner} for user input
	 * @return fileNames
	 * 		{@code ArrayList<String>} of all valid file names
	 */
	private static ArrayList<String> scanFileFolder(Scanner keyboard) {
		ArrayList<String> fileNames = new ArrayList<String>();
		// Prompts user for name of directory
		System.out.println("Enter the name of the directory: ");
		String pathName = keyboard.nextLine();
		// Puts all of file names/subfolder names in a file array
		File[] directory =  new File(pathName).listFiles();

		for (int i = 0; i < directory.length; i++) {
			// checks to make sure we aren't including directories, just files
			if (directory[i].isFile()) {
				fileNames.add(directory[i].toString());
			} 
		}
		
		keyboard.close();
		return fileNames;
	}

	/**
	 * Prompts the user for the list of file names, tests if exists,
	 * imports each name, into an ArrayList.
	 * 
	 * @param keyboard
	 * 		{@code Scanner} for user input
	 * @return fileNames
	 * 		{@code ArrayList<String>} of all valid file names
	 */
	 private static ArrayList<String> promptFileNames(Scanner keyboard) {
		 ArrayList<String> fileNames = new ArrayList<String>();
		 // Prompts user for the name of the first file
		 System.out.print("Enter file number 1 (Leave empty to exit): ");
			
		 String current = keyboard.nextLine();
		 int numFiles = 1;
		 // Continuously prompts user for new file paths until null is entered
		 while (current != null) {
			 try {
				 // Checks to see if file exists
				 File tempFile = new File(current);
				 if (tempFile.exists()) {
					 fileNames.add(current);
					 numFiles++;
				 }
			 } catch (Exception e) {
				 // If file doesn't exist, reports an error to console
				 System.err.println("Error finding or reading file: " + current);
				 System.err.println(e);
			 }
			 System.out.print("Enter file number " + numFiles + "(Leave empty to exit): ");
			 current = keyboard.nextLine();
		 }

		 keyboard.close();
		 return fileNames;
	 }
	 
	 /**
	  * Iterates through every file and creates a map of unique lines
	  * and number of occurrences.
	  * 
	  * @param fileNames
	  * 	{@code ArrayList<String>} of all valid file names
	  */
	 private static void scanFiles(ArrayList<String> fileNames) {
		 // Iterates through all of the entered file names (or directory)
		 for (String current : fileNames) {
			 // Stores strings and number of occurrences in map entries
			 Map<String, Integer> entries = new HashMap<String, Integer>();
			 File inFile = new File(current);
			 try {
				 BufferedReader fileIn = new BufferedReader(new FileReader(inFile));
				 String next = fileIn.readLine();
				 // Checks for trailing spaces on inputs
				 if (next.charAt(next.length()-1) == ' ') {
					 next = next.substring(0, next.length()-2);
				 }
				 // Checks to see if string is already in map
				 while (next != null) {
					 // if not, insert into map with value 1
					 if (!entries.containsKey(next)) {
						 entries.put(next, 1);
					 } else {
						 // if already in map, increment occurrence value
						 int value = entries.get(next);
						 entries.replace(next, value, value+1);
					 }
					 next = fileIn.readLine();
				 }
				 fileIn.close();
			 } catch (IOException e) {
				 // Report error if reading file
				 System.err.println("Error reading file: " + current);
				 System.err.println(e);
			 }
			 printFiles(entries, current);	
		 }

	 }

	 /**
	  * Iterates through entries, prints all in formatted order
	  * 
	  * @param entries
	  * 	{@code Map} of all of the unique strings w/ number of occurrences
	  * @param current
	  * 	{@code String} of the current file name
	  */
	 private static void printFiles(Map<String, Integer> entries, String current) {
		 // Prints header of file name
		 System.out.println("\n PRINTING FILE " + current);
		 // Passes to sorting method to sort by highest occurring string
		 List<Entry<String, Integer>> MapSorted = entriesSortedByValues(entries);
		 int numPrev = 0;
		 // Iterates through the entire map
		 for (Map.Entry<String, Integer> x : MapSorted) {
			 String key = x.getKey();
			 /*
			  * For example, if its the first string that appears 5 times,
			  * a " - 5" appears next to it. If the next string also had 
			  * 5 occurrences, then it doesn't get a " - 5". 
			  * 
			  * If the following string appears 4 times, a " - 4" appears
			  * next to it.
			  * 
			  */
			 if (entries.get(key) != numPrev) {
				 System.out.println(key + " - " + entries.get(key));
			 } else {
				 System.out.println(key);
			 }
			 numPrev = entries.get(key);
		 }
	 }

	 /**
	  * Returns then same map but sorted in descending order by value.
	  * 
	  * @param map
	  * 	{@code Map} of all of the unique strings w/ number of occurrences
	  * @return sortedEntries
	  * 	{@code Map} of all of the unique strings sorted in descending number of occurrences
	  */
	 static <K,V extends Comparable<? super V>> List<Entry<K, V>> entriesSortedByValues(Map<K,V> map) {
		 List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());
		 Collections.sort(sortedEntries, 
			 new Comparator<Entry<K,V>>() {
				 @Override
				 public int compare(Entry<K,V> e1, Entry<K,V> e2) {
					 // switch e2 and e1 to switch from descending to ascending
					 return e2.getValue().compareTo(e1.getValue());
				 }
	 		}
		);

		 return sortedEntries;
	 }

}


