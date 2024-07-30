import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * This class tests the functionality of the Backend's code, including reading in files, getting
 * minimum and threshold duration lists, and confirming that Movie objects and movieList(RBT)
 * objects are created correctly.
 * 
 * @author dianakotsonis
 */
public class BackendDeveloperTests {

  /**
   * This tests the readFile method of the Backend Interface by confirming that a csv file is read
   * in correctly. It does so by confirming that the correct movie objects are made from the file,
   * and that they are inserted properly into the RBT movieList.
   * 
   * @throws FileNotFoundException if the file name is not valid
   */
  @Test
  public void testReadingFile() throws FileNotFoundException {
    // 1. Use a valid file name in csv format and confirm that when calling the readFile, a valid
    // RBT tree is returned with all the movie objects created correctly
    {
      try {
        // Using the valid file name, call the readFile method and save it in the RBT movieList
        IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
        BackendDeveloperIndividual<Movie> test = new BackendDeveloperIndividual<Movie>(movieList);
        String filename = "MovieList.csv";
        test.readFile(filename);

        // Create a list that has the expected values of movieList based on the file contents
        IterableMultiKeyRBT<Movie> expectedList = new IterableMultiKeyRBT<Movie>();
        Movie movie1 = new Movie("movieTitle1", "Animation", 2000, "United States", 100);
        expectedList.insertSingleKey(movie1);
        Movie movie2 = new Movie("movieTitle2", "Drama", 2001, "Italy", 101);
        expectedList.insertSingleKey(movie2);
        Movie movie3 = new Movie("movieTitle3", "Comedy", 2002, "Greece", 102);
        expectedList.insertSingleKey(movie3);
        Movie movie4 = new Movie("movieTitle4", "Comedy", 2003, "United States", 103);
        expectedList.insertSingleKey(movie4);

        // Confirm that the movieList created is equivalent to the expectedList
        Iterator<Movie> movieListIterator = movieList.iterator();
        Iterator<Movie> expectedListIterator = expectedList.iterator();
        while (movieListIterator.hasNext()) {
          Movie actual = movieListIterator.next();
          Movie expected = expectedListIterator.next();
          assertTrue(actual.compareTo(expected) == 0,
              "the RBT of Movie objects was not created correctly from the file");
        }
      } catch (Exception e) {
        assertEquals(0, 1, "Exception for file creation was thrown when it was not supposed to");
      }
    }
    // 2. Use an invalid file name and confirm it throws an exception
    {
      try {
        String fileName = "invalidName.pdf";
        IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
        BackendDeveloperIndividual<Movie> test = new BackendDeveloperIndividual<Movie>(movieList);
        test.readFile(fileName);
      } catch (Exception e) {
        assertEquals(1, 0, "Incorrect exeption was thrown when an invalid filename was used");
      }
    }
  }

  /**
   * This tests the getMinDurationList() method in the Backend Interface. When calling this method,
   * with a RBT movieList, an arrayList of movie titles that have the minimum duration should be
   * returned.
   */
  @Test
  public void testGetMinDurationList() {
    // 1. If the movieList is null, an empty list is returned
    {
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      BackendDeveloperIndividual<Movie> test = new BackendDeveloperIndividual<Movie>(movieList);
      ArrayList<String> returnedList = test.getMinDurationList(movieList);
      ArrayList<String> expectedList = null;
      assertTrue(returnedList == null && expectedList == null,
          "Calling getMinDurationList with a null movieList did not return a null list");
    }
    // 2. If the movie list is valid and contains one minimum duration, it returns an arrayList with
    // one element containing that duration
    {
      // This will not compile yet, but these are the movie objects (created with the Movie class)
      Movie minMovie = new Movie("Title1", "Genre1", 2000, "Country1", 120);
      Movie maxMovie = new Movie("Title2", "Genre2", 2001, "Country2", 140);

      // Insert these movies into the movieList object
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      movieList.insertSingleKey(maxMovie);
      movieList.insertSingleKey(minMovie);
      // Initialize an instance of the Backend's class
      BackendDeveloperIndividual<Movie> test = new BackendDeveloperIndividual<Movie>(movieList);

      // Call the get minDuration list, and see if the correct Movie title is returned
      ArrayList<String> returnedList = test.getMinDurationList(movieList);
      ArrayList<String> expectedList = new ArrayList<String>();
      expectedList.add("Title1");
      for (int i = 0; i < expectedList.size(); i++) {
        assertEquals(returnedList.get(i), expectedList.get(i),
            "ArrayList returned for only one minimum duration element is not correct.");
      }

    }
    // 3. If there are multiple movies with the same minimum duration, an arrayList containing each
    // movie title is returned.
    {
      // Create Movie objects using the Movie class
      Movie maxMovie1 = new Movie("Title1", "Genre2", 2000, "Country2", 160);
      Movie maxMovie2 = new Movie("Title2", "Genre2", 2000, "Country2", 140);
      Movie minMovie1 = new Movie("Title3", "Genre2", 2001, "Country1", 120);
      Movie minMovie2 = new Movie("Title4", "Genre1", 2000, "Country1", 120);

      // Add them into movieList
      // And set up their references to each other
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      movieList.insertSingleKey(maxMovie1);
      movieList.insertSingleKey(maxMovie2);
      movieList.insertSingleKey(minMovie1);
      movieList.insertSingleKey(minMovie2);
      // Initialize an instance of the Backend's class
      BackendDeveloperIndividual<Movie> test = new BackendDeveloperIndividual<Movie>(movieList);

      // Call the getMinDuration method and confirm the arrayList is returned as expected.
      ArrayList<String> returnedList = test.getMinDurationList(movieList);
      ArrayList<String> expected2List = new ArrayList<String>();
      expected2List.add("Title4");
      expected2List.add("Title3");
      for (int i = 0; i < expected2List.size(); i++) {
        assertEquals(expected2List.get(i), returnedList.get(i),
            "the List returned did not have the required nodes.");
      }
    }
    // 4. if the RBT contains movies with the same duration, all movies are returned when calling
    // getMinDurationList()
    {
      // Create Movie objects using the Movie class
      Movie minMovie1 = new Movie("Title1", "Genre2", 2003, "Country2", 120);
      Movie minMovie2 = new Movie("Title2", "Genre2", 2002, "Country2", 120);
      Movie minMovie3 = new Movie("Title3", "Genre1", 2001, "Country1", 120);
      Movie minMovie4 = new Movie("Title4", "Genre1", 2000, "Country1", 120);

      // Add them into movieList
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      movieList.insertSingleKey(minMovie1);
      movieList.insertSingleKey(minMovie2);
      movieList.insertSingleKey(minMovie3);
      movieList.insertSingleKey(minMovie4);
      // Initialize an instance of the Backend's class
      BackendDeveloperIndividual<Movie> test = new BackendDeveloperIndividual<Movie>(movieList);

      // Call the getMinDuration method and confirm the arrayList is returned as expected.
      ArrayList<String> returnedList = test.getMinDurationList(movieList);
      assertTrue(returnedList.contains(minMovie1.getTitle()),
          "ArrayList does not contain minMovie1 when it should");
      assertTrue(returnedList.contains(minMovie2.getTitle()),
          "ArrayList does not contain minMovie1 when it should");
      assertTrue(returnedList.contains(minMovie3.getTitle()),
          "ArrayList does not contain minMovie1 when it should");
      assertTrue(returnedList.contains(minMovie4.getTitle()),
          "ArrayList does not contain minMovie1 when it should");
    }
  }

  /**
   * This method tests the getThresholdDurationList specified in the BackendInterface. The goal is
   * to return an ArrayList that contains only the movies that have a duration within and including
   * the given threshold.
   */
  @Test
  public void testGetThresholdList() {
    // 1. If the movieList is null, an empty list is returned
    {
      IterableMultiKeyRBT<Movie> movieList = null;
      BackendDeveloperIndividual<Movie> test = new BackendDeveloperIndividual<Movie>(movieList);
      ArrayList<String> returnedList = test.getThresholdDurationList(movieList, 100, 120);
      ArrayList<String> expectedList = null;
      assertTrue(returnedList == null && expectedList == null, "Calling getThresholdDurationList "
          + "with a null movieList did not result in a returned null list");
    }
    // 2. Confirm that the list returned contains all movies within the valid threshold, including
    // durations that equal the threshold
    {
      // Create movie objects using the Movie Clas
      Movie movie1 = new Movie("Title1", "Genre2", 2000, "Country2", 160);
      Movie movie2 = new Movie("Title2", "Genre2", 2001, "Country2", 140);
      Movie movie3 = new Movie("Title3", "Genre1", 2002, "Country1", 130);
      Movie movie4 = new Movie("Title4", "Genre1", 2003, "Country1", 120);

      // Add them to a movieList object
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      movieList.insertSingleKey(movie1);
      movieList.insertSingleKey(movie2);
      movieList.insertSingleKey(movie3);
      movieList.insertSingleKey(movie4);
      // Initialize an instance of the Backend's class
      BackendDeveloperIndividual<Movie> test = new BackendDeveloperIndividual<Movie>(movieList);

      // Call the getThresholdDurationList method and confirm that the list returned is correct
      ArrayList<String> returnedList = test.getThresholdDurationList(movieList, 120, 135);
      ArrayList<String> expectedList = new ArrayList<String>();
      expectedList.add("Title4");
      expectedList.add("Title3");
      for (int i = 0; i < expectedList.size(); i++) {
        assertEquals(returnedList.get(i), expectedList.get(i),
            "The returned list when calling getThresholdDurationList is not correct.");
      }

    }
    // 3. If the user inputs an invalid threshold, confirm that the list returned is empty
    {
      // Create movie objects using the Movie Class
      Movie movie1 = new Movie("Title1", "Genre2", 2001, "Country2", 160);
      Movie movie2 = new Movie("Title2", "Genre2", 2002, "Country2", 140);
      Movie movie3 = new Movie("Title3", "Genre1", 2003, "Country1", 130);
      Movie movie4 = new Movie("Title4", "Genre1", 2004, "Country1", 120);

      // Add them to a movieList object
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      movieList.insertSingleKey(movie1);
      movieList.insertSingleKey(movie2);
      movieList.insertSingleKey(movie3);
      movieList.insertSingleKey(movie4);
      // Initialize an instance of the Backend's class
      BackendDeveloperIndividual<Movie> test = new BackendDeveloperIndividual<Movie>(movieList);

      // Call the getThresholdDurationList method with invalid thresholds
      ArrayList<String> returnedList = test.getThresholdDurationList(movieList, 140, 100);
      ArrayList<String> expectedList = null;
      assertTrue(returnedList == null && expectedList == null,
          "The list returned was not null when the thresholds given are invalid.");
    }
    {
      // Create movie objects using the Movie Class
      Movie movie1 = new Movie("Title1", "Genre2", 2000, "Country2", 160);
      Movie movie2 = new Movie("Title2", "Genre2", 2001, "Country2", 140);
      Movie movie3 = new Movie("Title3", "Genre1", 2002, "Country1", 130);
      Movie movie4 = new Movie("Title4", "Genre1", 2003, "Country1", 120);
      Movie movie5 = new Movie("Title5", "Genre5", 2003, "Country", 135);
      Movie movie6 = new Movie("Title6", "Genre5", 2003, "Country", 170);

      // Add them to a movieList object
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      movieList.insertSingleKey(movie1);
      movieList.insertSingleKey(movie2);
      movieList.insertSingleKey(movie3);
      movieList.insertSingleKey(movie4);
      movieList.insertSingleKey(movie5);
      movieList.insertSingleKey(movie6);
      // Initialize an instance of the Backend's class
      BackendDeveloperIndividual<Movie> test = new BackendDeveloperIndividual<Movie>(movieList);

      // Call the getThresholdDurationList method and confirm that the list returned is correct
      ArrayList<String> returnedList = test.getThresholdDurationList(movieList, 120, 135);
      ArrayList<String> expectedList = new ArrayList<String>();
      expectedList.add("Title4");
      expectedList.add("Title3");
      expectedList.add("Title5");
      for (int i = 0; i < expectedList.size(); i++) {
        assertEquals(returnedList.get(i), expectedList.get(i),
            "The returned list when calling getThresholdDurationList is not correct.");
      }
    }
  }

  /**
   * This tests that the BackendDeveloper class constructs objects correctly by creating Backend
   * Developer objects.
   * 
   */
  @Test
  public void testBackendConstructor() {
    // 1. Confirm that when given the correct inputs, a valid BackendConstructor is created
    {
      try {
        // Create a BackendMovieTimer object with the correct parameters
        IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
        BackendDeveloperIndividual<Movie> testObject =
            new BackendDeveloperIndividual<Movie>(movieList);

        // Confirm that the instances were created correctly:
        IterableMultiKeyRBT<Movie> actualMovieList = testObject.movieList;
        IterableMultiKeyRBT<Movie> expectedMovieList = movieList;
        assertEquals(actualMovieList, expectedMovieList,
            "movieList was not saved as expected when creating a BackendMovieTimer object");
      } catch (Exception e) {
        assertEquals(1, 0, "Exception was thrown for creating a valid BackendMovieTimer Object");
      }
    }
  }

  /**
   * This tests the Movie Class by confirming that Movie Objects are created and accessed correctly.
   * This test will not compile until the Movie Class is implemented.
   */
  @Test
  public void testMovieClass() {
    // 1. Create a valid Movie Object and confirm its instance variables are initialized correctly.
    // by accessing them using the accessor methods.
    {
      // Create a movie object with valid parameters (title, genre, year, country, and duration)
      Movie testObject = new Movie("title", "genre", 2003, "country", 120);

      // Confirm that the instances were initialized correctly by calling their respective accessor
      // method.
      String expectedTitle = "title";
      String actualTitle = testObject.getTitle();
      assertEquals(expectedTitle, actualTitle, "Title was not initialized correctly");

      String expectedGenre = "genre";
      String actualGenre = testObject.getGenre();
      assertEquals(expectedGenre, actualGenre, "Genre was not initialized correctly");

      int expectedYear = 2003;
      int actualYear = testObject.getYear();
      assertEquals(expectedYear, actualYear, "Year was not initialized correctly");

      String expectedCountry = "country";
      String actualCountry = testObject.getCountry();
      assertEquals(expectedCountry, actualCountry, "Country was not initialized correctly");

      int expectedDuration = 120;
      int actualDuration = testObject.getDuration();
      assertEquals(expectedDuration, actualDuration, "Duration was not initialized correctly");
    }
  }

  /**
   * Tests that all "getting instructions" methods work in the Frontend Interface Note: This
   * FrontendDeveloper class is a placeholder class, because my partner's work was not done on time.
   * All tests should still pass. The "num" variable below allows for the placeholder class to
   * return the correct outputs based on the circumstances
   */
  @Test
  public void testIntergration1() {
    // 1. Test that shortestDurationMenu() method works correctly
    // 1a. Using valid inputs
    {
      // Instantiate objects
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      BackendDeveloperIndividual<Movie> backend = new BackendDeveloperIndividual<Movie>(movieList);
      Scanner userInput = new Scanner(System.in);
      FrontendDeveloper frontend = new FrontendDeveloper(userInput, backend);
      // Instantiate an input that is valid using the TextUITester class
      TextUITester frontendText = new TextUITester("MovieList.csv");

      // Run the inputMovieDataMenu method, confirm it's outputs are correct using the TextUITester
      // class
      try {
        frontend.inputMovieDataMenu();
        String output = frontendText.checkOutput();
        if (output.startsWith("Please enter the name and file path for the movie list:")
            && output.contains(
                "All files should be in CSV format, and the file path must be given as well,")) {
          assertEquals(1, 1, "Expected output is correct when starting program");
        } else {
          assertEquals(1, 0, "Expected output is incorrect when starting program");
        }
      } catch (Exception e) {
        assertEquals(1, 0, "Exception thrown when it was not supposed to");
      }
    }
    // 1b. Using invalid inputs
    {
      // Instantiate objects
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      BackendDeveloperIndividual<Movie> backend = new BackendDeveloperIndividual<Movie>(movieList);
      Scanner userInput = new Scanner(System.in);
      FrontendDeveloper frontend = new FrontendDeveloper(userInput, backend);
      frontend.num = 1; // This number allows the placeholder class to return the correct response
      // Instantiate an input that is invalid using the TextUITester class
      TextUITester frontendText = new TextUITester("/users/folder/example");

      // Run the inputMovieDataMenu method, confirm it's outputs are correct using the TextUITester
      // class
      try {
        frontend.inputMovieDataMenu();
        String output = frontendText.checkOutput();
        if (output.startsWith("Please enter the name and file path for the movie list:")
            && output.contains(
                "All files should be in CSV format, and the file path must be given as well,")) {
          assertEquals(1, 1, "Expected output is correct when starting program");
        } else {
          assertEquals(1, 0, "Expected output is incorrect when starting program");
        }
        // Since the user will have input an invalid input, create an assertion failure if an
        // IllegalArgumentException is not thrown
        assertEquals(1, 0, "Did not throw an IllegalArgumentException when input was invalid");
      } catch (IllegalArgumentException e) {
        // If we catch an IllegalArgumentException, this method worked as expected for invalid
        // inputs
      } catch (Exception e) {
        assertEquals(1, 0, "threw the wrong kind of exception");
      }
    }
    // 2. Test that findBetweenTwoDurationsMenu() works correctly
    // 2a. Using valid inputs, which are either number duration values or "m"
    {
      // Instantiate objects
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      BackendDeveloperIndividual<Movie> backend = new BackendDeveloperIndividual<Movie>(movieList);
      Scanner userInput = new Scanner(System.in);
      FrontendDeveloper frontend = new FrontendDeveloper(userInput, backend);
      // Instantiate an input that is valid using the TextUITester class
      TextUITester frontendText = new TextUITester("100 130");
      frontend.num = 0; // This number allows the placeholder class to return the correct response

      // Run the findBetweenTwoDurationsMenu() method, confirm it's outputs are correct using the
      // TextUITester class
      try {
        frontend.findBetweenTwoDurationsMenu();
        String output = frontendText.checkOutput();
        if (output
            .startsWith("Please enter your time values in minutes or m to return to main menu:")
            && output.contains("The movies in this range are:")) {
          assertEquals(1, 1, "Expected output is correct when starting program");
        } else {
          assertEquals(1, 0, "Expected output is incorrect when starting program");
        }
      } catch (Exception e) {
        assertEquals(1, 0, "Exception thrown when it was not supposed to");
      }
    }
    {
      // Instantiate objects
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      BackendDeveloperIndividual<Movie> backend = new BackendDeveloperIndividual<Movie>(movieList);
      Scanner userInput = new Scanner(System.in);
      FrontendDeveloper frontend = new FrontendDeveloper(userInput, backend);
      frontend.num = 2; // This number allows the placeholder class to return the correct response
      // Instantiate an input that is valid using the TextUITester class
      TextUITester frontendText = new TextUITester("m");

      // Run the findBetweenTwoDurationsMenu() method, confirm it's outputs are correct using the
      // TextUITester class
      try {
        frontend.findBetweenTwoDurationsMenu();
        String output = frontendText.checkOutput();
        if (output
            .startsWith("Please enter your time values in minutes or m to return to main menu:")
            && (!output.contains("The movies in this range are:"))) {
          assertEquals(1, 1, "Expected output is correct when starting program");
        } else {
          assertEquals(1, 0, "Expected output is incorrect when starting program");
        }
      } catch (Exception e) {
        assertEquals(1, 0, "Exception thrown when it was not supposed to");
      }
    }
    // 2b. Using invalid inputs
    {
      // Instantiate objects
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      BackendDeveloperIndividual<Movie> backend = new BackendDeveloperIndividual<Movie>(movieList);
      Scanner userInput = new Scanner(System.in);
      FrontendDeveloper frontend = new FrontendDeveloper(userInput, backend);
      // Instantiate an input that is invalid using the TextUITester class
      TextUITester frontendText = new TextUITester("string");
      frontend.num = 1;// This number allows the placeholder class to return the correct response

      // Run the findBetweenTwoDurationsMenu() method, confirm it's outputs are correct using the
      // TextUITester class
      try {
        frontend.findBetweenTwoDurationsMenu();
        String output = frontendText.checkOutput();
        if (output
            .startsWith("Please enter your time values in minutes or m to return to main menu:")
            && output.contains(
                "All files should be in CSV format, and the file path must be given as well,")) {
          assertEquals(1, 1, "Expected output is correct when starting program");
        } else {
          assertEquals(1, 0, "Expected output is incorrect when starting program");
        }
        // Since the user will have input an invalid input, create an assertion failure if an
        // IllegalArgumentException is not thrown
        assertEquals(1, 0, "Did not throw an IllegalArgumentException when input was invalid");
      } catch (IllegalArgumentException e) {
        // If we catch an IllegalArgumentException, this method worked as expected for invalid
        // inputs
      } catch (Exception e) {
        assertEquals(1, 0, "threw the wrong kind of exception");
      }
    }
    // 3. Test that the shortestDurationMenu() works correclty
    // 3a. Using valid inputs: "y" and "n"
    {
      // Instantiate objects
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      BackendDeveloperIndividual<Movie> backend = new BackendDeveloperIndividual<Movie>(movieList);
      Scanner userInput = new Scanner(System.in);
      FrontendDeveloper frontend = new FrontendDeveloper(userInput, backend);
      // Instantiate an input that is valid using the TextUITester class
      TextUITester frontendText = new TextUITester("y");
      frontend.num = 0;// This number allows the placeholder class to return the correct response

      // Run the shortestDuationMenu() method, confirm it's outputs are correct using the
      // TextUITester class
      try {
        frontend.shortestDurationMenu();
        String output = frontendText.checkOutput();
        if (output.startsWith("Confirm viewing the shortest films (y/n):")
            && output.contains("The movies with the shortest duration are:")) {
          assertEquals(1, 1, "Expected output is correct when starting program");
        } else {
          assertEquals(1, 0, "Expected output is incorrect when starting program");
        }
      } catch (Exception e) {
        assertEquals(1, 0, "Exception thrown when it was not supposed to");
      }
    }
    {
      // Instantiate objects
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      BackendDeveloperIndividual<Movie> backend = new BackendDeveloperIndividual<Movie>(movieList);
      Scanner userInput = new Scanner(System.in);
      FrontendDeveloper frontend = new FrontendDeveloper(userInput, backend);
      // Instantiate an input that is valid using the TextUITester class
      TextUITester frontendText = new TextUITester("n");
      frontend.num = 2;// This number allows the placeholder class to return the correct response

      // Run the shortestDurationMenu() method, confirm it's outputs are correct using the
      // TextUITester class
      try {
        frontend.shortestDurationMenu();
        String output = frontendText.checkOutput();
        if (output.startsWith("Confirm viewing the shortest films (y/n):")
            && (!output.contains("The movies with the shortest duration are:"))) {
          assertEquals(1, 1, "Expected output is correct when starting program");
        } else {
          assertEquals(1, 0, "Expected output is incorrect when starting program");
        }
      } catch (Exception e) {
        assertEquals(1, 0, "Exception thrown when it was not supposed to");
      }
    }
    // 3b. Using invalid inputs
    {
      // Instantiate objects
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      BackendDeveloperIndividual<Movie> backend = new BackendDeveloperIndividual<Movie>(movieList);
      Scanner userInput = new Scanner(System.in);
      FrontendDeveloper frontend = new FrontendDeveloper(userInput, backend);
      // Instantiate an input that is invalid using the TextUITester class
      TextUITester frontendText = new TextUITester("snay");
      frontend.num = 1;// This number allows the placeholder class to return the correct response

      // Run the shortestDurationMenu() method, confirm it's outputs are correct using the
      // TextUITester class
      try {
        frontend.shortestDurationMenu();
        String output = frontendText.checkOutput();
        if (output.startsWith("Please enter the name and file path for the movie list:")
            && output.contains(
                "All files should be in CSV format, and the file path must be given as well,")) {
          assertEquals(1, 1, "Expected output is correct when starting program");
        } else {
          assertEquals(1, 0, "Expected output is incorrect when starting program");
        }
        // Since the user will have input an invalid input, create an assertion failure if an
        // IllegalArgumentException is not thrown
        assertEquals(1, 0, "Did not throw an IllegalArgumentException when input was invalid");
      } catch (IllegalArgumentException e) {
        // If we catch an IllegalArgumentException, this method worked as expected for invalid
        // inputs
      } catch (Exception e) {
        assertEquals(1, 0, "threw the wrong kind of exception");
      }
    }
  }

  /**
   * Tests that all "startup" methods for beginning the application work correctly
   */
  @Test
  public void testIntegration2() {
    // 1. Test runFrontEnd method to confirm that starting the application prints out the messages
    {
      // Instantiate objects
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      BackendDeveloperIndividual<Movie> backend = new BackendDeveloperIndividual<Movie>(movieList);
      Scanner userInput = new Scanner(System.in);
      FrontendDeveloper frontend = new FrontendDeveloper(userInput, backend);
      // Instantiate TextUITester class with no user input (because this method doesn't require any)
      TextUITester frontendText = new TextUITester("");

      // Run the runFrontEnd() method, confirm it's outputs are correct using the TextUITester
      // class
      try {
        frontend.runFrontEnd();
        String output = frontendText.checkOutput();
        if (output.startsWith("Welcome to Movie Timer")
            && output.contains("Thank you for using Movie Timer")) {
          assertEquals(1, 1, "Expected output is correct when starting program");
        } else {
          assertEquals(1, 0, "Expected output is incorrect when starting program");
        }
      } catch (Exception e) {
        assertEquals(1, 0, "Exception thrown when it was not supposed to");
      }
    }
    // 2. Confirm the homePageMenu() method works correctly
    // 2a. Test homePageMenu() method to confirm the correct subpage is opened if user's input is
    // valid
    // If 1 is input, ensure that the message from inputMovieDataMenu() is output.
    {
      // Instantiate objects
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      BackendDeveloperIndividual<Movie> backend = new BackendDeveloperIndividual<Movie>(movieList);
      Scanner userInput = new Scanner(System.in);
      FrontendDeveloper frontend = new FrontendDeveloper(userInput, backend);
      // Instantiate an input that is valid using the TextUITester class
      TextUITester frontendText = new TextUITester("1");
      frontend.num = 1; // This number allows the placeholder class to return the correct response

      // Run the homePageMenu() method, confirm it's outputs are correct using the
      // TextUITester class
      try {
        frontend.homePageMenu();
        String output = frontendText.checkOutput();
        if (output.startsWith("Main Menu\n" + "+Enter leading number to open+")
            && output.contains("Please enter the name and file path for the movie list:")) {
          assertEquals(1, 1, "Expected output is correct when starting program");
        } else {
          assertEquals(1, 0, "Expected output is incorrect when starting program");
        }
      } catch (Exception e) {
        assertEquals(1, 0, "Exception thrown when it was not supposed to");
      }
    }
    // If 2 is input, ensure that the message from shortestDurationMenu() is output.
    {
      // Instantiate objects
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      BackendDeveloperIndividual<Movie> backend = new BackendDeveloperIndividual<Movie>(movieList);
      Scanner userInput = new Scanner(System.in);
      FrontendDeveloper frontend = new FrontendDeveloper(userInput, backend);
      // Instantiate an input that is valid using the TextUITester class
      TextUITester frontendText = new TextUITester("2");
      frontend.num = 2; // This number allows the placeholder class to return the correct response

      // Run the homePageMenu() method, confirm it's outputs are correct using the
      // TextUITester class
      try {
        frontend.homePageMenu();
        String output = frontendText.checkOutput();
        if (output.startsWith("Main Menu\n" + "+Enter leading number to open+")
            && output.contains("Confirm viewing the shortest films (y/n):")) {
          assertEquals(1, 1, "Expected output is correct when starting program");
        } else {
          assertEquals(1, 0, "Expected output is incorrect when starting program");
        }
      } catch (Exception e) {
        assertEquals(1, 0, "Exception thrown when it was not supposed to");
      }
    }
    // If 3 is input, ensure that the message from findBetweenTwoDurationsMenu() is output.
    {
      // Instantiate objects
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      BackendDeveloperIndividual<Movie> backend = new BackendDeveloperIndividual<Movie>(movieList);
      Scanner userInput = new Scanner(System.in);
      FrontendDeveloper frontend = new FrontendDeveloper(userInput, backend);
      // Instantiate an input that is valid using the TextUITester class
      TextUITester frontendText = new TextUITester("3");
      frontend.num = 3; // This number allows the placeholder class to return the correct response

      // Run the homePageMenu() method, confirm it's outputs are correct using the
      // TextUITester class
      try {
        frontend.homePageMenu();
        String output = frontendText.checkOutput();
        if (output.startsWith("Main Menu\n" + "+Enter leading number to open+") && output
            .contains("Please enter your time values in minutes or m to return to main menu:")) {
          assertEquals(1, 1, "Expected output is correct when starting program");
        } else {
          assertEquals(1, 0, "Expected output is incorrect when starting program");
        }
      } catch (Exception e) {
        assertEquals(1, 0, "Exception thrown when it was not supposed to");
      }
    }
    // If 4 is input, ensure that the final message from runFrontEnd is output.
    {
      // Instantiate objects
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      BackendDeveloperIndividual<Movie> backend = new BackendDeveloperIndividual<Movie>(movieList);
      Scanner userInput = new Scanner(System.in);
      FrontendDeveloper frontend = new FrontendDeveloper(userInput, backend);
      // Instantiate an input that is valid using the TextUITester class
      TextUITester frontendText = new TextUITester("4");
      frontend.num = 4; // This number allows the placeholder class to return the correct response

      // Run the homePageMenu() method, confirm it's outputs are correct using the
      // TextUITester class
      try {
        frontend.homePageMenu();
        String output = frontendText.checkOutput();
        if (output.startsWith("Main Menu\n" + "+Enter leading number to open+")
            && output.contains("Thank you for using Movie Timer")) {
          assertEquals(1, 1, "Expected output is correct when starting program");
        } else {
          assertEquals(1, 0, "Expected output is incorrect when starting program");
        }
      } catch (Exception e) {
        assertEquals(1, 0, "Exception thrown when it was not supposed to");
      }
    }

    // 2b. Test homePageMenu() method if the user's input is invalid (not a number 1-4)
    {
      // Instantiate objects
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      BackendDeveloperIndividual<Movie> backend = new BackendDeveloperIndividual<Movie>(movieList);
      Scanner userInput = new Scanner(System.in);
      FrontendDeveloper frontend = new FrontendDeveloper(userInput, backend);
      // Instantiate an input that is invalid using the TextUITester class
      TextUITester frontendText = new TextUITester("5");
      frontend.num = 5; // This number allows the placeholder class to return the correct response

      // Run the homePageMenu() method, confirm it's outputs are correct using the
      // TextUITester class
      try {
        frontend.homePageMenu();
        String output = frontendText.checkOutput();
        if (output.startsWith("Main Menu\n" + "+Enter leading number to open+")
            && output.contains("1) Input Movies")) {
          assertEquals(1, 1, "Expected output is correct when starting program");
        } else {
          assertEquals(1, 0, "Expected output is incorrect when starting program");
        }
        // Since the user will have input an invalid input, create an assertion failure if an
        // IllegalArgumentException is not thrown
        assertEquals(1, 0, "Did not throw an IllegalArgumentException when input was invalid");
      } catch (IllegalArgumentException e) {
        // If we catch an IllegalArgumentException, this method worked as expected for invalid
        // inputs
      } catch (Exception e) {
        assertEquals(1, 0, "threw the wrong kind of exception");
      }
    }
  }

  /**
   * This JUnit test tests my own code (my partner's code was not done in time). This confirms that
   * the readFile() method from BackendDeveloperIndividual.java runs with the Movie csv file used
   * for our project
   */
  @Test
  public void testPersonalCode1() {
    // 1. Use the p1 file name and confirm that when calling the readFile, a valid
    // RBT tree is returned with all the movie objects created correctly
    {
      try {
        // Using the valid file name, call the readFile method and save it in the RBT movieList
        IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
        BackendDeveloperIndividual<Movie> test = new BackendDeveloperIndividual<Movie>(movieList);
        String filename = "filmtv_movies.csv";
        test.readFile(filename);

        // Create 4 Movie objects that should be in the MovieList
        ArrayList<String> actualList = new ArrayList<String>();
        Movie movie1 = new Movie("Bugs Bunny's Third Movie: 1001 Rabbit Tales", "Animation", 1982,
            "United States", 76);
        Movie movie2 = new Movie("18 anni tra una settimana", "Drama", 1991, "Italy", 98);
        Movie movie3 = new Movie("Ride a Wild Pony", "Romantic", 1976, "United States", 91);
        Movie movie4 = new Movie("Diner", "Comedy", 1982, "United States", 95);

        // Confirm that movieList contains these Movie objects by putting their titles into an
        // arrayList
        Iterator<Movie> movieListIterator = movieList.iterator();
        while (movieListIterator.hasNext()) {
          Movie actual = movieListIterator.next();
          actualList.add(actual.getTitle());
        }
        if (!(actualList.contains(movie1.getTitle()))) {
          assertEquals(0, 1, "Movie List does not contain Movie title it is supposed to have");
        }
        if (!(actualList.contains(movie2.getTitle()))) {
          assertEquals(0, 1, "Movie List does not contain Movie title it is supposed to have");
        }
        if (!(actualList.contains(movie3.getTitle()))) {
          assertEquals(0, 1, "Movie List does not contain Movie title it is supposed to have");
        }
        if (!(actualList.contains(movie4.getTitle()))) {
          assertEquals(0, 1, "Movie List does not contain Movie title it is supposed to have");
        }
      } catch (Exception e) {
        assertEquals(0, 1, "Exception for file creation was thrown when it was not supposed to");
      }
    }
  }

  /**
   * This JUnit test tests my own code (my partner's code was not done in time). This tests all edge
   * cases for both getMinDurationList() and getThresholdDurationList()
   */
  @Test
  public void testPersonalCode2() {
    // 1. getMinDurationList():
    // 1a. If minimum duration movie is negative, still return it's title.
    {
      // Create Movie objects using the Movie class
      Movie maxMovie1 = new Movie("Title1", "Genre2", 2000, "Country2", -100);
      Movie maxMovie2 = new Movie("Title2", "Genre2", 2000, "Country2", 140);
      Movie minMovie1 = new Movie("Title3", "Genre2", 2001, "Country1", 120);
      Movie minMovie2 = new Movie("Title4", "Genre1", 2000, "Country1", 120);

      // Add them into movieList
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      movieList.insertSingleKey(maxMovie1);
      movieList.insertSingleKey(maxMovie2);
      movieList.insertSingleKey(minMovie1);
      movieList.insertSingleKey(minMovie2);
      // Initialize an instance of the Backend's class
      BackendDeveloperIndividual<Movie> test = new BackendDeveloperIndividual<Movie>(movieList);

      // Call the getMinDuration method and confirm the arrayList is returned as expected.
      ArrayList<String> returnedList = test.getMinDurationList(movieList);
      ArrayList<String> expected2List = new ArrayList<String>();
      expected2List.add("Title1");
      for (int i = 0; i < expected2List.size(); i++) {
        assertEquals(expected2List.get(i), returnedList.get(i),
            "the List returned did not have the required nodes.");
      }
    }
    // 1b. If minimum duration is 0, still return its title
    {
   // Create Movie objects using the Movie class
      Movie maxMovie1 = new Movie("Title1", "Genre2", 2000, "Country2", 0);
      Movie maxMovie2 = new Movie("Title2", "Genre2", 2000, "Country2", 0);
      Movie minMovie1 = new Movie("Title3", "Genre2", 2001, "Country1", 120);
      Movie minMovie2 = new Movie("Title4", "Genre1", 2000, "Country1", 120);

      // Add them into movieList
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      movieList.insertSingleKey(maxMovie1);
      movieList.insertSingleKey(maxMovie2);
      movieList.insertSingleKey(minMovie1);
      movieList.insertSingleKey(minMovie2);
      // Initialize an instance of the Backend's class
      BackendDeveloperIndividual<Movie> test = new BackendDeveloperIndividual<Movie>(movieList);

      // Call the getMinDuration method and confirm the arrayList is returned as expected.
      ArrayList<String> returnedList = test.getMinDurationList(movieList);
      ArrayList<String> expected2List = new ArrayList<String>();
      expected2List.add("Title2");
      expected2List.add("Title1");
      for (int i = 0; i < expected2List.size(); i++) {
        assertEquals(expected2List.get(i), returnedList.get(i),
            "the List returned did not have the required nodes.");
      }
    }
    // 2. getThresholdDurationList:
    // 2a. If threshold range is exceptionally wide, return whole list
    {
   // Create movie objects using the Movie Clas
      Movie movie1 = new Movie("Title1", "Genre2", 2000, "Country2", 160);
      Movie movie2 = new Movie("Title2", "Genre2", 2001, "Country2", 140);
      Movie movie3 = new Movie("Title3", "Genre1", 2002, "Country1", 130);
      Movie movie4 = new Movie("Title4", "Genre1", 2003, "Country1", 120);

      // Add them to a movieList object
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      movieList.insertSingleKey(movie1);
      movieList.insertSingleKey(movie2);
      movieList.insertSingleKey(movie3);
      movieList.insertSingleKey(movie4);
      // Initialize an instance of the Backend's class
      BackendDeveloperIndividual<Movie> test = new BackendDeveloperIndividual<Movie>(movieList);

      // Call the getThresholdDurationList method and confirm that the list returned is correct
      ArrayList<String> returnedList = test.getThresholdDurationList(movieList, 0, 10000);
      ArrayList<String> expectedList = new ArrayList<String>();
      expectedList.add("Title4");
      expectedList.add("Title3");
      expectedList.add("Title2");
      expectedList.add("Title1");
      for (int i = 0; i < expectedList.size(); i++) {
        assertEquals(returnedList.get(i), expectedList.get(i),
            "The returned list when calling getThresholdDurationList is not correct.");
      }
    }
    // 2b. If threshold range is exceptionally small, return a size 0 array list
    {
      // Create movie objects using the Movie Class
      Movie movie1 = new Movie("Title1", "Genre2", 2000, "Country2", 160);
      Movie movie2 = new Movie("Title2", "Genre2", 2001, "Country2", 140);
      Movie movie3 = new Movie("Title3", "Genre1", 2002, "Country1", 130);
      Movie movie4 = new Movie("Title4", "Genre1", 2003, "Country1", 120);

      // Add them to a movieList object
      IterableMultiKeyRBT<Movie> movieList = new IterableMultiKeyRBT<Movie>();
      movieList.insertSingleKey(movie1);
      movieList.insertSingleKey(movie2);
      movieList.insertSingleKey(movie3);
      movieList.insertSingleKey(movie4);
      // Initialize an instance of the Backend's class
      BackendDeveloperIndividual<Movie> test = new BackendDeveloperIndividual<Movie>(movieList);

      // Call the getThresholdDurationList method and confirm that the list returned is size 0
      ArrayList<String> returnedList = test.getThresholdDurationList(movieList, 118, 119);
      ArrayList<String> expectedList = new ArrayList<String>();
      assertTrue(returnedList.size() == 0 && expectedList.size() == 0, "Calling getThresholdDurationList "
          + "with a null movieList did not result in a returned null list");
    }
  }
}
