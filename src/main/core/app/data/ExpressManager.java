package core.app.data;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ExpressManager {
    private static HashMap<String, Express> allExpress = new HashMap<>();

    private static LinkedHashSet<String> graphExpress = new LinkedHashSet<>();

    public static void load() throws FileNotFoundException{
        File file = new File("./src/main/resources/save.csv");
        Scanner sc = new Scanner(file);
        while(sc.hasNext())
        {
            String[] line = sc.nextLine().split(";");
            addToExpressList(line[0], line[1]);
        }
        sc.close();
    }

    public static void save() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/resources/save.csv"));
        writer.write("");

        ArrayList<String> nameList = getExpressNames();
        Collections.sort(nameList);
        for (String key : nameList)
        {
            writer.append(key).append(";").append(allExpress.get(key).getFunction()).append("\n");
        }
        writer.close();
    }

    public static boolean containsExpress(String name) {
        return allExpress.containsKey(name);
    }

    public static String refactorExpress(String name)
    {
        Map<String, Express> treeMap = new TreeMap<>(
            new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    if (s1.length() > s2.length()) {
                        return -1;
                    } else if (s1.length() < s2.length()) {
                        return 1;
                    } else {
                        return s1.compareTo(s2);
                    }
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

    public static Express getExpress(String name) { return allExpress.get(name); }

    public static ArrayList<Express> getExpressList() { return new ArrayList<>(allExpress.values()); }

    public static ArrayList<String> getExpressNames() { return new ArrayList<>(allExpress.keySet()); }

    public static ArrayList<Express> getExpressGraphList() {
        return new ArrayList<>(graphExpress.stream().map(allExpress::get).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    public static void clearAll() {
        allExpress.clear();
        graphExpress.clear();
    }

    public static void addToExpressList(String name, String function) {
        if (allExpress.containsKey(name)) {
            System.out.println("créer une exception \"already used name\" et faire pop une fenêtre d'erreur?");
            return;
        }

        allExpress.put(name, new Express(name, function));
    }

    public static void addToGraphList(String name) {
        if (!allExpress.containsKey(name)) {
            System.out.println("créer une exception \"unknown function name\" et faire pop une fenêtre d'erreur?");
            return;
        }

        allExpress.get(name).setIsActive(true);//if added but disabled, enable
        graphExpress.add(name);//set so no verification needed
    }

    public static void updateName(String oldName, String newName) {
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
