import org.junit.Before;
import org.junit.Test;
import exception.IncorrectFileDb;
import exception.NotEnoughMoneyException;
import exception.UnknownAccountException;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountServiceImplTest {

    AccountDataSource accountDataSource = Mockito.mock(AccountDataSource.class);
    AccountService service = new AccountServiceImpl(accountDataSource);
    List<Account> accounts;

    @Before
    public void initialize() {
        accounts = new ArrayList<>();
        accounts.add(new Account(1, "Иван Иванов", 10));
        accounts.add(new Account(2, "Петр Петров", 20));
    }

    @Test
    public void testGetBalanceWhenAccountExists() throws UnknownAccountException, IncorrectFileDb {
        when(accountDataSource.getAccount(1)).thenReturn(accounts.get(0));
        when(accountDataSource.getAccount(2)).thenReturn(accounts.get(1));
        assertEquals(service.getBalance(1), 10);
        assertEquals(service.getBalance(2), 20);
    }

    @Test(expected = UnknownAccountException.class)
    public void testGetBalanceWhenAccountNotExistsThenException() throws UnknownAccountException, IncorrectFileDb {
        when(accountDataSource.getAccount(100)).thenThrow(UnknownAccountException.class);
        service.getBalance(100);
    }

    @Test
    public void testDepositWhenPresentInDataSource()
            throws UnknownAccountException, NotEnoughMoneyException, IncorrectFileDb {

        Account[] updateAccounts = {
                new Account(1, "Иван Иванов", 110),
                new Account(2, "Петр Петров", 220)
        };

        when(accountDataSource.getAccount(1)).thenReturn(accounts.get(0));
        when(accountDataSource.getAccount(2)).thenReturn(accounts.get(1));

        service.deposit(1, 100);
        service.deposit(2, 200);

        verify(accountDataSource).updateAccount(updateAccounts[0]);
        verify(accountDataSource).updateAccount(updateAccounts[1]);

    }

    @Test(expected = UnknownAccountException.class)
    public void testDepositWhenAccountNotExistsThenException()
            throws UnknownAccountException, NotEnoughMoneyException, IncorrectFileDb {
        when(accountDataSource.getAccount(100)).thenThrow(UnknownAccountException.class);

        service.deposit(100, 1);
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void testDepositWhenAmountValuesNegativeWhenException()
            throws UnknownAccountException, NotEnoughMoneyException, IncorrectFileDb {

        service.deposit(1, -1);
    }

    @Test
    public void testWithDrawWhenPresentInDataSource()
            throws UnknownAccountException, NotEnoughMoneyException, IncorrectFileDb {

        Account[] updateAccounts = {
                new Account(1, "Иван Иванов", 9),
                new Account(2, "Петр Петров", 18)
        };

        when(accountDataSource.getAccount(1)).thenReturn(accounts.get(0));
        when(accountDataSource.getAccount(2)).thenReturn(accounts.get(1));

        service.withDraw(1, 1);
        service.withDraw(2, 2);

        verify(accountDataSource).updateAccount(updateAccounts[0]);
        verify(accountDataSource).updateAccount(updateAccounts[1]);

    }

    @Test(expected = UnknownAccountException.class)
    public void testWithDrawWhenAccountNotExistsThenException()
            throws UnknownAccountException, NotEnoughMoneyException, IncorrectFileDb {

        when(accountDataSource.getAccount(100)).thenThrow(UnknownAccountException.class);

        service.withDraw(100, 1);
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void testWithDrawWhenAmountValuesNegativeWhenException()
            throws UnknownAccountException, NotEnoughMoneyException, IncorrectFileDb {

        service.withDraw(1, -1);
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void testWithDrawWhenNotEnoughMoneyAccountThenException()
            throws UnknownAccountException, NotEnoughMoneyException, IncorrectFileDb {

        when(accountDataSource.getAccount(1)).thenReturn(accounts.get(0));
        service.withDraw(1, 20);
    }


    @Test
    public void testTransferWhenPresentInDataSource()
            throws UnknownAccountException, NotEnoughMoneyException, IncorrectFileDb {

        Account[] updateAccounts = {
                new Account(1, "Иван Иванов", 5),
                new Account(2, "Петр Петров", 25)
        };

        when(accountDataSource.getAccount(1)).thenReturn(accounts.get(0));
        when(accountDataSource.getAccount(2)).thenReturn(accounts.get(1));

        service.transfer(1, 2, 5);

        verify(accountDataSource).updateTwoAccounts(updateAccounts[0], updateAccounts[1]);

    }

    @Test(expected = UnknownAccountException.class)
    public void testTransferWhenAccount1NotExistsThenException()
            throws UnknownAccountException, NotEnoughMoneyException, IncorrectFileDb {

        when(accountDataSource.getAccount(100)).thenThrow(UnknownAccountException.class);

        service.transfer(100, 1, 5);
    }

    @Test(expected = UnknownAccountException.class)
    public void testTransferWhenAccount2NotExistsThenException()
            throws UnknownAccountException, NotEnoughMoneyException, IncorrectFileDb {

        when(accountDataSource.getAccount(100)).thenThrow(UnknownAccountException.class);

        service.transfer(2, 100, 5);
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void testTransferWhenAmountValuesNegativeWhenException()
            throws UnknownAccountException, NotEnoughMoneyException, IncorrectFileDb {

        service.transfer(2, 1, -5);
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void testTransferWhenNotEnoughMoneyAccountThenException()
            throws UnknownAccountException, NotEnoughMoneyException, IncorrectFileDb {

        when(accountDataSource.getAccount(1)).thenReturn(accounts.get(0));
        when(accountDataSource.getAccount(2)).thenReturn(accounts.get(1));

        service.transfer(2, 1, 30);
    }
}