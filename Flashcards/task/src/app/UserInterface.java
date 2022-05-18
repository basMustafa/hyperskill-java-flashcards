package app;

import flashcards.FlashCardManager;
import java.util.Scanner;

public class UserInterface {
    public static final Scanner scanner = new Scanner(System.in);
    public static FlashCardManager manager = new FlashCardManager();
    public static boolean exit = false;

    public static void displayMenu() {
        while (!exit) {
            String menu = "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):";
            manager.printAndAddToLog(menu);
            String action = manager.logAndReturn(scanner.nextLine());

            switch (action) {
                case "add":
                    manager.add();
                    break;
                case "remove":
                    manager.remove();
                    break;
                case "import":
                    manager.startImport();
                    break;
                case "export":
                    manager.startExport();
                    break;
                case "ask":
                    manager.ask();
                    break;
                case "exit":
                    exit = true;
                    System.out.println("Bye bye!");
                    if (!manager.getExportFile().isEmpty()) {
                        manager.exportCards();
                    }
                    break;
                case "log":
                    manager.saveLog();
                    break;
                case "hardest card":
                    manager.hardestCard();
                    break;
                case "reset stats" :
                    manager.resetStats();
                    break;
            }
        }
    }
}
