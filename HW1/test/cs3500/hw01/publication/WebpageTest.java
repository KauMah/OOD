package cs3500.hw01.publication;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for Webpage: unit tests to ensure that Webpages can be cited correctly and otherwise
 * behave correctly.
 */
public class WebpageTest {

  Publication freebie =
      new Webpage("CS3500: Object-Oriented Design", "http://google.com/", "August 11, 2014");
  Publication myOwn =
      new Webpage("Personal Website", "http://kaumah.github.io/", "January 14, 2020");

  @Test
  public void testCiteMla() {
    assertEquals(
        freebie.citeMla(),
        "\"CS3500: Object-Oriented Design.\" Web. August 11, 2014 <http://google.com/>.");
    assertEquals(
        myOwn.citeMla(), "\"Personal Website.\" Web. January 14, 2020 <http://kaumah.github.io/>.");
  }

  @Test
  public void testCiteApa() {
    assertEquals(
        freebie.citeApa(),
        "CS3500: Object-Oriented Design. Retrieved August 11, 2014, from http://google.com/.");
    assertEquals(
        myOwn.citeApa(),
        "Personal Website. Retrieved January 14, 2020, from http://kaumah.github.io/.");
  }
}
