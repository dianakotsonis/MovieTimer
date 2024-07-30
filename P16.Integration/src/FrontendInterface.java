import java.util.Scanner;

/**
* Interface for operating and running the front end of the Movie Timer project
* 
* @author Wylie Dituri
* @author Avi Tamotia
* @author Lucy Finnerty
*/
public interface FrontendInterface {

 /**
  * Constructor of the Movie Timer front end. Creates and initializes a
  * MovieTimerFrontEndDriver object.
  *
  * @param movieCSV connects the front end to the base of the red black BST on the backend
  */
	
	/*public MovieTimerFrontEndDriver(Scanner userInput, BackendInterface backend){
	scanner = userInput;
	BackendMovieTimer movies = new BackendMovieTimer(backend);
  }*/
	
 /**
  * Launches the front end application
  */
 public static void main(String[] args){}


 /**
  * Starts the application and holds an opening and closing message. Closes the scanner when
  * the user is done
  *
  * "Welcome to Movie Timer"
  * "Thank you for using Movie Timer"
  */
 public void runFrontEnd();

 /**
  * Prints out the home menu of this application. Reads user input from the command line, and
  * goes to the appropriate sub menu or throws proper error.
  *
  * "Main Menu
  * +Enter leading number to open+
  *  1) Input Movies
  *  2) Find Shortest Films
  *  3) Find Films between two Durations
  *  4) Exit application"
  *
  * @throws IllegalArgumentException if  non-int or an int outside the menu's accepted
  * values in entered
  */
 public void homePageMenu();

 /**
  * Prints out the menu and instructions for inputting data into the application. Prompts the
  * user for the CSV name and file path and then reads it in and sends it to the constructor
  * and back end.
  *
  * "Please enter the name and file path for the movie list:
  * All files should be in a CSV format, and the file path must be given as well,
  * I.E /users/folder/example.csv"
  *
  * @throws IllegalArgumentException if the input is a non-string or does not end in .csv
  */
 public void inputMovieDataMenu();

 /**
  * Prints out the instructions and menu asking the user to confirm their request. Prompts
  * the user to continue or return to the main menu. Prints out all values
  * with the shortest duration.
  *
  * "Confirm viewing the shortest films (y/n):"
  * "The movies with the shortest duration are:
  * Press m to return to main menu"
  *
  * @throws IllegalArgumentException if the input is not "y" or "n"
  */
 public void shortestDurationMenu();

 /**
  * Prints out the instructions and menu asking the user for their two duration values. Prints
  * out all movies between those two values.
  *
  * "Please enter your time values in minutes or m to return to main menu:"
  * "The movies in this range are:
  * Press m to return to main menu"
  *
  * @throws IllegalArgumentException if the values are above the max time or below the min
  * time in the tree, or are not integers
  */
 public void findBetweenTwoDurationsMenu();

}
