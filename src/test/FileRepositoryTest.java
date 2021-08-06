import exception.IncorrectFileDb;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FileRepositoryTest {

    File file = new File("src/main/resources/test.txt");
    FileRepository fileRepository = new FileRepository(file);

    @After
    public void deleteFile() {
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testWriteFileListAccounts() {
        String result =
                "1|Иван Иванов|100\n" +
                        "2|Петр Петров|300\n" +
                        "3|Евгений Евгенов|5000\n";

        List<Account> accounts = Arrays.asList(
                new Account(1, "Иван Иванов", 100),
                new Account(2, "Петр Петров", 300),
                new Account(3, "Евгений Евгенов", 5000));

        fileRepository.write(accounts);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                stringBuilder.append(bufferedReader.readLine()).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(result, stringBuilder.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testWriteFileListAccountsWhenInputParameterNullThenException() {
        fileRepository.write(null);
    }

    @Test
    public void testGetListAccountsWhenFileCorrect() throws IncorrectFileDb {

        String text =
                "1|Иван Иванов|1000\n" +
                        "2|Петр Петров|2000\n" +
                        "3|Евгений Евгенов|3000\n";
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Account> accounts = fileRepository.read();

        assertEquals(3, accounts.size());
        assertEquals(accounts.get(0), new Account(1, "Иван Иванов", 1000));
        assertEquals(accounts.get(1), new Account(2, "Петр Петров", 2000));
        assertEquals(accounts.get(2), new Account(3, "Евгений Евгенов", 3000));
    }

    @Test(expected = IncorrectFileDb.class)
    public void testGetListAccountsWhenFileIncorrectThenException() throws IncorrectFileDb {
        String text = "0|Иванов Иван|Много\n";
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileRepository.read();
    }
}
