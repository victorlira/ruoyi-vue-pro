package cn.iocoder.yudao.module.product.enums.spu;

import lombok.AllArgsConstructor;
import lombok.Getter;

// TODO @puhui999：中英文之间要有空格； 商品 spu Tab 标签枚举；这个类可以改成 ProductSpuPageTabEnum 会更好一点哈；分页 Tab 的意思；
/**
 * 商品spu标签枚举类型
 *
 * @author HUIHUI
 */
@Getter
@AllArgsConstructor
public enum ProductSpuTabTypeEnum {

    FOR_SALE(0,"出售中商品"),
    IN_WAREHOUSE(1,"仓库中商品"),
    SOLD_OUT(2,"已售空商品"),
    ALERT_STOCK(3,"警戒库存"),
    RECYCLE_BIN(4,"商品回收站");

    /**
     * 状态
     */
    private final Integer type;
    /**
     * 状态名
     */
    private final String name;

}