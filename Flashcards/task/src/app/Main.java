package app;

public class Main {
    public static void main(String[] args) {
        String importFile = "";
        String exportFile = "";

        for (int i = 0; i < args.length; i++) {
            importFile = "-import".equals(args[i]) ? args[i + 1] : importFile;
            exportFile = "-export".equals(args[i]) ? args[i + 1] : exportFile;
        }

        UserInterface.manager.setImportFile(importFile);
        UserInterface.manager.setExportFile(exportFile);

        if (!importFile.isEmpty()) {
            UserInterface.manager.importCards();
        }

        UserInterface.displayMenu();
    }
}
