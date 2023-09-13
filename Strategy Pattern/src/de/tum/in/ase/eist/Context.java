package de.tum.in.ase.eist;

import java.util.List;

public class Context {
    private List<Chapter> book;
    private SearchStrategy searchAlgorithm;

    public int search(String name) {
        return searchAlgorithm.performSearch(book, name);
    }

    public boolean isChaptersSortedByName() {
        //List<String> sortedName=book.stream().map(e->e.getName()).sorted().collect(Collectors.toList());
        //List<String> unsotedName=book.stream().map(e -> e.getName()).collect(Collectors.toList());
        if (book.size() == 1) {
            return true;
        }
        for (int i = 1; i < book.size(); i++) {
            int comp = book.get(i - 1).getName().compareTo(book.get(i).getName());
            if (comp > 0) {
                return false;
            }
        }
        return true;
    }

    public List<Chapter> getBook() {
        return book;
    }

    public SearchStrategy getSearchAlgorithm() {
        return searchAlgorithm;
    }

    public void setBook(List<Chapter> book) {
        this.book = book;
    }

    public void setSearchAlgorithm(SearchStrategy searchAlgorithm) {
        this.searchAlgorithm = searchAlgorithm;
    }
}
