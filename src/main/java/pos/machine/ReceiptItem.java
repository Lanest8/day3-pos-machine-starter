package pos.machine;

public class ReceiptItem {
    private final Item item;
    private final int quantity;
    private final int subtotal;

    public ReceiptItem(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
        this.subtotal = item.getPrice() * quantity;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getSubtotal() {
        return subtotal;
    }
}
