import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * This backend interface exposes required functionality to the frontend developers, and accesses
 * the RBT that holds the Movie objects.
 * 
 * @author Diana Kotsonis
 * @author Max Mitchell
 * @author Abby Carlson
 *
 * @param <T> is the RBT of Movie objects that uses the the IterableMultiKeySortedCollection 
 * interface
 */
public interface BackendInterface<T extends IterableMultiKeySortedCollectionInterface> {

  // For the backend class being implemented, here are the instance varaibles and constructor that 
  // would be used:
  /*
   * private String fileName;
   * private T movieList // The RBT instance that contains the movies from the file organized by 
   *                     // duration
   * public BackendMovieTimer(String filename, T movieList) {
   *    if (filename == null) {
   *        throw new NullPointerException("File is null");
   *    }
   *    this.filename = filename;
   *    this.movieList = movieList;
   * }
   */
  
  /**
   * This uses the file name to access and read data from the file. It creates movie objects by
   * calling the Movie class (the class described by the BackendMovieInterface) with the data 
   * found from the file. 
   * If the file name is not valid, a FileNotFoundException is thrown.
   * <p>
   * This method is capable of correctly reading in files as long as they are formatted the same as
   * the CSV movie data set from Kaggle (https://www.kaggle.com/datasets/stefanoleone992/filmtv-movi
   * es-dataset)
   * 
   * @param fileName the name of the file being accessed
   */
  public void readFile(String fileName) ;
  
  /**
   * Using a RBT of movies, this method creates a list of movies that have the shortest duration.
   * If the RBT is null, an empty ArrayList is returned.
   * @param movieList the RBT of movies based on their duration
   * @return a list of the movie names with the shortest duration
   */
  public ArrayList<String> getMinDurationList(T movieList);
  
  /**
   * Using a RBT of movies, this method creates a list of movies that have a duration that a falls
   * between the 2 specified thresholds. 
   * If a movie duration equals one of the thresholds, this movie is included in the list.
   * If the RBT is null, and empty string ArrayList is returned. If no movies fall within the
   * threshold, an empty string ArrayList is returned.
   * 
   * @param movieList the RBT of movies based on their duration
   * @param lowerThreshold the lower bound of the duration threshold
   * @param upperThreshold the upper bound of the duration threshold
   * @return a list of the movie names with a duration between the two thresholds
   */
  public ArrayList<String> getThresholdDurationList(T movieList, int lowerThreshold, 
      int upperThreshold);

}
