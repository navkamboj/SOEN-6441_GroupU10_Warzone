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
 */
public class MapTest{
    Map d_map;
    GameState d_gameState;
    MapService d_mapService;

    /**
     *This method will called every time before every testcase of this MapTest file
     */
    @Before
    public void mapSetup(){
      d_map=new Map();
      d_mapService= new MapService();
      d_gameState- new GameState();
    }

    /**
     *This method is used to check for no continent in the map(@link InvalidMap)
     *This method can throws @throws InvalidMap
     */
    @Test (expected InvalidMap.class)
    public void testNoContinentValidate() throws InvalidMap{
      asserteEquals(d_map.Validate(),false);
    }

    /**
     *This method will validate the map that its valid or invalid
     * @throws InvalidMap
     */
   public void testValidate() throws InvalidMap{
      d_map=d_mapService.mapLoad(d_gameState,canada.map);
      assertEquals(d_map.Validate(), true);
   }

    /**
     *This method tests Continent connectivity
     * @throws InvalidMap Exception
     */
    @Test (expected = InvalidMap.class)
    public void testConnectivityOfContinent() throws InvalidMap{
       d_map = d_mapService.mapLoad(d_gameState,"connectivityOfContinent.map");
       d_map.Validate();
    }

    /**
     *This method tests countries connectivity
     * @throws Invalid Exception
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