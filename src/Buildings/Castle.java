package Buildings;

import java.util.ArrayList;
import java.util.List;

import Entities.Hero;

public class Castle {
    private List<Building> buildings = new ArrayList<>();
    private List<Hero> heroes = new ArrayList<>();
    private int gold;
    private char symbol;

    private int xCoord;
    private int yCoord;

    private boolean isBuiltThisTurn;

    public Castle(int gold, char castleSymbol, char heroSymbol, int xCoord, int yCoord) {
        this.gold = gold;
        symbol = castleSymbol;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        heroes.add(new Hero(this, heroSymbol));
    }

    public boolean isHeroInCastle() {
        for (Hero hero : heroes) {
            if (hero.getxCoord() == xCoord && hero.getyCoord() == yCoord)
                return true;
        }
        return false;
    }

    public boolean isHeroInCastle(Hero hero) {
        return hero.getxCoord() == xCoord && hero.getyCoord() == yCoord;
    }

    public boolean isEnemyHeroInCastle(Castle castle) {
        for (Hero hero : castle.getHeroes()) {
            if (hero.getxCoord() == xCoord && hero.getyCoord() == yCoord)
                return true;
        }
        return false;
    }

    public boolean isHaveBuilding(BuildingType building) {
        return buildings.stream().anyMatch(b -> b.getType().equals(building));
    }

    public boolean canHireHero(Castle enemy) {
        return !isHeroInCastle() && !isEnemyHeroInCastle(enemy) && isHaveBuilding(BuildingType.TAVERN) && (gold - 100) > 0;
    }

    public void hireHero(char name) {
        gold -= 100;
        heroes.add(new Hero(this, name));
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public int getxCoord() {
        return this.xCoord;
    }

    public int getyCoord() {
        return this.yCoord;
    }

    public char getSymbol() {
        return this.symbol;
    }

    public void buyBuilding(BuildingType type) {
        if (!isBuiltThisTurn) {
            Building building = new Building(type);
            if (buildings.stream().anyMatch(b -> b.getType() == type) || gold - building.getCost() < 0)
                return;
            gold -= building.getCost();
            buildings.add(building);
            isBuiltThisTurn = true;
        }
    }

    public void addGold(int in) {
        this.gold += in;
    }

    public int getGold() {
        return this.gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void newTurn() {
        isBuiltThisTurn = false;
        for (Hero hero : heroes) {
            hero.newTurn();
        }
    }
    // Методы для найма юнитов/героя
}