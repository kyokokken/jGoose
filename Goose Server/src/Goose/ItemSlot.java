//
// Translated by CS2J (http://www.cs2j.com): 15/11/2013 18:39:01
//

package Goose;

import Goose.Item;
import Goose.ItemSlot;

/**
     * ItemSlot, holds an item and stack size
     * 
     * Used for inventory, bank, combine bag slots
     * 
     */
public class ItemSlot   
{
    private Item __Item;
    public Item getItem() {
        return __Item;
    }

    public void setItem(Item value) {
        __Item = value;
    }

    private long __Stack;
    public long getStack() {
        return __Stack;
    }

    public void setStack(long value) {
        __Stack = value;
    }

    /**
             * CanStack, returns true if other slot can stack with this slot
             * 
             * Note: Doesn't do null checking
             * 
             */
    public boolean canStack(ItemSlot other) throws Exception {
        if (this.getItem().getTemplateID() != other.getItem().getTemplateID())
            return false;
         
        if (this.getItem().getStackSize() == 1)
            return false;
         
        if (other.getItem().getStackSize() == 1)
            return false;
         
        if (this.getItem().getStackSize() != other.getItem().getStackSize())
            return false;
         
        if (this.getItem().getStackSize() == 0)
            return true;
         
        if (this.getStack() + other.getStack() > this.getItem().getStackSize())
            return false;
         
        return true;
    }

}


