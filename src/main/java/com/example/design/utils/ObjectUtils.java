package com.example.design.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.math.NumberUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** 
 * @Description: 
 * @author mazc 
 */
public class ObjectUtils {
	
	public static boolean isEmpty(Object obj) {
		if (null == obj) {
			return true;
		}
		return false;
	}
	
	/**
	 * @Description: 
	 * @author mazc
	 * @param str1
	 * @param str2
	 * @return 
	 */
	public static boolean equals(String str1, String str2) {
		if (null != str1 && null != str2 && str1.equals(str2)) {
			return true;
		} else if(null == str1 && null == str2){
			return true;
		}else {
			return false;	
		}
	}
	
	
	/**
	 * @Description: 
	 * @author mazc
	 * @param obj
	 * @param defaultValue
	 * @return 
	 */
	public static int getIntValue(Object obj, int defaultValue) {
		if (null == obj) return defaultValue;
		if (obj instanceof Integer) return (Integer) obj;
		try {
			return Integer.parseInt(obj.toString().trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static String getObjString(Object obj){
		if(null == obj){
			return "";
		}else {
			return obj.toString();
		}
	}
	
	public static BigDecimal getObjBigDecimal(Object obj){
        BigDecimal value = null;
        if(null == obj){
            return null;
        }else {
            try {
                value = new BigDecimal(obj.toString().trim().replace(",", ""));
            } catch (Exception e) {
                value = null;
            }
        }
        return value;
    }
	
	public static BigDecimal getObjBigDecimalDefault(Object obj){
        BigDecimal value = null;
        if(null == obj){
            return BigDecimal.ZERO;
        }else {
            try {
                value = new BigDecimal(obj.toString().trim().replace(",", ""));
            } catch (Exception e) {
                value = BigDecimal.ZERO;
            }
        }
        return value;
    }

	public static void main(String[] args) {
//		String json = "{\"code\":200102,\"message\":\"查询数据成功\",\"affectedRowNum\":157,\"count\":157,\"data\":[{\"UNIFIED_ACCEPT_CODE\":\"JSNJ20231225-GC-0003-FJ-001F1\",\"IS_ZIGEPASS\":null,\"PRICE_CURRENCY\":null,\"DATA_TIMESTAMP\":\"2024-03-11 13:23:10\",\"BID_MANAGER\":null,\"ISSECONDCY\":null,\"PUB_SERVICE_PLAT_CODE\":null,\"MARGIN_PAY_FORM\":null,\"BID_SECTION_CODE\":\"BHX23A8001-01SG        \",\"TENDER_PROJECT_CODE\":null,\"IS_COMMIT_MARGIN\":null,\"BIDDER_INFO_VERSION\":null,\"ISSENDYAOQINGHAN\":null,\"CHECKIN_TIME\":null,\"JISHUMARK\":null,\"BIDDER_ROLE\":null,\"PINGBIAO_ZUOFEI_REASON\":null,\"UNION_ORGAN_SET_CODE\":null,\"QITAMARK\":null,\"PUB_SERVICE_PLAT\":null,\"PRICE_UNIT\":null,\"MARGIN_PAY_RECEIVE_TIME\":null,\"PL_ID_CARD\":null,\"PLATFORM_CODE\":null,\"MARGIN_PAY_PRICE\":null,\"BIDDER_CODE_TYPE\":null,\"BZJISTK\":null,\"ROWGUID\":\"d9054160-c0f2-4b90-9c50-39095d3680c3\",\"UNION_CONTACT_PHONE\":null,\"EPCXMPNAME\":null,\"HELIMARK\":null,\"TRADE_PLAT\":null,\"UNION_CONTACT_NAME\":null,\"BIDDER_NAME\":\"南京众浔建设工程有限公司\",\"BZJMONEY\":0,\"FILE_MACCODE\":null,\"PAIMING\":null,\"KAIBIAODATE\":null,\"SHANGWUMARK\":null,\"UNION_ENTERPRISE_NAME\":null,\"BIAODUANGUID\":\"JSNJ20231225-GC-0003-FJ-001F1\",\"OTHER_BID_PRICE\":null,\"REMARK\":null,\"YEJIMARK\":null,\"BID_DOC_DOWNLOAD_TIME\":null,\"BIDDER_ORG_CODE\":\"91320115MA1MFXTA7M\",\"ISTUIKUANLIXI\":null,\"BID_PRICE\":4194144.64,\"PMNAME\":\"何成龙\",\"BIDDER_REGION_CODE\":null,\"ALL_UNIT_NAME\":null,\"UNIFIED_DEAL_CODE\":null,\"EPCSJPNAME\":null,\"TIME_LIMIT\":\"90\",\"NOTPASS_REASON\":null,\"PRICE_FORM_CODE\":null}]}";
//
////		Object bidPrice = 4194144.64;
////		BigDecimal BID_PRICE = ObjectUtils.getObjBigDecimal(bidPrice);
////		System.out.println(BID_PRICE);
//
//
//		JSONObject jsonObj = JSONObject.fromObject(json);
//
//
//		String headerObject = ObjectUtils.getObjString(jsonObj.get("code"));
//		if (null == headerObject || !headerObject.contains("0")) {
//		}
//		JSONArray supplierListArray = (JSONArray) jsonObj.get("data");
//		Object[] supplierJsonList = supplierListArray.toArray();
//
//		if (null != supplierJsonList && supplierJsonList.length > 0) {
//			List<Supplier> supplierList = new ArrayList<Supplier>();
//			List<SupplierOpeningResult> supplierOpeningResultList = new ArrayList<SupplierOpeningResult>();
//			for (Object object : supplierJsonList) {
//				JSONObject supplierJson = (JSONObject) object;
//
//				String BIDDER_ORG_CODE2 = ObjectUtils.getObjString(supplierJson.get("BIDDER_ORG_CODE"));
//				if (BIDDER_ORG_CODE2.equals("91320115MA1MFXTA7M")) {
//					Object b = supplierJson.get("BID_PRICE");
//					System.err.println("-----------------------");
//					System.err.println(b);
//				}
//
//			}
//		}
//
//
////		ObjectMapper objectMapper =new ObjectMapper();
//
////		    JsonNode jsonNode = objectMapper.valueToTree(json);
////			JsonNode data = jsonNode.get("data");
////			//data.findValues()
////			JsonNode jsonNode1 = data.get(1);
////			String bid_price = jsonNode1.get("BID_PRICE").asText();
////			System.out.println(bid_price);
//
//		JsonParser jsonParser =new JsonParser();
//		JsonElement rootElement = jsonParser.parse(json);
//		if(rootElement.isJsonObject()){
//			JsonObject rootJsonObject = rootElement.getAsJsonObject();
//			JsonArray dataElement = rootJsonObject.getAsJsonArray("data");
//			for(JsonElement element : dataElement){
//				if(element.isJsonObject()){
//					JsonObject jsonObject = element.getAsJsonObject();
//					BigDecimal bid_price = jsonObject.get("BID_PRICE").getAsBigDecimal();
//					//.....
//					System.out.println(bid_price);
//				}
//			}
//
//		}


		String bidPrice = "4194144.6400";
//		float bidP1=new Float(bidPrice);
		float bidP2 = Float.valueOf(bidPrice);
//		Double aDouble = Double.valueOf(bidPrice);
		Number number = NumberUtils.createNumber(bidPrice);
//		System.out.println(bidP1);
//		System.out.println(bidP2);
//		System.out.println(aDouble);
		System.out.println(number);



	}
	
}
