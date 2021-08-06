import exception.IncorrectFileDb;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileRepository {
    private final File file;

    public FileRepository(File file) {
        this.file = file;
    }

    /**
     * Считывает текст из файла и преобразует его в список аккаунтов.
     *
     * @return Список аккаунтов.
     * @throws IncorrectFileDb - Если возникла ошибка при чтении файла,
     *                         либо при преобразовании текста из файла в объекты аккаунтов.
     */
    public List<Account> read() throws IncorrectFileDb {
        List<Account> result = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.equals("")) {
                    continue;
                }
                String[] accountText = line.split("\\|");
                if (accountText.length != 3) {
                    throw new IncorrectFileDb("Ошибка при чтении файла. Список не может быть составлен");
                }

                int id = Integer.parseInt(accountText[0]);
                String holder = accountText[1];
                int amount = Integer.parseInt(accountText[2]);
                Account account = new Account(id, holder, amount);
                result.add(account);
            }
        } catch (Exception e) {
            throw new IncorrectFileDb("Ошибка при чтении файла. Список не может быть составлен", e);
        }
        return result;
    }

    /**
     * Преобразует в текст список аккаунтов и записывает в файл.
     *
     * @param accounts - Список аккаунтов, которые должны быть записаны в файл.
     */
    public void write(List<Account> accounts) {

        if (accounts == null) {
            throw new NullPointerException("Входной параметр accounts - null");
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Account account : accounts) {
            stringBuilder.append(account.getId());
            stringBuilder.append("|");
            stringBuilder.append(account.getHolder());
            stringBuilder.append("|");
            stringBuilder.append(account.getAmount());
            stringBuilder.append("\n");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
