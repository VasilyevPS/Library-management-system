package code.vasilyevps.library.mapper;

import code.vasilyevps.library.entity.reader.Reader;
import code.vasilyevps.library.entity.reader.dto.ReaderCreateDto;
import code.vasilyevps.library.entity.reader.dto.ReaderDto;
import code.vasilyevps.library.entity.reader.dto.ReaderUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReaderMapper {

    ReaderDto toDto(Reader reader);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    Reader toEntity(ReaderCreateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    void updateEntityFromDto(ReaderUpdateDto dto, @MappingTarget Reader reader);
}
