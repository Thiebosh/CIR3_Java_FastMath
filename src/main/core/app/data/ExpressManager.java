package core.app.data;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manager des instances de Express
 * @see Express
 */
public class ExpressManager {
    /**
     * La liste de l'ensemble des expressions chargées en mémoire
     * @see Express
     */
    private static HashMap<String, Express> allExpress = new HashMap<>();
    /**
     * La liste des noms des expressions affichées graphiquement
     * @see core.app.view.scene.GraphicController
     */
    private static LinkedHashSet<String> graphExpress = new LinkedHashSet<>();

    /**
     * Le mode des expressions (degrés ou radians)
     */
    private static boolean isDegree = true;

    public static void setDegree(final boolean b){ isDegree = b; }

    public static boolean getDegree(){ return isDegree; }


    public static void load() {
        try {
            File file = new File("./src/main/resources/save.csv");
            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                String[] line = sc.nextLine().split(";");
                addToExpressList(line[0], line[1]);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/resources/save.csv"));
            writer.write("");

            ArrayList<String> nameList = getExpressNames();
            Collections.sort(nameList);
            for (String key : nameList) {
                writer.append(key).append(";").append(allExpress.get(key).getFunction()).append("\n");
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet de savoir si le nom est associé à une expression
     * @param name nom à tester
     * @return true si le nom est utilisé, false sinon
     */
    public static boolean containsExpress(final String name) {
        return allExpress.containsKey(name);
    }

    public static String replaceExpressNameByFunctionRecursively(String function) {
        int secu = 0;
        while(function != refactorWithExpress(function)) {
            function = refactorWithExpress(function);
            if(secu++ >= 50) {
                System.out.println("Créer exception : récursivité au sein d'une fonction");
                function = "0";
                break;
            }
        }
        return function;
    }

    private static String refactorWithExpress(String name) {
        Map<String, Express> treeMap = new TreeMap<>(
                (string1, string2) -> {
                    if (string1.length() > string2.length()) {
                        return -1;
                    } else if (string1.length() < string2.length()) {
                        return 1;
                    } else {
                        return string1.compareTo(string2);
                    }
                });
        treeMap.putAll(allExpress);
        for(int i = 0; i < treeMap.size(); i++)
        {
            String key = String.valueOf(treeMap.keySet().toArray()[i]);
            if(name.contains(key))
            {
                name = name.replace(key, treeMap.get(key).getFunction());
            }
        }
        return name;
    }

    /**
     * Getter d'une expression spécifique
     * @param name nom de l'expresson que l'on veut récupérer
     * @return l'expression si elle existe, sinon null
     */
    public static Express getExpress(final String name) {
        return allExpress.get(name);
    }

    /**
     * Getter de la liste de toutes les expression sous forme d'ArrayList itérable
     * @return ArrayList contenant toutes les expression
     */
    public static ArrayList<Express> getExpressList() {
        return new ArrayList<>(allExpress.values());
    }

    /**
     * Getter de la liste des noms de toutes les expressions sous forme d'ArrayList itérable
     * @return ArrayList contenant tous les noms d'expression
     */
    public static ArrayList<String> getExpressNames() {
        return new ArrayList<>(allExpress.keySet());
    }

    /**
     * Getter de la liste des expressions utilisées par le graphe sous forme d'ArrayList itérable
     * @return ArrayList contenant les expression utilisées par le graphe
     * @see core.app.view.scene.GraphicController
     */
    public static ArrayList<Express> getExpressGraphList() {
        return new ArrayList<>(graphExpress.stream().map(allExpress::get).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    /**
     * Supprimer toutes les instances enregistrées : libère la mémoire
     */
    public static void clearAll() {
        allExpress.clear();
        graphExpress.clear();
    }

    /**
     * Créer une nouvelle instance d'express et l'ajouter à la liste totale
     * @see Express
     * @param name nom désignant l'expression
     * @param function fonction associée au nom
     */
    public static void addToExpressList(final String name, final String function) {
        if (allExpress.containsKey(name)) {
            System.out.println("créer une exception \"already used name\" et faire pop une fenêtre d'erreur?");
            return;
        }

        allExpress.put(name, new Express(name, function));
    }

    /**
     * Ajouter une expression à la liste du graphe en vérifiant qu'elle existe
     * @param name nom de l'expression à ajouter
     */
    public static void addToGraphList(final String name) {
        if (!allExpress.containsKey(name)) {
            System.out.println("créer une exception \"unknown function name\" et faire pop une fenêtre d'erreur?");
            return;
        }

        allExpress.get(name).setIsActive(true);//if added but disabled, enable
        graphExpress.add(name);//set so no verification needed
    }

    public static void removeFromExpressList(final String name) {
        if (graphExpress.contains(name)) graphExpress.remove(name);
        if (allExpress.containsKey(name)) allExpress.remove(name);
    }

    public static void removeFromGraphList(final String name) {
        if (graphExpress.contains(name)) graphExpress.remove(name);
    }

    /**
     * Modifier le nom d'une expression existante dans l'instance de l'expression et dans les listes de stockage
     * @param oldName nom de l'expression à modifier
     * @param newName nom à appliquer à l'expression
     */
    public static void renameExpress(final String oldName, final String newName) {
        if (newName.equals(oldName)) return;//no modification

        if (newName.equals("fonction") /* || sin || cos || ...*/) {//nom réservé pour ajout ligne
            System.out.println("créer une exception \"reserved name\" et faire pop une fenêtre d'erreur?");
            return;
        }

        if (allExpress.containsKey(newName)) {
            System.out.println("créer une exception \"already used name\" et faire pop une fenêtre d'erreur?");
            return;
        }

        allExpress.get(oldName).setName(newName);
        allExpress.put(newName, allExpress.get(oldName));
        allExpress.remove(oldName);

        for (String key : allExpress.keySet()) {
            //verifier verif que oldName entouré de signes (ne remplace pas qu'une partie, ex i dans sin) : regex?
            String newFunction = allExpress.get(key).getFunction().replace(oldName, newName);
            allExpress.get(key).setFunction(newFunction);
        }

        if (graphExpress.contains(oldName)) {
            graphExpress.remove(oldName);
            graphExpress.add(newName);
        }
    }
}
