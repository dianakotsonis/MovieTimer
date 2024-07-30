import java.io.FileNotFoundException;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * This is the BackendDeveloper's class implements the methods describes in the BackendInterface
 * 
 * @implements BackendInterface
 * @author dianakotsonis
 */
public class BackendDeveloperIndividual<T extends Comparable<T>> implements BackendInterface<IterableMultiKeyRBT<T>> {
  public IterableMultiKeyRBT<Movie> movieList; // The RBT that holds the Movie objects based on
                                               // duration

  /**
   * This constructor initializes the movieList instance variable and creates a
   * BackendDeveloperIndividual object
   * 
   * @param movieList the RBT that holds the Movie objects based on duration
   */
  public BackendDeveloperIndividual(IterableMultiKeyRBT<Movie> movieList) {
    this.movieList = movieList;
  }

  /**
   * This method reads in a csv file (formatted the same as "filmtv_movies - ENG.csv") and creates
   * Movie objects based on the information within the file. It then adds these Movies to the
   * RedBlackTree movieList object.
   * 
   * @param fileName the name of the file to read from
   * @throws FileNotFoundException if the file name is invalid
   */
  public void readFile(String fileName) {
    try {
      // Create a file object (using the fileName given) and read from it using scanner
      File movieFile = new File(fileName);
      Scanner scnr = new Scanner(movieFile);
      ArrayList<String> lineContents = new ArrayList<String>(); // Holds the contents in each line
                                                                // as string objects

      int quoteAmount = 0; // The number of quotes in a line
      int previousChar = 0; // The last character that we added to lineContents

      // Skip the title line
      scnr.nextLine();

      while (scnr.hasNextLine()) {
        String movieLine = scnr.nextLine(); // Stores the next line in the file
        previousChar = 0;
        quoteAmount = 0;

        // For each character in movieLine, check if there is a comma separating two objects. Split
        // the string into substrings based on these comma separating characters, and add them to
        // the lineContents ArrayList
        for (int i = 0; i < movieLine.length(); i++) {
          // We don't need any content other than the Movie properties describes in the Movie class.
          // These can be found in the first 5 comma separated strings of each line.
          if (lineContents.size() > 5) {
            break;
          }
          char currentChar = movieLine.charAt(i);
          // If the current character is a quotation, increment the quoteAmount variable
          if (currentChar == '"') {
            quoteAmount++;
          }
          // If the current character is a comma and not in-between quotation marks, create a
          // substring from previousChar to the current character
          if (currentChar == ',' && (quoteAmount == 0 || quoteAmount == 2)) {
            if (previousChar == 0) {
              lineContents.add(movieLine.substring(previousChar, i));
            } else {
              lineContents.add(movieLine.substring(previousChar + 1, i));
            }
            // Reset quoteAmount to 0 for the next substring
            quoteAmount = 0;
            // Update the previous char variable
            previousChar = i;
          } else if (i == movieLine.length() - 1) {
            // If we are on the last character in the string, add a substring from the prevous char
            // to the end of the string
            lineContents.add(movieLine.substring(previousChar + 1, i + 1));
          }
        }

        // Save the String values in lineContents into their respective Movie property variables
        String title = "";
        String genre = "";
        String year = "";
        Integer year1 = 0;
        String country = "";
        String duration = "";
        Integer duration1 = 0;

        if (lineContents.size() > 1 && lineContents.get(1) != null) {
          title = lineContents.get(1);
        }
        if (lineContents.size() > 3 && lineContents.get(3) != null) {
          genre = lineContents.get(3);
        }
        if (lineContents.size() > 2 && lineContents.get(2) != null) {
          year = lineContents.get(2);
          year1 = Integer.parseInt(year);
        }
        if (lineContents.size() > 5 && lineContents.get(5) != null) {
          country = lineContents.get(5);
        }
        if (lineContents.size() > 4 && lineContents.get(4) != null) {
          duration = lineContents.get(4);
          duration1 = Integer.parseInt(duration);
        }

        // Create a Movie object using these saved properties and insert it into movieList
        Movie movieToAdd = new Movie(title, genre, year1, country, duration1);
        this.movieList.insertSingleKey(movieToAdd);
        // Clear line contents so it is empty for the next line in the file
        lineContents.clear();
      }
      scnr.close();
    } catch (FileNotFoundException e) {
      System.out.println("File is invalid");
    }
  }


  /**
   * This method creates an ArrayList of movie titles that have the minimum duration in the Red
   * Black Tree. If there are multiple movie titles with the same minimum duration, add them all to
   * the ArrayList.
   * 
   * @param movieList the RBT of movie objects based on duration
   * @return ArrayList<String> of movie titles with the minimum duration.
   */
  @Override
  public ArrayList<String> getMinDurationList(IterableMultiKeyRBT<T> movieList) {
    // If the movieList object is null, return null
    if (movieList == null) {
      return null;
    }
    ArrayList<String> minMovieList = new ArrayList<String>(); // the ArrayList storing the titles
                                                              // for the Movies with the smallest
                                                              // duration
    // Cast movieList to become an instance of the IterableyMultiKeyRBT class
    IterableMultiKeyRBT<T> movieList1 = (IterableMultiKeyRBT<T>) movieList;
    // If the movieList has 0 keys, return null
    if (movieList1.numKeys() == 0) {
      return null;
    }
    // Initialize an iterator for the IterableMultiKeyRBT object
    Iterator<T> iterator = movieList1.iterator();
    // Set the minimum duration value to a very high value to start
    int currentMinDuration = 100000;
    Movie currentMinMovie = null;
    // Iterate through the RBT using the iterator object
    while (iterator.hasNext()) {
      Movie movie = (Movie) iterator.next();
     // Movie movie = iterator.next();
      // If the movie's getDuration value is less than the currently saved minimum duration, update
      // the currently saved minimum duration movie to equal this movie, and update the currently
      // saved minimum duration value to its duration.
      if (movie.getDuration() <= currentMinDuration) {
        currentMinMovie = movie;
        currentMinDuration = movie.getDuration();
      }
    }
    // Add the minimum duration movie to the arrayList
    minMovieList.add(currentMinMovie.getTitle());
    // Create another iterator object and go through the RBT again, this time saving all references
    // that have an equal duration to the saved minimum duration value.
    Iterator<T> iterator2 = movieList1.iterator();
    
    while (iterator2.hasNext()) {
      Movie movie = (Movie) iterator2.next();
      if (movie.getDuration() == currentMinDuration) {
        if (movie.getTitle().equals(currentMinMovie.getTitle())){
          continue;
        }
        minMovieList.add(movie.getTitle());
      }
    }
    // Return the minMovieList ArrayList
    return minMovieList;
  }

  /**
   * This method gets an ArrayList of all the movie titles that have a duration within the given
   * threshold. Note that if a movie has a duration equal to one of the threshold bounds, it is
   * included in the ArrayList.
   * 
   * @param movieList the RBT of Movie objects
   * @lowerThreshold the lower bound of the threshold
   * @upperThreshold the upper bound of the threshold
   * @return ArrayList<String> containing the movie titles that have a duration within the threshold
   */
  @Override
  public ArrayList<String> getThresholdDurationList(
      IterableMultiKeyRBT<T> movieList, int lowerThreshold, int upperThreshold) {
    
    // If the movieLIst object is null, or if the lower and upper thresholds do not make sense,
    // return null
    if (movieList == null || lowerThreshold > upperThreshold || upperThreshold < lowerThreshold) {
      return null;
    }
    // Cast movieList to become an instance of the IterableMultiKeyRBT class
    IterableMultiKeyRBT<T> movieList1 = (IterableMultiKeyRBT<T>) movieList;
    // If the movieList has 0 keys, return null
    if (movieList.numKeys() == 0) {
      return null;
    }
    // Create an iterator object from the IterableMultiKeyRBT class
    Iterator<T> iterator = movieList1.iterator();
    ArrayList<String> thresholdDurationList = new ArrayList<String>(); // the ArrayList containing
    // movie titles with a duration within the given threshold

    // Iterate through the entire RBT using the iterator object
    while (iterator.hasNext()) {
      Movie movie = (Movie) iterator.next();
      // If the movie's duration is within the given threshold, add it to the thresholdDurationList
      if (movie.getDuration() <= upperThreshold && movie.getDuration() >= lowerThreshold) {
        thresholdDurationList.add(movie.getTitle());
      }
    }
    // Return the arrayList containing movie titles that have durations within the threshold.
    return thresholdDurationList;
  }

  /**
   * This main method starts running the MovieTimer application by calling the FrontendDeveloper
   * class Note: The FrontendDeveloper class is a placeholder class and will not actually run the
   * desired application (partner's work was not done on time)
   * 
   * @param args unused
   */
  public static void main(String[] args) {
    // Initialize a Backend object and its required parameters
    IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
    BackendDeveloperIndividual<Movie> backend = new BackendDeveloperIndividual<Movie>(movieList);

    // Initialize a Frontend object and its required parameters
    Scanner userInput = new Scanner(System.in);
    FrontendDeveloper frontend = new FrontendDeveloper(userInput, backend);
    // Call runFrontEnd() to begin running the application
    frontend.runFrontEnd();
  }


}
