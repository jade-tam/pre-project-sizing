package vn.gtel.pm2.sizing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import vn.gtel.pm2.sizing.dto.request.UpdateUserRequest;
import vn.gtel.pm2.sizing.dto.response.UserResponse;
import vn.gtel.pm2.sizing.entity.User;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface UserMapper {
    UserResponse toResponse(User entity);

    List<UserResponse> toResponseList(List<User> entities);

    void updateEntity(UpdateUserRequest request,
                      @MappingTarget User entity);
}
