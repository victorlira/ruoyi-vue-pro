package cn.iocoder.yudao.module.product.controller.admin.comment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 商品评价 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductCommentRespVO extends ProductCommentBaseVO {

    @Schema(description = "订单项编号", required = true, example = "24965")
    private Long id;

    @Schema(description = "是否匿名：[0:不匿名 1:匿名]", required = true)
    private Boolean anonymous;

    @Schema(description = "交易订单编号", required = true, example = "24428")
    private Long orderId;

    @Schema(description = "交易订单项编号", required = true, example = "8233")
    private Long orderItemId;

    @Schema(description = "是否可见：[1:显示 0:隐藏]", required = true)
    private Boolean visible;

    @Schema(description = "商家是否回复：[1:回复 0:未回复]", required = true)
    private Boolean replied;

    @Schema(description = "回复管理员编号", example = "22212")
    private Long replyUserId;

    @Schema(description = "商家回复内容")
    private String replyContent;

    @Schema(description = "商家回复时间")
    private LocalDateTime replyTime;

    @Schema(description = "追加评价内容")
    private String additionalContent;

    @Schema(description = "追评评价图片地址数组，以逗号分隔最多上传9张")
    private List<String> additionalPicUrls;

    @Schema(description = "追加评价时间")
    private LocalDateTime additionalCreateTime;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

}