package Goose;

import Goose.ItemTemplate;

import java.util.HashMap;
import java.util.List;

public class Combination {
  private int __ID;

  public int getID() {
    return __ID;
  }

  public void setID(int value) {
    __ID = value;
  }

  private String __Name;

  public String getName() {
    return __Name;
  }

  public void setName(String value) {
    __Name = value;
  }

  private int __MinLevel;

  public int getMinLevel() {
    return __MinLevel;
  }

  public void setMinLevel(int value) {
    __MinLevel = value;
  }

  private int __MaxLevel;

  public int getMaxLevel() {
    return __MaxLevel;
  }

  public void setMaxLevel(int value) {
    __MaxLevel = value;
  }

  private long __MinExperience;

  public long getMinExperience() {
    return __MinExperience;
  }

  public void setMinExperience(long value) {
    __MinExperience = value;
  }

  private long __MaxExperience;

  public long getMaxExperience() {
    return __MaxExperience;
  }

  public void setMaxExperience(long value) {
    __MaxExperience = value;
  }

  private long __ClassRestrictions;

  public long getClassRestrictions() {
    return __ClassRestrictions;
  }

  public void setClassRestrictions(long value) {
    __ClassRestrictions = value;
  }

  private List<ItemTemplate> __ResultItems;

  public List<ItemTemplate> getResultItems() {
    return __ResultItems;
  }

  public void setResultItems(List<ItemTemplate> value) {
    __ResultItems = value;
  }

  private HashMap<Integer, Integer> __RequiredHash;

  public HashMap<Integer, Integer> getRequiredHash() {
    return __RequiredHash;
  }

  public void setRequiredHash(HashMap<Integer, Integer> value) {
    __RequiredHash = value;
  }

}
