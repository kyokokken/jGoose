package Goose;

public class NPCDropInfo {
    private double __DropRate;

    public double getDropRate() {
        return __DropRate;
    }

    public void setDropRate(double value) {
        __DropRate = value;
    }

    private int __Stack;

    public int getStack() {
        return __Stack;
    }

    public void setStack(int value) {
        __Stack = value;
    }

    private ItemTemplate __ItemTemplate;

    public ItemTemplate getItemTemplate() {
        return __ItemTemplate;
    }

    public void setItemTemplate(ItemTemplate value) {
        __ItemTemplate = value;
    }

}
