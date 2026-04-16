import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.lang.Math.round;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.FileWriter;


/**
 * This is the test class called projectTesting
 * @Author- Anish Joshi(aj485)
 *
 */


public class projectTesting {
    /**
     * This class reads the file to find the destination path to extract the user's code
     */
    class relativePath{
        public static String path;

        public relativePath(){

            path = "";
        }
        public static String findPath() throws IOException {
            //gets the path of the src folder
            Path currentPath = Paths.get("");
            Path srcPath = Paths.get(currentPath.toAbsolutePath().toString(), "comp6000 submission","src");//gets the current path
            String destinationLogin = srcPath.toString() + "/" + "login.txt";


            BufferedReader reader = new BufferedReader(new FileReader(destinationLogin));//gets the login from the login file
            //reads each line in the text file
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    if(line.contains("Users")){//reads the path in the file that will be used as a directory
                        path = line;
                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
            return path;//returns path
        }
        public static void main(String argsp[]) throws IOException {
            findPath();
        }
    }

    class GameNewQuit extends Game {

        public String quit() {
            return "TEST";
        }
    }

    private Game game;
    private Character Anish;

    private Character Anthony;
    private Room room;

    private Character Olaf;

    private static File report;

    private static File login;

    public static int count;

    public static String directory;

    public static projectTesting testing;

    public static String destination;

    public static double goalCount  = 0;

    public static double timeCount  = 0;

    public static double lookCount = 0;

    public static double itemCount = 0;

    public static double characterCount = 0;

    public static double roomCount = 0;

    public static double takeCount = 0;

    public static double cookCount = 0;


    /**
     * constructor for projectTesting
     */
    public projectTesting() throws IOException {
        game = new Game();
        Anish = new Character("Anish", Item.SUGAR);
        Anthony = new Character("Anthony", null);
        room = new Room("in the maze");
        room.setExit(Direction.EAST, room);
        room.setExit(Direction.WEST, room);
        Olaf = new Character("Olaf", Item.EGG);
        directory = projectTesting.relativePath.findPath();
        report = new File(directory + "/"+"report.txt");
        login = new File(directory + "/"+"login.txt");



    }


    /**
     * returns the report text file containing the user's marks
     *
     */
    public static File getReport(){
        return report;
    }
    /**
     * returns the login text file containing the user's login
     *
     */
    public static File getLogin(){
        return login;
    }

    /**
     *
     *returns the path to all the files
     */
    public static String getDirectory(){
        return directory;
    }

    /**
     * returns the directory of where the src folder is stored
     *
     */

    public static String getStudentLogin() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(login));//gets the login from the login file
        //reads each line in the text file
        String line;
        String name = "";
        while ((line = reader.readLine()) != null) {
            try {
                if (!line.contains("Users")) {
                    name = line;
                }


            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }
        return name;



    }

    /**
     * reads the report file and calculates the total mark of the student
     */
    public static double getMark(){
        double totalMark = round(projectTesting.goalCount + projectTesting.timeCount + projectTesting.lookCount + projectTesting.itemCount + projectTesting.characterCount+ projectTesting.roomCount + projectTesting.takeCount + projectTesting.cookCount);
        return totalMark;
    }

    /**
     *
     *
     * calls the pdf and spreadsheet class when testing is complete
     */
    @AfterClass
    public static void afterCall() throws IOException {

        PDFmaker.pdfMaker();//calls the PDFmaker class and generates a PDF that contains the information about the users assessment
        spreadsheet.studentInfo();//calls the spreadsheet class and generates a spreadsheet containing each their marks


    }


    /**************************************************************************
     * Adding a goal
     **************************************************************************/

    /**
     * checking the user can reach the goal successfully
     */
    @Test
    public void goalTesting() throws IOException {



        FileWriter writer = new FileWriter(report, true);

        Boolean found = false;// flag variable

        try {
            //swap this section to add your own tests
            game.goRoom(Direction.NORTH); // hall
            game.goRoom(Direction.UP); // stairs
            game.goRoom(Direction.NORTH); // master
            String result = game.goRoom(Direction.EAST); // ensuite

            assertEquals("Unable to reach goal.",
                    game.getCurrent().getLongDescription() +
                            "\nCongratulations! You reached the goal.\nThank you for playing.  Good bye.",
                    result);
            //swapping section ends here

            found = true;
            goalCount = goalCount + 2;//change mark accordingly
            count++;//test counter


        }

        catch (AssertionError error) {
            writer.write("Test failed: unable to reach goal, marks deducted -2" + "\n");
            found = false;

        }
        writer.close();

        if(!found) {
            throw new Error("Test failed: Unable to reach goal");
        }

    }



    @Test
    /**
     * checks if the user did not reach the goal successfully
     */
    public void notGoalTesting() throws IOException {
        FileWriter writer = new FileWriter(report, true);
        boolean found = false;

        try {
            //swap this section to add your own tests
            String test1 = game.goRoom(Direction.NORTH); //  goes to the hall
            String test2 = game.goRoom(Direction.UP); // goes to the stairs
            String test3 = game.goRoom(Direction.NORTH); // goes to master bedroom

            game.goRoom(Direction.WEST);
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.WEST);
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.WEST);
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.WEST);
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.WEST);
            game.goRoom(Direction.NORTH);
            String result = game.goRoom(Direction.EAST); // goes to ensuite
            assertEquals("Goal should have been reached in 4 steps!.",
                    game.getCurrent().getLongDescription() +
                            "\nCongratulations! You reached the goal.\nThank you for playing.  Good bye.",
                    result);
            //swapping section ends here

            found = true;
            count++;//test counter
            goalCount = goalCount + 2;//change mark accordingly
        }
        catch (AssertionError error) {
            writer.write("Test failed: Goal should have been reached in 4 steps, marks deducted -2" + "\n");//writes to the file
            found = false;

        }

        writer.close();



        if(!found) {

            throw new Error("Test failed: Goal should have been reached in 4 steps!");//throws an error if test has failed
        }

    }

    /**
     * checks if game is finished by user reaching the goal
     */
    @Test
    public void finishedGoal() throws IOException {
        FileWriter writer = new FileWriter(report, true);

        Boolean found = false;//flag variable

        try {
            //swap this section to add your own tests
            String test1 = game.goRoom(Direction.NORTH);
            String test2 = game.goRoom(Direction.UP);
            String test3 = game.goRoom(Direction.NORTH);
            String test4 = game.goRoom(Direction.EAST);
            assertEquals("Finished at goal.",true, game.finished());
            //swapping section ends here

            found = true;
            goalCount = goalCount + 2;//change mark accordingly
            count++;//test counter

        }
        catch (AssertionError error) {
            writer.write("Test failed: Finished at goal was false not true, marks deducted -2" + "\n");//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: Finished at goal was false not true");//throws an error if test has failed
        }


    }

    /**
     * checks if game is finished by user reaching the goal
     */
    @Test
    public void notFinishedGoal() throws IOException {
        FileWriter writer = new FileWriter(report, true);
        boolean found = true;


        try {
            //swap this section to add your own tests
            String test1 = game.goRoom(Direction.NORTH);
            String test2 = game.goRoom(Direction.UP);
            String test3 = game.goRoom(Direction.NORTH);
            assertEquals("not Finished at goal.", false, game.finished());
            //swapping section ends here

            goalCount = goalCount + 2;//change mark accordingly
            found = true;
            count++;//test counter

            writer.close();

        }
        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: not finished at goal was false not true, marks deducted -2" + "\n"));//writes to the file
            found = false;

    }

        writer.close();

        if(!found) {

        throw new Error();//throws an error if test has failed
    }


    }
    /**
     * checks if the user is able to reuse the quit function when typed
     */
    @Test
    public void reusesQuitGoal() throws IOException {
        FileWriter writer = new FileWriter(report, true);
        boolean found = true;

        try {
            //swap this section to add your own tests
            GameNewQuit game = new GameNewQuit();
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.UP);
            game.goRoom(Direction.NORTH);
            String result = game.goRoom(Direction.EAST);
            assertTrue("Adding goal should reuse existing quit method.",
                    result.contains("TEST"));
            //swapping section ends here

            goalCount = goalCount + 2;//change mark accordingly
            found = true;
            count++;//test counter



            // Writes the content to the file

            writer.close();

        }
        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: not finished at goal was false not true, marks deducted -2" + "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error();//throws an error if test has failed
        }

    }



    /**************************************************************************
     * Adding time
     **************************************************************************/

    /**
     * checks if the user was able to get to the goal within the time limit
     */

    @Test
    public void OutOfTime() throws IOException {
        boolean found = false;
        FileWriter writer = new FileWriter(report, true);
        try {
            //swap this section to add your own tests
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.SOUTH);
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.SOUTH);
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.SOUTH);
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.SOUTH);
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.SOUTH);
            game.goRoom(Direction.NORTH);

            String result = game.goRoom(Direction.SOUTH); // 12 stairs
            assertEquals("Should check for user not able to get to the goal within 12 steps.",
                    game.getCurrent().getLongDescription() +
                            "\nLost! You ran out of time.\nThank you for playing.  Good bye.",
                    result);
            //swapping section ends here

            timeCount = timeCount+3.75;//change mark accordingly
            found = true;
            count++;//test counter

        }
        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: should check for user not able to get to the goal within 12 steps, marks deducted -3.75" + "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: should check for user not able to get to the goal within 12 steps");//throws an error if test has failed
        }


    }

    /**
     * checks if the user is not able to get to goal over 12 steps
     */
    @Test
    public void OutOfTime2() throws IOException {
        boolean found = false;
        FileWriter writer = new FileWriter(report, true);
        try {
            //swap this section to add your own tests
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.SOUTH);
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.SOUTH);
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.SOUTH);
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.SOUTH);
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.SOUTH);
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.SOUTH);


            String result = game.goRoom(Direction.NORTH);
            assertEquals("Should check for user not able to get to the goal within more than 12 steps",
                    game.getCurrent().getLongDescription() +
                            "\nLost! You ran out of time.\nThank you for playing.  Good bye.",
                    result);
            //swapping section ends here

            timeCount = timeCount+3.75;//change mark accordingly
            found = true;
            count++;//test counter
        }
        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: should check for user not able to get to the goal within more than 12 steps, marks deducted -3.75"+ "\n"));//writes to the file
            found = false;

        }
        writer.close();



        if(!found) {

            throw new Error("Test failed: should check for user not able to get to the goal within more than 12 steps");//throws an error if test has failed
        }



    }


    /**
     * checks if the user was not able to get to the goal within the time limit
     */

    @Test
    public void reachesGoalInTime() throws IOException {
        boolean found = false;
        FileWriter writer = new FileWriter(report, true);
        try {
            //swap this section to add your own tests
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.UP);
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.SOUTH);
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.SOUTH);
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.SOUTH);
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.SOUTH);
            game.goRoom(Direction.NORTH);

            String result = game.goRoom(Direction.EAST);
            assertEquals("Should reach goal within 12 steps.",
                    game.getCurrent().getLongDescription() +
                            "\nCongratulations! You reached the goal.\nThank you for playing.  Good bye.",
                    result);
            //swapping section ends here

            found = true;
            timeCount = timeCount+3.75;//change mark accordingly
            count++;//test counter
        }
        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: Should reach goal within 12 steps, marks deducted -3.75" + "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: Should reach goal within 12 steps");//throws an error if test has failed
        }



    }

    /**
     *
     * Checks if user is able to reuse quit within the time limit
     */

    @Test
    public void reusesQuitTime() throws IOException {
        boolean found = false;
        FileWriter writer = new FileWriter(report, true);

        GameNewQuit game = new GameNewQuit();
        try{
            //swap this section to add your own tests
        game.goRoom(Direction.NORTH);
        game.goRoom(Direction.UP);
        game.goRoom(Direction.NORTH);
        game.goRoom(Direction.SOUTH);
        game.goRoom(Direction.NORTH);
        game.goRoom(Direction.SOUTH);
        game.goRoom(Direction.NORTH);
        game.goRoom(Direction.SOUTH);
        game.goRoom(Direction.NORTH);
        game.goRoom(Direction.SOUTH);
        game.goRoom(Direction.NORTH);

        String result = game.goRoom(Direction.SOUTH); // 12 stairs
        assertTrue("Adding time should reuse quit method.",
                result.contains("TEST"));
        //swapping section ends here

        found = true;
        timeCount = timeCount+3.75;//change mark accordingly
        count++;//test counter
    }
        catch (AssertionError error) {
        writer.write(String.valueOf("Test failed: Should be able to reuse quit within the time limit, marks deducted -3.75" + "\n"));//writes to the file
        found = false;

    }

        writer.close();

        if(!found) {

        throw new Error("Test failed: Should be able to reuse quit within the time limit");//throws an error if test has failed
    }

}


    /**************************************************************************
     * Adding a look command
     **************************************************************************/

    /**
     * checks if user gave a description at the start for the user
     */

    @Test
    public void lookImmidiately() throws IOException {
        boolean found = false;
        FileWriter writer = new FileWriter(report, true);
        try {
            //swap this section to add your own tests
            assertEquals("Looking at start should provide a description of front.",
                    game.getCurrent().getLongDescription(), game.look());
            //swapping section ends here
            found = true;

            lookCount = lookCount + 3.33;//change mark accordingly
            count++;//test counter
        }
        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: looking at start should provide a description of front, marks deducted -3.33"+ "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: looking at start should provide a description of front.");//throws an error if test has failed
        }

        // Writes the content to the file


    }

    /**
     * checks if user gave a description at the start for the user after moving in a direction
     */

    @Test
    public void lookAfterStep() throws IOException {
        boolean found = false;
        FileWriter writer = new FileWriter(report, true);
        try {
            //swap this section to add your own tests
            game.goRoom(Direction.SOUTH);
            assertEquals("Looking after one step should give a description regardless of the direction.",
                    game.getCurrent().getLongDescription(), game.look());
            //swapping section ends here

            found = true;
            lookCount = lookCount + 3.34;//change mark accordingly



            count++;//test counter

        }

        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: looking after one step should give a description regardless of the direction, marks deducted -3.33" + "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: looking after one step should give a description regardless of the direction.");//throws an error if test has failed
        }

    }

    /**
     * checks if look command is an option for the user
     */
    @Test
    public void lookCommand() throws IOException {
        boolean found = false;
        FileWriter writer = new FileWriter(report, true);

        try {
            //swap this section to add your own tests
            Parser parser = new Parser();
            assertTrue(" 'look' should be an option for the user.",
                    parser.commands().contains("look"));
            //swapping section ends here

            found = true;

            lookCount = lookCount + 3.33;//change mark accordingly

            count++;//test counter

        }

        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: 'look' should be an option for the user, marks deducted -3.34" + "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: 'look' should be an option for the user");//throws an error if test has failed
        }


    }
    /**************************************************************************
     * Adding Items
     **************************************************************************/

    /**
     * checks if the user is able to get a description of each item
     */
    @Test
    public void itemFlour() throws IOException {
        boolean found = false;
        FileWriter writer = new FileWriter(report, true);

        try {
            //swap this section to add your own tests
            assertEquals("flour", Item.FLOUR.toString());
            //swapping section ends here

            found = true;
            itemCount = itemCount + 3.33;//change mark accordingly
            count++;//test counter

        }
        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: item should have description of flour, marks deducted -3.33" + "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: item should have description of flour");//throws an error if test has failed
        }


    }

    @Test
    //last one in the code
    public void itemSugar() throws IOException {
        boolean found = false;
        FileWriter writer = new FileWriter(report, true);
        try {
            //swap this section to add your own tests
            assertEquals("sugar", Item.SUGAR.toString());
            //swapping section ends here


            found = true;
            itemCount = itemCount + 3.33;//change mark accordingly
            count++;//test counter

        }

        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: item should have description of sugar, marks deducted -3.33"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: item should have description of sugar");//throws an error if test has failed
        }


    }

    @Test
    public void itemEgg() throws IOException {
        boolean found = false;
        FileWriter writer = new FileWriter(report, true);

        try {
            //swap this section to add your own tests
            assertEquals("egg", Item.EGG.toString());
            //
            found = true;
            //swapping section ends here

            itemCount = itemCount + 3.34;//change mark accordingly

            count++;//test counter


        }

        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: item should have description of egg, marks deducted -3.34" + "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: item should have description of egg");//throws an error if test has failed
        }

    }

    /**************************************************************************
     * Adding Characters
     **************************************************************************/

    /**
     * Checks if the user is able to take the item
     */

    @Test
    public void takeAnish() throws IOException {
        boolean found = false;
        FileWriter writer = new FileWriter(report, true);

        try {
            //swap this section to add your own tests
            assertEquals("Anish should be able to take sugar",
                    true, Anish.take(Item.SUGAR));

            //swap section ends here
            found = true;
            characterCount = characterCount + 3.75;//change mark accordingly



            count++;//test counter

        }

        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: character should be able to take sugar, marks deducted -3.75" + "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: character should be able to take sugar");//throws an error if test has failed
        }



    }

    /**
     * Checks if the user is not able to take the item twice
     */

    @Test
    public void takeAnish2() throws IOException {
        FileWriter writer = new FileWriter(report, true);

        boolean found = false;

        try {
            //swap this section to add your own tests
            Anish.take(Item.SUGAR);
            assertEquals("Anish should be not able to take sugar twice.",
                    false, Anish.take(Item.SUGAR));
            //swapping section ends here
            found = true;
            characterCount = characterCount + 3.75;//change mark accordingly
            count++;//test counter

        }

        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: character should be not able to take sugar twice, marks deducted -3.75" + "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: character should be not able to take sugar twice");//throws an error if test has failed
        }



    }

    /**
     * Checks if the user is not able to take an item that isn't there
     */
    @Test
    public void takeAnthony2() throws IOException {
        boolean found = false;

        FileWriter writer = new FileWriter(report, true);
        try {
            //swap this section to add your own tests
            assertEquals("Should not be able take flour from Anthony",
                    false, Anthony.take(Item.FLOUR));
            //swapping section ends here

            found = true;
            characterCount = characterCount + 3.75;//change mark accordingly
            count++;//test counter

        } catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: Should not be able take flour from character, marks deducted -3.75" + "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: Should not be able take flour from character");//throws an error if test has failed
        }



        // Writes the content to the file

    }

    /**
     * Checks if the user is and is not able to take an item in several situations
     */
    @Test
    public void takeRepeated() throws IOException {
        FileWriter writer = new FileWriter(report, true);
        boolean found = false;
        try {
            //swap this section to add your own tests
            assertEquals("Olaf having the item egg", Olaf.toString());

            assertEquals("take should return false when trying to take wrong item",
                    false, Olaf.take(Item.SUGAR));

            assertEquals("take should return false when trying to take wrong item 2nd time",
                    false, Olaf.take(Item.FLOUR));

            assertEquals("take should return true when item successfully taken",
                    true, Olaf.take(Item.EGG));

            assertEquals("take should return false when item taken twice",
                    false, Olaf.take(Item.EGG));

            //swapping section ends here

            found = true;
            characterCount = characterCount + 3.75;//change mark accordingly


            count++;//test counter
        }

        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: should check if you can or cannot take items,marks deducted -3.75"+"\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error();//throws an error if test has failed
        }




    }
    /**************************************************************************
     * Adding Characters to Rooms
     **************************************************************************/

    /**
     * Checks description of the character
     */
    @Test
    public void longDescriptionCharacter() {
        room.addCharacter(new Character("Jack", null));
        assertEquals("Wrong description of Jack.",
                "You are in the maze.\nExits: west east\nCharacters: Jack; ",
                room.getLongDescription());

    }

    /**
     * Checks description of two characters.
     */
    @Test
    public void longDescriptionCharacter2() {
        room.addCharacter(new Character("Jeff", null));
        room.addCharacter(new Character("Danyal", Item.EGG));
        assertEquals("Wrong description of the room with Jeff and Danyal.",
                "You are in the maze.\nExits: west east\nCharacters: Jeff; Danyal having the item egg; ",
                room.getLongDescription());


    }

    /**
     * checks item description of character Anish
     */
    @Test
    public void descriptionOfAnish() {
        assertEquals("wrong description for Anish.",
                "Anish having the item sugar", Anish.toString());
    }

    /**
     * checks item description of character Anthony
     */

    @Test
    public void descriptionOfAnthony() {
        assertEquals("wrong description for Anthony.",
                "Anthony", Anthony.toString());
    }

    /**
     * Checks description of character Jason and checks if time has been taken
     */
    @Test
    public void checkItem() {
        room.addCharacter(new Character("Jason", Item.FLOUR));
        assertEquals("Able to take flour from Jason", true, room.take(Item.FLOUR));

    }

    /**
     * Checks description of character Jason and George and checks if item is able and not able to be taken
     */

    @Test
    public void checkItem2() {
        room.addCharacter(new Character("Jason", Item.EGG));
        room.addCharacter(new Character("George", Item.EGG));

        room.take(Item.EGG);
        assertEquals("Able to take egg from Jason", true, room.take(Item.EGG));

        room.take(Item.EGG);
        assertEquals("Not able to take egg from George", false, room.take(Item.EGG));

    }

    /**
     * checks mother,daughter,father and son and checks if they have item descriptions
     */
    @Test
    public void visitingFamily() throws IOException {
        FileWriter writer = new FileWriter(report, true);

        boolean found = false;

        try {
            //swap this section to add your own tests
            String test = game.goRoom(Direction.NORTH);
            test += game.goRoom(Direction.WEST);
            game.goRoom(Direction.EAST);
            test += game.goRoom(Direction.NORTH);
            test += game.goRoom(Direction.NORTH);
            game.goRoom(Direction.SOUTH);
            test += game.goRoom(Direction.EAST);
            game.goRoom(Direction.WEST);
            test += game.goRoom(Direction.UP);
            test += game.goRoom(Direction.SOUTH);

            // new game, because otherwise run out of time
            game = new Game();
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.UP);
            test += game.goRoom(Direction.NORTH);
            test += game.goRoom(Direction.EAST);

            // new game otherwise time runs out
            game = new Game();
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.UP);
            test += game.goRoom(Direction.NORTH);
            test += game.goRoom(Direction.EAST);

            assertTrue("Find mother",
                    test.contains("mother"));

            assertTrue("Find daughter",
                    test.contains("daughter"));

            assertTrue("Find father",
                    test.contains("father"));

            assertTrue("Find son",
                    test.contains("son"));

            assertTrue("Should see flour",
                    test.contains("flour"));
            assertTrue("Should see egG",
                    test.contains("egg"));
            assertTrue("Should see sugar",
                    test.contains("sugar"));
            //swapping section ends here

            found = true;

            roomCount = roomCount+5;//change mark accordingly

            count++;//test counter

        }
        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: "+error+", marks deducted -5"+"\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: should have item descriptions  marks deducted -5\"");//throws an error if test has failed
        }

    }

    @Test
    /**
     * checks if the student entered more than three items for the family characters
     */
    public void checkMoreThanThree() throws IOException {
        boolean found = false;

        FileWriter writer = new FileWriter(report, true);

    try {
        //swap this section to add your own tests
        String test = game.goRoom(Direction.NORTH);
        test += game.goRoom(Direction.WEST);
        game.goRoom(Direction.EAST);
        test += game.goRoom(Direction.NORTH);
        test += game.goRoom(Direction.NORTH);
        game.goRoom(Direction.SOUTH);
        test += game.goRoom(Direction.EAST);
        game.goRoom(Direction.WEST);
        test += game.goRoom(Direction.UP);
        test += game.goRoom(Direction.SOUTH);
        test += game.goRoom(Direction.DOWN);

        // new game otherwise time runs out
        game = new Game();
        game.goRoom(Direction.NORTH);
        game.goRoom(Direction.UP);
        test += game.goRoom(Direction.NORTH);
        test += game.goRoom(Direction.EAST);


        String sentence = test;
        String son = "son;";
        String daughter = "daughter;";
        String father = "father;";
        String mother = "mother;";


        //checks to see if the user inputted more than three items to a character
        if (sentence.indexOf(son.toLowerCase()) == -1 && sentence.indexOf(daughter.toLowerCase()) == -1 && sentence.indexOf(mother.toLowerCase()) == -1 && sentence.indexOf(daughter.toLowerCase()) == -1) {
            assertTrue("Should not have more than three items, marks deducted -5", false);

        }

        found = true;
        roomCount = roomCount+5;//change mark accordingly
        count++;//test counter
    }
    catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: Should not have more than three items, marks deducted -5" + "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: Should not have more than three items!");//throws an error if test has failed
        }

    }

    /**
     * checks all the rooms to find the items: flour,sugar and egg
     */

    @Test
    public void findAllItems() throws IOException {
        FileWriter writer = new FileWriter(report, true);
        boolean found = false;

        try {
            //swap this section to add your own tests
            boolean egg = false;
            boolean sugar = false;
            boolean flour = false;

            // go through whole house an collect all items
            game.goRoom(Direction.NORTH);
            egg = egg || game.getCurrent().take(Item.EGG);
            sugar = sugar || game.getCurrent().take(Item.SUGAR);
            flour = flour || game.getCurrent().take(Item.FLOUR);

            game.goRoom(Direction.WEST);
            egg = egg || game.getCurrent().take(Item.EGG);
            sugar = sugar || game.getCurrent().take(Item.SUGAR);
            flour = flour || game.getCurrent().take(Item.FLOUR);

            game.goRoom(Direction.EAST);
            game.goRoom(Direction.NORTH);
            egg = egg || game.getCurrent().take(Item.EGG);
            sugar = sugar || game.getCurrent().take(Item.SUGAR);
            flour = flour || game.getCurrent().take(Item.FLOUR);


            game.goRoom(Direction.NORTH);
            egg = egg || game.getCurrent().take(Item.EGG);
            sugar = sugar || game.getCurrent().take(Item.SUGAR);
            flour = flour || game.getCurrent().take(Item.FLOUR);

            game.goRoom(Direction.SOUTH);
            game.goRoom(Direction.EAST);
            egg = egg || game.getCurrent().take(Item.EGG);
            sugar = sugar || game.getCurrent().take(Item.SUGAR);
            flour = flour || game.getCurrent().take(Item.FLOUR);

            game.goRoom(Direction.WEST);
            game.goRoom(Direction.UP);
            egg = egg || game.getCurrent().take(Item.EGG);
            sugar = sugar || game.getCurrent().take(Item.SUGAR);
            flour = flour || game.getCurrent().take(Item.FLOUR);

            game.goRoom(Direction.SOUTH);
            egg = egg || game.getCurrent().take(Item.EGG);
            sugar = sugar || game.getCurrent().take(Item.SUGAR);
            flour = flour || game.getCurrent().take(Item.FLOUR);

            // new game otherwise time runs out
            game = new Game();
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.UP);
            game.goRoom(Direction.NORTH);
            egg = egg || game.getCurrent().take(Item.EGG);
            sugar = sugar || game.getCurrent().take(Item.SUGAR);
            flour = flour || game.getCurrent().take(Item.FLOUR);

            game.goRoom(Direction.EAST);
            egg = egg || game.getCurrent().take(Item.EGG);
            sugar = sugar || game.getCurrent().take(Item.SUGAR);
            flour = flour || game.getCurrent().take(Item.FLOUR);

            assertTrue("Should get egg", egg);
            assertTrue("Should get sugar", sugar);
            assertTrue("Should get flour", flour);
            //swapping section ends here

            found = true;
            roomCount = roomCount+5;//change mark accordingly

            count++;//test counter
        }
        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: "+error +",marks deducted -5" +"\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error();//throws an error if test has failed
        }


    }

    /**************************************************************************
     * Adding a take command
     **************************************************************************/

    @Test
    /**
     * checks if user is not able to take a non-existing item
     */
    public void takeFail() throws IOException {
        FileWriter writer = new FileWriter(report, true);
        Boolean found = false;
        try {
            //swap this section to add your own tests
            assertEquals(" Item should not be in this room",
                    "Item not in this room.", game.take(Item.EGG));
            //swapping section ends here

            found = true;

            takeCount = takeCount + 3.75;//change mark accordingly


            count++;//test counter


        }
        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: item should not be in this room,marks deducted -3.75" + "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: item should not be in this room");//throws an error if test has failed
        }


    }

    /**
     * checks to see if user can take item successfully
     */
    @Test
    public void takeSuccess() throws IOException {
        FileWriter writer = new FileWriter(report, true);
        boolean found = false;

        try {
            //swap this section to add your own tests
            game.getCurrent().addCharacter(new Character("Ronald", Item.SUGAR));
            assertEquals(" Item should be in this room",
                    "Item taken.", game.take(Item.SUGAR));
            //swapping section ends here

            found = true;
            takeCount = takeCount + 3.75;//change mark accordingly


            count++;//test counter
        }
        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: item should be in this room, marks deducted -3.75"+ "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: item should be in this room");//throws an error if test has failed
        }



        // Writes the content to the file


    }

    /**
     * checks if user is able to take item more than once
     */
    @Test
    public void takeTwice() throws IOException {
        FileWriter writer = new FileWriter(report, true);
        boolean found = false;
        try {
            //swap this section to add your own tests
            game.getCurrent().addCharacter(new Character("Joesph", Item.EGG));
            game.take(Item.EGG);
            assertEquals(" Item should not be in this room",
                    "Item not in this room.", game.take(Item.EGG));
            //swapping section ends here

            found = true;

            // Writes the content to the file
            takeCount = takeCount + 3.75;//change mark accordingly


            count++;//test counter

        }
        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: item should not be in this room, marks deducted -3.75" + "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: item should not be in this room");//throws an error if test has failed
        }


    }

    /**
     * checks if take is an option for the user
     */
    @Test
    public void takeCommand() throws IOException {
        FileWriter writer = new FileWriter(report, true);
        boolean found = false;
        try {
            //swap this section to add your own tests
            Parser p = new Parser();
            assertTrue("'take' should be an option for the user",
                    p.commands().contains("take"));
            //swapping section ends here

            found = true;
             takeCount = takeCount + 3.75;//change mark accordingly

            count++;//test counter
        }
        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: 'take' should be an option for the user. marks deducted -3.75" + "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: 'take' should be an option for the user");//throws an error if test has failed
        }


        // Writes the content to the file

    }

    /**************************************************************************
     * Adding a cook command
     **************************************************************************/
    /**
     * checks if the user can cook once items are taken
     */
    @Test
    public void cookSucceessful() throws IOException {
        FileWriter writer = new FileWriter(report, true);
        boolean found = false;
        try {
            //swap this section to add your own tests
            game.getCurrent().addCharacter(new Character("Jack", Item.FLOUR));
            game.getCurrent().addCharacter(new Character("Greg", Item.EGG));
            game.getCurrent().addCharacter(new Character("Paul", Item.SUGAR));

            //checks to see if items were taken by the above characters
            assertEquals("Item taken.", game.take(Item.FLOUR));
            assertEquals("Item taken.", game.take(Item.EGG));
            assertEquals("Item taken.", game.take(Item.SUGAR));

            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.EAST); //leads to the kitchen

            assertEquals("Should be able to cook after all items are taken.",
                    "Congratulations! You have won.\nThank you for playing.  Good bye.",
                    game.cook());
            //swapping section ends here

            found = true;
            cookCount = cookCount +2;//change mark accordingly


            count++;//test counter

        } catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: should be able to cook after all items are taken, marks deducted -2"+ "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if (!found) {

            throw new Error("Test failed: should be able to cook after all items are taken.");//throws an error if test has failed
        }
    }


    @Test
    /**
     * checks that the user isn't able to cook
     */
    public void cookFailure() throws IOException {
        FileWriter writer = new FileWriter(report, true);
        boolean found = false;

        try {
            //swap this section to add your own tests
            assertEquals("Should not able to cook yet.",
                    "You cannot cook yet.", game.cook());
            //swapping section ends here

            cookCount = cookCount +2;//change mark accordingly
            found = true;
            count++;//test counter

    } catch (AssertionError error) {
            // Writes the content to the file
        writer.write(String.valueOf("Test failed: Should not able to cook yet, marks deducted -2"+ "\n"));//writes to the file
        found = false;

    }

        writer.close();

        if (!found) {

        throw new Error("Test failed: Should not able to cook yet");//throws an error if test has failed
    }


    }


    @Test
    /**
     * checks that the user isn't able to cook as they are not in the kitchen
     */
    public void cookWrongRoom() throws IOException {

        if (report.exists() && report != null) {
            report.delete();//deletes the file and creates a new one above
        }

        FileWriter writer = new FileWriter(report, true);
        boolean found = false;

        try {
            //swap this section to add your own tests
            game.getCurrent().addCharacter(new Character("Jack", Item.FLOUR));
            game.getCurrent().addCharacter(new Character("Greg", Item.EGG));
            game.getCurrent().addCharacter(new Character("Paul", Item.SUGAR));

            assertEquals("Item taken.", game.take(Item.FLOUR));
            assertEquals("Item taken.", game.take(Item.EGG));
            assertEquals("Item taken.", game.take(Item.SUGAR));

            game.goRoom(Direction.SOUTH); // leads to the hall
            assertEquals("Should not be able to cook as they are not in kitchen.",
                    "You cannot cook yet.", game.cook());
            //swapping section ends here

            found = true;
            cookCount = cookCount +2;//change mark accordingly

            count++;//test counter

        } catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: should not be able to cook as they are not in kitchen, marks deducted -2 "+ "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if (!found) {

            throw new Error("Test failed: should not be able to cook as they are not in kitchen");//throws an error if test has failed
        }
    }



        /**
         * checks that the user can't cook without eggs
         */

    @Test
    public void cookMissingItems() throws IOException {
        Boolean found = false;
        FileWriter writer = new FileWriter(report, true);

        try {
            //swap this section to add your own tests
            game.getCurrent().addCharacter(new Character("Anish", Item.FLOUR));
            game.getCurrent().addCharacter(new Character("Anthony", Item.EGG));
            game.getCurrent().addCharacter(new Character("Olaf", Item.EGG));
            assertEquals("Item taken.", game.take(Item.FLOUR));
            assertEquals("Item taken.", game.take(Item.EGG));
            assertEquals("Item taken.", game.take(Item.EGG));
            game.goRoom(Direction.NORTH);
            game.goRoom(Direction.EAST); // kitchen
            assertEquals("You should not be able to cook without sugar.",
                    "You cannot cook yet.", game.cook());
            //swapping section ends here

            found = true;

            // Writes the content to the file
            cookCount = cookCount +2;//change mark accordingly

            count++;//test counter
        }
        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: you should not be able to cook without sugar, marks deducted -2" + "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: you should not be able to cook without sugar\n");//throws an error if test has failed
        }


    }

    /**
     * checks if take is an option for the user
     */
    @Test
    public void commandCook() throws IOException {
        FileWriter writer = new FileWriter(report, true);
        boolean found = false;

        try {
            //swap this section to add your own tests
            Parser p = new Parser();
            assertTrue(" 'cook' should be an option for the user.",
                    p.commands().contains("cook"));

            found = true;
            //swapping section ends here
            cookCount = cookCount +2;//change mark accordingly


            count++;//test counter
        }
        catch (AssertionError error) {
            writer.write(String.valueOf("Test failed: 'cook' should be an option for the user, marks deducted -2"+ "\n"));//writes to the file
            found = false;

        }

        writer.close();

        if(!found) {

            throw new Error("Test failed: 'cook' should be an option for the user");//throws an error if test has failed
        }


    }
}
/**
 * This is the  class called PDFmaker
 * @Author- Zinuo Chen(zc99) and Wenbo Wu(ww221)
 *         -Anish Joshi(aj485)
 *
 */

class PDFmaker {
    public static void pdfMaker() throws IOException {
        /**
         * author of this section Zinuo Chen(zc99) and Wenbo Wu(ww221);
         */

        Document document = null;

        String pdfFolderPath = projectTesting.directory + "/" + "PDF" + "/";

        File pdfFolder = new File(pdfFolderPath);

        if (!pdfFolder.exists()) {
            boolean success = pdfFolder.mkdir();//checks the pdf folder exists and creates a new one if it doesn't exist
        }
        try {
            String pdfFilePath = pdfFolderPath + projectTesting.getStudentLogin() + ".pdf";
            document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
            String line;
            document.open();


            File pdfFile = new File(pdfFilePath);
            if (pdfFile.exists()) {
                System.out.println("PDF file saved successfully at: " + pdfFilePath);//checks the PDF exists
            } else {
                System.out.println("PDF file could not be saved at: " + pdfFilePath);

            }

            /**
             * author of this section Anish Joshi(aj485)
             */

            projectTesting testing = new projectTesting();

            File report = testing.getReport();

            BufferedReader br = new BufferedReader(new FileReader(report));

            Font headingFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);//creates the title for PDF
            int totalMark = (int) projectTesting.getMark();
            // Create a Paragraph object for the heading
            Paragraph heading = new Paragraph("Feedback for COMP5200\n", headingFont);//

            //line spacing
            heading.setSpacingAfter(20);

            document.add(heading);//adds the heading to the PDF


            //reads each line in the text file and adds the tests and marks of the student's code
            while ((line = br.readLine()) != null) {

                try {
                     if (line.contains("Test")) {//if line in the text file contains the word "test", it will add it to the PDF
                        document.add((new Paragraph(line)));
                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            //adds these lines to the pdf document
            document.add(new Paragraph("The final mark is "+totalMark));
            document.add(new Paragraph(projectTesting.count+"/31 tests were successful"));



        } catch (Exception e) {
            e.printStackTrace();
        }


            document.close();//closes the document


    }
}



/**
 * This is the  class called spreadsheet
 *  @Author-Anish Joshi(aj485) and Anthony Tse(at777)
 *
 */

class spreadsheet {

    /**
     * creates and adds data to the spreadsheet
     * @Author of this method-Anish Joshi(aj485)
     */
    public static void studentInfo() throws IOException {
        String path = projectTesting.directory + "/" + "studentMarks.xlsx";
        File file = new File(path);

        XSSFWorkbook workbook = null;
        Sheet sheet = null;


        //creates a spreadsheet or checks for an existing one
        if (file.exists()) {
            workbook = new XSSFWorkbook(new FileInputStream(file));
            sheet = workbook.getSheet("student data");
        } else {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("student data");
        }
        //creates the headers for the spreadsheet
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Login");
            headerRow.createCell(1).setCellValue("Marks");
            headerRow.createCell(2).setCellValue("GoalTest");
            headerRow.createCell(3).setCellValue("TimeTest");
            headerRow.createCell(4).setCellValue("LookTest");
            headerRow.createCell(5).setCellValue("ItemTest");
            headerRow.createCell(6).setCellValue("CharTest");
            headerRow.createCell(7).setCellValue("RoomTest");
            headerRow.createCell(8).setCellValue("TakeTest");
            headerRow.createCell(9).setCellValue("CookTest");
        }

        //gets the name and login to be put into the spreadsheet
        String name = projectTesting.getStudentLogin();
        int totalMark = (int) projectTesting.getMark();

        boolean studentFound = false;
        //creates each cell of data in the spreadsheet
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell loginCell = row.getCell(0);
            //checks if the login and mark already exists and updates accordingly
            if (loginCell != null && loginCell.getStringCellValue().equals(name)) {
                studentFound = true;
                updateRow(row);
                break;
            }
        }
        //if the data doesn't exist, creates a new row of data
        if (!studentFound) {
            Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
            updateRow(newRow);
        }

        FileOutputStream out = new FileOutputStream(new File(projectTesting.directory + "/" + "studentMarks.xlsx"));
        workbook.write(out);
        workbook.close();
        out.close();
    }

    /**
     *
     * adds a new row of data of the breakdown of marks of the student
     * @Author of this method-Anthony Tse(at777)
     *
     **/

    private static void updateRow(Row row) throws IOException {
        String name = projectTesting.getStudentLogin();
        int totalMark = (int) projectTesting.getMark();

        //adds each cell in the spreadsheet
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(totalMark);
        row.createCell(2).setCellValue(round(projectTesting.goalCount) + "/10");
        row.createCell(3).setCellValue(round(projectTesting.timeCount) + "/15");
        row.createCell(4).setCellValue(round(projectTesting.lookCount) + "/10");
        row.createCell(5).setCellValue(round(projectTesting.itemCount) + "/10");
        row.createCell(6).setCellValue(round(projectTesting.characterCount) + "/15");
        row.createCell(7).setCellValue(round(projectTesting.roomCount) + "/15");
        row.createCell(8).setCellValue(round(projectTesting.takeCount) + "/15");
        row.createCell(9).setCellValue(round(projectTesting.cookCount) + "/10");
    }

}



















