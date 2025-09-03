package pos.machine;

import java.util.*;
import java.util.stream.Collectors;

import static pos.machine.ItemsLoader.loadAllItems;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        List<Item> items = selectAllItems(barcodes);
        List<ReceiptItem> receiptItems = generateReceiptItems(items);
        int total = calculateTotalCost(receiptItems);
        return generateReceipt(receiptItems, total);
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

    public int calculateTotalCost(List<ReceiptItem> receiptItems) {
        return receiptItems.stream()
                .mapToInt(ReceiptItem::getSubtotal)
                .sum();
    }

    public String generateReceipt(List<ReceiptItem> receiptItems, int total) {
        StringBuilder receiptBuilder = new StringBuilder();
        receiptBuilder.append("***<store earning no money>Receipt***\n");
        for (ReceiptItem receiptItem : receiptItems) {
            Item item = receiptItem.getItem();
            receiptBuilder.append(String.format("Name: %s, Quantity: %d, Unit price: %d (yuan), Subtotal: %d (yuan)\n",
                    item.getName(), receiptItem.getQuantity(), item.getPrice(), receiptItem.getSubtotal()));
        }
        receiptBuilder.append("----------------------\n");
        receiptBuilder.append(String.format("Total: %d (yuan)\n", total));
        receiptBuilder.append("**********************");
        return receiptBuilder.toString();
    }
}
