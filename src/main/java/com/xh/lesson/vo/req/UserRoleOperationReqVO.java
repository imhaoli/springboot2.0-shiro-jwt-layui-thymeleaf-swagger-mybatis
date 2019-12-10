package com.xh.lesson.vo.req;

        import io.swagger.annotations.ApiModelProperty;
        import lombok.Data;

        import javax.validation.constraints.NotBlank;
        import javax.validation.constraints.NotEmpty;
        import java.util.List;

/**
 * @ClassName: UserRoleOperationReqVO
 * TODO:类文件简单描述
 * @Author: 小霍
 * @CreateDate: 2019/9/20 14:40
 * @UpdateUser: 小霍
 * @UpdateDate: 2019/9/20 14:40
 * @Version: 0.0.1
 */
@Data
public class UserRoleOperationReqVO {
    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;
    @ApiModelProperty(value = "角色id集合")
    @NotEmpty(message = "角色id集合不能为空")
    private List<String> roleIds;
}
