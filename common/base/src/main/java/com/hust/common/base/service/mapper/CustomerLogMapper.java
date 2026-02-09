package com.hust.common.base.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.hust.common.base.model.Customer;
import com.hust.common.base.model.CustomerLog;

@Mapper(componentModel = "spring")
public interface CustomerLogMapper extends EntityMapper<CustomerLog, Customer> {
    @Override
    @Mappings({
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "lastModifiedBy", ignore = true),
            @Mapping(target = "lastModifiedDate", ignore = true),
            @Mapping(target = "id", ignore = true),
    })
    CustomerLog toDto(Customer entity);
}
