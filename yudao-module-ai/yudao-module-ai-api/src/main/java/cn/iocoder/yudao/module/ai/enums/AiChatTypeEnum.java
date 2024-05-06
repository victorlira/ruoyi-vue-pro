package cn.iocoder.yudao.module.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 聊天类型
 *
 * @author fansili
 * @time 2024/4/14 17:58
 * @since 1.0
 */
@AllArgsConstructor
@Getter
public enum AiChatTypeEnum {

    ROLE_CHAT("roleChat", "角色模板聊天"),
    USER_CHAT("userChat", "用户普通聊天"),

    ;

    private String type;

    private String name;

    public static AiChatTypeEnum valueOfType(String type) {
        for (AiChatTypeEnum itemEnum : AiChatTypeEnum.values()) {
            if (itemEnum.getType().equals(type)) {
                return itemEnum;
            }
        }
        throw new IllegalArgumentException("Invalid MessageType value: " + type);
    }
}