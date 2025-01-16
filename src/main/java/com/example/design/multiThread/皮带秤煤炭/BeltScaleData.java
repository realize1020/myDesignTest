package com.example.design.multiThread.皮带秤煤炭;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 皮带秤数据表
 * </p>
 *
 * @author wyn
 * @since 2024-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BeltScaleData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
      private String id;

    /**
     * 明细磅单号
     */
    private String zmxdocNo;

    /**
     * 所属二级公司
     */
    private String ssejdw;

    /**
     * 所属二级公司代码
     */
    private String ssejdwid;

    /**
     * 所属三级公司
     */
    private String sssjdw;

    /**
     * 所属三级公司代码
     */
    private String sssjdwid;

    /**
     * 煤源矿点名称
     */
    private String mykuangdianmc;

    /**
     * 煤源矿点编码
     */
    private String mykuangdianbm;

    /**
     * 发运站名称
     */
    private String zsitename;

    /**
     * 发运站编码
     */
    private String zsite;

    /**
     * 收货单位名称（终到用户）
     */
    private String zname1;

    /**
     * 收货单位编号（终到用户）
     */
    private String zkunnr;

    /**
     * 煤炭来源
     */
    private String meitanlaiyuan;

    /**
     * 煤炭来源编码
     */
    private String meitanlaiyuanbm;

    /**
     * 煤种
     */
    private String meizhong;

    /**
     * 煤种编码
     */
    private String meizhongbm;

    /**
     * 煤源类型编码
     */
    private String myleixingbm;

    /**
     * 销售组织描述
     */
    private String vtext;

    /**
     * 销售组织代码
     */
    private String vkorg;

    /**
     * 数据上传方式
     */
    private String sjscfs;

    /**
     * 输送开始日期
     */
    private String zbeginI;

    /**
     * 输送开始时间
     */
    private String zbeginT;

    /**
     * 输送停止日期
     */
    private String zendI;

    /**
     * 输送停止时间
     */
    private String zendT;

    /**
     * 计量衡器名称
     */
    private String jlhqmc;

    /**
     * 计量衡器ID
     */
    private String jlhqmcid;

    /**
     * 发运量
     */
    private String zmeng;

    /**
     * 皮带秤起始底码
     */
    private String pdcqishidima;

    /**
     * 皮带秤截止底码
     */
    private String pdcjiezhidima;

    /**
     * 水尺计重煤量
     */
    private String shuichijizhongml;

    /**
     * 计量单位
     */
    private String jiliangdanwei;

    /**
     * 主磅单号
     */
    private String zmasterNo;

    /**
     * 机组
     */
    private String jizu;

    /**
     * 班值
     */
    private String banzhi;

    /**
     * 数据推送单位
     */
    private String sjtsdanwei;

    /**
     * 数据源系统
     */
    private String sjyxt;

    /**
     * 数据推送日期
     */
    private String xtscjlI;

    /**
     * 数据推送时间
     */
    private String xtscjlT;

    /**
     * 计量衡器用途
     */
    private String jlhqyt;

    /**
     * 备用字段1
     */
    private String byzd1;

    /**
     * 备用字段2
     */
    private String byzd2;

    /**
     * 备用字段3
     */
    private String byzd3;

    /**
     * 备用字段4
     */
    private String byzd4;

    /**
     * 备用字段5
     */
    private String byzd5;

    /**
     * 备用字段6
     */
    private String byzd6;

    /**
     * 备用字段7
     */
    private String byzd7;

    /**
     * 备用字段8
     */
    private String byzd8;

    /**
     * 备用字段9
     */
    private String byzd9;

    /**
     * 备用字段10
     */
    private String byzd10;

    /**
     * 数据是否已发送，1：已发送，0：未发送
     */
    private Integer send;


}
