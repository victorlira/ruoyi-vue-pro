package cn.iocoder.yudao.module.product.service.comment;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.product.controller.admin.comment.vo.ProductCommentPageReqVO;
import cn.iocoder.yudao.module.product.controller.admin.comment.vo.ProductCommentReplyVO;
import cn.iocoder.yudao.module.product.controller.admin.comment.vo.ProductCommentUpdateVisibleReqVO;
import cn.iocoder.yudao.module.product.controller.app.comment.vo.AppCommentAdditionalReqVO;
import cn.iocoder.yudao.module.product.controller.app.comment.vo.AppCommentPageReqVO;
import cn.iocoder.yudao.module.product.dal.dataobject.comment.ProductCommentDO;
import cn.iocoder.yudao.module.product.dal.mysql.comment.ProductCommentMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.product.enums.ErrorCodeConstants.*;

/**
 * 商品评论 Service 实现类
 *
 * @author wangzhs
 */
@Service
@Validated
public class ProductCommentServiceImpl implements ProductCommentService {

    @Resource
    private ProductCommentMapper productCommentMapper;

    @Override
    public PageResult<ProductCommentDO> getCommentPage(ProductCommentPageReqVO pageReqVO) {
        return productCommentMapper.selectPage(pageReqVO);
    }

    @Override
    public void updateCommentVisible(ProductCommentUpdateVisibleReqVO updateReqVO) {
        // 校验评论是否存在
        validateCommentExists(updateReqVO.getId());

        productCommentMapper.updateCommentVisible(updateReqVO.getId(), updateReqVO.getVisible());
    }

    @Override
    public void commentReply(ProductCommentReplyVO replyVO, Long loginUserId) {
        // 校验评论是否存在
        validateCommentExists(replyVO.getId());

        productCommentMapper.commentReply(replyVO, loginUserId);
    }

    @Override
    public PageResult<ProductCommentDO> getCommentPage(AppCommentPageReqVO pageVO, Boolean visible) {
        return productCommentMapper.selectPage(pageVO, visible);
    }

    @Override
    public void createComment(ProductCommentDO productComment, Boolean system) {
        if (!system) {
            // 判断当前订单的当前商品用户是否评价过
            ProductCommentDO exist = productCommentMapper.findByUserIdAndOrderIdAndSpuId(productComment.getId(), productComment.getOrderId(), productComment.getSpuId());
            if (null != exist) {
                throw exception(ORDER_SPU_COMMENT_EXISTS);
            }
        }
        productCommentMapper.insert(productComment);
    }

    @Override
    public void additionalComment(MemberUserRespDTO user, AppCommentAdditionalReqVO createReqVO) {
        // 校验评论是否存在
        ProductCommentDO productComment = validateCommentExists(createReqVO.getId());

        // 判断是否是同一用户追加评论
        if (!Objects.equals(productComment.getUserId(), user.getId())) {
            throw exception(COMMENT_ERROR_OPT);
        }

        // 判断是否已经追加评论过了
        if (StringUtils.hasText(productComment.getAdditionalContent())) {
            throw exception(COMMENT_ADDITIONAL_EXISTS);
        }

        productCommentMapper.additionalComment(createReqVO);
    }

    private ProductCommentDO validateCommentExists(Long id) {
        ProductCommentDO productComment = productCommentMapper.selectById(id);
        if (productComment == null) {
            throw exception(COMMENT_NOT_EXISTS);
        }
        return productComment;
    }


}