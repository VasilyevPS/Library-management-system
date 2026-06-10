package code.vasilyevps.library.entity.book;

import org.springframework.data.jpa.domain.Specification;

public class BookSpecs {

    public static Specification<Book> hasTitle(String title) {
        return (root, query, cb) ->
                title == null || title.isBlank() ? cb.conjunction()
                        : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Book> hasAuthor(String author) {
        return (root, query, cb) ->
                author == null || author.isBlank() ? cb.conjunction()
                        : cb.like(cb.lower(root.get("author")), "%" + author.toLowerCase() + "%");
    }

    public static Specification<Book> hasIsbn(String isbn) {
        return (root, query, cb) ->
                isbn == null || isbn.isBlank() ? cb.conjunction()
                        : cb.equal(root.get("isbn"), isbn);
    }

    public static Specification<Book> hasPublicationYear(Integer year) {
        return (root, query, cb) ->
                year == null ? cb.conjunction()
                        : cb.equal(root.get("publicationYear"), year);
    }

    public static Specification<Book> hasTotalCopies(Integer totalCopies) {
        return (root, query, cb) ->
                totalCopies == null ? cb.conjunction()
                        : cb.equal(root.get("totalCopies"), totalCopies);
    }

    public static Specification<Book> hasAvailableCopies(Integer availableCopies) {
        return (root, query, cb) ->
                availableCopies == null ? cb.conjunction()
                        : cb.equal(root.get("availableCopies"), availableCopies);
    }
}
