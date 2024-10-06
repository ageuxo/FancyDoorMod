package io.github.ageuxo.FancyDoorMod.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionDiff<T> {
    public static final Codec<CollectionDiff<String>> STRING = makeCodec(Codec.STRING);

    private final boolean clear;
    private List<T> add;
    private List<T> remove;

    public CollectionDiff(Collection<T> origin, Collection<T> destination) {
        if (destination.isEmpty()) {
            this.clear = true;
        } else {
            this.clear = false;

            this.add = new ArrayList<>();
            this.remove = new ArrayList<>();

            List<T> oUniques = origin.stream()
                    .filter(o -> !destination.contains(o))
                    .toList();

            remove.addAll(oUniques);

            List<T> dUniques = destination.stream()
                    .filter(d -> !origin.contains(d))
                    .toList();

            add.addAll(dUniques);
        }
    }

    public CollectionDiff(boolean clear, List<T> add, List<T> remove) {
        this.clear = clear;
        this.add = add;
        this.remove = remove;
    }

    public static <O> Codec<CollectionDiff<O>> makeCodec(Codec<O> tCodec) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.BOOL.fieldOf("clear").forGetter(CollectionDiff::clear),
                tCodec.listOf().optionalFieldOf("add", new ArrayList<>()).forGetter(CollectionDiff::add),
                tCodec.listOf().optionalFieldOf("remove", new ArrayList<>()).forGetter(CollectionDiff::remove)
        ).apply(instance, CollectionDiff::new));
    }

    public static <O> void handle(CollectionDiff<O> diff, Collection<O> target) {
        if (diff.clear()) {
            target.clear();
            return;
        }
        List<O> remove = diff.remove();
        if (remove != null) {
            target.removeAll(remove);
        }
        List<O> add = diff.add();
        if (add != null) {
            target.addAll(add);
        }
    }

    public boolean clear() {
        return clear;
    }

    public List<T> add() {
        return add;
    }

    public List<T> remove() {
        return remove;
    }

}
