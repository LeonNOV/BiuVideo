package com.leon.biuvideo.values.apis;

public class AmapAPIs {

    /**
     * 高德天气查询接口：https://developer.amap.com/api/webservice/guide/api/weatherinfo
     * 参数：
     *      key:请求服务权限标识
     *      city:城市编码
     *      extensions:气象类型;可选值：base/all
     *          base:返回实况天气
     *          all:返回预报天气
     */
    public static String amapWeather = "https://restapi.amap.com/v3/weather/weatherInfo";

    /**
     * 高德行政区域查询接口：https://developer.amap.com/api/webservice/guide/api/district
     * 参数：
     *      key：请求服务权限标识
     *      keywords：查询关键字
     *              规则：只支持单个关键词语搜索关键词支持：行政区名称、citycode、adcode
     *              例如，在subdistrict=2，搜索省份（例如山东），能够显示市（例如济南），区（例如历下区）
     *      subdistrict：子级行政区（默认为0）
     *              规则：设置显示下级行政区级数（行政区级别包括：国家、省/直辖市、市、区/县、乡镇/街道多级数据）
     *              可选值：0、1、2、3等数字，并以此类推
     *              0：不返回下级行政区；
     *              1：返回下一级行政区；
     *              2：返回下两级行政区；
     *              3：返回下三级行政区；
     *              需要在此特殊说明，目前部分城市和省直辖县因为没有区县的概念，故在市级下方直接显示街道。
     *              例如：广东-东莞、海南-文昌市
     *      filter：根据区划过滤，按照指定行政区划进行过滤，填入后则只返回该省/直辖市信息，需填入adcode，为了保证数据的正确，强烈建议填入此参数
     *     其他参数暂不需要
     * 返回结果（这里的注释只显示重要参数）：
     *      country:国家
     *      province:省份（直辖市会在province和city显示）
     *      city:市（直辖市会在province和city显示）
     *      center：区域中心点
     *      street:街道（该项目不需要该结果）
     */
    public static String amapDistrict = "https://restapi.amap.com/v3/config/district";

    /**
     * 高德地理编码API:https://developer.amap.com/api/webservice/guide/api/georegeo
     * 参数（主要参数）
     *      address:结构化地址信息
     *              规则遵循：国家、省份、城市、区县、城镇、乡村、街道、门牌号码、屋邨、大厦
     *              如：北京市朝阳区阜通东大街6号。如果需要解析多个地址的话，请用"|"进行间隔
     *              并且将 batch 参数设置为 true，最多支持 10 个地址进进行"|"分割形式的请求。
     *      city:指定查询的城市
     *              可选输入内容包括：指定城市的中文（如北京）、指定城市的中文全拼（beijing）、citycode（010）、adcode（110000）
     *              不支持县级市。当指定城市查询内容为空时，会进行全国范围内的地址转换检索。
     */
    public static String amapGeocode = "https://restapi.amap.com/v3/geocode/geo";
}
