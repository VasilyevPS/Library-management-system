package code.vasilyevps.library.mapper;

import code.vasilyevps.library.entity.loan.Loan;
import code.vasilyevps.library.entity.loan.dto.LoanDto;
import code.vasilyevps.library.entity.loan.dto.LoanReaderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LoanMapper {

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    @Mapping(source = "reader.id", target = "readerId")
    @Mapping(target = "readerName", source = "loan", qualifiedByName = "createReaderName")
    LoanDto toDto(Loan loan);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    LoanReaderDto toReaderDto(Loan loan);

    @Named("createReaderName")
    default String concatReaderName(Loan loan) {
        if (loan.getReader() == null) {
            return null;
        }
        return loan.getReader().getFirstName() + " " + loan.getReader().getLastName();
    }
}
