package com.xh.lesson.vo.resp;

import com.xh.lesson.entity.SysRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: UserOwnRoleRespVO
 * TODO:类文件简单描述
 * @Author: 小霍
 * @CreateDate: 2019/11/3 21:50
 * @UpdateUser: 小霍
 * @UpdateDate: 2019/11/3 21:50
 * @Version: 0.0.1
 */
@Data
public class UserOwnRoleRespVO {
    @ApiModelProperty("所有角色集合")
    private List<SysRole> allRole;
    @ApiModelProperty(value = "用户所拥有角色集合")
    private List<String> ownRoles;
}
