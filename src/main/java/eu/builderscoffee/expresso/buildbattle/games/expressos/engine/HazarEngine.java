package eu.builderscoffee.expresso.buildbattle.games.expressos.engine;

import eu.builderscoffee.api.common.configuration.Configuration;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.BuildBattleEngine;
import eu.builderscoffee.expresso.inventory.game.HazardExpressoInventory;
import eu.builderscoffee.expresso.utils.Log;
import eu.builderscoffee.expresso.utils.Tuple;
import eu.builderscoffee.expresso.utils.blocks.BlockData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Listener;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HazarEngine implements BuildBattleEngine {

    private final HashMap<BlockData.BlockCategory, List<BlockData>> cachedBlock = new HashMap<>();
    public Map<Object, Object> convertBlockdata = new HashMap<>();
    @Getter
    @Setter
    private ExpressoBukkit instance;

    public HazarEngine(ExpressoBukkit instance) {
        // On définit l'instance de la class principale
        setInstance(instance);
        // On init le menu
        new HazardExpressoInventory(this);
    }

    @Override
    public void load() {
        Log.get().info("HazardEngine init");
        // Get all bockdata and push then to a list
        Stream.of(BlockData.BlockCategory.values())
                .forEach(blockCategory -> cachedBlock.put(blockCategory, BlockData.blockDataCategory(blockCategory)));
        cachedBlock.forEach((blockCategory, blockData) -> System.out.print(blockCategory.name() + " " + blockData.size()));
        // Generate random block data
        generateRandomBlockData();
    }

    @Override
    public List<Listener> registerListener() {
        return Collections.singletonList(new HazarListener(getInstance(), this));
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
                Log.get().info("engine key : " + key);
                List<BlockData> blockData = cachedBlock.get(key);
                Log.get().info("engine key : " + blockData.size());
                Collections.shuffle(blockData); // Mélanger aléatoirement la liste

                // Fonction générique pour split la list en deux
                int midIndex = (blockData.size() - 1) / 2;

                List<List<BlockData>> lists = new ArrayList<>(
                        blockData.stream()
                                .collect(Collectors.partitioningBy(s -> blockData.indexOf(s) > midIndex))
                                .values()
                );

                // Retourné un tableau de liste avec les 2 fraichement spliter
                List[] twopart = new List[]{lists.get(0), lists.get(1)};

                List l1 = twopart[0];
                List l2 = twopart[1];

                List<Tuple> set = new ArrayList<>();
                // 1 -> Key && 2 -> Values
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

    /***
     * Pousser en configuration un cache des blocks convertis par l'engine
     */
    public void PrintBlockData() {
        List<Tuple<Object, Object>> tupleList = new ArrayList<>();
        convertBlockdata.forEach((o, o2) -> tupleList.add(new Tuple(o, o2)));
        ExpressoBukkit.getCache().getPairList().addAll(tupleList);
        Configuration.writeConfiguration(ExpressoBukkit.getInstance().getName(), ExpressoBukkit.getCache());
    }
}
