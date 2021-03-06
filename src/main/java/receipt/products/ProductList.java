package receipt.products;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import receipt.DataMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ProductList implements DataMap<Integer, Product> {

    public static final int LIST_GEN_LENGTH = 100; // количество генерируемых позиций в объекте позиций

    Map<Integer, Product> productList;

    public boolean contains(Integer id) { return !productList.containsKey(id); }

    public Product getValue(Integer id) { return productList.get(id); }


    public ProductList(String fileName) {
        productList = (fileName == null) ? generator(LIST_GEN_LENGTH) : reader(fileName);
    }


    // возвращает хэшмап позиций, заполняя его из файла
    public Map<Integer, Product> reader(String fileName) {

        final Map<Integer, Product> productList = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String productLine;
            while ((productLine = br.readLine()) != null) {
                String[] elements = productLine.split(",");
                Product product = new Product();
                product.description = elements[1];
                product.price = Integer.parseInt(elements[2]);
                product.promoValue = Integer.parseInt(elements[3]);
                product.promoQty = Integer.parseInt(elements[4]);
                productList.put(Integer.parseInt(elements[0]), product);
            }
        } catch (IOException ex) {
            System.out.printf("ERROR: file \"%s\" doesn't exists!", fileName);
            System.exit(0);
        }
        System.out.printf("\nProduct list was loaded from \"%s\"\n", fileName);
        return productList;

    }


    // возвращает сгенерированный хэшмап позиций
    public Map<Integer, Product> generator(int listLength) {

        Map<Integer, Product> productList = new LinkedHashMap<>();
        int MAX_DESCRIPTION_WORDS = 4,
            MAX_PRICE = 100,
            MIN_PROMO = 10, // минимальный % акционной скидки
            MAX_PROMO = 50,
            MIN_QTY_PROMO = 5, // минимальное количество от которого возможна акция
            MAX_QTY_PROMO = 10;
        Lorem lorem = LoremIpsum.getInstance();

        for (int id = 0; id < listLength; ) {
            Product product = new Product();
            String name = lorem.getWords(1, MAX_DESCRIPTION_WORDS);
            product.description = name.substring(0, 1).toUpperCase() + name.substring(1);
            product.price = 1 + (int) (Math.random() * 100 * MAX_PRICE);
            if (Math.random() < 0.5) {
                product.promoValue = MIN_PROMO + (int) (Math.random() * (MAX_PROMO - MIN_PROMO));
                product.promoQty = MIN_QTY_PROMO + (int) (Math.random() * (MAX_QTY_PROMO - MIN_QTY_PROMO));
            }
            productList.put(++id, product);
        }
        System.out.println("\nProduct list has been successfully generated");
        return productList;

    }


    // метод сохранения хэшмапа позиций в файл типа .csv
    public void save(String fileName) {

        try (FileWriter writer = new FileWriter(fileName, false)) {
            for (Map.Entry<Integer, Product> entry : productList.entrySet()) {
                Product product = entry.getValue();
                StringJoiner line = new StringJoiner(",");
                line.add("" + entry.getKey());
                line.add(product.description);
                line.add("" + product.price);
                line.add("" + product.promoValue);
                line.add("" + product.promoQty);
                writer.write(line + "\n");
            }
            writer.flush();
            System.out.printf("Product list saved in \"%s\" file\n", fileName);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
        }

    }

}
