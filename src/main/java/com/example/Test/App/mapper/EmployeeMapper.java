package com.example.Test.App.mapper;

import com.example.Test.App.dao.Employee;
import com.example.Test.App.dto.EmployeeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper {
    EmployeeMapper MAPPER = Mappers.getMapper(EmployeeMapper.class);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "password", source = "password"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "surname", source = "surname"),


    })
    Employee mapToEntity(EmployeeDto source);


}
