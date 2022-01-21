package cn.iocoder.yudao.adminserver.modules.bpm.service.message.impl;

import cn.iocoder.yudao.adminserver.modules.bpm.enums.message.BpmMessageEnum;
import cn.iocoder.yudao.adminserver.modules.bpm.service.message.BpmMessageService;
import cn.iocoder.yudao.adminserver.modules.bpm.service.message.dto.BpmMessageSendWhenTaskCreatedReqDTO;
import cn.iocoder.yudao.coreservice.modules.system.service.sms.SysSmsCoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * BPM 消息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class BpmMessageServiceImpl implements BpmMessageService {

    @Resource
    private SysSmsCoreService smsCoreService;

    @Value("${yudao.url.admin-ui}")
    private String adminUiUrl;

    @Override
    public void sendMessageWhenTaskAssigned(BpmMessageSendWhenTaskCreatedReqDTO reqDTO) {
        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("processInstanceName", reqDTO.getProcessInstanceName());
        templateParams.put("taskName", reqDTO.getTaskName());
        templateParams.put("startUserNickname", reqDTO.getStartUserNickname());
        templateParams.put("taskDetailUrl", getTaskDetailUrl(reqDTO.getTaskId()));
        smsCoreService.sendSingleSmsToAdmin(null, reqDTO.getAssigneeUserId(),
                BpmMessageEnum.TASK_ASSIGNED.getSmsCode(), templateParams);
    }

    private String getTaskDetailUrl(String taskId) {
        return adminUiUrl + "bpm/process-instance/detail?id=" + taskId;
    }

}
