package Models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 *This Ordertest class file is used to test some important functionalities of order file
 *
 * @version 1.0.0
 * @author Nihal Galani
 */

public class OrderTest{
    /**
     * Reference variable for Order class
     */
    Order d_detailsOfOrder;

    /**
     * Reference variable for Player class
     */
    Player d_testInformationAboutPlayer;

    /**
     * This method orderSetup called every time before the execution of every test case that
     * are availble in this OrderTest class file
     */
     @Before
     public void orderSetup(){
        d_detailsOfOrder=new Order();
        d_testInformationAboutPlayer= new Player();
     }

    /**
     * This method is used to check that entered countryname for deployorder is
     * belongs to player or not
     */
    @Test
    public void testCountryDeploOrderValidation(){
      d_detailsOfOrder.setD_targetCountry("Seoul");
      List<Country> l_countries = new ArrayList<Country>();
      l_countries.add(new Country("Seoul"));
      l_countries.add(new Country("Busan"));

      d_testInformationAboutPlayer.setD_ownedCountries(l_countries);
      boolean l_realvalue= d_detailsOfOrder.verifyDeployCountryOrder(d_testInformationAboutPlayer,d_detailsOfOrder);
      assertTrue(l_realvalue);
    }
}