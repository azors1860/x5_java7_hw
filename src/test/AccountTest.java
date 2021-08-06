import org.junit.Test;
import exception.NotEnoughMoneyException;

public class AccountTest {

    Account account = new Account(1, "Ivan Ivanov", 50);

    @Test(expected = NotEnoughMoneyException.class)
    public void whenBalanceIsNegativeThenOccursException() throws NotEnoughMoneyException {
        account.setAmount(-50);
    }
}