package dcom.focuz.api.domain.group.dto;

import dcom.focuz.api.domain.group.Group;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class GroupRequestDto {

    @ApiModel(value="그룹 등록")
    @Builder
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Register {
        @NotEmpty @Size(min = 4, max = 128, message = "길이는 4 ~ 128자 이내여야 합니다.")
        @ApiModelProperty(value = "그룹 이름")
        private String name;

        @NotEmpty @Size(min = 4, max = 256, message = "길이는 4 ~ 256자 이내여야 합니다.")
        @ApiModelProperty(value = "그룹 설명")
        private String description;

        public Group toEntity() {
            return Group.builder()
                    .name(this.name)
                    .description(this.description)
                    .build();
        }
    }
}
