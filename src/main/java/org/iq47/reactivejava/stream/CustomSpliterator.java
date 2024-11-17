package org.iq47.reactivejava.stream;

import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

@Setter
public class CustomSpliterator<Deal> implements Spliterator<Deal> {
    private final int LIMIT = 2;
    private final List<Deal> dealList;
    int index;

    public CustomSpliterator(List<Deal> dealList) {
        this.dealList = dealList;
        index = 0;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Deal> action) {
        boolean isFinished = (index >= dealList.size());
        while (!isFinished) {
            isFinished = (index >= dealList.size() - 1);
            action.accept(dealList.get(index));
            index++;
        }
        return false;
    }

    @Override
    public Spliterator<Deal> trySplit() {
        int currentSize = dealList.size() - index;
        if (currentSize < LIMIT) {
            return null;
        }

        int splitIndex = index + currentSize / 2;
        Spliterator<Deal> spliterator = new CustomSpliterator<>(dealList.subList(index, splitIndex));
        index = splitIndex;
        return spliterator;
    }

    @Override
    public long estimateSize() {
        return dealList.size() - index;
    }

    @Override
    public int characteristics() {
        return SIZED | SUBSIZED | NONNULL | IMMUTABLE;
    }
}
