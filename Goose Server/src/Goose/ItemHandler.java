package Goose;

import Goose.Events.ItemSaveEvent;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * ItemHandler, handles item templates/items
 */
public class ItemHandler {
    Hashtable<Integer, ItemTemplate> templates;
    Hashtable<Integer, Item> items;
    List<Item> newitems;
    int currentid = 5002;

    public ItemHandler() throws Exception {
        this.templates = new Hashtable<Integer, ItemTemplate>();
        this.items = new Hashtable<Integer, Item>();
        this.newitems = new ArrayList<Item>();
    }

    /**
     * Gets/sets the next available item id
     */
    public int getCurrentID() throws Exception {
        return this.currentid;
    }

    public void setCurrentID(int value) throws Exception {
        this.currentid = value;
    }

    /**
     * LoadTemplates, loads item templates
     */
    public void loadTemplates(GameWorld world) throws Exception {
        String query = "SELECT * FROM item_templates";
        ResultSet resultSet = world.getSqlConnection().createStatement().executeQuery(query);
        while (resultSet.next()) {
            ItemTemplate template = new ItemTemplate();
            template.setID(resultSet.getInt("item_template_id"));
            template.setType(Goose.ItemTemplate.ItemTypes.values()[resultSet.getInt("item_type")]);
            template.setSlot(Goose.ItemTemplate.ItemSlots.values()[resultSet.getInt("item_slot")]);
            template.setUseType(Goose.ItemTemplate.UseTypes.values()[resultSet.getInt("item_usetype")]);
            template.setName(resultSet.getString("item_name"));
            template.setDescription(resultSet.getString("item_description"));
            template.setBaseStats(new AttributeSet());
            template.getBaseStats().setHP(resultSet.getInt("player_hp"));
            template.getBaseStats().setMP(resultSet.getInt("player_mp"));
            template.getBaseStats().setSP(resultSet.getInt("player_sp"));
            template.getBaseStats().setAC(resultSet.getInt("stat_ac"));
            template.getBaseStats().setStrength(resultSet.getInt("stat_str"));
            template.getBaseStats().setStamina(resultSet.getInt("stat_sta"));
            template.getBaseStats().setIntelligence(resultSet.getInt("stat_int"));
            template.getBaseStats().setDexterity(resultSet.getInt("stat_dex"));
            template.getBaseStats().setFireResist(resultSet.getInt("res_fire"));
            template.getBaseStats().setAirResist(resultSet.getInt("res_air"));
            template.getBaseStats().setEarthResist(resultSet.getInt("res_earth"));
            template.getBaseStats().setSpiritResist(resultSet.getInt("res_spirit"));
            template.getBaseStats().setWaterResist(resultSet.getInt("res_water"));
            template.setMinLevel(resultSet.getInt("min_level"));
            template.setMaxLevel(resultSet.getInt("max_level"));
            template.setMinExperience(resultSet.getLong("min_experience"));
            template.setMaxExperience(resultSet.getLong("max_experience"));
            template.setWeaponDamage(resultSet.getInt("weapon_damage"));
            template.setWeaponDelay(resultSet.getInt("weapon_delay"));
            template.setValue(resultSet.getLong("item_value"));
            template.setGraphicTile(resultSet.getInt("graphic_tile"));
            template.setGraphicEquipped(resultSet.getInt("graphic_equip"));
            template.setGraphicR(resultSet.getInt("graphic_r"));
            template.setGraphicG(resultSet.getInt("graphic_g"));
            template.setGraphicB(resultSet.getInt("graphic_b"));
            template.setGraphicA(resultSet.getInt("graphic_a"));
            template.setClassRestrictions(resultSet.getLong("class_restrictions"));
            template.setIsLore(("0".equals(resultSet.getString("lore")) ? false : true));
            template.setIsBindOnPickup(("0".equals(resultSet.getString("bindonpickup")) ? false : true));
            template.setIsBindOnEquip(("0".equals(resultSet.getString("bindonequip")) ? false : true));
            template.setIsEvent(("0".equals(resultSet.getString("event")) ? false : true));
            template.setStackSize(resultSet.getInt("stack_size"));
            template.setBodyState(resultSet.getInt("body_state"));
            template.setSpellEffectID(resultSet.getInt("spell_effect_id"));
            template.setSpellEffect(world.getSpellHandler().getSpellEffect(template.getSpellEffectID()));
            if (template.getSpellEffectID() != 0 && template.getSpellEffect() == null) {
                continue;
            }

            // log bad spell effect on item
            template.setSpellEffectChance(Double.parseDouble(resultSet.getString("spell_effect_chance")));
            template.setLearnSpellID(resultSet.getInt("learn_spell_id"));
            this.templates.put(template.getID(), template);
        }
        resultSet.close();
    }

    /**
     * TemplateCount, returns item template count
     */
    public int getTemplateCount() throws Exception {
        return this.templates.size();
    }

    /**
     * GetTemplate, returns template by id
     */
    public ItemTemplate getTemplate(int id) throws Exception {
        return (ItemTemplate) this.templates.get(id);
    }

    /**
     * LoadItems, loads items
     */
    public void loadItems(GameWorld world) throws Exception {
        String query = "SELECT * FROM items";
        ResultSet resultSet = world.getSqlConnection().createStatement().executeQuery(query);
        while (resultSet.next()) {
            Item item = new Item();
            item.setItemID(resultSet.getInt("item_id"));
            item.setTemplateID(resultSet.getInt("item_template_id"));
            item.setTemplate(this.getTemplate(item.getTemplateID()));
            if (item.getTemplate() == null) {
                continue;
            }

            // something went wrong, continue
            // need to log later
            item.setName(resultSet.getString("item_name"));
            item.setDescription(resultSet.getString("item_description"));
            item.setBaseStats(new AttributeSet());
            item.getBaseStats().setHP(resultSet.getInt("player_hp"));
            item.getBaseStats().setMP(resultSet.getInt("player_mp"));
            item.getBaseStats().setSP(resultSet.getInt("player_sp"));
            item.getBaseStats().setAC(resultSet.getInt("stat_ac"));
            item.getBaseStats().setStrength(resultSet.getInt("stat_str"));
            item.getBaseStats().setStamina(resultSet.getInt("stat_sta"));
            item.getBaseStats().setIntelligence(resultSet.getInt("stat_int"));
            item.getBaseStats().setDexterity(resultSet.getInt("stat_dex"));
            item.getBaseStats().setFireResist(resultSet.getInt("res_fire"));
            item.getBaseStats().setAirResist(resultSet.getInt("res_air"));
            item.getBaseStats().setEarthResist(resultSet.getInt("res_earth"));
            item.getBaseStats().setSpiritResist(resultSet.getInt("res_spirit"));
            item.getBaseStats().setWaterResist(resultSet.getInt("res_water"));
            item.setWeaponDamage(resultSet.getInt("weapon_damage"));
            item.setValue(resultSet.getLong("item_value"));
            item.setGraphicTile(resultSet.getInt("graphic_tile"));
            item.setGraphicEquipped(resultSet.getInt("graphic_equip"));
            item.setGraphicR(resultSet.getInt("graphic_r"));
            item.setGraphicG(resultSet.getInt("graphic_g"));
            item.setGraphicB(resultSet.getInt("graphic_b"));
            item.setGraphicA(resultSet.getInt("graphic_a"));
            item.setBodyState(resultSet.getInt("body_state"));
            item.loadTemplate(item.getTemplate());
            item.setDirty(false);
            item.setIsBound(("0".equals(resultSet.getString("bound")) ? false : true));
            item.setUnsaved(false);
            this.items.put(item.getItemID(), item);
            if (item.getItemID() >= this.getCurrentID()) {
                this.setCurrentID(item.getItemID() + 1);
            }

        }
        resultSet.close();
    }

    /**
     * ItemCount, returns item count
     */
    public int getItemCount() throws Exception {
        return this.items.size();
    }

    /**
     * AddItem, adds an item to the handler
     * <p>
     * What it does is adds the item to the newitems list, which temporarily holds the item until the
     * ItemHandler is called to save the items to database. So once it has an ID it gets put into the
     * items hashtable.
     */
    public void addItem(Item item) throws Exception {
        item.setItemID(this.getCurrentID());
        this.setCurrentID(this.getCurrentID() + 1);
        this.newitems.add(item);
    }

    /**
     * GetGold, returns item for gold
     */
    public Item getGold() throws Exception {
        return this.items.get(GameSettings.getDefault().getItemIDStartpoint() + GameSettings.getDefault().getGoldItemID());
    }

    /**
     * Save, saves items TODO - Concurrent Modification - Should be fixed with looping through locally created lists instead of the globally created lists.
     */
    public Thread save(GameWorld world) throws Exception {
        Thread t = new Thread(() -> {
            try {
                List<Integer> remove = new ArrayList<>();
                ArrayList<Item> itemsCopy = new ArrayList<>(items.values());
//                for (Item item : this.items.values()) {
                for (Item item : itemsCopy) {
                    if (item.getDelete()) {
                        item.deleteItem(world);
                        remove.add(item.getItemID());
                    } else if (item.getDirty()) {
                        item.saveItem(world);
                    }

                }
                for (int id : remove) {
//                    removeItem(id);
                    this.items.remove(id);
                }

                ArrayList<Item> newItemsCopy = new ArrayList<>(newitems);
//                for (Item item : this.newitems) {
                for (Item item : newItemsCopy) {
                    if (item.getDelete()) continue;

                    if (item.getUnsaved()) item.addItem(world);

                    this.items.put(item.getItemID(), item);
                }
                this.newitems.clear();
                this.addSaveEvent(world);
            } catch (Exception e) {
                Logger.INSTANCE.println(e);
            }
        });
        return t;
    }

    public synchronized void removeItem(int id){
        this.items.remove(id);
    }

    /**
     * AddSaveEvent, adds save event to eventhandler
     */
    public void addSaveEvent(GameWorld world) throws Exception {
        ItemSaveEvent ev = new ItemSaveEvent();
        ev.setTicks(ev.getTicks() + (GameSettings.getDefault().getItemSavePeriod() * world.getTimerFrequency()));
        world.getEventHandler().addEvent(ev);
    }

    /**
     * GetItem, returns item by id
     */
    public Item getItem(int id) throws Exception {
        Item item = null;
        item = (Item) this.items.get(id);
        if (item == null) {
            for (Item i : this.newitems) {
                if (i.getItemID() == id) return i;

            }
        }

        return item;
    }

}
