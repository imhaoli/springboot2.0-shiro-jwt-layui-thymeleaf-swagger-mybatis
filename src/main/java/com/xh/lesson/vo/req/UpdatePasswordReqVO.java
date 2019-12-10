package com.xh.lesson.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: UpdatePasswordReqVO
 * TODO:类文件简单描述
 * @Author: 小霍
 * @CreateDate: 2019/10/27 0:27
 * @UpdateUser: 小霍
 * @UpdateDate: 2019/10/27 0:27
 * @Version: 0.0.1
 */
@Data
public class UpdatePasswordReqVO {
    @ApiModelProperty(value = "旧密码")
    private String oldPwd;
    @ApiModelProperty(value = "新密码")
    private String newPwd;
}
