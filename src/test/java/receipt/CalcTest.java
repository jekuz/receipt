package receipt;

import org.junit.jupiter.api.RepeatedTest;
import receipt.args.Args;
import receipt.args.Data;
import receipt.cards.CardList;
import receipt.products.ProductList;
import receipt.receipt.Calc;
import receipt.receipt.Result;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CalcTest extends Test {

    private static final ProductList productList = new ProductList(EXISTING_PRODUCT_LIST_FILENAME);
    private static final CardList cardList = new CardList(EXISTING_CARD_LIST_FILENAME);
    private long amount = 0, amountPromo = 0; // аккумуляторы


    @RepeatedTest(TEST_QTY)
    void calc_random_valid_args_test() throws Exception {

        // случайные валидные аргументы
        String[] args = possibleAdd(validProductArgs(randomInt(1, MAX_POSITIONS)),
                validProductListArg(), validCardListArg(), validCardArg());

        // объект распарсенных аргументов
        Data data = Args.parser(args);

        // объект вычисленных результатов
        Result result = Calc.result(data, productList, cardList);

        // перебор позиций с их вычислением
        for (Map.Entry<Integer, Integer> entry : data.products.entrySet()) {
            calcPosition(entry.getKey(), entry.getValue());
        }

        // подсчёт итогов
        long discountTotal = (long) Math.ceil((double) (amount - amountPromo)
                * cardList.getValue(data.cardNumber) / 100);
        long total = amount - discountTotal;

        // сравнение результатов с результатами объекта
        assertEquals(amount, result.amount);
        assertEquals(discountTotal, result.discountTotal);
        assertEquals(total, result.total);

    }

    // расчёт позиции с аккумулированием итогов в переменные
    private void calcPosition(int id, int qty) {

        int price = productList.getValue(id).price;
        int promoValue = productList.getValue(id).promoValue;
        int promoQty = productList.getValue(id).promoQty;
        int total = price * qty;

        if (promoValue > 0 && qty > promoQty) {
            long promoTotal = (long) Math.ceil((double) total * (100 - promoValue) / 100);
            amountPromo += promoTotal;
            amount += promoTotal;
        }
        else amount += total;

    }

}
