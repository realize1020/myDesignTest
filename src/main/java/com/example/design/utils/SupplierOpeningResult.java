package com.example.design.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author mazc
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("obe_supplier_opening_result")
public class SupplierOpeningResult extends Model<SupplierOpeningResult> {

    private static final long serialVersionUID = 1L;

    public static String OPENING_RESULT_ID = "OPENING_RESULT_ID";

    /**
     * 主键
     */
    @TableId("OPENING_RESULT_ID")
    private String id;

    /**
     * 项目外键
     */
    @TableField("TENDER_ID")
    private String tenderId;

    /**
     * 投标人外键
     */
    @TableField("SUPPLIER_ID")
    private String supplierId;

    /**
     * 投标人开标状态
     */
    @TableField("BID_OPENING_STATUS")
    private String bidOpeningStatus;

    /**
     * 技术标解密状态
     */
    @TableField("TECHNOLOGY_BID_STATUS")
    private String technologyBidStatus;

    /**
     * 商务标解密状态
     */
    @TableField("BUSINESS_BID_STATUS")
    private String businessBidStatus;

    /**
     * 价格标解密状态
     */
    @TableField("PRICE_BID_STATUS")
    private String priceBidStatus;

    /**
     * 远程解密
     */
    @TableField("REMOTE_DECRYPT")
    private String remoteDecrypt;

    /**
     * 投标人开标意见
     */
    @TableField("BID_OPENING_OPINION")
    private String bidOpeningOpinion;

    /**
     * 确认时间
     */
    @TableField("CONFIRM_TIME")
    private LocalDateTime confirmTime;

    /**
     * 确认状态
     */
    @TableField("CONFIRM_STATUS")
    private String confirmStatus;

    /**
     * 经济标确认时间
     */
    @TableField("PRICE_CONFIRM_TIME")
    private LocalDateTime priceConfirmTime;

    /**
     * 经济标确认状态
     */
    @TableField("PRICE_CONFIRM_STATUS")
    private String priceConfirmStatus;

    /**
     * 确认意见
     */
    @TableField("CONFIRM_OPINION")
    private String confirmOpinion;

    /**
     * 废标状态
     */
    @TableField("REVOKE_STATUS")
    private String revokeStatus;

    /**
     * 废标原因
     */
    @TableField("REVOKE_REASON")
    private String revokeReason;

    /**
     * 投标人解密记录
     */
    @TableField("BID_OPENING_RECORD")
    private String bidOpeningRecord;

    /**
     * 基准价
     */
    @TableField("BASE_PRICE")
    private BigDecimal basePrice;

    /**
     * 偏差率
     */
    @TableField("DEVIATION_RATE")
    private BigDecimal deviationRate;

    /**
     * 解密时间
     */
    @TableField("BID_OPENING_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date bidOpeningTime;

    /**
     * 经济标解密时间
     */
    @TableField("PRICE_BID_OPENING_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date priceBidOpeningTime;

    /**
     * 报价文件
     */
    @TableField("PRICE_XML")
    private byte[] priceXml;

    /**
     * 原始投标报价
     */
    @TableField("ORIGINAL_BID_PRICE")
    private BigDecimal originalBidPrice;

    /**
     * 投标报价
     */
    @TableField("BID_PRICE")
    private BigDecimal bidPrice;

    /**
     * 最终报价
     */
    @TableField("FINAL_BID_PRICE")
    private BigDecimal finalBidPrice;

    /**
     * 原始评标价
     */
    @TableField("EVALUATION_PRICE_ORIGINAL")
    private BigDecimal evaluationPriceOriginal;

    /**
     * 评标价
     */
    @TableField("EVALUATION_PRICE")
    private BigDecimal evaluationPrice;

    /**
     * 币种
     */
    @TableField("CURRENCY_CODE")
    private String currencyCode;

    /**
     * 币种名称
     */
    @TableField("CURRENCY_NAME")
    private String currencyName;

    /**
     * 币种单位
     */
    @TableField("PRICE_UNIT")
    private String priceUnit;

    /**
     * 评标价修正原因
     */
    @TableField("MODIFY_REASON")
    private String modifyReason;

}
