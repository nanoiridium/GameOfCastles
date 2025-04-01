package Entities;

import Buildings.BuildingType;
import Buildings.Castle;

import java.util.ArrayList;
import java.util.List;


public class Hero {
    private List<Unit> army = new ArrayList<>();
    private int movementBonus = 0; // бонус от конюшни
    private Castle homeCastle;
    private int maxMovement = 3;
    private int currentMovement;

    private int xCoord;
    private int yCoord;
    private char symbol;

    private int prevxCoord;
    private int prevyCoord;
    private char prevSymbol;

    public Hero(Castle homeCastle, char symbol) {
        this.homeCastle = homeCastle;
        currentMovement = maxMovement;
        this.symbol = symbol;
        prevSymbol = homeCastle.getSymbol();
        xCoord = prevxCoord = homeCastle.getxCoord();
        yCoord = prevyCoord = homeCastle.getyCoord();
    }

    public void addUnit(Unit unit) {
        army.add(unit);
    }
    // Другие методы
    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
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

    public int getPrevxCoord() {
        return this.prevxCoord;
    }

    public int getPrevyCoord() {
        return this.prevyCoord;
    }

    public int getCurrentMovement() {
        return this.currentMovement;
    }

    public char getPrevSymbol() {
        return this.prevSymbol;
    }

    public void move(Direction dir, char symbol)
    {
        if(currentMovement <= 0)
            return;

        currentMovement--;

        prevxCoord = xCoord;
        prevyCoord = yCoord;

        if (prevSymbol != symbol) {
            if (symbol == '+')
                currentMovement *= 2;
        }
        prevSymbol = symbol;



        switch (dir) {
            case UP -> {
                xCoord--;
            }
            case RIGHT -> {
                yCoord++;
            }
            case DOWN -> {
                xCoord++;
            }
            case LEFT -> {
                yCoord--;
            }
            case UPRIGHT -> {
                xCoord--;
                yCoord++;
            }
            case UPLEFT -> {
                xCoord--;
                yCoord--;
            }
            case DOWNLEFT -> {
                xCoord++;
                yCoord--;
            }
            case DOWNRIGHT -> {
                xCoord++;
                yCoord++;
            }
        }
    }

    public void newTurn() {
        currentMovement = maxMovement;
        if (homeCastle.isHeroInCastle(this) && homeCastle.isHaveBuilding(BuildingType.STABLE)) {
            currentMovement += movementBonus;
        }
        for (Unit unit : army) {
            unit.newTurn();
        }
    }

    public List<Unit> getArmy() {
        return army;
    }

    public Castle getHomeCastle() {
        return homeCastle;
    }

    public void buyUnit(UnitType type, int amount) {
        int price = amount * (new Unit(this, type, 1).getPrice());
        if(price > homeCastle.getGold())
            return;
        homeCastle.addGold(-price);

        if (army.stream().anyMatch(b -> b.getType().equals(type))) {
            for (Unit unit : army) {
                if (unit.getType().equals(type)) {
                    unit.setAmount(unit.getAmount() + amount);
                    break;
                }
            }
        } else {
            Unit unit = new Unit(this, type, amount);
            army.add(unit);
        }
    }

    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder("<" + this.symbol + ">Army:[ ");
        for (Unit unit : army) {
            output.append(unit.toString()).append("(").append(unit.getAmount()).append(".").append(unit.getCurrentHealth()).append(") ");
        }
        output.append("]");
        return output.toString();
    }

    public void damageFromTheWall(Castle enemy)
    {
        ElectroWall wall = enemy.getElectroWall();
        for (Unit unit : army) {
            unit.acceptDamage(wall.getDamage());
        }
        removeDeadUnits();
    }

    public void removeDeadUnits()
    {
        army.removeIf(Unit::isDead);
    }

    public boolean isDead()
    {
        return army.isEmpty();
    }
}