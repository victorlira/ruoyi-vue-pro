package cn.iocoder.yudao.module.trade.framework.delivery.core.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.trade.framework.delivery.config.TradeExpressQueryProperties;
import cn.iocoder.yudao.module.trade.framework.delivery.core.ExpressQueryProvider;
import cn.iocoder.yudao.module.trade.framework.delivery.core.dto.ExpressQueryReqDTO;
import cn.iocoder.yudao.module.trade.framework.delivery.core.dto.ExpressQueryRespDTO;
import cn.iocoder.yudao.module.trade.framework.delivery.core.dto.provider.kdniao.KdNiaoExpressQueryReqDTO;
import cn.iocoder.yudao.module.trade.framework.delivery.core.dto.provider.kdniao.KdNiaoExpressQueryRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.trade.enums.ErrorCodeConstants.EXPRESS_API_QUERY_FAILED;
import static cn.iocoder.yudao.module.trade.enums.ErrorCodeConstants.EXPRESS_API_QUERY_ERROR;
import static cn.iocoder.yudao.module.trade.framework.delivery.core.convert.ExpressQueryConvert.INSTANCE;

/**
 * 快递鸟服务商
 *
 * @author jason
 */
@Slf4j
public class KdNiaoExpressQueryProvider implements ExpressQueryProvider {

    private static final String REAL_TIME_QUERY_URL = "https://api.kdniao.com/Ebusiness/EbusinessOrderHandle.aspx";

    /**
     * 快递鸟即时查询免费版 RequestType
     */
    private static final String REAL_TIME_FREE_REQ_TYPE = "1002";

    private final RestTemplate restTemplate;
    private final TradeExpressQueryProperties.KdNiaoConfig config;

    // TODO @jason：可以改成 lombok 哈
    public KdNiaoExpressQueryProvider(RestTemplate restTemplate, TradeExpressQueryProperties.KdNiaoConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    /**
     * 快递鸟即时查询免费版本
     *
     * @see <a href="https://www.yuque.com/kdnjishuzhichi/dfcrg1/wugo6k">快递鸟接口文档</a>
     * @param reqDTO 查询请求参数
     */
    @Override
    public List<ExpressQueryRespDTO> realTimeQueryExpress(ExpressQueryReqDTO reqDTO) {
        KdNiaoExpressQueryReqDTO kdNiaoReqData = INSTANCE.convert(reqDTO);
        // 快递公司编码需要转成大写
        kdNiaoReqData.setExpressCompanyCode(reqDTO.getExpressCompanyCode().toUpperCase());
        KdNiaoExpressQueryRespDTO respDTO = sendKdNiaoApiRequest(REAL_TIME_QUERY_URL, REAL_TIME_FREE_REQ_TYPE,
                kdNiaoReqData, KdNiaoExpressQueryRespDTO.class);
        log.debug("[realTimeQueryExpress][快递鸟即时查询接口返回 {}]", respDTO);
        if(!respDTO.getSuccess()){
            throw exception(EXPRESS_API_QUERY_FAILED, respDTO.getReason());
        }else{
            if (CollUtil.isNotEmpty(respDTO.getTracks())) {
                return INSTANCE.convertList(respDTO.getTracks());
            }else{
                return Collections.emptyList();
            }
        }
    }

    /**
     * 快递鸟 通用的 API 请求, 暂时没有其他应用场景， 暂时放这里
     * @param url 请求 url
     * @param requestType 对应的请求指令 (快递鸟的RequestType)
     * @param req  对应请求的请求参数
     * @param respClass 对应请求的响应 class
     * @param <Req> 每个请求的请求结构 Req DTO
     * @param <Resp> 每个请求的响应结构 Resp DTO
     */
    private  <Req, Resp> Resp sendKdNiaoApiRequest(String url, String requestType, Req req,
                                                   Class<Resp> respClass){
        // 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 请求体
        String reqData = JsonUtils.toJsonString(req);
        String dataSign = generateDataSign(reqData, config.getApiKey());
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("RequestData", reqData);
        requestBody.add("DataType", "2");
        requestBody.add("EBusinessID", config.getBusinessId());
        requestBody.add("DataSign", dataSign);
        requestBody.add("RequestType", requestType);
        log.debug("[sendKdNiaoApiRequest][快递鸟接口 RequestType : {}, 的请求参数 {}]", requestType, requestBody);

        // 发送请求
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        log.debug("快递鸟接口 RequestType : {}, 的响应结果 {}", requestType,  responseEntity);
        // 处理响应
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String response = responseEntity.getBody();
            return JsonUtils.parseObject(response, respClass);
        } else {
            throw exception(EXPRESS_API_QUERY_ERROR);
        }
    }

    /**
     * 快递鸟生成请求签名 参见 <a href="https://www.yuque.com/kdnjishuzhichi/dfcrg1/zes04h">签名说明</a>
     * @param reqData 请求实体
     * @param apiKey  api Key
     */
    private String generateDataSign(String reqData, String apiKey) {
        String plainText = String.format("%s%s", reqData, apiKey);
        return URLEncodeUtil.encode(Base64.encode(DigestUtil.md5Hex(plainText)));
    }

}