package GameLogic;

import java.io.IOException;
import java.util.List;
import jline.console.ConsoleReader;

import Entities.Hero;
import Entities.Unit;
import Entities.UnitType;
import Entities.Direction;
import Buildings.Castle;
import GameMap.GameMap;
import Buildings.Building;
import Buildings.BuildingType;

public class GameLogic {
    private GameMap map; //Объект карты
    private Castle playerCastle; //Объект замка/фракции игрока
    private Castle computerCastle; //Объект замка/фракции компьютера
    private BattleMap battleMap; //Объект карты сражения
    private boolean isPlayerTurn = true;
    private int choosenHero = 0;
    ConsoleReader console;
    private BotAI bot; //Объект действий компьютера
    private boolean isBattle = false;
    int gameEndCountdown = 2;
    private Castle winner = null;


    public Castle startGame() throws IOException, InterruptedException {
        initializeGame(); //Старт игры
        map.updateMap(playerCastle, computerCastle);
        map.printMap();
        while (!isGameOver()) {
            if (isPlayerTurn) {
                playerTurn();
                map.updateMap(playerCastle, computerCastle);
                map.printMap();
            } else {
                computerTurn();
                map.updateMap(playerCastle, computerCastle);
                map.printMap();
            }
            isPlayerTurn = !isPlayerTurn;
        }
        return winner;
    }

    private void playerTurn() throws IOException {
        String input;
        // Обработка действий игрока
        do {
            if(isBattle) { //Битва или нет
                battleMap.startFight();
                Hero loser = battleMap.getLoser();
                loser.getHomeCastle().getHeroes().remove(loser); //Удаляем проигравшего героя из замка
                isBattle = false;
                map.updateMap(playerCastle, computerCastle);
                map.printMap();
            }
            map.updateMap(playerCastle, computerCastle);
            map.printMap();
            List<Hero> heroes = playerCastle.getHeroes();
            List<Hero> enemies = computerCastle.getHeroes();

            console.println("Current Heroes: ");
            for (Hero hero : heroes) { //Выводим на экран героев игрока
                console.print(hero.getSymbol() + " ");
            }
            console.println();

            console.println("Enemy Heroes: ");
            for (Hero hero : enemies) { //Выводим на экран героев компьютера
                console.print(hero.getSymbol() + " ");
            }
            console.println();

            if(!heroes.isEmpty()) {
                console.print("Selected hero: " + heroes.get(choosenHero).getSymbol());
                console.println();
                console.println(heroes.get(choosenHero).toString());
                input = console.readLine(
                    """
                            (Q - next turn)
                            (R - next hero)
                            (W - up)
                            (A - left)
                            (S - down)
                            (D - right)
                            (WD - up right)
                            (WA - up left)
                            (SA - down left)
                            (SD - down right)
                            (B - buy building)
                            (U - buy hero)
                            (Y - hire army)
                            action>""");
                chooseAction(heroes.get(choosenHero), input); //Выбор действия
            }
            else {
                console.println("No heroes! Buy Tavern and hire Entities.Hero!");
                input = console.readLine(
                    """
                            Choose your action:
                            (Q - next turn)
                            (B - buy building)
                            (U - buy hero)
                            action>""");
                chooseAction(null, input); //Выбор действия
            }

            map.updateMap(playerCastle, computerCastle);
            map.printMap();

        } while(!input.equals("q"));
        playerCastle.newTurn();
    }

    private void chooseAction(Hero hero, String input) throws IOException {
        int size = map.getSize();
        if(hero != null) {
            int x = hero.getxCoord();
            int y = hero.getyCoord();
            Hero enemy = null;
            switch (input) {
                case "w" -> {
                    map.setSymbol(x, y, hero.getPrevSymbol()); //Запоминаем символ под игроком
                    if (x - 1 >= 0)
                        if ((enemy = isBattle(computerCastle, x - 1, y)) == null) //Проверяем будет ли битва по направлению движения
                            hero.move(Direction.UP, map.getSymbol(x - 1, y)); //Битвы нет, идем
                        else {
                            isBattle = true; //Битва есть, алярм!
                            battleMap = new BattleMap(9, hero, enemy); //Начинаем замес
                        }

                }
                case "a" -> {
                    map.setSymbol(x, y, hero.getPrevSymbol());
                    if (y - 1 >= 0)
                        if ((enemy = isBattle(computerCastle, x, y - 1)) == null)
                            hero.move(Direction.LEFT, map.getSymbol(x, y - 1));
                        else {
                            isBattle = true;
                            battleMap = new BattleMap(9, hero, enemy);
                        }
                }
                case "s" -> {
                    map.setSymbol(x, y, hero.getPrevSymbol());
                    if (x + 1 < size)
                        if ((enemy = isBattle(computerCastle, x + 1, y)) == null)
                            hero.move(Direction.DOWN, map.getSymbol(x + 1, y));
                        else {
                            isBattle = true;
                            battleMap = new BattleMap(9, hero, enemy);
                        }
                }
                case "d" -> {
                    map.setSymbol(x, y, hero.getPrevSymbol());
                    if (y + 1 < size)
                        if ((enemy = isBattle(computerCastle, x, y + 1)) == null)
                            hero.move(Direction.RIGHT, map.getSymbol(x, y + 1));
                        else {
                            isBattle = true;
                            battleMap = new BattleMap(9, hero, enemy);
                        }
                }
                case "wd" -> {
                    map.setSymbol(x, y, hero.getPrevSymbol());
                    if (x - 1 >= 0 && y + 1 < size)
                        if ((enemy = isBattle(computerCastle, x - 1, y + 1)) == null)
                            hero.move(Direction.UPRIGHT, map.getSymbol(x - 1, y + 1));
                        else {
                            isBattle = true;
                            battleMap = new BattleMap(9, hero, enemy);
                        }
                }
                case "wa" -> {
                    map.setSymbol(x, y, hero.getPrevSymbol());
                    if (x - 1 >= 0 && y - 1 >= 0)
                        if ((enemy = isBattle(computerCastle, x - 1, y - 1)) == null)
                            hero.move(Direction.UPLEFT, map.getSymbol(x - 1, y - 1));
                        else {
                            isBattle = true;
                            battleMap = new BattleMap(9, hero, enemy);
                        }
                }
                case "sa" -> {
                    map.setSymbol(x, y, hero.getPrevSymbol());
                    if (x + 1 < size && y - 1 >= 0)
                        if ((enemy = isBattle(computerCastle, x + 1, y - 1)) == null)
                            hero.move(Direction.DOWNLEFT, map.getSymbol(x + 1, y - 1));
                        else {
                            isBattle = true;
                            battleMap = new BattleMap(9, hero, enemy);
                        }
                }
                case "sd" -> {
                    map.setSymbol(x, y, hero.getPrevSymbol());
                    if (x + 1 < size && y + 1 < size)
                        if ((enemy = isBattle(computerCastle, x + 1, y + 1)) == null)
                            hero.move(Direction.DOWNRIGHT, map.getSymbol(x + 1, y + 1));
                        else {
                            isBattle = true;
                            battleMap = new BattleMap(9, hero, enemy);
                        }
                }
                case "b" -> {
                    BuildingType b;
                    if ((b = chooseBuilding()) != null) {
                        playerCastle.buyBuilding(b);
                    }
                }
                case "u" -> {
                    if (playerCastle.canHireHero(computerCastle)) { //Не может быть двух героев в замке
                        String name = console.readLine("Name your hero: ");
                        playerCastle.hireHero(name.charAt(0));
                    }
                }
                case "r" -> { //Выбираем героя
                    choosenHero++;
                    if (choosenHero >= playerCastle.getHeroes().size())
                        choosenHero = 0;
                }
                case "y" -> { //Нанимаем армию
                    if (playerCastle.isHeroInCastle(hero)) {
                        hireArmy(playerCastle, hero);
                    }
                }

                default -> {
                    break;
                }
            }
        } else
        {
            switch (input) {
                case "b" -> {
                    BuildingType b;
                    if ((b = chooseBuilding()) != null) {
                        playerCastle.buyBuilding(b);
                    }
                }
                case "u" -> {
                    if (playerCastle.canHireHero(computerCastle)) {
                        String name = console.readLine("Name your hero: ");
                        playerCastle.hireHero(name.charAt(0));
                    }
                }
                default -> {
                    break;
                }
            }
        }
    }

    private BuildingType chooseBuilding() throws IOException { //Выбираем что построить
        console.flush();
        console.clearScreen();
        console.println("You have " + playerCastle.getGold() + " gold!");
        List<Building> buildings = playerCastle.getBuildings();
        console.println("Already built: ");
        for (Building building : buildings) {
            console.println(building.getName());
        }
        String input = console.readLine("""
                (0 - TAVERN)
                (1 - STABLE)
                (2 - BARRACKS)
                (3 - RANGE)
                (4 - SMITH)
                (5 - ACADEMY)
                (6 - CHURCH)
                (anykey - back)
                action>""");
        int index;
        return switch (input) {
            case "0", "1", "2", "3", "4", "5", "6" -> {
                index = Integer.parseInt(input); //Заменяем целвм числом
                yield BuildingType.values()[index]; //Достаём нужный тип здания
            }
            default -> null;
        };
    }

    private boolean hireArmy(Castle castle, Hero hero) throws IOException {
        console.flush();
        console.clearScreen();
        console.println("You have " + playerCastle.getGold() + " gold!");
        List<Unit> army = hero.getArmy();
        console.println("Current Army: ");
        for (Unit unit : army) {
            console.println(unit.getType().toString() + ": " + unit.getAmount());
        }
        String input = console.readLine("(0 - SPEARMAN|cost: " + new Unit(hero, UnitType.SPEARMAN, 1).getPrice() + ")\n" +
                                               "(1 - CROSSBOWMAN|cost: " + new Unit(hero, UnitType.CROSSBOWMAN, 1).getPrice() + ")\n" +
                                               "(2 - SWORDSMAN|cost: " + new Unit(hero, UnitType.SWORDSMAN, 1).getPrice() + ")\n" +
                                               "(3 - CAVALRYMAN|cost: " + new Unit(hero, UnitType.CAVALRYMAN, 1).getPrice() + ")\n" +
                                               "(4 - PALADIN|cost: " + new Unit(hero, UnitType.PALADIN, 1).getPrice() + ")\n" +
                                               "(anykey - back)\n" +
                                               "action>");
        int index;
        UnitType type = switch (input) {
            case "0", "1", "2", "3", "4" -> {
                index = Integer.parseInt(input);
                yield UnitType.values()[index];
            }
            default -> null;
        };

        if (type == null)
            return false;

        if (castle.getBuildings().stream().noneMatch(b -> b.getUnitType() == type)) { //В замке нету нужного здания выбранного юнита
            return false;
        }

        String input_amount = console.readLine("Type amount(Max amount: " + castle.getGold() / (new Unit(hero, type, 1).getPrice()) + "):");
        int amount = Integer.parseInt(input_amount);
        hero.buyUnit(type, amount);

        return true;
    }

    private void computerTurn() throws IOException {
        // Логика бота: атака -> движение -> найм юнитов
        bot.performMapAction();

        if(isBattle) {
            battleMap.startFight();
            Hero loser = battleMap.getLoser();
            loser.getHomeCastle().getHeroes().remove(loser);
            isBattle = false;
            map.updateMap(playerCastle, computerCastle);
            map.printMap();
        }

        map.updateMap(playerCastle, computerCastle);
        map.printMap();
    }

    private boolean isGameOver() {
        if(gameEndCountdown <= 0)
            return true;
        // Проверка условий победы
        for(Hero hero:computerCastle.getHeroes())
            if(playerCastle.isHeroInCastle(hero)) {
                winner = computerCastle;
                gameEndCountdown--;
                return false;
            }
        for(Hero hero:playerCastle.getHeroes())
            if(computerCastle.isHeroInCastle(hero)) {
                winner = playerCastle;
                gameEndCountdown--;
                return false;
            }
        winner = null;
        gameEndCountdown = 2;
        return false;
    }

    public Hero isBattle(Castle enemy, int x, int y) {
        for(Hero enemyHero:enemy.getHeroes())
        {
            if(enemyHero.getxCoord() == x && enemyHero.getyCoord() == y)
                return enemyHero;
        }
        return null;
    }

    private void initializeGame() throws IOException {
        map = new GameMap(11); //Определение карты
        console = new ConsoleReader();
        playerCastle = new Castle(5000, 'P', '1', 0, 0);
        computerCastle = new Castle(5000, 'C', '2', map.getSize()-1, map.getSize()-1);

        map.updateMap(playerCastle, computerCastle); //Расставляет символы на карте
        map.printMap();

        bot = new BotAI(map, computerCastle, this); //Инициализируем бота
    }

    public Castle getPlayerCastle() {
        return playerCastle;
    }

    public Castle getComputerCastle() {
        return computerCastle;
    }

    public void setBattleMap(BattleMap map) {
        battleMap = map;
        isBattle = true;
    }
}
