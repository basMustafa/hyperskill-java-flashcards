package flashcards;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FlashCardManager {

    private final List<FlashCard> flashCards;
    private final Scanner scanner;
    private final List<String> log;
    private String importFile;
    private String exportFile;

    public FlashCardManager() {
        this.flashCards = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.log = new ArrayList<>();
    }

    public void add() {
        printAndAddToLog("The card:");
        String term = logAndReturn(scanner.nextLine());

        if (flashCards.stream().anyMatch(x -> x.getTerm().equals(term))) {
            printAndAddToLog(String.format("The card \"%s\" already exists.%n", term));
            return;
        }

        printAndAddToLog("The definition of the card:");
        String definition = logAndReturn(scanner.nextLine());

        if (flashCards.stream().anyMatch(x -> x.getDefinition().equals(definition))) {
            printAndAddToLog(String.format("The definition \"%s\" already exists.%n", definition));
            return;
        }

        flashCards.add(new FlashCard(term, definition, 0));
        printAndAddToLog(String.format("The pair (\"%s\":\"%s\") has been added.%n", term, definition));
    }

    public void remove() {
        printAndAddToLog("Which card?");
        String term = logAndReturn(scanner.nextLine());
        if (flashCards.removeIf(x -> x.getTerm().equals(term))) {
            printAndAddToLog("The card has been removed.");
        } else {
            printAndAddToLog(String.format("Can't remove \"%s\": there is no such card.%n", term));
        }
    }

    public void startImport() {
        printAndAddToLog("File name:");
        String fileName = logAndReturn(scanner.nextLine());
        setImportFile(fileName);
        importCards();
    }

    public void importCards() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(importFile))) {
            String line = bufferedReader.readLine();
            int counter = 0;
            while (line != null) {
                counter++;
                String[] cardInfo = line.split(":");
                FlashCard newCard = new FlashCard(cardInfo[0], cardInfo[1], Integer.parseInt(cardInfo[2]));
                flashCards.removeIf(e -> newCard.getTerm().equals(e.getTerm()));
                flashCards.add(newCard);
                line = bufferedReader.readLine();
            }
            printAndAddToLog(counter + " cards have been loaded.");
        } catch (FileNotFoundException e) {
            printAndAddToLog("File not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startExport() {
        printAndAddToLog("File name:");
        String fileName = logAndReturn(scanner.nextLine());
        setExportFile(fileName);
        exportCards();
    }

    public void exportCards() {
        String format = "%s:%s:%s" + System.lineSeparator();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(exportFile))) {
            for (FlashCard card : flashCards) {
                bufferedWriter.write(String.format(format,
                        card.getTerm(),
                        card.getDefinition(),
                        card.getErrorCounter()));
            }
            printAndAddToLog(flashCards.size() + " cards have been saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ask() {
        printAndAddToLog("How many times to ask?");
        int num = logAndReturn(scanner.nextInt());
        scanner.nextLine();
        for (FlashCard card : flashCards) {
            if (num == 0) {
                return;
            }
            printAndAddToLog(String.format("Print the definition of \"%s\":%n", card.getTerm()));
            String definition = logAndReturn(scanner.nextLine());
            if (definition.equals(card.getDefinition())) {
                printAndAddToLog("Correct!");
            } else if (flashCards.stream().anyMatch(x -> x.getDefinition().equals(definition))) {
                card.incrementErrorCounter();
                FlashCard flashCard = flashCards.stream().
                        filter(x -> x.getDefinition().equals(definition)).
                        findAny().
                        orElse(null);
                printAndAddToLog(String.format("Wrong. The right answer is \"%s\", but your definition is correct for \"%s\".%n",
                        card.getDefinition(), flashCard.getTerm()));
            } else {
                card.incrementErrorCounter();
                printAndAddToLog(String.format("Wrong. The right answer is \"%s\".%n", card.getDefinition()));
            }
            num--;
        }
    }

    public void saveLog() {
        printAndAddToLog("File name:");
        String fileName = logAndReturn(scanner.nextLine());

        File file = new File(fileName);

        try (FileWriter fileWriter = new FileWriter(file)) {
            for (String s : log) {
                fileWriter.write(s + "\n");
            }
        } catch (IOException e) {
            printAndAddToLog("Not written to file");
        }
        printAndAddToLog("The log has been saved.");
    }

    public void hardestCard() {
        int maxErrors = flashCards.stream()
                .filter(card -> card.getErrorCounter() > 0)
                .mapToInt(FlashCard::getErrorCounter)
                .max().orElse(-1);

        List<String> errorList = flashCards.stream()
                .filter(card -> card.getErrorCounter() == maxErrors)
                .map(FlashCard::getTerm)
                .collect(Collectors.toList());

        if (errorList.isEmpty()) {
            printAndAddToLog("There are no cards with errors.");
        } else {
            StringBuilder sb = new StringBuilder()
                    .append("The hardest card")
                    .append(errorList.size() > 1 ? "s are " : " is ");
            for (String term : errorList) {
                sb.append("\"")
                        .append(term)
                        .append("\"")
                        .append(", ");
            }
            sb.deleteCharAt(sb.lastIndexOf(","))
                    .append(". ")
                    .append("You have ")
                    .append(maxErrors)
                    .append(" errors answering ")
                    .append(errorList.size() > 1 ? "them" : "it")
                    .append(".");
            printAndAddToLog(sb.toString());
        }
    }

    public void resetStats() {
        flashCards.forEach(FlashCard::resetErrorCounter);
        printAndAddToLog("Card statistics have been reset.");
    }

    public void printAndAddToLog(String s) {
        System.out.println(s);
        log.add(s);
    }

    public String logAndReturn(String s) {
        log.add(s);
        return s;
    }

    public int logAndReturn(Integer i) {
        log.add(i.toString());
        return i;
    }

    public void setImportFile(String importFile) {
        this.importFile = importFile;
    }

    public void setExportFile(String exportFile) {
        this.exportFile = exportFile;
    }

    public String getExportFile() {
        return exportFile;
    }
}
