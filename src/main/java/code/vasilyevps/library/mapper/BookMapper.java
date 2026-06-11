package code.vasilyevps.library.mapper;

import code.vasilyevps.library.entity.book.Book;
import code.vasilyevps.library.entity.book.dto.BookCreateDto;
import code.vasilyevps.library.entity.book.dto.BookDto;
import code.vasilyevps.library.entity.book.dto.BookUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {

    BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    Book toEntity(BookCreateDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(BookUpdateDto dto, @MappingTarget Book book);
}
