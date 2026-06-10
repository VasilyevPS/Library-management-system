package code.vasilyevps.library.entity.book.dto;

public class BookSearchFilter {

    private String title;
    private String author;
    private String isbn;
    private Integer publicationYear;
    private Integer totalCopies;
    private Integer availableCopies;

    public BookSearchFilter() {
    }

    public BookSearchFilter(String title, String author,
                            String isbn, Integer publicationYear,
                            Integer totalCopies, Integer availableCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public Integer getTotalCopies() {
        return totalCopies;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }
}
