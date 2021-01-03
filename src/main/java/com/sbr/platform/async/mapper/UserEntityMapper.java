package com.sbr.platform.async.mapper;

import com.sbr.platform.async.model.domain.Address;
import com.sbr.platform.async.model.domain.CustomUser;
import com.sbr.platform.async.model.entity.AddressEntity;
import com.sbr.platform.async.model.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    UserEntity convert(CustomUser customUser);

    AddressEntity convert(Address address);
}
