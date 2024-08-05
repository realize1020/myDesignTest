package com.example.design.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
//import com.gx.obe.server.management.evaluation.entity.BidPriceResult;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: 投标人
 * @author mazc 
 */

@TableName("obe_supplier")
public class Supplier extends Model<Supplier> {
	private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId("SUPPLIER_ID")
     private String id;
    /**
     * 投标人名称
     */
    @TableField("SUPPLIER_NAME")
        private String supplierName;
    /**
     * 投标人编号
     */
    @TableField("ORG_CODE")
        private String orgCode;
    /**
     * 所属投标项目或标段
     */
    @TableField("TENDER_ID")
        private String tenderId;
    /**
     * 联系人
     */
    @TableField("LINKER")
        private String linker;
    /**
     * 身份证号
     */
    @TableField("ID_CARD")
        private String idCard;
    /**
     * 联系地址
     */
    @TableField("ADDRESS")
        private String address;
    /**
     * 联系电话
     */
    @TableField("LINKER_TEL")
        private String linkerTel;
    /**
     * 邮编
     */
    @TableField("ZIPCODE")
        private String zipcode;
    /**
     * 备注
     */
    @TableField("MEMO")
        private String memo;
    /**
     * 投标文件下载申请地址
     */
    @TableField("APPLY_URL")
        private String applyUrl;
    /**
     * 报名时间
     */
    @TableField("APPLY_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date applyTime;
    /**
     * 投标时间
     */
    @TableField("TENDER_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date tenderTime;
    /**
     * 签到类型
     */
    @TableField("SIGN_TYPE")
        private String signType;
    /**
     * 投标保证金
     */
    @TableField("BID_BOND")
        private BigDecimal bidBond;
    /**
     * 投标保证金缴纳类型
     */
    @TableField("BID_BOND_TYPE")
        private String bidBondType;
    /**
     * 标书费
     */
    @TableField("TENDER_FEE")
        private BigDecimal tenderFee;
    /**
     * 使用状态
     */
    @TableField("USE_STATUS")
        private String useStatus;
    /**
     * 排序
     */
    @TableField("SORT_NO")
        private Integer sortNo;
    /**
     * 投标编号
     */
    @TableField("BID_NUMBER")
        private String bidNumber;
    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
    /**
     * 签到人
     */
    @TableField("SIGN_PERSON")
        private String signPerson;
    /**
     * 签到人联系电话
     */
    @TableField("SIGN_PERSON_TEL")
        private String signPersonTel;
    /**
     * 标书类型
     */
    @TableField("BID_FILE_TYPE")
        private String bidFileType;
    /**
     * 签到人身份证
     */
    @TableField("SIGN_PERSON_CARD")
        private String signPersonCard;
    /**
     * 签到时间
     */
    @TableField("SIGN_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date signTime;
    /**
     * 经济标签到时间
     */
    @TableField("PRICE_SIGN_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date priceSignTime;
    /**
     * 经济标签到人
     */
    @TableField("PRICE_SIGN_PERSON")
        private String priceSignPerson;
    /**
     * 经济标签到人联系电话
     */
    @TableField("PRICE_SIGN_PERSON_TEL")
        private String priceSignPersonTel;
    /**
     * 经济标签到人身份证
     */
    @TableField("PRICE_SIGN_PERSON_CARD")
        private String priceSignPersonCard;
    /**
     * 邮箱地址
     */
    @TableField("EMAIL")
        private String email;
    /**
     *  社会统一编码
     */
    @TableField("COMPANY_CODE") 
    private String companyCode;
    
    @TableField("SOURCE_ID") 
    private String sourceId;
    
    
    
    
    public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}



	/**
     * 投标文件下载时间
     */
    @TableField("BID_FILE_DOWNLOAD_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date bidFileDownloadTime;
    /**
     * 标书下载时记录的投标时间（用于验证已下载的是否最新的标书）
     */
    @TableField("DOWNLOAD_TIME_TENDER_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date downloadTimeTenderTime;
    /**
     * 签到文件字节流
     */
    @TableField("SIGN_FILE")
        private byte[] signFile;
    /**
     * 暗标编码
     */
    @TableField("SEALED_BID_CODE")
        private String sealedBidCode;
    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date modifyTime;
    /**
     * mainid
     */
    @TableField("ORG_ID")
    private String orgId;
    
    @TableField("INVITATION_STATUS")
    private String invitationStatus;//视频会议邀请状态//00，取消，01，邀请
    
    @TableField("OPENFILE_SIGNSTATUS")
    private String openFileSignStatus;//开标一览表签章状态
    
    @TableField("IS_BASIC_ACCOUNT")
    private String isBasicAccount;
    
    @TableField("BOND_IN_DATE")
    private String inDate;
    @TableField("TB_FILE_ATTCH_GUID")
    private String TBFileAttchGuid;
    
	
	public String getIsBasicAccount() {
		return isBasicAccount;
	}
	public void setIsBasicAccount(String isBasicAccount) {
		this.isBasicAccount = isBasicAccount;
	}
	public String getInDate() {
		return inDate;
	}
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	public String getInvitationStatus() {
		return invitationStatus;
	}
	public void setInvitationStatus(String invitationStatus) {
		this.invitationStatus = invitationStatus;
	}

	public String getOpenFileSignStatus() {
		return openFileSignStatus;
	}
	public void setOpenFileSignStatus(String openFileSignStatus) {
		this.openFileSignStatus = openFileSignStatus;
	}



	@TableField(exist = false)
	private SupplierOpeningResult supplierOpeningResult;
//
//	@TableField(exist = false)
//	private SupplierEvaluationResult supplierEvaluationResult;
//
//	@TableField(exist = false)
//	private List<BidPriceResult> bidPriceResultList;

	@TableField(exist = false)
	private String anonymName ="";// 投标人匿名名称
	
//	@TableField(exist = false)
//	private ProjectRule projectRule;// 项目规则
	
	@TableField(exist = false)
	private boolean isAnonymous = false;// 是否匿名
	
	@TableField(exist = false)
	private Integer anonymousIndex  = 1;// 匿名投标人序号

    /**
     * 供应商合格状态 (00非合格 01合格 03查询失败)
     */
    @TableField("QUALIFIED_STATUS")
    private String qualifiedStatus ; // 供应商合格状态
    @TableField("ATTACH_GUID")
    private String attachGuid ; // 供应商合格状态

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSupplierName(){
        return supplierName;
        }

        public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
        }

    public String getOrgCode(){
        return orgCode;
        }

        public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
        }

    public String getTenderId(){
        return tenderId;
        }

        public void setTenderId(String tenderId) {
        this.tenderId = tenderId;
        }

    public String getLinker(){
        return linker;
        }

        public void setLinker(String linker) {
        this.linker = linker;
        }

    public String getIdCard(){
        return idCard;
        }

        public void setIdCard(String idCard) {
        this.idCard = idCard;
        }

    public String getAddress(){
        return address;
        }

        public void setAddress(String address) {
        this.address = address;
        }

    public String getLinkerTel(){
        return linkerTel;
        }

        public void setLinkerTel(String linkerTel) {
        this.linkerTel = linkerTel;
        }

    public String getZipcode(){
        return zipcode;
        }

        public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
        }

    public String getMemo(){
        return memo;
        }

        public void setMemo(String memo) {
        this.memo = memo;
        }

    public String getApplyUrl(){
        return applyUrl;
        }

        public void setApplyUrl(String applyUrl) {
        this.applyUrl = applyUrl;
        }

    public Date getApplyTime(){
        return applyTime;
        }

        public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
        }

    public Date getTenderTime(){
        return tenderTime;
        }

        public void setTenderTime(Date tenderTime) {
        this.tenderTime = tenderTime;
        }

    public String getSignType(){
        return signType;
        }

        public void setSignType(String signType) {
        this.signType = signType;
        }

    public BigDecimal getBidBond(){
        return bidBond;
        }

        public void setBidBond(BigDecimal bidBond) {
        this.bidBond = bidBond;
        }

    public String getBidBondType(){
        return bidBondType;
        }

        public void setBidBondType(String bidBondType) {
        this.bidBondType = bidBondType;
        }

    public BigDecimal getTenderFee(){
        return tenderFee;
        }

        public void setTenderFee(BigDecimal tenderFee) {
        this.tenderFee = tenderFee;
        }

    public String getUseStatus(){
        return useStatus;
        }

        public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
        }

    public Integer getSortNo(){
        return sortNo;
        }

        public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
        }

    public String getBidNumber(){
        return bidNumber;
        }

        public void setBidNumber(String bidNumber) {
        this.bidNumber = bidNumber;
        }

    public Date getCreateTime(){
        return createTime;
        }

        public void setCreateTime(Date createTime) {
        this.createTime = createTime;
        }

    public String getSignPerson(){
        return signPerson;
        }

        public void setSignPerson(String signPerson) {
        this.signPerson = signPerson;
        }

    public String getSignPersonTel(){
        return signPersonTel;
        }

        public void setSignPersonTel(String signPersonTel) {
        this.signPersonTel = signPersonTel;
        }

    public String getBidFileType(){
        return bidFileType;
        }

        public void setBidFileType(String bidFileType) {
        this.bidFileType = bidFileType;
        }

    public String getSignPersonCard(){
        return signPersonCard;
        }

        public void setSignPersonCard(String signPersonCard) {
        this.signPersonCard = signPersonCard;
        }

    public Date getSignTime(){
        return signTime;
        }

        public void setSignTime(Date signTime) {
        this.signTime = signTime;
        }

    public Date getPriceSignTime(){
        return priceSignTime;
        }

        public void setPriceSignTime(Date priceSignTime) {
        this.priceSignTime = priceSignTime;
        }

    public String getPriceSignPerson(){
        return priceSignPerson;
        }

        public void setPriceSignPerson(String priceSignPerson) {
        this.priceSignPerson = priceSignPerson;
        }

    public String getPriceSignPersonTel(){
        return priceSignPersonTel;
        }

        public void setPriceSignPersonTel(String priceSignPersonTel) {
        this.priceSignPersonTel = priceSignPersonTel;
        }

    public String getPriceSignPersonCard(){
        return priceSignPersonCard;
        }

        public void setPriceSignPersonCard(String priceSignPersonCard) {
        this.priceSignPersonCard = priceSignPersonCard;
        }

    public String getEmail(){
        return email;
        }

        public void setEmail(String email) {
        this.email = email;
        }

    public Date getBidFileDownloadTime(){
        return bidFileDownloadTime;
        }

        public void setBidFileDownloadTime(Date bidFileDownloadTime) {
        this.bidFileDownloadTime = bidFileDownloadTime;
        }

    public Date getDownloadTimeTenderTime(){
        return downloadTimeTenderTime;
        }

        public void setDownloadTimeTenderTime(Date downloadTimeTenderTime) {
        this.downloadTimeTenderTime = downloadTimeTenderTime;
        }

    public byte[] getSignFile(){
        return signFile;
        }

        public void setSignFile(byte[] signFile) {
        this.signFile = signFile;
        }

    public String getSealedBidCode(){
        return sealedBidCode;
        }

        public void setSealedBidCode(String sealedBidCode) {
        this.sealedBidCode = sealedBidCode;
        }

    public Date getModifyTime(){
        return modifyTime;
        }

        public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        }

    public String getOrgId(){
        return orgId;
        }

        public void setOrgId(String orgId) {
        this.orgId = orgId;
        }
        

    public SupplierOpeningResult getSupplierOpeningResult() {
			return supplierOpeningResult;
		}
//
		public void setSupplierOpeningResult(SupplierOpeningResult supplierOpeningResult) {
			this.supplierOpeningResult = supplierOpeningResult;
		}
//
//		public SupplierEvaluationResult getSupplierEvaluationResult() {
//			return supplierEvaluationResult;
//		}
//
//		public void setSupplierEvaluationResult(SupplierEvaluationResult supplierEvaluationResult) {
//			this.supplierEvaluationResult = supplierEvaluationResult;
//		}


	public String getAnonymName() {
			return anonymName;
		}

		public void setAnonymName(String anonymName) {
			this.anonymName = anonymName;
		}

		public boolean isAnonymous() {
			return isAnonymous;
		}

		public void setAnonymous(boolean isAnonymous) {
			this.isAnonymous = isAnonymous;
		}

		public Integer getAnonymousIndex() {
			return anonymousIndex;
		}

		public void setAnonymousIndex(Integer anonymousIndex) {
			this.anonymousIndex = anonymousIndex;
		}

//	public List<BidPriceResult> getBidPriceResultList() {
//			return bidPriceResultList;
//		}
//
//		public void setBidPriceResultList(List<BidPriceResult> bidPriceResultList) {
//			this.bidPriceResultList = bidPriceResultList;
//		}

    public String getQualifiedStatus() {
        return qualifiedStatus;
    }

    public void setQualifiedStatus(String qualifiedStatus) {
        this.qualifiedStatus = qualifiedStatus;
    }
    
    

	public String getAttachGuid() {
		return attachGuid;
	}
	public void setAttachGuid(String attachGuid) {
		this.attachGuid = attachGuid;
	}



	public String getTBFileAttchGuid() {
		return TBFileAttchGuid;
	}
	public void setTBFileAttchGuid(String tBFileAttchGuid) {
		TBFileAttchGuid = tBFileAttchGuid;
	}

	public static final String SUPPLIER_ID ="SUPPLIER_ID";

    public static final String SUPPLIER_NAME ="SUPPLIER_NAME";

    public static final String ORG_CODE ="ORG_CODE";

    public static final String TENDER_ID ="TENDER_ID";

    public static final String LINKER ="LINKER";

    public static final String ID_CARD ="ID_CARD";

    public static final String ADDRESS ="ADDRESS";

    public static final String LINKER_TEL ="LINKER_TEL";

    public static final String ZIPCODE ="ZIPCODE";

    public static final String MEMO ="MEMO";

    public static final String APPLY_URL ="APPLY_URL";

    public static final String APPLY_TIME ="APPLY_TIME";

    public static final String TENDER_TIME ="TENDER_TIME";

    public static final String SIGN_TYPE ="SIGN_TYPE";

    public static final String BID_BOND ="BID_BOND";

    public static final String BID_BOND_TYPE ="BID_BOND_TYPE";

    public static final String TENDER_FEE ="TENDER_FEE";

    public static final String USE_STATUS ="USE_STATUS";

    public static final String SORT_NO ="SORT_NO";

    public static final String BID_NUMBER ="BID_NUMBER";

    public static final String CREATE_TIME ="CREATE_TIME";

    public static final String SIGN_PERSON ="SIGN_PERSON";

    public static final String SIGN_PERSON_TEL ="SIGN_PERSON_TEL";

    public static final String BID_FILE_TYPE ="BID_FILE_TYPE";

    public static final String SIGN_PERSON_CARD ="SIGN_PERSON_CARD";

    public static final String SIGN_TIME ="SIGN_TIME";

    public static final String PRICE_SIGN_TIME ="PRICE_SIGN_TIME";

    public static final String PRICE_SIGN_PERSON ="PRICE_SIGN_PERSON";

    public static final String PRICE_SIGN_PERSON_TEL ="PRICE_SIGN_PERSON_TEL";

    public static final String PRICE_SIGN_PERSON_CARD ="PRICE_SIGN_PERSON_CARD";

    public static final String EMAIL ="EMAIL";

    public static final String BID_FILE_DOWNLOAD_TIME ="BID_FILE_DOWNLOAD_TIME";

    public static final String DOWNLOAD_TIME_TENDER_TIME ="DOWNLOAD_TIME_TENDER_TIME";

    public static final String SIGN_FILE ="SIGN_FILE";

    public static final String SEALED_BID_CODE ="SEALED_BID_CODE";

    public static final String MODIFY_TIME ="MODIFY_TIME";

    public static final String ORG_ID ="ORG_ID";
    public static final String IS_BASIC_ACCOUNT ="IS_BASIC_ACCOUNT";
    public static final String BOND_IN_DATE ="BOND_IN_DATE";

@Override
protected Serializable pkVal(){
        return this.id;
    }

@Override
public String toString() {//supplierId
    return "Supplier{" +
            ", id=" + id +
            ", supplierName=" + supplierName +
            ", orgCode=" + orgCode +
            ", tenderId=" + tenderId +
            ", linker=" + linker +
            ", idCard=" + idCard +
            ", address=" + address +
            ", linkerTel=" + linkerTel +
            ", zipcode=" + zipcode +
            ", memo=" + memo +
            ", applyUrl=" + applyUrl +
            ", applyTime=" + applyTime +
            ", tenderTime=" + tenderTime +
            ", signType=" + signType +
            ", bidBond=" + bidBond +
            ", bidBondType=" + bidBondType +
            ", tenderFee=" + tenderFee +
            ", useStatus=" + useStatus +
            ", sortNo=" + sortNo +
            ", bidNumber=" + bidNumber +
            ", createTime=" + createTime +
            ", signPerson=" + signPerson +
            ", signPersonTel=" + signPersonTel +
            ", bidFileType=" + bidFileType +
            ", signPersonCard=" + signPersonCard +
            ", signTime=" + signTime +
            ", priceSignTime=" + priceSignTime +
            ", priceSignPerson=" + priceSignPerson +
            ", priceSignPersonTel=" + priceSignPersonTel +
            ", priceSignPersonCard=" + priceSignPersonCard +
            ", email=" + email +
            ", bidFileDownloadTime=" + bidFileDownloadTime +
            ", downloadTimeTenderTime=" + downloadTimeTenderTime +
            ", signFile=" + signFile +
            ", sealedBidCode=" + sealedBidCode +
            ", modifyTime=" + modifyTime +
            ", orgId=" + orgId +
            ", invitationStatus=" + invitationStatus +
            ", openFileSignStatus=" + openFileSignStatus +
            ", isBasicAccount=" + isBasicAccount +
            ", inDate=" + inDate +
    "}";
    }

    }
