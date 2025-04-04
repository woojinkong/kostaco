package vo;

import java.time.LocalDateTime;

public class ItemVO {

    private int itemId;
    private String itemName;
    private int itemQty;
    private int itemPrice;
    private String itemManuf;
    private String itemPromo;
    private LocalDateTime itemMadeDate;
    private LocalDateTime ItemExpDate;

    public ItemVO(LocalDateTime itemExpDate, int itemId, LocalDateTime itemMadeDate, String itemManuf, String itemName, int itemPrice, String itemPromo, int itemQty) {
        ItemExpDate = itemExpDate;
        this.itemId = itemId;
        this.itemMadeDate = itemMadeDate;
        this.itemManuf = itemManuf;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemPromo = itemPromo;
        this.itemQty = itemQty;
    }

    public ItemVO() {
    }

    public LocalDateTime getItemExpDate() {
        return ItemExpDate;
    }

    public void setItemExpDate(LocalDateTime itemExpDate) {
        ItemExpDate = itemExpDate;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public LocalDateTime getItemMadeDate() {
        return itemMadeDate;
    }

    public void setItemMadeDate(LocalDateTime itemMadeDate) {
        this.itemMadeDate = itemMadeDate;
    }

    public String getItemManuf() {
        return itemManuf;
    }

    public void setItemManuf(String itemManuf) {
        this.itemManuf = itemManuf;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemPromo() {
        return itemPromo;
    }

    public void setItemPromo(String itemPromo) {
        this.itemPromo = itemPromo;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    @Override
    public String toString() {
        return "ItemVO{" +
                "ItemExpDate=" + ItemExpDate +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemQty=" + itemQty +
                ", itemPrice=" + itemPrice +
                ", itemManuf='" + itemManuf + '\'' +
                ", itemPromo='" + itemPromo + '\'' +
                ", itemMadeDate=" + itemMadeDate +
                '}';
    }
}