package Goose;

import Goose.ItemTemplate;

public class NPCVendorSlot   
{
    private int __Slot;
    public int getSlot() {
        return __Slot;
    }

    public void setSlot(int value) {
        __Slot = value;
    }

    private ItemTemplate __ItemTemplate;
    public ItemTemplate getItemTemplate() {
        return __ItemTemplate;
    }

    public void setItemTemplate(ItemTemplate value) {
        __ItemTemplate = value;
    }

    private int __Stack;
    public int getStack() {
        return __Stack;
    }

    public void setStack(int value) {
        __Stack = value;
    }

    private boolean __CanSeeStats;
    public boolean getCanSeeStats() {
        return __CanSeeStats;
    }

    public void setCanSeeStats(boolean value) {
        __CanSeeStats = value;
    }

}


