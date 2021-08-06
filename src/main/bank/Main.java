import exception.IncorrectFileDb;
import exception.NotEnoughMoneyException;
import exception.UnknownAccountException;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws NotEnoughMoneyException, UnknownAccountException, IncorrectFileDb {

        AccountService service =
                new AccountServiceImpl(new AccountDataSource(new FileRepository
                        (new File("src/main/resources/db.txt"))));

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = scanner.nextLine();
            if (str.equals("exit")) {
                break;
            } else if (str.matches("balance [0-9]+")) {
                String stringId = str.split(" ")[1];
                int id = Integer.parseInt(stringId);
                System.out.printf("Баланс аккаунта id-%s = %s", id, service.getBalance(id));
            } else if (str.matches("withdraw [0-9]+ [0-9]+")) {
                String[] array = str.split(" ");
                int id = Integer.parseInt(array[1]);
                int sum = Integer.parseInt(array[2]);
                service.withDraw(id, sum);
            } else if (str.matches("deposite [0-9]+ [0-9]+")) {
                String[] array = str.split(" ");
                int id = Integer.parseInt(array[1]);
                int sum = Integer.parseInt(array[2]);
                service.deposit(id, sum);
            } else if (str.matches("transfer [0-9]+ [0-9]+ [0-9]+")) {
                String[] array = str.split(" ");
                int id1 = Integer.parseInt(array[1]);
                int id2 = Integer.parseInt(array[2]);
                int sum = Integer.parseInt(array[3]);
                service.transfer(id1, id2, sum);
            } else {
                throw new UnsupportedOperationException("Команда: '" + str + "' не найдена");
            }
        }
    }
}