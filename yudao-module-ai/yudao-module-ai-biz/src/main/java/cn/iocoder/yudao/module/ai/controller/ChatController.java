package cn.iocoder.yudao.module.ai.controller;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.iocoder.yudao.framework.ai.chat.ChatResponse;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.ai.service.ChatService;
import cn.iocoder.yudao.module.ai.vo.ChatReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * ia 模块
 *
 * @author fansili
 * @time 2024/4/13 17:44
 * @since 1.0
 */
@Tag(name = "AI模块")
@RestController
@RequestMapping("/ai")
@Slf4j
@AllArgsConstructor
public class ChatController {

    @Autowired
    private final ChatService chatService;

    @Operation(summary = "聊天-chat", description = "这个一般等待时间比较久，需要全部完成才会返回!")
    @GetMapping("/chat")
    public CommonResult<String> chat(@Validated @ModelAttribute ChatReq req) {
        return CommonResult.success(chatService.chat(req));
    }

    // TODO @芋艿：调用这个方法异常，Unable to handle the Spring Security Exception because the response is already committed.
    @Operation(summary = "聊天-stream", description = "这里跟通义千问一样采用的是 Server-Sent Events (SSE) 通讯模式")
    @GetMapping(value = "/chatStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chatStream(@Validated @ModelAttribute ChatReq req) {
        Utf8SseEmitter sseEmitter = new Utf8SseEmitter();
        chatService.chatStream(req, sseEmitter);
        return sseEmitter;
    }
}