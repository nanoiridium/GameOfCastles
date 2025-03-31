package GameMap;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import Buildings.Castle;
import Entities.Hero;
import jline.console.ConsoleReader;

public class GameMap {
    private char[][] grid;
    private int size;
    private String clearStr;
    private ConsoleReader console = new ConsoleReader();

    public GameMap(int size) throws IOException {
        this.size = size;
        grid = new char[size][size];
        inititializeClrScr();
        initializeMap(); // генерация препятствий, дороги и замков
    }

    private void inititializeClrScr(){
        char[] clear = new char[size*size];
        Arrays.fill(clear, '\b');
        clearStr = new String(clear);
    }

    private void initializeMap() {
        // Дорога по диагонали
        for (int i = 1; i < size-1; i++) {
            grid[i][i] = '+';
        }

        // Симметричные препятствия
        for (int i = 1; i < size-1; i++) {
            for (int j = 1; j < size-1; j++) {
                if (i != j && (i + j) % 3 == 0) { // Пример условия
                    grid[i][j] = '#';
                    grid[j][i] = '#';
                }
            }
        }
    }

    public void updateMap(Castle playerCastle, Castle computerCastle) {
        initializeMap();
        // Отрисовка героев на карте
        List<Hero> playerHeroes = playerCastle.getHeroes();
        List<Hero> computerHeroes = computerCastle.getHeroes();

        for (Hero hero : playerHeroes) {
            grid[hero.getxCoord()][hero.getyCoord()] = hero.getSymbol();

            grid[playerCastle.getxCoord()][playerCastle.getyCoord()] = playerCastle.getSymbol();
        }

        for (Hero hero : computerHeroes) {
            grid[hero.getxCoord()][hero.getyCoord()] = hero.getSymbol();

            grid[computerCastle.getxCoord()][computerCastle.getyCoord()] = computerCastle.getSymbol();
        }
    }

    public void printMap() throws IOException {
        // Вывод карты в консоль
        console.flush();
        console.clearScreen();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                console.print(grid[i][j] + " ");
            }
            console.println();
        }
    }

    public int getSize(){
        return size;
    }

    public char getSymbol(int x, int y){
        return grid[x][y];
    }

    public void setSymbol(int x, int y, char symbol){
        grid[x][y] = symbol;
    }
}
