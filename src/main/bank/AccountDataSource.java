import exception.IncorrectFileDb;
import exception.UnknownAccountException;

import java.util.List;

/**
 * Позволяет производить операции с аккаунтами.
 *
 * @author Chuvashov Sergey
 */
public class AccountDataSource {

    private final FileRepository fileRepository;

    public AccountDataSource(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    /**
     * Получить аккаунт по id.
     *
     * @param id Идентификатор аккаунта.
     * @return Аккаунт, идентификатор которого совпадает с параметром.
     * @throws UnknownAccountException Если аккаунт с идентификатором из параметра не найден в списке,
     *                                 либо если параметр имеет отризательное значение.
     * @throws IncorrectFileDb         Если возникли проблемы при прочтении файла
     *                                 или преобразовании информации из файла в объекты аккаунта.
     */
    public Account getAccount(int id) throws UnknownAccountException, IncorrectFileDb {

        if (id < 0) {
            throw new UnknownAccountException("Значение 'accountId' не может быть отрицательеым: " + id);
        }
        Account result = null;
        List<Account> accounts = fileRepository.read();
        for (Account account : accounts) {
            if (account.getId() == id) {
                result = account;
            }
        }
        if (result == null) {
            throw new UnknownAccountException("Аккаунт с указанным id не найден: " + id);
        } else {
            return result;
        }
    }

    /**
     * Заменяет аккаунт из списка аккаунтов (БД - файл) на переданный в параметре аккаунт,
     * при условии что аккаунты иметют один id.
     *
     * @param account - Объект аккаунта, которым будет заменен аккаунт из общего списка.
     * @throws UnknownAccountException - Если в общем списке аккаунтов, среди всех аккаунтов, не найдётся идентификатор
     *                                 равный идентификатору аккаунта переданного в параметре.
     * @throws IncorrectFileDb         - Если при получении списка аккаунтов возникнет ошибка.
     *                                 Например, некорректные данные в файле.
     */
    public void updateAccount(Account account) throws UnknownAccountException, IncorrectFileDb {

        if (account == null) {
            throw new NullPointerException("Входной параметр account - null");
        }

        int searchId = -1;
        List<Account> accounts = fileRepository.read();
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getId() == account.getId()) {
                searchId = i;
                break;
            }
        }
        if (searchId == -1) {
            throw new UnknownAccountException("Аккаунт с данным id не найден: " + account.getId());
        } else {
            accounts.set(searchId, account);
            fileRepository.write(accounts);
        }
    }

    /**
     * Заменяет аккаунт из списка аккаунтов (БД - файл) на переданный в параметре аккаунт,
     * при условии что аккаунты иметют один id.
     * <p>
     * Функция необходима для изменения двух аккаунтов в рамках одной транзакции.
     * </p>
     *
     * @param account1,account2 - Объекты аккаунта, которыми будут заменены аккаунты из общего списка.
     * @throws UnknownAccountException - Если в общем списке аккаунтов, среди всех аккаунтов, не найдётся идентификатор
     *                                 равный идентификатору аккаунта переданного в параметре.
     * @throws IncorrectFileDb         - Если при получении списка аккаунтов возникнет ошибка.
     *                                 Например, некорректные данные в файле.
     */
    public void updateTwoAccounts(Account account1, Account account2)
            throws UnknownAccountException, IncorrectFileDb {

        if (account1 == null) {
            throw new NullPointerException("Входной параметр account1 - null");
        }
        if (account2 == null) {
            throw new NullPointerException("Входной параметр account2 - null");
        }

        int searchId1 = -1;
        int searchId2 = -1;
        List<Account> accounts = fileRepository.read();
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getId() == account1.getId()) {
                searchId1 = i;
            } else if (accounts.get(i).getId() == account2.getId()) {
                searchId2 = i;
            }
            if ((searchId1 != -1) && (searchId2 != -1)) {
                break;
            }
        }
        if (searchId1 == -1) {
            throw new UnknownAccountException("Аккаунт с данным id не найден: " + account1.getId());
        } else if (searchId2 == -1) {
            throw new UnknownAccountException("Аккаунт с данным id не найден: " + account2.getId());
        } else {
            accounts.set(searchId1, account1);
            accounts.set(searchId2, account2);
            fileRepository.write(accounts);
        }
    }
}
