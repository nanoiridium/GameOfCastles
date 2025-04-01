package Buildings;

import Entities.UnitType;

public class Building {
    private BuildingType type;
    private UnitType unitType;
    private String name;
    private int cost;
    private int level;

    public Building(BuildingType type) {
        this.type = type;
        switch (type) {
            case TAVERN -> {
                this.cost = 100;
                this.name = "Tavern";
                this.unitType = null;
            }
            case STABLE -> {
                this.cost = 100;
                this.name = "Stable";
                this.unitType = null;
            }
            case BARRACKS -> {
                this.cost = 100;
                this.name = "Barracks";
                this.unitType = UnitType.SPEARMAN;
            }
            case RANGE -> {
                this.cost = 100;
                this.name = "Range";
                this.unitType = UnitType.CROSSBOWMAN;
            }
            case SMITH -> {
                this.cost = 100;
                this.name = "Smith";
                this.unitType = UnitType.SWORDSMAN;
            }
            case ACADEMY -> {
                this.cost = 100;
                this.name = "Academy";
                this.unitType = UnitType.CAVALRYMAN;
            }
            case CHURCH -> {
                this.cost = 100;
                this.name = "Church";
                this.unitType = UnitType.PALADIN;
            }
            case ELECTROHOUSE -> {
                this.cost = 100;
                this.name = "Electrohouse";
                this.unitType = null;
            }
        }
        this.level = 1;
    }
    // Геттеры
    public BuildingType getType() {
        return type;
    }

    public UnitType getUnitType() {
        return unitType;
    }
    
    public int getCost() {
        return cost;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    // Сеттеры
    public void setLevel(int level) {
        this.level = level;
    }

    // Апдейтеры
    public void upgradeBuilding(int level) {
        this.level += level;
    }
}
