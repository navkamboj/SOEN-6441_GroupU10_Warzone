# SOEN-6441 GroupU10 : Warzone
## Project Description
### Overview
 <p align="justify">
The goal of this project is to create a computer version of the popular board game "Risk." The game will adhere to the guidelines and map data from Risk's "Warzone" edition. The ability to create custom maps using a text file representation is also provided, giving players the freedom to design their own gaming worlds. Incorporating crucial components like deployment, progress, and special orders, the gameplay has a captivating turn-based structure and offers a strategic complexity that is similar to the original board game.  Realistic battle simulations add a thrilling dimension to conflicts, while the inclusion of card-based strategic elements introduces dynamic decision-making. Furthermore, the game features player elimination as part of its competitive aspect, and victory conditions are in place to determine the ultimate conqueror of the virtual world. Overall, this project aims to deliver an immersive and faithful adaptation of the classic "Risk" experience while offering room for personalization and strategic depth.
 </p>

### Team Members and GitHub IDs
<table>
  <tr>
    <th>Team member</th>
    <th>Github ID</th>
  </tr>
  <tr>
    <td>Harsh Tank</td>
    <td>harsh-tank</td>
  </tr>
  <tr>
    <td>Navjot Kamboj</td>
    <td>navkamboj</td>
  </tr>
  <tr>
    <td>Nihal Galani</td>
    <td>nihal514</td>
  </tr>
  <tr>
    <td>Pranjalesh Ghansiyal</td>
    <td>pranjalesh</td>
  </tr>
  <tr>
    <td>Yatish Chutani</td>
    <td>YatishChutani</td>
  </tr>
</table>

### Tools Used
<table>
  <tr>
    <th>Tool</th>
    <th>Version</th>
	<th>Description</th>
  </tr>
  <tr>
    <td>JDK 17</td>
    <td>17.0.8</td>
	<td>Java programming language is used for the complete development of Risk game using OOPs concepts.</td>
  </tr>
  <tr>
    <td>Maven</td>
    <td>3.8.1</td>
	<td> Apache Maven is an Open Source Project management plugin tool that is used to manages project's build and documentation .</td>
  </tr>
  <tr>
    <td>JUnit</td>
    <td>4.13.1</td>
	<td>JUnit is a Testing Framework used to generate Test Cases that facilitates test-driven development.</tr>
  <tr>
	<td>GitHub</td>
	<td>N/A</td>
	<td>GitHub is used for collaboration with team members and for source code management.</tr>
</table>

## Architecture Design
This project follows Model-View-Controller (MVC) architecture pattern as explained below : 

##### MODEL
<li> Map : This class is created to manage the country and continent objects and verifies the map by examining the connectivity between them </li>
<li> Country : This class controls every country as well as the number of armies present on them </li>
<li> Continent: This class controls all the continents and their respective bonus values. In order to map all the countries, the country object is linked to the Continent object. </li>
<li> Player: This class controls all the continents, countries, commands issued, and armies that are an asset of every Player object </li>
<li> Order: This class handles the order action, source and destination countries and number of armies to be moved </li>
<li> GameState: This class controls Player and Order objects, as well as keeping track of orders that are on the list but have not yet been executed.
</li>

##### VIEW
<li> MapView : This class handles the execution of existing map as well as displays the current state of the Map when the user selects the 'showmap' command in the GamePlay State. </li>

##### CONTROLLLER
<li>GameEngine : This class contains the logic for execution of commands needed to run the game. </li>

##### SERVICES
<li>MapServices: This is where the map's state is mostly handled, along with features for loading, editing, and saving the map.</li>
<li>PlayerServices: This feature makes it possible to add, remove, and assign continents, countries, and armies for players</li>

##### COMMAND
<li>Command: Acquires and formats player-issued commands</li>
<li>CommonUtil: Handles the input files</li>

##### RESOURCES
<li>The .map files including old and new ones users have made, are saved and handled here.</li>

##### CONSTANTS
<li>GameConstant : All the constants ( static data members ) are defined here.</li>

##### EXCEPTIONS 
<li>InvalidCommand: When an invalid command is entered, the player is given an error message.</li>
<li>InvalidMap: When an invalid map is constructed, the player is given an error message.</li>

### The following features are included in Build v.1 game release:
1. Starting the game by loading any already-built map.
2. Building the map using edit features and generating a new map file
3. Verifying if the map is correct and Saving a copy of it
4. Including functionality to add or remove multiple players
5. Play Loop Features: Execute and deploy the instructions for assigning reinforcements




