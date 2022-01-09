import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.utils.Tuple;
import eu.builderscoffee.expresso.utils.blocks.BlockData;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HazarEngine {

    private final HashMap<BlockData.BlockCategory, List<BlockData>> cachedBlock = new HashMap<>();
    public Map<Object, Object> convertBlockdata = new HashMap<>();
    @Getter
    @Setter
    private ExpressoBukkit instance;

    @Test
    public void HazarEngine() {
        // On défini l'instance de la class principale
        setInstance(ExpressoBukkit.getInstance());
        // On charge l'engine
        load();
        // On enregistre les listeners
        //registerListener();
        // On init le menu
    }


    public void load() {
        System.out.println("HazardEngine init");
        // Get all bockdata and push then to a list
        Stream.of(BlockData.BlockCategory.values())
                .forEach(blockCategory -> cachedBlock.put(blockCategory, BlockData.blockDataCategory(blockCategory)));
        cachedBlock.forEach((blockCategory, blockData) -> System.out.print(blockCategory.name() + " " + blockData.size()));
        // Generate random block data
        generateRandomBlockData();
    }

    /***
     * Générer des pairs de blocs pour chaque catégories
     * //TODO Beautifully the code
     */
    public void generateRandomBlockData() {
        // On vient chercher chaques listes individuellement
        cachedBlock.keySet().forEach(key -> {
            // On évite les listes vides
            if (!cachedBlock.get(key).isEmpty()) {
                System.out.println("engine key : " + key);
                List<BlockData> blockData = cachedBlock.get(key);
                System.out.println("engine key : " + blockData.size());
                Collections.shuffle(blockData); // Mélanger aléatoirement la liste

                // Fonction générique pour split la list en deux
                int midIndex = (blockData.size() - 1) / 2;

                List<List<BlockData>> lists = new ArrayList<>(
                        blockData.stream()
                                .collect(Collectors.partitioningBy(s -> blockData.indexOf(s) > midIndex))
                                .values()
                );

                // Retourné un tableaux de liste avec les 2 fraichement spliter
                List[] twopart = new List[]{lists.get(0), lists.get(1)};

                List l1 = twopart[0];
                List l2 = twopart[1];

                List<Tuple> set = new ArrayList<>();
                // Executer la boucle pour Pairer la liste 1/2
                for (Object i : l1)
                    for (Object j : l2)
                        if (set.stream().noneMatch(tuple -> tuple.x.equals(i)) && set.stream().noneMatch(tuple -> tuple.y.equals(j)))
                            set.add(new Tuple(i, j));
                // Executer une nouvelle fois la boucle pour lister cette fois en 2/1
                for (Object i : l2)
                    for (Object j : l1)
                        if (set.stream().noneMatch(tuple -> tuple.x.equals(i)) && set.stream().noneMatch(tuple -> tuple.y.equals(j)) && set.stream().noneMatch(tuple -> tuple.x.equals(j) && tuple.y.equals(i)))
                            set.add(new Tuple(i, j));

                convertBlockdata.putAll(set.stream()
                        .collect(Collectors.toMap(keys -> keys.x, values -> values.y)));

                System.out.println(convertBlockdata.size());

                set.forEach(pair -> System.out.print("Key " + pair.x + " Valeur " + pair.y + "\n"));
            }
        });
    }
}
