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
     *      filter：根据区划过滤，按照指定行政区划进行过滤，填入后则只返回该省/直辖市信息，需填入adcode，为了保证数据的正确，强烈建议填入此参数（配合IP定位使用）
     *     其他参数暂不需要
     * 返回结果（这里的注解只显示重要参数）：
     *      country:国家
     *      province:省份（直辖市会在province和city显示）
     *      city:市（直辖市会在province和city显示）
     *      street:街道（该项目不需要该结果）
     */
    public static String amapDistrict = "https://restapi.amap.com/v3/config/district";

    /**
     * 高德IP定位API
     * 参数：
     *      key：请求服务权限标识
     *      ip：ip地址，需要搜索的IP地址（仅支持国内），若用户不填写IP，则取客户http之中的请求来进行定位
     *
     */
    public static String amapIp = "https://restapi.amap.com/v3/ip";
}
