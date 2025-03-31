import Buildings.Castle;
import GameLogic.GameLogic;
import jline.console.ConsoleReader;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        GameLogic game = new GameLogic(); //Инициализируем класс логики игры
        Castle winner = game.startGame(); //Запуск игры
        ConsoleReader console = new ConsoleReader();
        console.flush(); //Очистка консоли
        console.clearScreen();
        if (winner != null) { // Проверка кто выиграл
            if (winner.getSymbol() == 'P')//P-замок игрока, C-замок компьютера
                console.readLine("You WIN! Congratulations!");
            else
                console.readLine("You LOSE! Unlucky!");
        }
    }
}