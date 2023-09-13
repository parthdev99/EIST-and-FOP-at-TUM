package de.tum.in.ase.eist;

import java.util.List;

public class BinarySearch implements SearchStrategy {
    public int performSearch(List<Chapter> book, String name) {
        int low = 0;
        int high = book.size() - 1;
        while (!(low > high)) {
            int mid = low + ((high - low) / 2);
            if (book.get(mid).getName().equals(name)) {
                return book.get(mid).getPageNumber();
            } else if (name.compareTo(book.get(mid).getName()) > 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }
}
