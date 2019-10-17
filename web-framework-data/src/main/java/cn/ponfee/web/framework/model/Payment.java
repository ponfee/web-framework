package cn.ponfee.web.framework.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 支付流水: t_payment
 * 
 * @author Ponfee
 */
public class Payment implements Serializable {

    private static final long serialVersionUID = -3826856448172182085L;

    private Long id; // 主键ID
    private Long userId; // 用户id
    private String orderId; // 支付ID
    private String orderNo; // 业务订单编号
    private Long orderAmt; // 订单金额（最小单位分）
    private Integer bizType; // 业务类型
    private String channelType; // 支付渠道类型
    private String thirdTradeNo; // 第三方支付订单号
    private String source; // 来源
    private Integer status; // 支付状态：0待买家付款；1支付成功；2支付失败；3待卖家收款；4交易关闭；
    private Date tradeTime; // 交易时间
    private String clientIp; // 客户端ip
    private String remark; // 备注
    private String extraData; // 附加数据
    private Integer retryCount; // 重试次数
    private Date createTime; // 创建时间
    private Date updateTime; // 最近更新时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(Long orderAmt) {
        this.orderAmt = orderAmt;
    }

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getThirdTradeNo() {
        return thirdTradeNo;
    }

    public void setThirdTradeNo(String thirdTradeNo) {
        this.thirdTradeNo = thirdTradeNo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}