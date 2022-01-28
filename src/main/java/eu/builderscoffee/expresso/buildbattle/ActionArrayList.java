package eu.builderscoffee.expresso.buildbattle;

import lombok.Setter;
import lombok.val;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Setter
public class ActionArrayList<T> extends ArrayList<T> {

    Consumer<T> onAdd;
    Consumer<T> onRemove;
    Consumer<Integer> onRemoveByIndex;
    BiConsumer<Integer, T> onAddByIndex;

    @Override
    public boolean add(T t) {
        val result = super.add(t);
        if (Objects.nonNull(onAdd))
            onAdd.accept(t);
        return result;
    }

    @Override
    public T remove(int index) {
        val result = super.remove(index);
        if (Objects.nonNull(onRemoveByIndex))
            onRemoveByIndex.accept(index);
        return result;
    }

    @Override
    public void add(int index, T element) {
        super.add(index, element);
        if (Objects.nonNull(onAddByIndex))
            onAddByIndex.accept(index, element);
    }

    @Override
    public boolean remove(Object o) {
        val result = super.remove(o);
        if (Objects.nonNull(onRemove))
            onRemove.accept((T) o);
        return result;
    }
}
