import exception.IncorrectFileDb;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import exception.UnknownAccountException;
import exception.NotEnoughMoneyException;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountDataSourceTest {

    List<Account> accounts;
    FileRepository repository = Mockito.mock(FileRepository.class);
    AccountDataSource dataSource = new AccountDataSource(repository);

    @Before
    public void initialization() {
        accounts = Arrays.asList(
                new Account(1, "Иван Иванов", 100),
                new Account(2, "Петр Петров", 300),
                new Account(3, "Евгений Евгенов", 5000)
        );
    }

    @Test
    public void testGetAccountWhenPresentInDataSource() throws UnknownAccountException, IncorrectFileDb {

        when(repository.read()).thenReturn(accounts);

        Account accountId1 = dataSource.getAccount(1);
        Account accountId2 = dataSource.getAccount(2);
        Account accountId3 = dataSource.getAccount(3);
        assertEquals(accounts.get(0), accountId1);
        assertEquals(accounts.get(1), accountId2);
        assertEquals(accounts.get(2), accountId3);
    }

    @Test(expected = UnknownAccountException.class)
    public void testGetAccountWhenAccountNotExistsThenException() throws UnknownAccountException, IncorrectFileDb {

        when(repository.read()).thenReturn(accounts);
        dataSource.getAccount(5);
    }

    @Test
    public void testUpdateAccountWhenPresentInDataSource()
            throws UnknownAccountException, NotEnoughMoneyException, IncorrectFileDb {

        when(repository.read()).thenReturn(Arrays.asList(
                new Account(1, "Иван Иванов", 100),
                new Account(2, "Петр Петров", 300),
                new Account(3, "Евгений Евгенов", 5000)
        ));

        accounts.get(2).setAmount(1000);
        dataSource.updateAccount(accounts.get(2));
        verify(repository).write(accounts);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateAccountWhenInputParameterNullThenException()
            throws UnknownAccountException, IncorrectFileDb {
        dataSource.updateAccount(null);
    }


    @Test(expected = UnknownAccountException.class)
    public void testUpdateAccountWhenUserNotInDatabaseThenException()
            throws UnknownAccountException, IncorrectFileDb {

        when(repository.read()).thenReturn(accounts);
        Account newAccount = new Account(4, "Лев Левщенко", 10);
        dataSource.updateAccount(newAccount);
    }

    @Test
    public void testUpdateTwoAccountsWhenPresentInDataSource()
            throws UnknownAccountException, NotEnoughMoneyException, IncorrectFileDb {

        when(repository.read()).thenReturn(Arrays.asList(
                new Account(1, "Иван Иванов", 100),
                new Account(2, "Петр Петров", 300),
                new Account(3, "Евгений Евгенов", 5000)
        ));

        accounts.get(2).setAmount(1000);
        accounts.get(2).setAmount(1000);
        dataSource.updateTwoAccounts(accounts.get(1), accounts.get(2));
        verify(repository).write(accounts);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateTwoAccountsWhenInputParameterNullThenException()
            throws UnknownAccountException, IncorrectFileDb {
        dataSource.updateTwoAccounts(null, null);
    }

    @Test(expected = UnknownAccountException.class)
    public void testUpdateTwoAccountsWhenUserNotInDatabaseThenException()
            throws UnknownAccountException, IncorrectFileDb {
        Account newAccount = new Account(4, "Лев Левщенко", 10);
        dataSource.updateTwoAccounts(newAccount, accounts.get(0));
    }
}