package cn.iocoder.yudao.module.ai.service.impl;

import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.ai.ErrorCodeConstants;
import cn.iocoder.yudao.module.ai.convert.ChatConversationConvert;
import cn.iocoder.yudao.module.ai.dal.dataobject.AiChatConversationDO;
import cn.iocoder.yudao.module.ai.dal.dataobject.AiChatRoleDO;
import cn.iocoder.yudao.module.ai.enums.ChatConversationTypeEnum;
import cn.iocoder.yudao.module.ai.mapper.AiChatConversationMapper;
import cn.iocoder.yudao.module.ai.mapper.AiChatRoleMapper;
import cn.iocoder.yudao.module.ai.service.ChatConversationService;
import cn.iocoder.yudao.module.ai.vo.ChatConversationCreateRoleReq;
import cn.iocoder.yudao.module.ai.vo.ChatConversationCreateUserReq;
import cn.iocoder.yudao.module.ai.vo.ChatConversationListReq;
import cn.iocoder.yudao.module.ai.vo.ChatConversationRes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * chat 对话
 *
 * @fansili
 * @since v1.0
 */
@Service
@Slf4j
@AllArgsConstructor
public class ChatConversationServiceImpl implements ChatConversationService {

    private final AiChatRoleMapper aiChatRoleMapper;
    private final AiChatConversationMapper aiChatConversationMapper;

    @Override
    public ChatConversationRes createConversation(ChatConversationCreateUserReq req) {
        // 获取用户id
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        // 查询最新的对话
        AiChatConversationDO latestConversation = aiChatConversationMapper.selectLatestConversation(loginUserId);
        // 如果有对话没有被使用过，那就返回这个
        if (latestConversation != null && latestConversation.getChatCount() <= 0) {
            return ChatConversationConvert.INSTANCE.covnertChatConversationRes(latestConversation);
        }
        // 创建新的 Conversation
        AiChatConversationDO insertConversation = saveConversation(req.getTitle(), loginUserId,
                null, null, ChatConversationTypeEnum.USER_CHAT);
        // 转换 res
        return ChatConversationConvert.INSTANCE.covnertChatConversationRes(insertConversation);
    }

    @Override
    public ChatConversationRes createRoleConversation(ChatConversationCreateRoleReq req) {
        // 获取用户id
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        // 查询最新的对话
        AiChatConversationDO latestConversation = aiChatConversationMapper.selectLatestConversation(loginUserId);
        // 如果有对话没有被使用过，那就返回这个
        if (latestConversation != null && latestConversation.getChatCount() <= 0) {
            return ChatConversationConvert.INSTANCE.covnertChatConversationRes(latestConversation);
        }
        AiChatRoleDO aiChatRoleDO = aiChatRoleMapper.selectById(req.getChatRoleId());
        // 创建新的 Conversation
        AiChatConversationDO insertConversation = saveConversation(req.getTitle(), loginUserId,
                req.getChatRoleId(), aiChatRoleDO.getRoleName(), ChatConversationTypeEnum.ROLE_CHAT);
        // 转换 res
        return ChatConversationConvert.INSTANCE.covnertChatConversationRes(insertConversation);
    }

    private @NotNull AiChatConversationDO saveConversation(String title,
                                                           Long userId,
                                                           Long chatRoleId,
                                                           String chatRoleName,
                                                           ChatConversationTypeEnum typeEnum) {
        AiChatConversationDO insertConversation = new AiChatConversationDO();
        insertConversation.setId(null);
        insertConversation.setUserId(userId);
        insertConversation.setChatRoleId(chatRoleId);
        insertConversation.setChatRoleName(chatRoleName);
        insertConversation.setTitle(title);
        insertConversation.setChatCount(0);
        insertConversation.setType(typeEnum.getType());
        aiChatConversationMapper.insert(insertConversation);
        return insertConversation;
    }

    @Override
    public ChatConversationRes getConversation(Long id) {
        AiChatConversationDO aiChatConversationDO = aiChatConversationMapper.selectById(id);
        if (aiChatConversationDO == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AI_CHAT_CONTINUE_NOT_EXIST);
        }
        return ChatConversationConvert.INSTANCE.covnertChatConversationRes(aiChatConversationDO);
    }

    @Override
    public List<ChatConversationRes> listConversation(ChatConversationListReq req) {
        // 获取用户id
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        // 查询前100对话
        List<AiChatConversationDO> top100Conversation
                = aiChatConversationMapper.selectTop100Conversation(loginUserId, req.getSearch());
        return ChatConversationConvert.INSTANCE.covnertChatConversationResList(top100Conversation);
    }

    @Override
    public void delete(Long id) {
        aiChatConversationMapper.deleteById(id);
    }
}