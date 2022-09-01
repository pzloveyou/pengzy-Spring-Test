package com.pengzy.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author pzy
 * @since 2022-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Goods1对象", description="")
public class Goods1 implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    @TableId(value = "gid", type = IdType.AUTO)
    private Long gid;

    @ApiModelProperty(value = "商品名称")
    private String gname;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "商品状态")
    private String gstatus;


}
