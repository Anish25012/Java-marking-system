import java.io.*;
import java.nio.file.*;
import java.util.*;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import org.apache.commons.io.FileUtils;


/**
 * This is the extraction class called compilation
 *  * @Author- Anish Joshi(aj485)
 */
public class compilation {
    //created to find the paths of where the zip folder is stored
    static Path currentPath = Paths.get("");
    static Path srcPath = Paths.get(currentPath.toAbsolutePath().toString(), "comp6000 submission", "src");//gets the current path of the src folder where the contents of students code should be
    static String destination = srcPath.toString();


    public compilation() {


    }

    /**
     *
     * checks and extracts the student's code
     */

    public static void analyse() throws IOException, InterruptedException {


        File zipCheckFolder = new File(destination);
        File[] files = zipCheckFolder.listFiles();
        boolean ifContainsZip = false;
        String source = null;
        int count = 0;
        int javaCount = 0;


        for (File file : files) {
            if (file.exists() && file.isDirectory()) {
                String folderName = file.getName();
                if (!folderName.contains("PDF")) {// ensures the code doesn't delete the pdf folder
                    FileUtils.deleteDirectory(file);//gets rid of the old folder for the new submission to be tested
                    file.delete();
                }


            }
            if (file.isFile() && file.getName().endsWith(".zip")) {//checks to see if it there is a zip folder
                ifContainsZip = true;
                count++;
                source = file.getAbsolutePath();//gets the path of where the zip folder is stored
            }

        }
        if (count > 1) {//if too many zip folders, throws an error
            throw new Error("Too many zip folders, please just provide one");

        }


        if (!ifContainsZip) {//throws an error if no zip folder was provided
            throw new Error("Please provide the student's zip or jar folder and insert into the src folder");
        }
        ;

        try {
            ZipFile zip = new ZipFile(source);//
            List<FileHeader> fileHeaderList = zip.getFileHeaders();

            String parentFolderName = null;

            for (FileHeader fileHead : fileHeaderList) {
                javaCount++;
                if (fileHead.getFileName().endsWith(".java") && !fileHead.getFileName().contains("__MACOSX")) {
                    // Gets the parent folder name
                    String pathToParent = fileHead.getFileName().substring(0, fileHead.getFileName().lastIndexOf("/"));
                    if (parentFolderName == null) {//checks that it exists
                        parentFolderName = pathToParent;

                    //throws en error if more than one file/folder detected outside of a folder
                    } else if (!parentFolderName.equals(pathToParent)) {//checks if they are the same or not
                        throw new Error("multiple java files/folders detected in submission, please check format");

                    }


                }
            //thrown if no java files are detected in the submission
            }
            if(javaCount == 0){
                throw new Error("Please provide java files in the submission");
            }

            Scanner scanner = new Scanner(System.in);
            System.out.println("please enter students login");//prints a statement
            String login = scanner.nextLine();//gets the login of the student

            //extracts the contents of the student's code
            zip.extractAll(destination);
            System.out.println("files are successfully extracted");//prints if everything was successful
            File report = new File(destination + "/" + "login.txt");

            //writes to the login.txt file of the user's login and src directory
            FileWriter writer = new FileWriter(report);
            writer.write(login + "\n");
            writer.write(destination + "\n");
            writer.close();

        } catch (Exception e) {
            //catches error if multiple java folders/files are found
            if(e instanceof StringIndexOutOfBoundsException){
                throw new Error("multiple java files/folders detected in submission, please check format");
            }
            else{
                throw new RuntimeException(e);
            }
        }




    }


    public static void main (String args[]) throws IOException, InterruptedException {
        analyse();


    }

}

