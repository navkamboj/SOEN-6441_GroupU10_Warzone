package Models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import Exceptions.InvalidMap;
import Services.MapService;
import java.util.*;

/**
 * This MapTest class file is used to test some important functionalities of Mapservice file
 *
 * @version 3.0.0
 * @author Nihal Galani
 */
public class MapTest{
    Map d_map;
    GameState d_gameState;
    MapService d_mapService;

    /**
     *This method will call every time before every testcase of this MapTest file
     */
    @Before
    public void mapSetup(){
      d_map=new Map();
      d_mapService= new MapService();
      d_gameState= new GameState();
    }

    /**
     *This method is used to check for no continent in the map
     *@throws InvalidMap
     */
    @Test (expected = InvalidMap.class)
    public void testNoContinentValidate() throws InvalidMap{
      assertEquals(d_map.Validate(),false);
    }

    /**
     *This method will validate the map that its valid or invalid
     * @throws InvalidMap
     */
   @Test( expected = InvalidMap.class)
    public void testValidate() throws InvalidMap{
      d_map=d_mapService.mapLoad(d_gameState,"Validate.map");
      assertEquals(d_map.Validate(),false);

   }

    /**
     *This method tests Continent connectivity
     * @throws InvalidMap Exception
     */
    @Test (expected = InvalidMap.class)
    public void testConnectivityOfContinent() throws InvalidMap{
       d_map = d_mapService.mapLoad(d_gameState,"ConnectivityOfContinent.map");
       d_map.Validate();
    }

    /**
     * Checking {@link InvalidMap} if there is no country in the map.
     *
     * @throws InvalidMap Exception
     */
    @Test (expected = InvalidMap.class)
    public void testCheckingNoCountryInTheMap() throws InvalidMap{
        Continent l_continent = new Continent();
        List <Continent> l_continentList = new ArrayList<Continent>();

        l_continentList.add(l_continent);
        d_map.setD_continents(l_continentList);
        d_map.Validate();
    }

    /**
     *This method tests countries connectivity
     * @throws InvalidMap Exception
     */
    @Test (expected = InvalidMap.class)
    public void testConnectivityOfCountries() throws InvalidMap{
        d_map.addContinent("Africa",7);
        d_map.addCountry("Sudan","Africa");
        d_map.addCountry("Kenya","Africa");
        d_map.addCountry("Egypt","Africa");
        d_map.addCountryNeighbor("Sudan","Egypt");
        d_map.addCountryNeighbor("Kenya","Sudan");
        d_map.verifyCountryConnectivity();
    }


}