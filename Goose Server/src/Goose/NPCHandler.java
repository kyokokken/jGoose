package Goose;

import Goose.AttributeSet;
import Goose.GameSettings;
import Goose.NPCDropInfo;
import Goose.NPCTemplate;
import Goose.NPCVendorSlot;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * NPCHandler, loads/holds npcs
 * 
 */
public class NPCHandler {
  List<NPCTemplate> templates;
  List<Goose.NPC> npcs;
  Hashtable<Integer, Goose.NPC> idToNPC;

  /**
   * Constructor
   * 
   */
  public NPCHandler() throws Exception {
    this.templates = new ArrayList<NPCTemplate>();
    this.npcs = new ArrayList<Goose.NPC>();
    this.idToNPC = new Hashtable<Integer, Goose.NPC>();
  }

  /**
   * LoadNPCTemplates, loads npc templates from database
   * 
   */
  public void loadNPCTemplates(GameWorld world) throws Exception {
    ResultSet resultSet =
        world.getSqlConnection().createStatement().executeQuery("SELECT * FROM npc_templates");
    while (resultSet.next()) {
      NPCTemplate npc = new NPCTemplate();

      npc.setNPCTemplateID(resultSet.getInt("npc_id"));
      npc.setNPCType(Goose.NPCTemplate.Types.values()[resultSet.getInt("npc_type")]);
      npc.setName(resultSet.getString("npc_name"));
      npc.setTitle(resultSet.getString("npc_title"));
      npc.setSurname(resultSet.getString("npc_surname"));
      npc.setRespawnTime(resultSet.getInt("respawn_time"));
      npc.setFacing(resultSet.getInt("npc_facing"));
      npc.setLevel(resultSet.getInt("npc_level"));
      npc.setExperience(resultSet.getLong("experience"));
      npc.setWeaponDamage(resultSet.getInt("weapon_damage"));
      npc.setAggroRange(resultSet.getInt("aggro_range"));
      npc.setAttackRange(resultSet.getInt("attack_range"));
      npc.setAttackSpeed(Double.parseDouble(resultSet.getString("attack_speed")));
      npc.setMoveSpeed(Double.parseDouble(resultSet.getString("move_speed")));
      npc.setCanMove(("1".equals(resultSet.getString("stationary")) ? false : true));
      npc.setCanBeStunned(("0".equals(resultSet.getString("stunnable")) ? false : true));
      npc.setCanBeRooted(("0".equals(resultSet.getString("rootable")) ? false : true));
      npc.setCanBeSlowed(("0".equals(resultSet.getString("slowable")) ? false : true));
      npc.setCanBeKilled(("1".equals(resultSet.getString("invincible")) ? false : true));
      npc.setClassID(resultSet.getInt("class_id"));
      npc.setEquippedItems(resultSet.getString("equipped_items"));
      npc.setBodyState(resultSet.getInt("body_state"));
      npc.setBodyID(resultSet.getInt("body_id"));
      npc.setFaceID(resultSet.getInt("face_id"));
      npc.setHairID(resultSet.getInt("hair_id"));
      npc.setHairR(resultSet.getInt("hair_r"));
      npc.setHairG(resultSet.getInt("hair_g"));
      npc.setHairB(resultSet.getInt("hair_b"));
      npc.setHairA(resultSet.getInt("hair_a"));
      npc.setBaseStats(new AttributeSet());
      npc.getBaseStats().setHP(resultSet.getInt("npc_hp"));
      npc.getBaseStats().setMP(resultSet.getInt("npc_mp"));
      npc.getBaseStats().setSP(resultSet.getInt("npc_sp"));
      npc.getBaseStats().setAC(resultSet.getInt("stat_ac"));
      npc.getBaseStats().setStrength(resultSet.getInt("stat_str"));
      npc.getBaseStats().setStamina(resultSet.getInt("stat_sta"));
      npc.getBaseStats().setIntelligence(resultSet.getInt("stat_int"));
      npc.getBaseStats().setDexterity(resultSet.getInt("stat_dex"));
      npc.getBaseStats().setFireResist(resultSet.getInt("res_fire"));
      npc.getBaseStats().setAirResist(resultSet.getInt("res_air"));
      npc.getBaseStats().setEarthResist(resultSet.getInt("res_earth"));
      npc.getBaseStats().setSpiritResist(resultSet.getInt("res_spirit"));
      npc.getBaseStats().setWaterResist(resultSet.getInt("res_water"));
      npc.getBaseStats().setHPPercentRegen(
          Double.parseDouble(resultSet.getString("hp_percent_regen")));
      npc.getBaseStats().setHPStaticRegen(resultSet.getInt("hp_static_regen"));
      npc.getBaseStats().setMPPercentRegen(
          Double.parseDouble(resultSet.getString("mp_percent_regen")));
      npc.getBaseStats().setMPStaticRegen(resultSet.getInt("mp_static_regen"));
      npc.setAlliesString(resultSet.getString("npc_alliance"));
      npc.setAllies(new ArrayList<NPCTemplate>());
      npc.setBehaviour(Goose.NPCTemplate.BehaviourTypes.values()[resultSet
          .getInt("stuck_behaviour")]);
      npc.setBehaviourTimeout(resultSet.getLong("stuck_timeout"));
      this.templates.add(npc);
    }
    resultSet.close();

    for (NPCTemplate npc : this.templates) {
      for (String ally : npc.getAlliesString().split(" ")) {
        try {
          NPCTemplate a = this.getNPCTemplate(Integer.valueOf(ally));
          if (a == null) {} else {
            // log bad template id in allies
            npc.getAllies().add(a);
          }
        } catch (Exception __dummyCatchVar0) {}

      }
    }
    for (NPCTemplate template : this.templates) {
      String query = "SELECT * FROM npc_drops WHERE npc_template_id=" + template.getNPCTemplateID();
      resultSet = world.getSqlConnection().createStatement().executeQuery(query);
      template.setDrops(new ArrayList<NPCDropInfo>());
      while (resultSet.next()) {
        NPCDropInfo drop = new NPCDropInfo();
        drop.setDropRate(Double.parseDouble(resultSet.getString("droprate")));
        drop.setStack(resultSet.getInt("stack"));
        drop.setItemTemplate(world.getItemHandler().getTemplate(
            resultSet.getInt("item_template_id")));
        if (drop.getItemTemplate() != null) template.getDrops().add(drop);

      }
      resultSet.close();
      query = "SELECT * FROM npc_vendor_items WHERE npc_template_id=" + template.getNPCTemplateID();
      resultSet = world.getSqlConnection().createStatement().executeQuery(query);
      if (resultSet.next()) {
        template
            .setVendorItems(new NPCVendorSlot[GameSettings.getDefault().getVendorSlotSize() + 1]);
        resultSet.previous();
        while (resultSet.next()) {
          NPCVendorSlot vslot = new NPCVendorSlot();
          vslot.setSlot(resultSet.getInt("slot"));
          vslot.setStack(resultSet.getInt("stack"));
          vslot.setItemTemplate(world.getItemHandler().getTemplate(
              resultSet.getInt("item_template_id")));
          vslot.setCanSeeStats(("0".equals(resultSet.getString("stats_visible")) ? false : true));
          if (vslot.getItemTemplate() != null && vslot.getSlot() > 0
              && vslot.getSlot() < GameSettings.getDefault().getVendorSlotSize()) {
            template.getVendorItems()[vslot.getSlot()] = vslot;
          } else {}
        }
      }

      // log bad vendor slot/item
      resultSet.close();
    }

  }

  /**
   * TemplateCount, returns npc template count
   * 
   */
  public int getTemplateCount() throws Exception {
    return this.templates.size();
  }

  /**
   * NPCCount, returns npc count
   * 
   */
  public int getNPCCount() throws Exception {
    return this.npcs.size();
  }

  /**
   * Gets NPCTemplate object from npc_id
   */
  public NPCTemplate getNPCTemplate(int npc_id) throws Exception {
    for (NPCTemplate template : this.templates) {
      if (template.getNPCTemplateID() == npc_id) return template;

    }
    return null;
  }

  /**
   * GetNewID, returns new login id for npc
   */
  public int getNewID() throws Exception {
    for (int i = GameSettings.getDefault().getMaxPlayers() + 1; i < GameSettings.getDefault()
        .getMaxNPCs(); i++) {
      if (this.idToNPC.get(i) == null) return i;

    }
    return 0;
  }

  /**
   * LoadNPCs, loads npc spawns from database
   * 
   */
  public void loadNPCs(GameWorld world) throws Exception {
    String query = "SELECT * FROM npc_spawns";
    ResultSet resultSet = world.getSqlConnection().createStatement().executeQuery(query);
    while (resultSet.next()) {
      Goose.NPC npc = new Goose.NPC();
      int map_id, map_x, map_y;
      int npc_id = resultSet.getInt("npc_id");
      map_id = resultSet.getInt("map_id");
      map_x = resultSet.getInt("map_x");
      map_y = resultSet.getInt("map_y");
      NPCTemplate template = this.getNPCTemplate(npc_id);
      if (template != null) {
        if (npc.loadFromTemplate(world, map_id, map_x, map_y, template)) {
          this.npcs.add(npc);
          npc.setLoginID(this.getNewID());
          this.idToNPC.put(npc.getLoginID(), npc);
        } else {}
      } else {}
    }
    // couldn't load map
    // log bad id
    resultSet.close();
  }

}
