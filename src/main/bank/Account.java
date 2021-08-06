import exception.NotEnoughMoneyException;

import java.util.Objects;

public class Account {
    private final int id;
    private String holder;
    private int amount;

    public Account(int id, String holder, int amount) {
        this.id = id;
        this.holder = holder;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public int getAmount() {
        return amount;
    }

    /**
     * @throws NotEnoughMoneyException В случае если параметр имеет отрицательное значение
     */
    public void setAmount(int amount) throws NotEnoughMoneyException {
        if (amount < 0) {
            throw new NotEnoughMoneyException("Баланс не может быть отрицательным!");
        }
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id &&
                amount == account.amount &&
                Objects.equals(holder, account.holder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, holder, amount);
    }

    @Override
    public String toString() {
        return "task1.Account{" +
                "id=" + id +
                ", holder='" + holder + '\'' +
                ", amount=" + amount +
                '}';
    }
}
