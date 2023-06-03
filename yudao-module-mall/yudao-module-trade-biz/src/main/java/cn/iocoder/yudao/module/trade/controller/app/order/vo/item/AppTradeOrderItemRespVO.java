package cn.iocoder.yudao.module.trade.controller.app.order.vo.item;

import cn.iocoder.yudao.module.trade.controller.app.base.property.AppProductPropertyValueDetailRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "用户 App - 订单交易项 Response VO")
@Data
public class AppTradeOrderItemRespVO {

    @Schema(description = "编号", required = true, example = "1")
    private Long id;

    @Schema(description = "商品 SPU 编号", required = true, example = "1")
    private Long spuId;
    @Schema(description = "商品 SPU 名称", required = true, example = "芋道源码")
    private String spuName;

    @Schema(description = "商品 SKU 编号", required = true, example = "1")
    private Long skuId;

    /**
     * 属性数组
     */
    private List<AppProductPropertyValueDetailRespVO> properties;

    @Schema(description = "商品图片", required = true, example = "https://www.iocoder.cn/1.png")
    private String picUrl;

    @Schema(description = "购买数量", required = true, example = "1")
    private Integer count;

    @Schema(description = "是否评价", required = true, example = "true")
    private Boolean commentStatus;

    // ========== 价格 + 支付基本信息 ==========

    @Schema(description = "商品原价（单）", required = true, example = "100")
    private Integer price;

    // ========== 营销基本信息 ==========

    // TODO 芋艿：在捉摸一下

    // ========== 售后基本信息 ==========

    @Schema(description = "售后状态", required = true, example = "1")
    private Integer afterSaleStatus;
    
}