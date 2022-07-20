package dcom.focuz.api.domain.user.dto;

import dcom.focuz.api.domain.group.Group;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserRequestDto {

    @ApiModel(value="유저 등록")
    @Builder
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Register {
        @NotEmpty
        @Size(min = 4, max = 32, message = "길이는 4 ~ 32자 이내여야 합니다.")
        @ApiModelProperty(value = "유저 닉네임", required = true)
        private String nickname;

        @NotEmpty @Size(min = 4, max = 128, message = "길이는 4 ~ 128자 이내여야 합니다.")
        @ApiModelProperty(value = "유저 좌우명", required = true)
        private String motto;
    }
}
