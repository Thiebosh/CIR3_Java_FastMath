package core.app.data;

import core.app.error.ErrorCode;
import core.services.mathLibrary.function.FunctionX;
import core.services.windowHolder.StageService;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Manager des instances de Express
 * @see Express
 */
public class ExpressManager {
    /**
     * Constructeur de la classe : crée le dossier de sauvegarde s'il n'existe pas
     */
    ExpressManager() {
        File directory = new File("./src/main/resources/");
        if (! directory.exists()) directory.mkdir();
    }
    /**
     * Chargement en mémoire des expressions à partir du fichier de sauvegarde
     */
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
    /**
     * Enregistrement des expressions en mémoire dans un fichier de sauvegarde
     */
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
    /**
     * Récupérer le mode actif (degré / radian)
     * @return mode actif (true : degré, false : radian)
     */
    public static boolean getDegree(){ return isDegree; }
    /**
     * Modifier le mode actf (degré / radian)
     * @param b mode à activer (true : degré, false : radian)
     */
    public static void setDegree(final boolean b) { isDegree = b; }


    /**
     * Getter de la liste des noms de toutes les expressions sous forme d'ArrayList itérable
     * @return ArrayList contenant tous les noms d'expression
     */
    public static ArrayList<String> getExpressNames() { return new ArrayList<>(allExpress.keySet()); }
    /**
     * Getter de la liste de toutes les expression sous forme d'ArrayList itérable
     * @return ArrayList contenant toutes les expression
     */
    public static ArrayList<Express> getExpressList() { return new ArrayList<>(allExpress.values()); }


    /**
     * Permet de savoir si le nom est associé à une expression
     * @param name nom à tester
     * @return true si le nom est utilisé, false sinon
     */
    public static boolean containsExpress(final String name) {
        return allExpress.containsKey(name);
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
            StageService.Holder.openErrorWindows("messageOnly", ErrorCode.AlreadyUsedName);
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
            StageService.Holder.openErrorWindows("messageOnly", ErrorCode.UnknowName);
            return;
        }

        allExpress.get(name).setIsActive(true);//if added but disabled, enable
        graphExpress.add(name);//set so no verification needed
    }
    /**
     * Retire une expression de la mémoire
     * @param name l'expression à supprimer
     */
    public static void removeFromExpressList(final String name) {
        if (graphExpress.contains(name)) graphExpress.remove(name);
        if (allExpress.containsKey(name)) allExpress.remove(name);
    }


    /**
     * Modifier le nom d'une expression existante dans l'instance de l'expression et dans les listes de stockage
     * @param oldName nom de l'expression à modifier
     * @param newName nom à appliquer à l'expression
     */
    public static void renameExpress(final String oldName, final String newName) {
        if (newName.equals(oldName)) return;//no modification

        try {
            Double.parseDouble(newName);

            //pas catch : nombre
            StageService.Holder.openErrorWindows("messageOnly", ErrorCode.InvalidNameCauseNumber);
        } catch (NumberFormatException e){
            //pas un nombre

            if (allExpress.containsKey(newName)) {
                StageService.Holder.openErrorWindows("messageOnly", ErrorCode.AlreadyUsedName);
                return;
            }

            if (newName.equals("fonction") ||//nom réservé pour ajout ligne
                    newName.equals("+") ||
                    newName.equals("-") ||
                    newName.equals("*") ||
                    newName.equals("/") ||
                    newName.equals("^") ||
                    newName.equals("i") ||
                    newName.equals(FunctionX.getPI()) ||
                    newName.equals(FunctionX.getSQRT()) ||
                    newName.equals(FunctionX.getCBRT()) ||
                    newName.equals(FunctionX.getE()) ||
                    newName.equals(FunctionX.getLN()) ||
                    newName.equals(FunctionX.getLOG()) ||
                    newName.equals(FunctionX.getCOS()) ||
                    newName.equals(FunctionX.getSIN()) ||
                    newName.equals(FunctionX.getTAN()) ||
                    newName.equals(FunctionX.getACOS()) ||
                    newName.equals(FunctionX.getASIN()) ||
                    newName.equals(FunctionX.getATAN()) ||
                    newName.equals(FunctionX.getCOSH()) ||
                    newName.equals(FunctionX.getSINH()) ||
                    newName.equals(FunctionX.getTANH())) {
                StageService.Holder.openErrorWindows("messageOnly", ErrorCode.InvalidNameCauseSyntax);
                return;
            }

            allExpress.get(oldName).setName(newName);
            allExpress.put(newName, allExpress.get(oldName));
            allExpress.remove(oldName);

            for (String key : allExpress.keySet()) {
                //verifier que oldName entouré de signes (ne remplace pas qu'une partie, ex i dans sin)
                String replacement = newName;
                Pattern r = Pattern.compile("[\\+\\-\\*\\/\\s]("+oldName+")[\\+\\-\\*\\/\\s]");
                Matcher m = r.matcher(" "+allExpress.get(key).getFunction()+" ");
                StringBuffer sb = new StringBuffer();
                while (m.find()) {
                    StringBuffer buf = new StringBuffer(m.group());
                    buf.replace(m.start(1)-m.start(), m.end(1)-m.start(), replacement);
                    m.appendReplacement(sb, buf.toString());
                }
                m.appendTail(sb);
                String newFunction = sb.toString();
                allExpress.get(key).setFunction(newFunction);
            }


            if (graphExpress.contains(oldName)) {
                graphExpress.remove(oldName);
                graphExpress.add(newName);
            }
        }
    }
    /**
     * Permet l'inclusion de noms d'expressions dans les fonctions : remplace les noms par leur fonction
     * @param function fonction à "déplier" : réduire en token de base
     * @return la fonction sous forme de token de base
     */
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
    /**
     * Moteur de replaceExpressNameByFunctionRecursively
     * @param name fonction à traiter
     * @return fonction sans nom d'expression
     */
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
}
