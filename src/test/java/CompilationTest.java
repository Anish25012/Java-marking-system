import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** Checks how compilation.analyse() accepts or rejects a student's submission. */
public class CompilationTest {
    private final Path src = Path.of(compilation.destination);
    private final InputStream realInput = System.in;

    @Before
    public void createSubmissionFolder() throws IOException {
        Files.createDirectories(src);
        System.setIn(new ByteArrayInputStream("student1\n".getBytes()));
    }

    @After
    public void removeSubmissionFolder() throws IOException {
        System.setIn(realInput);
        FileUtils.deleteDirectory(src.getParent().toFile());
    }

    private void zip(String name, String... entries) throws IOException {
        try (OutputStream file = Files.newOutputStream(src.resolve(name));
                ZipOutputStream zip = new ZipOutputStream(file)) {
            for (String entry : entries) {
                zip.putNextEntry(new ZipEntry(entry));
                zip.write("public class Game {}".getBytes());
                zip.closeEntry();
            }
        }
    }

    @Test
    public void rejectsAFolderWithNoZip() {
        Error error = assertThrows(Error.class, compilation::analyse);
        assertTrue(error.getMessage().contains("Please provide"));
    }

    @Test
    public void rejectsMoreThanOneZip() throws IOException {
        zip("one.zip", "game/Game.java");
        zip("two.zip", "game/Game.java");
        Error error = assertThrows(Error.class, compilation::analyse);
        assertTrue(error.getMessage().contains("Too many zip"));
    }

    @Test
    public void rejectsJavaFilesSplitAcrossFolders() throws IOException {
        zip("submission.zip", "first/Game.java", "second/Room.java");
        Error error = assertThrows(Error.class, compilation::analyse);
        assertTrue(error.getMessage().contains("multiple java"));
    }

    @Test
    public void rejectsJavaFilesOutsideAFolder() throws IOException {
        zip("submission.zip", "Game.java");
        Error error = assertThrows(Error.class, compilation::analyse);
        assertTrue(error.getMessage().contains("multiple java"));
    }

    @Test
    public void extractsAValidSubmissionAndRecordsTheLogin() throws Exception {
        zip("submission.zip", "game/Game.java", "game/Room.java");
        compilation.analyse();
        assertTrue(new File(src.toFile(), "game/Game.java").exists());
        List<String> login = Files.readAllLines(src.resolve("login.txt"));
        assertEquals("student1", login.get(0));
    }
}
