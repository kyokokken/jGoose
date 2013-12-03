package Goose;

import Goose.ITile;
import Goose.Map;

public class WarpTile implements ITile {
  private Map __WarpMap;

  public Map getWarpMap() {
    return __WarpMap;
  }

  public void setWarpMap(Map value) {
    __WarpMap = value;
  }

  private int __WarpX;

  public int getWarpX() {
    return __WarpX;
  }

  public void setWarpX(int value) {
    __WarpX = value;
  }

  private int __WarpY;

  public int getWarpY() {
    return __WarpY;
  }

  public void setWarpY(int value) {
    __WarpY = value;
  }

}
