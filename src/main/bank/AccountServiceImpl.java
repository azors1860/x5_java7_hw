import exception.IncorrectFileDb;
import exception.NotEnoughMoneyException;
import exception.UnknownAccountException;

public class AccountServiceImpl implements AccountService {

    private final AccountDataSource accountDataSource;

    public AccountServiceImpl(AccountDataSource accountDataSource) {
        this.accountDataSource = accountDataSource;
    }

    @Override
    public void withDraw(int accountId, int amount)
            throws NotEnoughMoneyException, UnknownAccountException, IncorrectFileDb {

        if (amount < 0) {
            throw new NotEnoughMoneyException("Значение 'amount' не может быть отрицательеым: " + amount);
        }
        if (accountId < 0) {
            throw new UnknownAccountException("Значение 'accountId' не может быть отрицательеым: " + accountId);
        }
        Account tmp = accountDataSource.getAccount(accountId);
        tmp.setAmount(tmp.getAmount() - amount);
        accountDataSource.updateAccount(tmp);
    }

    @Override
    public int getBalance(int accountId) throws UnknownAccountException, IncorrectFileDb {

        if (accountId < 0) {
            throw new UnknownAccountException("Значение 'accountId' не может быть отрицательеым: " + accountId);
        }
        return accountDataSource.getAccount(accountId).getAmount();
    }

    @Override
    public void deposit(int accountId, int amount)
            throws NotEnoughMoneyException, UnknownAccountException, IncorrectFileDb {

        if (amount < 0) {
            throw new NotEnoughMoneyException("Значение 'amount' не может быть отрицательеым: " + amount);
        }
        if (accountId < 0) {
            throw new UnknownAccountException("Значение 'accountId' не может быть отрицательеым: " + accountId);
        }
        Account tmp = accountDataSource.getAccount(accountId);
        tmp.setAmount(tmp.getAmount() + amount);
        accountDataSource.updateAccount(tmp);
    }

    @Override
    public void transfer(int from, int to, int amount)
            throws NotEnoughMoneyException, UnknownAccountException, IncorrectFileDb {

        if (amount < 0) {
            throw new NotEnoughMoneyException("Значение 'amount' не может быть отрицательеым: " + amount);
        }
        if (from < 0) {
            throw new UnknownAccountException("Значение 'from' не может быть отрицательеым: " + from);
        }
        if (to < 0) {
            throw new UnknownAccountException("Значение 'accountId' не может быть отрицательеым: " + to);
        }
        Account accountFrom = accountDataSource.getAccount(from);
        Account accountTo = accountDataSource.getAccount(to);
        System.out.println(accountFrom.getAmount());
        accountFrom.setAmount(accountFrom.getAmount() - amount);
        System.out.println(accountFrom.getAmount());
        accountTo.setAmount(accountTo.getAmount() + amount);
        accountDataSource.updateTwoAccounts(accountFrom, accountTo);
    }
}
