package Goose;

/**
 * ItemTile
 * <p>
 * Holds an x,y and an ItemSlot
 */
public class ItemTile implements ITile {
    private int __X;

    public int getX() {
        return __X;
    }

    public void setX(int value) {
        __X = value;
    }

    private int __Y;

    public int getY() {
        return __Y;
    }

    public void setY(int value) {
        __Y = value;
    }

    private ItemSlot __ItemSlot;

    public ItemSlot getItemSlot() {
        return __ItemSlot;
    }

    public void setItemSlot(ItemSlot value) {
        __ItemSlot = value;
    }

    /**
     * If dropped by NPC only this player can pick up until pickuptime below
     */
    private Goose.Player __Owner;

    public Goose.Player getOwner() {
        return __Owner;
    }

    public void setOwner(Goose.Player value) {
        __Owner = value;
    }

    private long __PickupTime;

    public long getPickupTime() {
        return __PickupTime;
    }

    public void setPickupTime(long value) {
        __PickupTime = value;
    }

    private long __DroppedTime;

    public long getDroppedTime() {
        return __DroppedTime;
    }

    public void setDroppedTime(long value) {
        __DroppedTime = value;
    }

    /**
     * MOBString, create Map Object string
     */
    public String mOBString() throws Exception {
        String rgba = "*";
        if (this.getItemSlot().getItem().getGraphicA() != 0) {
            rgba =
                    this.getItemSlot().getItem().getGraphicR() + ","
                            + this.getItemSlot().getItem().getGraphicG() + ","
                            + this.getItemSlot().getItem().getGraphicB() + ","
                            + this.getItemSlot().getItem().getGraphicA();
        }

        return "MOB" + this.getItemSlot().getItem().getGraphicTile() + "," + this.getX() + ","
                + this.getY() + "," + this.getItemSlot().getItem().getName()
                + (this.getItemSlot().getItem().getIsBindOnPickup() ? " (BoP)" : "") + ","
                + this.getItemSlot().getStack() + "," + rgba;
    }

    /**
     * EOBString, erase map object string
     */
    public String eOBString() throws Exception {
        return "EOB" + this.getX() + "," + this.getY();
    }

}
