package cn.iocoder.yudao.module.ai.dal.dataobject.chat;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.ai.dal.dataobject.model.AiChatModalDO;
import cn.iocoder.yudao.module.ai.dal.dataobject.model.AiChatRoleDO;
import cn.iocoder.yudao.module.ai.enums.AiOpenAiModelEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * AI Chat 会话 DO
 *
 * 用户每次发起 Chat 聊天时，会创建一个 {@link AiChatConversationDO} 对象，将它的消息关联在一起
 *
 * @author fansili
 * @since 2024/4/14 17:35
 */
@TableName("ai_chat_conversation")
@KeySequence("ai_chat_conversation_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiChatConversationDO extends BaseDO {

    /**
     * ID 编号，自增
     */
    @TableId
    private Long id;

    /**
     * 用户编号
     *
     * 关联 AdminUserDO 的 userId 字段
     */
    private Long userId;

    /**
     * 会话标题
     *
     * 默认由系统自动生成，可用户手动修改
     */
    private String title;
    /**
     * 是否置顶
     */
    private Boolean pinned;

    /**
     * 角色编号
     *
     * 关联 {@link AiChatRoleDO#getId()}
     */
    private Long roleId;

    /**
     * 模型编号
     *
     * 关联 {@link AiChatModalDO#getId()} 字段
     */
    private Long modelId;
    /**
     * 模型标志
     *
     * 枚举 {@link AiOpenAiModelEnum}
     */
    private String model;

    // ========== 会话配置 ==========

    /**
     * 温度参数
     *
     * 用于调整生成回复的随机性和多样性程度：较低的温度值会使输出更收敛于高频词汇，较高的则增加多样性
     */
    private Double temperature;
    /**
     * 单条回复的最大 Token 数量
     */
    private Integer maxTokens;
    /**
     * 上下文的最大 Message 数量
     */
    private Integer maxContexts;

}