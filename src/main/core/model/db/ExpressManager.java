package core.model.db;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class ExpressManager {
    private static LinkedHashSet<String> expressNames = new LinkedHashSet<String>();

    private static ArrayList<Express> expressList = new ArrayList<Express>();//map convertible en arrayList? accès immédiat à fonction

    private static ArrayList<Express> expressGraph = new ArrayList<Express>();//map convertible en arrayList? accès immédiat à fonction

    public static ArrayList<Express> getExpressList() {
        return expressList;
    }

    public static ArrayList<Express> getExpressGraph() {
        return expressGraph;
    }

    public static ArrayList<String> getExpressNames() {
        return new ArrayList<String>(expressNames);
    }

    public static void update(Express expression, String oldName, String newName) {
        if (!newName.equals(oldName)) {
            if (expressNames.contains(newName) || newName.equals("fonction")) {//nom réservé pour ajout ligne
                System.out.println("créer une exception \"already used name\" et faire pop une fenêtre d'erreur?");
            } else {
                expressNames.add(newName);
                expressNames.remove(oldName);
                expression.setName(newName);
            }
        }
    }

    public static void addExpress(String name, String function) {
        if (expressNames.contains(name)) {
            System.out.println("créer une exception \"already used name\" et faire pop une fenêtre d'erreur?");
        }
        else {
            expressNames.add(name);
            expressList.add(new Express(name, function));
        }
    }

    public static void addGraph(String name) {
        if (expressNames.contains(name)) {
            Express expression = new Express();
            for (Express element : expressList) {
                if (element.getName().equals(name)) {
                    expression = element;
                    break;
                }
            }

            if (!expressGraph.contains(expression)) {
                expression.setIsActive(true);//activation automatique en paramètre généraux...
                expression.setSampling(10);//valeur par défaut
                expression.setColor(Color.BLACK);//valeur par défaut

                expressGraph.add(expression);
            }
        }
        else {
            //wut
        }
    }

    public static void addExpressNames(String name) {
        expressNames.add(name);
    }
}
