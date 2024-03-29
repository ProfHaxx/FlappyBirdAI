package util;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class Util {

    public static <Type> Type getRandomElementFromSet(final Set<Type> set, Random random) {
        final int index = random.nextInt(set.size());

        Iterator<Type> iterator = set.iterator();
        for (int i = 0; i < index - 1; i++) {
            iterator.next();
        }

        return iterator.next();
    }

    public static void sleep(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored){}
    }
}

