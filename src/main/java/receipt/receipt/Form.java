package receipt.receipt;

import java.util.ArrayList;

public interface Form {

    ArrayList<String> RECEIPT_LINES = new ArrayList<>();
    ArrayList<String> get(); // возвращает форму RECEIPT_LINES;
    void print();
    void save();
    int WIDTH = 45;
    int QTY_WIDTH = 3;
    int PRICE_WIDTH = WIDTH/6;
    int TOTAL_WIDTH = PRICE_WIDTH+3;
    int DESC_WIDTH = WIDTH-QTY_WIDTH-PRICE_WIDTH-TOTAL_WIDTH;
    String EMPTY_LINE = " ".repeat(WIDTH), DASH_LINE = "-".repeat(WIDTH);

}