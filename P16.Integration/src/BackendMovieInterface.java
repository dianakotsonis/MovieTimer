/**
 * This interface is used for a class that creates Movie objects and accesses information on each 
 * movie.
 * 
 * @author Diana Kotsonis
 * @author Max Mitchell
 * @author Abby Carlson
 */
public interface BackendMovieInterface extends Comparable<Movie>{

  // For the class being implemented, here are the instance variables and constructor that would be
  // used to create a Movie object:
  /*
   * private String title;
   * private String genre;
   * private int year;
   * private String country;
   * private int duration;
   * 
   * public Movie(String title, String genre, int year, String country, int duration) {
   *    this.title = title;
   *    this.genre = genre;
   *    this.year = year;
   *    this.country = country;
   *    this.duration = duration;
   * }
   */
  
  /**
   * Accessor for the movie's title in String format.
   * @return the title of the movie
   */
  public String getTitle();
  
  /**
   * Accessor for the movie's genre in String format.
   * @return the genre of the movie
   */
  public String getGenre();

  /**
   * Accessor for the movie's release year. 
   * @return the release year of the movie
   */
  public int getYear();

  /**
   * Accessor for the movie's country.
   * @return the country the movie is from.
   */
  public String getCountry();

  /**
   * Accessor for the movie's runtime. Runtime is rounded to the nearest minute.
   * @return the movie's duration (in minutes) 
   */
  public int getDuration();
}
