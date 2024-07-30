/**
 * This class creates Movie objects that are inserted into the Red Black Tree based on their
 * duration. Note: Once the MovieList RBT class is fully implemented, the accessor and setter
 * methods for the Movie object's parent, left child, and right child may be edited/updated to
 * coincide with this class.
 * 
 * @implements BackendMovieInterface
 * @author dianakotsonis
 * @param <T>
 */
public class Movie implements BackendMovieInterface, Comparable<Movie>{

  // These are properties of the Movie
  private String title;
  private String genre;
  private int year;
  private String country;
  private int duration;

  // These are references to the Movie's parent and children in the Red Black tree.
  protected Movie downLeft; // The Movie object's left child in the RBT
  protected Movie downRight; // The movie object's right child in the RBT
  protected Movie up; // The movie object's parent in the RBT


  /**
   * This constructor initializes the private instance variables that define a movie object.
   * 
   * @param title    the title of the movie
   * @param genre    the genre of the movie
   * @param year     the year the movie was released
   * @param country  the country the movie is from
   * @param duration the movie runtime (in minutes)
   */
  public Movie(String title, String genre, int year, String country, int duration) {
    this.title = title;
    this.genre = genre;
    this.year = year;
    this.country = country;
    this.duration = duration;
  }

  /**
   * Accessor method for the movie title
   * 
   * @return the movie title
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * Accessor method for the movie genre
   * 
   * @return the movie genre
   */
  public String getGenre() {
    return this.genre;
  }

  /**
   * Accessor method for the movie year
   * 
   * @return the movie year
   */
  public int getYear() {
    return this.year;
  }

  /**
   * Accessor method for the movie country
   * 
   * @return the movie country
   */
  public String getCountry() {
    return this.country;
  }

  /**
   * Accessor method for the movie duration
   * 
   * @return the movie duration
   */
  public int getDuration() {
    return this.duration;
  }

  @Override
  public int compareTo(Movie o) {
    Movie objectComparing = (Movie) o;
    // If the current Movie instance has a greater duration than the Movie object parameter, return
    // 1.
    if (this.duration > objectComparing.getDuration())
      return 1;
    // If the current Movie instance has a lesser duration than the Movie object parameter, return
    // -1.
    else if (this.duration < objectComparing.getDuration())
      return -1;
    // If the current Movie instance has an equal duration to the Movie object parameter, return 0
    else {
      return 0;
    }
  }
}
