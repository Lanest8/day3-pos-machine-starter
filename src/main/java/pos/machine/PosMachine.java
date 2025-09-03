package pos.machine;

import java.util.*;
import java.util.stream.Collectors;

import static pos.machine.ItemsLoader.loadAllItems;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        List<Item> items = selectAllItems(barcodes);
        List<ReceiptItem> receiptItems = generateReceiptItems(items);
        return null;
    }

    public List<Item> selectAllItems(List<String> barcodes) {
        List<Item> items = loadAllItems();
        return barcodes.stream()
                .map(barcode -> {
                    Item item = findItemByBarcode(items, barcode);
                    if (item == null) {
                        throw new IllegalArgumentException("Unknown barcode: " + barcode);
                    }
                    return item;
                })
                .collect(Collectors.toList());
    }

    public Item findItemByBarcode(List<Item> items, String barcode) {
        return items.stream()
                .filter(item -> item.getBarcode().equals(barcode))
                .findFirst()
                .orElse(null);
    }


    public List<ReceiptItem> generateReceiptItems(List<Item> items) {
        Map<Item, Integer> itemCounts = generateItemsCountMap(items);
        return itemCounts.entrySet().stream()
                .map(entry -> new ReceiptItem(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public static Map<Item, Integer> generateItemsCountMap(List<Item> items) {
        Map<Item, Integer> itemCounts = new LinkedHashMap<>();
        for (Item item : items) {
            itemCounts.put(item, itemCounts.getOrDefault(item, 0) + 1);
        }
        return itemCounts;
    }

}
