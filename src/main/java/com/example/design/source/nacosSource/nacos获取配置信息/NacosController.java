//package com.example.design.source.nacosSource.nacos获取配置信息;
//
//import com.alibaba.nacos.api.NacosFactory;
//import com.alibaba.nacos.api.PropertyKeyConst;
//import com.alibaba.nacos.api.config.ConfigService;
//import com.alibaba.nacos.api.config.listener.Listener;
//import com.alibaba.nacos.api.naming.NamingMaintainService;
//import com.alibaba.nacos.api.naming.NamingService;
//import com.alibaba.nacos.api.naming.pojo.Instance;
//import com.alibaba.nacos.api.naming.pojo.ListView;
//import com.alibaba.nacos.api.naming.pojo.Service;
//import com.alibaba.nacos.api.naming.pojo.ServiceInfo;
//import com.alibaba.nacos.common.utils.StringUtils;
//import lombok.SneakyThrows;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.yaml.snakeyaml.Yaml;
//import org.yaml.snakeyaml.constructor.SafeConstructor;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.*;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.Executor;
//
//import static com.example.design.source.nacosSource.nacos获取配置信息.MapUtil.getFlattenedMap;
// 放开nacos的依赖
//
///** erpm-config.yml配置文件内容
// * erpm:
// *   services:
// *     erpmPlanService:
// *       url: https://tecesb-api.ejianlong.com/public/proxy/message/0100
// *       method: post
// *       token: "i2MRmaLlyvQDg%2F7oowcafAoUa59D5LBPElpfrMRQab0lljYm9HobxnL52Xc0DRuEtIGjWukS5H1MgyfV2LOSGyWbRgIoDGPN7JpvRIlS1zw0hgEOQjoE5Q0jSAXbWR7Kk0bvb0OAalzBRzgUpjaPweo%2FYmw00pXt0g4TdZ%2B8Ec4%3D"
// *     erpmStockService:
// *       url: https://tecesb-api.ejianlong.com/public/proxy/message/0100
// *       method: post
// *       token: "i2MRmaLlyvQDg%2F7oowcafAoUa59D5LBPElpfrMRQab0lljYm9HobxnL52Xc0DRuEtIGjWukS5H1MgyfV2LOSGyWbRgIoDGPN7JpvRIlS1zw0hgEOQjoE5Q0jSAXbWR7Kk0bvb0OAalzBRzgUpjaPweo%2FYmw00pXt0g4TdZ%2B8Ec4%3D"
// *     erpmReturnOrderService:
// *       url: https://tecesb-api.ejianlong.com/public/proxy/message/0100
// *       method: post
// *       token: "i2MRmaLlyvQDg%2F7oowcafAoUa59D5LBPElpfrMRQab0lljYm9HobxnL52Xc0DRuEtIGjWukS5H1MgyfV2LOSGyWbRgIoDGPN7JpvRIlS1zw0hgEOQjoE5Q0jSAXbWR7Kk0bvb0OAalzBRzgUpjaPweo%2FYmw00pXt0g4TdZ%2B8Ec4%3D"
// *     erpmDutyUseService:
// *       url: https://tecesb-api.ejianlong.com/public/proxy/message/0100
// *       method: post
// *       token: "i2MRmaLlyvQDg%2F7oowcafAoUa59D5LBPElpfrMRQab0lljYm9HobxnL52Xc0DRuEtIGjWukS5H1MgyfV2LOSGyWbRgIoDGPN7JpvRIlS1zw0hgEOQjoE5Q0jSAXbWR7Kk0bvb0OAalzBRzgUpjaPweo%2FYmw00pXt0g4TdZ%2B8Ec4%3D"
// *     erpmQualityFeedService:
// *       url: https://tecesb-api.ejianlong.com/public/proxy/message/0100
// *       method: post
// *       token: "i2MRmaLlyvQDg%2F7oowcafAoUa59D5LBPElpfrMRQab0lljYm9HobxnL52Xc0DRuEtIGjWukS5H1MgyfV2LOSGyWbRgIoDGPN7JpvRIlS1zw0hgEOQjoE5Q0jSAXbWR7Kk0bvb0OAalzBRzgUpjaPweo%2FYmw00pXt0g4TdZ%2B8Ec4%3D"
// *     rzCloseAlarmService:
// *       url: https://sbjc.sxjlsy.cn/opensc/api/app/sxjlCloseRondsAlarm/sxjlCloseFault
// *       method: post
// *       token: "AT-9b94936fc2534d4b8c6edb401cc78123"
// */
//
//
///** 扁平化erpm-config.yml配置文件的map后
// *
// "erpm.services.erpmPlanService.url": "https://tecesb-api.ejianlong.com/public/proxy/message/0100",
// "erpm.services.erpmPlanService.method": "post",
// "erpm.services.erpmPlanService.token": "i2MRmaLlyvQDg%2F7oowcafAoUa59D5LBPElpfrMRQab0lljYm9HobxnL52Xc0DRuEtIGjWukS5H1MgyfV2LOSGyWbRgIoDGPN7JpvRIlS1zw0hgEOQjoE5Q0jSAXbWR7Kk0bvb0OAalzBRzgUpjaPweo%2FYmw00pXt0g4TdZ%2B8Ec4%3D",
// "erpm.services.erpmStockService.url": "https://tecesb-api.ejianlong.com/public/proxy/message/0100",
// "erpm.services.erpmStockService.method": "post",
// "erpm.services.erpmStockService.token": "i2MRmaLlyvQDg%2F7oowcafAoUa59D5LBPElpfrMRQab0lljYm9HobxnL52Xc0DRuEtIGjWukS5H1MgyfV2LOSGyWbRgIoDGPN7JpvRIlS1zw0hgEOQjoE5Q0jSAXbWR7Kk0bvb0OAalzBRzgUpjaPweo%2FYmw00pXt0g4TdZ%2B8Ec4%3D",
// "erpm.services.erpmReturnOrderService.url": "https://tecesb-api.ejianlong.com/public/proxy/message/0100",
// "erpm.services.erpmReturnOrderService.method": "post",
// "erpm.services.erpmReturnOrderService.token": "i2MRmaLlyvQDg%2F7oowcafAoUa59D5LBPElpfrMRQab0lljYm9HobxnL52Xc0DRuEtIGjWukS5H1MgyfV2LOSGyWbRgIoDGPN7JpvRIlS1zw0hgEOQjoE5Q0jSAXbWR7Kk0bvb0OAalzBRzgUpjaPweo%2FYmw00pXt0g4TdZ%2B8Ec4%3D",
// "erpm.services.erpmDutyUseService.url": "https://tecesb-api.ejianlong.com/public/proxy/message/0100",
// "erpm.services.erpmDutyUseService.method": "post",
// "erpm.services.erpmDutyUseService.token": "i2MRmaLlyvQDg%2F7oowcafAoUa59D5LBPElpfrMRQab0lljYm9HobxnL52Xc0DRuEtIGjWukS5H1MgyfV2LOSGyWbRgIoDGPN7JpvRIlS1zw0hgEOQjoE5Q0jSAXbWR7Kk0bvb0OAalzBRzgUpjaPweo%2FYmw00pXt0g4TdZ%2B8Ec4%3D",
// "erpm.services.erpmQualityFeedService.url": "https://tecesb-api.ejianlong.com/public/proxy/message/0100",
// "erpm.services.erpmQualityFeedService.method": "post",
// "erpm.services.erpmQualityFeedService.token": "i2MRmaLlyvQDg%2F7oowcafAoUa59D5LBPElpfrMRQab0lljYm9HobxnL52Xc0DRuEtIGjWukS5H1MgyfV2LOSGyWbRgIoDGPN7JpvRIlS1zw0hgEOQjoE5Q0jSAXbWR7Kk0bvb0OAalzBRzgUpjaPweo%2FYmw00pXt0g4TdZ%2B8Ec4%3D",
// "erpm.services.rzCloseAlarmService.url": "https://sbjc.sxjlsy.cn/opensc/api/app/sxjlCloseRondsAlarm/sxjlCloseFault",
// "erpm.services.rzCloseAlarmService.method": "post",
// "erpm.services.rzCloseAlarmService.token": "rz"
// }
// */
//@RestController
//@RequestMapping("/eam" + "/nacos")
//public class NacosController {
//    public static final String SERVER_ADDR = "10.81.0.103:8848";
//    public static final String TEST_NAMESPACE = "3be723d2-bd5d-497e-a13a-5aa4275e4331";
//
//    /****
//     * 获取erpm-config.yml配置文件内容
//     * @return
//     */
//    @SneakyThrows
//    @GetMapping("/geterpmConfig")
//     public Map<String,Object> geterpmConfigByNacosFactory() {
//         Properties properties = new Properties();
//         // nacos服务器地址，127.0.0.1:8848
//         properties.put(PropertyKeyConst.SERVER_ADDR, SERVER_ADDR);
//         // 配置中心的命名空间id
//         properties.put(PropertyKeyConst.NAMESPACE, TEST_NAMESPACE);
//         ConfigService configService = NacosFactory.createConfigService(properties);
//         // 根据dataId、group定位到具体配置文件，获取其内容. 方法中的三个参数分别是: dataId, group, 超时时间
//         String content = configService.getConfig("erpm-config.yml", "DEFAULT_GROUP", 3000L);
//         // 因为我的配置内容是JSON数组字符串，这里将字符串转为JSON数组
//
////        Yaml yaml = new Yaml();
////        return yaml.loadAs(content,  ErpmConfig.class);
//        Map<String, Object> oldMap = Collections.emptyMap();
//        Yaml yaml = new Yaml(new SafeConstructor());
//        if (StringUtils.isNotBlank(content)) {
//            oldMap = yaml.load(content);
//            oldMap = getFlattenedMap(oldMap);//获取平铺的map
//        }
//            return oldMap;
//    }
//
//    /**
//     * 获取配置内容，修改后发布
//     * @return
//     */
//    @SneakyThrows
//    @GetMapping("/publishErpConfig")
//    public Boolean publishErpConfig() {
//        Properties properties = new Properties();
//        // nacos服务器地址，127.0.0.1:8848
//        properties.put(PropertyKeyConst.SERVER_ADDR, SERVER_ADDR);
//        // 配置中心的命名空间id
//        properties.put(PropertyKeyConst.NAMESPACE, TEST_NAMESPACE);
//        ConfigService configService = NacosFactory.createConfigService(properties);
//        // 根据dataId、group定位到具体配置文件，获取其内容. 方法中的三个参数分别是: dataId, group, 超时时间
//        String content = configService.getConfig("erpm-config.yml", "DEFAULT_GROUP", 3000L);
//        // 因为我的配置内容是JSON数组字符串，这里将字符串转为JSON数组
//
////        Yaml yaml = new Yaml();
////        return yaml.loadAs(content,  ErpmConfig.class);
//        Map<String, Object> oldMap = Collections.emptyMap();
//        Yaml yaml = new Yaml(new SafeConstructor());
//        if (StringUtils.isNotBlank(content)) {
//            oldMap = yaml.load(content);
//            oldMap = getFlattenedMap(oldMap);
//        }
//        for(Map.Entry<String,Object> entry : oldMap.entrySet()){
//            if(entry.getKey().contains(".token")){
//                entry.setValue("AT-123");
//            }
//        }
//
//        HashMap newMap = new HashMap(oldMap);
//
//        Map map = unflattenMap(newMap);
//
//        String publishContent = yaml.dumpAsMap(map);
//
//        String encodedContent = URLEncoder.encode(publishContent, "UTF-8");
//
//        // 构造请求URL和参数
//        String baseUrl = "http://10.81.0.103:8848/nacos/v1/cs/configs";
//        String params = "tenant="+TEST_NAMESPACE+"&dataId=erpm-config2.yml&group=DEFAULT_GROUP&content="+encodedContent;
//        String url = baseUrl + "?" + params;
//
//
//        // 创建连接
//        URL obj = new URL(url);
//        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
//        // 设置请求方法和头部
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        connection.setDoOutput(true);  // 允许写入输出流
//
//        // 发送请求（对于POST请求，参数已经在URL中，不需要额外写入body）
//        try (OutputStream os = connection.getOutputStream()) {
//            // 如果需要在body中发送参数，可以在这里写入
//            // byte[] input = params.getBytes("utf-8");
//            // os.write(input, 0, input.length);
//        }
//
//        // 获取响应码
//        int responseCode = connection.getResponseCode();
//
//        // 读取响应内容
//        StringBuilder response = new StringBuilder();
//        try (BufferedReader in = new BufferedReader(
//                new InputStreamReader(connection.getInputStream()))) {
//            String inputLine;
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//        }
//
//        // 关闭连接
//        connection.disconnect();
//
//        //返回是否成功
//        return responseCode == HttpURLConnection.HTTP_OK;
//    }
//
//
//    @SneakyThrows
//    @GetMapping("/publishErpConfig2")
//    public Boolean publishErpConfig2() {
//        Properties properties = new Properties();
//        // nacos服务器地址，127.0.0.1:8848
//        properties.put(PropertyKeyConst.SERVER_ADDR, SERVER_ADDR);
//        // 配置中心的命名空间id
//        properties.put(PropertyKeyConst.NAMESPACE, TEST_NAMESPACE);
//        ConfigService configService = NacosFactory.createConfigService(properties);
//        // 根据dataId、group定位到具体配置文件，获取其内容. 方法中的三个参数分别是: dataId, group, 超时时间
//        String content = configService.getConfig("erpm-config.yml", "DEFAULT_GROUP", 3000L);
//        // 因为我的配置内容是JSON数组字符串，这里将字符串转为JSON数组
//
////        Yaml yaml = new Yaml();
////        return yaml.loadAs(content,  ErpmConfig.class);
//        Map<String, Object> oldMap = Collections.emptyMap();
//        Yaml yaml = new Yaml(new SafeConstructor());
//        if (StringUtils.isNotBlank(content)) {
//            oldMap = yaml.load(content);
//            oldMap = getFlattenedMap(oldMap);
//        }
//        for(Map.Entry<String,Object> entry : oldMap.entrySet()){
//            if(entry.getKey().contains(".token")){
//                entry.setValue("AT-123456789ahadfh");
//            }
//        }
//
//        HashMap newMap = new HashMap(oldMap);
//
//        Map map = unflattenMap(newMap);
//
//        String publishContent = yaml.dumpAsMap(map);
//
//        boolean result = configService.publishConfig("erpm-config2.yml", "DEFAULT_GROUP", publishContent);
//
//        //返回是否成功
//        return result;
//    }
//
//
//    @SneakyThrows
//    @GetMapping("/listenerConfigChange")
//    public Boolean listenerConfigChange(){
//        Properties properties = new Properties();
//        // nacos服务器地址，127.0.0.1:8848
//        properties.put(PropertyKeyConst.SERVER_ADDR, SERVER_ADDR);
//        // 配置中心的命名空间id
//        properties.put(PropertyKeyConst.NAMESPACE, TEST_NAMESPACE);
//        ConfigService configService = NacosFactory.createConfigService(properties);
//        CountDownLatch  countDownLatch = new CountDownLatch(1);
//        configService.addListener("erpm-config2.yml", "DEFAULT_GROUP", new Listener() {
//            @Override
//            public Executor getExecutor() {
//                return null;// 使用默认线程池
//            }
//
//            @Override
//            public void receiveConfigInfo(String configInfo) {
//                // 配置变更时的处理逻辑
//                System.out.println("配置已更新: " + configInfo);
//                countDownLatch.countDown();
//            }
//        });
//        System.out.println("等待配置更新...");
//        countDownLatch.await();
//        return true;
//    }
//
//    /**
//     * 从缓存获取的方法：
//     * namingService.getSubscribeServices():
//     * 返回当前客户端已订阅的服务列表
//     * 这些信息存储在客户端本地缓存中
//     * 通过订阅机制实时更新
//     * 实时从服务器获取的方法：
//     * namingService.getAllInstances("aitos-gateway-service"):
//     * 每次调用都会向Nacos服务器发起请求
//     * 获取指定服务的最新实例列表
//     * namingService.getServicesOfServer(1, 20):
//     * 实时查询Nacos服务器上的服务列表
//     * 支持分页查询
//     * 总结：
//     * 订阅相关数据：使用本地缓存，提高访问效率
//     * 主动查询数据：实时从服务器获取，保证数据新鲜度
//     * 这种设计平衡了性能和数据一致性需求
//     * @return
//     */
//    @SneakyThrows
//    @GetMapping("/getNamingService")
//    public Boolean getNamingService(){
//        Properties properties = new Properties();
//        // nacos服务器地址，127.0.0.1:8848
//        properties.put(PropertyKeyConst.SERVER_ADDR, SERVER_ADDR);
//        // 配置中心的命名空间id
//        properties.put(PropertyKeyConst.NAMESPACE, TEST_NAMESPACE);// 命名空间id
//        NamingService namingService = NacosFactory.createNamingService(properties);
//        // 获取所有订阅服务
//        System.out.println("getSubscribeServices()方法获取所有订阅服务");
//        List<ServiceInfo> subscribeServices = namingService.getSubscribeServices();
//        for (ServiceInfo serviceInfo : subscribeServices) {
//            System.out.println("订阅服务: " + serviceInfo.getName());
//        }
//
//        System.out.println("获取aitos-gateway-service服务实例");
//        List<Instance> allInstances = namingService.getAllInstances("aitos-gateway-service");
//        for(Instance instance : allInstances){
//            System.out.println( instance.getServiceName());
//        }
//        System.out.println("获取所有服务");
//        ListView<String> servicesOfServer = namingService.getServicesOfServer(1, 20);
//        System.out.println(servicesOfServer);
//        return true;
//    }
//
//
//    @SneakyThrows
//    @GetMapping("/getMatainService")
//    public Boolean getMatainService(){
//        Properties properties = new Properties();
//        // nacos服务器地址，127.0.0.1:8848
//        properties.put(PropertyKeyConst.SERVER_ADDR, SERVER_ADDR);
//        // 配置中心的命名空间id
//        properties.put(PropertyKeyConst.NAMESPACE, TEST_NAMESPACE);// 命名空间id
//        NamingMaintainService maintainService = NacosFactory.createMaintainService(properties);
//        Service service = maintainService.queryService("aitos-gateway-service", "DEFAULT_GROUP");
//        System.out.println("查询的服务名称为:"+service.getName());
//
//        return true;
//    }
//
//    /**
//     * 关键点说明
//     * 服务必须存在：确保 myService 已经通过 createService 创建
//     * 实例信息完整：至少需要 IP 地址和端口号
//     * 自动关联：注册的实例会自动关联到对应的服务名称
//     * 健康检查：注册后 Nacos 会根据配置进行健康检查
//     * @return
//     */
//    @SneakyThrows
//    @GetMapping("/createServiceAndInstance")
//    public Boolean createServiceAndInstance(){
//        Properties properties = new Properties();
//        // nacos服务器地址，127.0.0.1:8848
//        properties.put(PropertyKeyConst.SERVER_ADDR, SERVER_ADDR);
//        // 配置中心的命名空间id
//        properties.put(PropertyKeyConst.NAMESPACE, TEST_NAMESPACE);// 命名空间id
//        NamingMaintainService maintainService = NacosFactory.createMaintainService(properties);
//        maintainService.createService("myService");
//
///*        //1. 基本注册方式
//        // 创建命名服务实例
//        NamingService namingService = NacosFactory.createNamingService("127.0.0.1:8848");
//
//        // 注册实例到已存在的服务
//        namingService.registerInstance("myService", "127.0.0.1", 8080);*/
//
//        //2. 带详细信息的注册方式
//        NamingService namingService = NacosFactory.createNamingService("127.0.0.1:8848");
//        // 创建实例对象
//        Instance instance = new Instance();
//        instance.setIp("127.0.0.1");
//        instance.setPort(8080);
//        instance.setWeight(1.0);
//        instance.setHealthy(true);
//        instance.setEnabled(true);
//        instance.setEphemeral(true);
//
//        // 可以添加元数据
//        Map<String, String> metadata = new HashMap<>();
//        metadata.put("version", "1.0");
//        metadata.put("region", "dev");
//        instance.setMetadata(metadata);
//
//        // 注册实例
//        namingService.registerInstance("myService","Default_GROUP", instance);
//
//
//
//
//        return true;
//    }
//
//
//
//    public Map<String, Object> unflattenMap(Map<String, Object> flatMap) {
//        Map<String, Object> result = new LinkedHashMap<>();
//
//        for (Map.Entry<String, Object> entry : flatMap.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//
//            // 按照点号分割键路径
//            String[] parts = key.split("\\.");
//            Map<String, Object> current = result;
//
//            // 遍历路径，逐层创建嵌套结构
//            for (int i = 0; i < parts.length - 1; i++) {
//                String part = parts[i];
//                if (!current.containsKey(part)) {
//                    current.put(part, new LinkedHashMap<>());
//                }
//                current = (Map<String, Object>) current.get(part);
//            }
//
//            // 设置最终值
//            current.put(parts[parts.length - 1], value);
//        }
//
//        return result;
//    }
//
//
//    public Map<String, Object> flattenMap(Map<String, Object> map) {
//        Map<String, Object> result = new LinkedHashMap<>();
//        flattenMapRecursive(map, "", result);
//        return result;
//    }
//
//    private void flattenMapRecursive(Map<String, Object> map, String prefix, Map<String, Object> result) {
//        for (Map.Entry<String, Object> entry : map.entrySet()) {
//            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
//            Object value = entry.getValue();
//
//            if (value instanceof Map) {
//                // 递归处理嵌套 Map
//                flattenMapRecursive((Map<String, Object>) value, key, result);
//            } else {
//                // 直接放入结果
//                result.put(key, value);
//            }
//        }
//    }
//}
