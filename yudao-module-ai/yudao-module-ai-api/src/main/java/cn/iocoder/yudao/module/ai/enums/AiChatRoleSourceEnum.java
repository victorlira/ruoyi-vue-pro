package cn.iocoder.yudao.module.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * chat角色 source
 *
 * @author fansili
 * @time 2024/4/24 16:37
 * @since 1.0
 */
@AllArgsConstructor
@Getter
public enum AiChatRoleSourceEnum {

    SYSTEM("system", "系统"),
    CUSTOMER("customer", "用户自定义"),


    ;

    private String type;

    private String name;


    public static AiChatRoleSourceEnum valueOfType(String type) {
        for (AiChatRoleSourceEnum itemEnum : AiChatRoleSourceEnum.values()) {
            if (itemEnum.getType().equals(type)) {
                return itemEnum;
            }
        }
        throw new IllegalArgumentException("Invalid MessageType value: " + type);
    }
}