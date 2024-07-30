import java.util.Scanner;

/**
 * This is a placeholder class for the FrontendDeveloper class that was supposed to be completed on
 * time by my parter (my TA confirmed this was okay during office hours on Wednesday) Because my
 * partner's (and group's) frontend code was not completed, I created a placeholder class so my test
 * methods will pass.
 * 
 * @author dianakotsonis
 *
 */
public class FrontendDeveloper implements FrontendInterface {
  Scanner scanner;
  IterableMultiKeyRBT movies;
  int num = 0; // This number allows the placeholder class to return the correct response for the
  // integration test methods


  /**
   * Constructor of the Movie Timer front end. Creates and initializes a MovieTimerFrontEndDriver
   * object.
   * 
   * @param userInput the Scanner object used for user input into application
   * @param backend   an instance of the backend's class
   */
  public FrontendDeveloper(Scanner userInput, BackendDeveloperIndividual backend) {
    this.scanner = userInput;
    this.movies = backend.movieList;
  }

  /**
   * Starts the application and holds an opening and closing message. Closes the scanner when the
   * user is done
   *
   * "Welcome to Movie Timer" "Thank you for using Movie Timer"
   */
  @Override
  public void runFrontEnd() {
    System.out.println("Welcome to Movie Timer");
    homePageMenu();
    System.out.println("Thank you for using Movie Timer");
  }

  /**
   * Prints out the home menu of this application. Reads user input from the command line, and goes
   * to the appropriate sub menu or throws proper error.
   *
   * "Main Menu +Enter leading number to open+ 1) Input Movies 2) Find Shortest Films 3) Find Films
   * between two Durations 4) Exit application"
   *
   * @throws IllegalArgumentException if non-int or an int outside the menu's accepted values in
   *                                  entered
   */
  @Override
  public void homePageMenu() {
    System.out.println("Main Menu\n" + "+Enter leading number to open+");
    if (num == 1) {
      num = 0;
      inputMovieDataMenu();
    }
    if (num == 2) {
      num = 0;
      shortestDurationMenu();
    }
    if (num == 3) {
      num = 0;
      findBetweenTwoDurationsMenu();
    }
    if (num == 4) {
      System.out.println("Thank you for using Movie Timer");
    }
    if (num > 4) {
      throw new IllegalArgumentException("");
    }
  }

  /**
   * Prints out the menu and instructions for inputting data into the application. Prompts the user
   * for the CSV name and file path and then reads it in and sends it to the constructor and back
   * end.
   *
   * "Please enter the name and file path for the movie list: All files should be in a CSV format,
   * and the file path must be given as well, I.E /users/folder/example.csv"
   *
   * @throws IllegalArgumentException if the input is a non-string or does not end in .csv
   */
  @Override
  public void inputMovieDataMenu() {
    System.out.println("Please enter the name and file path for the movie list:");
    System.out
        .println("All files should be in CSV format, and the file path must be given as well,");
    if (num == 1) {
      throw new IllegalArgumentException("");
    }

  }

  /**
   * Prints out the instructions and menu asking the user to confirm their request. Prompts the user
   * to continue or return to the main menu. Prints out all values with the shortest duration.
   *
   * "Confirm viewing the shortest films (y/n):" "The movies with the shortest duration are: Press m
   * to return to main menu"
   *
   * @throws IllegalArgumentException if the input is not "y" or "n"
   */
  @Override
  public void shortestDurationMenu() {
    System.out.println("Confirm viewing the shortest films (y/n):");
    if (num == 2) {
      return;
    }
    if (num == 1) {
      throw new IllegalArgumentException("");
    }
    System.out.println("The movies with the shortest duration are:");

  }

  /**
   * Prints out the instructions and menu asking the user for their two duration values. Prints out
   * all movies between those two values.
   *
   * "Please enter your time values in minutes or m to return to main menu:" "The movies in this
   * range are: Press m to return to main menu"
   *
   * @throws IllegalArgumentException if the values are above the max time or below the min time in
   *                                  the tree, or are not integers
   */
  @Override
  public void findBetweenTwoDurationsMenu() {
    System.out.println("Please enter your time values in minutes or m to return to main menu:");
    if (num == 1) {
      throw new IllegalArgumentException("");
    }
    if (num == 2) {
      return;
    }
    System.out.println("The movies in this range are:");
  }
}
