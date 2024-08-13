package com.example.wangjian.syslog1.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.wangjian.syslog1.entity.AliPay;
import com.example.wangjian.syslog1.entity.ProductFrom;
import com.example.wangjian.syslog1.service.ProductService;
import com.example.wangjian.syslog1.service.WxPayService;
import com.example.wangjian.syslog1.util.QRCodeUtil;
import com.example.wangjian.syslog1.util.UploadFileUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Result;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("wxpay")
@Transactional(rollbackFor = Exception.class)
public class WxPayController {


    @Autowired
    private ProductService productService;

    /** 商户API私钥路径 */
    @Value("${wechat.pay.privateKeyPath}")
    public  String privateKeyPath;

    /** 商户证书序列号 */
    @Value("${wechat.pay.merchantSerialNumber}")
    public  String merchantSerialNumber;

    /** 商户APIV3密钥 */
    @Value("${wechat.pay.apiV3key}")
    public  String apiV3key;

    /** AppId */
    @Value("${wechat.pay.appId}")
    public String appId;

    /** 商户号 */
    @Value("${wechat.pay.mchId}")
    public String mchId;

    /** 支付回调地址 */
    @Value("${wechat.pay.notifyUrl}")
    private String notifyUrl;

    @Value("${wechat.pay.platformCertPath}")
    private String platformCertPath;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private ResourceLoader resourceLoader;



    private static final String SE_NUM = "5BC5958BD5AEAF67149B3AAEDB741AA8D7B8F6D5";

    @PostMapping("/generateWxOrder11")  // 注意这里必须是POST接口
    public String generateWxOrder11( @RequestParam(value="orderId",required=true)String orderId,
                                   @RequestParam(value="quantity",required=true)int quantity,
                                   @RequestParam(value="userGame",required=true)String userGame,
                                   @RequestParam(value="phone",required=true)String phone,
                                   @RequestParam(value="ids",required=true)String ids,
                                   @RequestParam(value="serviceRange",required=true)String serviceRange) throws Exception {

        return "111111111111111111";
    }


//    public static String uploadFile(MultipartFile file,HttpServletRequest request) throws IllegalStateException, IOException{
//        Logger logger = Logger.getLogger(UploadImg.class);
//        if(file!=null){
//            //获取上传文件的原始名称
//            String originalFilename = file.getOriginalFilename();
//            String newFileName ="";
//            String pic_path;
//            // 上传图片
//            if ( originalFilename != null && originalFilename.length() > 0) {
//                //获取Tomcat服务器所在的路径
//                String tomcat_path = System.getProperty( "user.dir" );
//                System.out.println(tomcat_path);
//                //获取Tomcat服务器所在路径的最后一个文件目录
//                String bin_path = tomcat_path.substring(tomcat_path.lastIndexOf("\\")+1,tomcat_path.length());
//                System.out.println(bin_path);
//                //若最后一个文件目录为bin目录，则服务器为手动启动
//                if(("bin").equals(bin_path)){//手动启动Tomcat时获取路径为：D:\Software\Tomcat-8.5\bin
//                    //获取保存上传图片的文件路径
//                    pic_path = tomcat_path.substring(0,System.getProperty( "user.dir" ).lastIndexOf("\\")) +"\\webapps"+"\\pic_file\\";
//                }else{//服务中自启动Tomcat时获取路径为：D:\Software\Tomcat-8.5
//                    pic_path = tomcat_path+"\\webapps"+"\\pic_file\\";
//                }
//                // 新的图片名称
//                newFileName =UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
//                logger.info("上传图片的路径：" + pic_path + newFileName);
//                // 新图片
//                File newFile = new File(pic_path + newFileName);
//                // 将内存中的数据写入磁盘
//                file.transferTo(newFile);
//            }
//            return newFileName;
//        }else{
//            return null;
//        }
//    }



    public String uploadFile(MultipartFile image) throws Exception {
        String os = System.getProperty("os.name");
        File imagePath;  //封面图片存放地址
        File fileRealPath;   //文件存放地址
        if (os.toLowerCase().startsWith("win")) {  //windows系统
            String path = System.getProperty("user.dir");  //获取项目相对路径
            imagePath = new File(path+"/src//main/resources/static/images");
        }else{//linux系统
            //获取根目录
            //如果是在本地windows环境下，目录为项目的target\classes下
            //如果是linux环境下，目录为jar包同级目录
            File rootPath = new File(ResourceUtils.getURL("classpath:").getPath());
            if(!rootPath.exists()){
                rootPath = new File("");
            }
            imagePath = new File(rootPath.getAbsolutePath()+"/images");
        }

        if(!imagePath.exists()){
            //不存在，创建
            imagePath.mkdirs();
        }
        //获取文件名称

        String imageName = image.getOriginalFilename();

        //创建图片存放地址
        File imageResultPath = new File(imagePath+"/"+imageName);
        if(imageResultPath.exists()){
            log.warn("图片已经存在！");
            return "false！";
        }
        image.transferTo(imageResultPath);
        System.out.println("imageResultPath:"+imageResultPath.getCanonicalPath());
        return "true！";
    }

    public String uploadFile1(MultipartFile image) throws Exception {
        String os = System.getProperty("os.name");
        File imagePath;  //封面图片存放地址
        File fileRealPath;   //文件存放地址
        if (os.toLowerCase().startsWith("win")) {  //windows系统
            String path = System.getProperty("user.dir");  //获取项目相对路径
            imagePath = new File(path+"/src//main/resources/static/images");
        }else{//linux系统
            //获取根目录
            //如果是在本地windows环境下，目录为项目的target\classes下
            //如果是linux环境下，目录为jar包同级目录
            File rootPath = new File(ResourceUtils.getURL("classpath:/resources/").getPath());
            if(!rootPath.exists()){
                rootPath = new File("");
            }
            imagePath = new File(rootPath.getAbsolutePath()+"/images");
        }

        if(!imagePath.exists()){
            //不存在，创建
            imagePath.mkdirs();
        }
        //获取文件名称

        String imageName = image.getOriginalFilename();

        //创建图片存放地址
        File imageResultPath = new File(imagePath+"/"+imageName);
        if(imageResultPath.exists()){
            log.warn("图片已经存在！");
            return "false！";
        }
        image.transferTo(imageResultPath);
        System.out.println("imageResultPath:"+imageResultPath.getCanonicalPath());
        return "true！";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        if(file.isEmpty()){
            log.error("上传失败，请选择文件！");
            return "redirect:/getAllFile";
        }
        try {
            String result = uploadFile(file);
            log.info(result);
            return "redirect:/getAllFile";
        } catch (Exception e) {
            e.printStackTrace();
            log.error("文件上传失败！");
            return "redirect:/getAllFile";
        }



    }


    public String getStaticResourcePath(String path) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:/resources/static/" + path);
        return resource.getFile().getAbsolutePath();
    }

    public static void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = new File(filePath).toPath();
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    private static final String STATIC_DIR = "src/main/resources/static/images/";
    @GetMapping("/generateQrCode")
    public String generateQrCode(String content) throws WriterException, IOException {
        try {
            String text = "Hello, World!";
            int width = 300;
            int height = 300;
            String filePath = STATIC_DIR + "hello_world.png";
            generateQRCodeImage(text, width, height, filePath);
            return "QR Code generated successfully";
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return "Error generating QR Code: " + e.getMessage();
        }
    }

    @GetMapping("/generateQrCode-linux")
    public String generateQrCodess(String content) throws WriterException, IOException {
        try {

//            // 获取static资源的Resource
//            Resource resource = resourceLoader.getResource("classpath:/static/");
//            // 获取资源的URI
//            URI resourceUri = resource.getURI();
//            // 解析URI得到路径
//            String staticPath = Paths.get(resourceUri).toString();


            String localPathDir2 = ResourceUtils.getURL("classpath:static").getPath();

            // 获取static目录 获取static就可以完成上传功能了
            String realPath = ResourceUtils.getURL("classpath:").getPath() + "static";



            System.out.println("staticPath-"+localPathDir2);
            String text = "Hello, World!";
            int width = 300;
            int height = 300;
            String filePath = localPathDir2 +"/"+ UUID.randomUUID()+".png";
            generateQRCodeImage(text, width, height, filePath);
            return "QR Code generated successfully";
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return "Error generating QR Code: " + e.getMessage();
        }
    }


    @PostMapping("/generateWxOrder")  // 注意这里必须是POST接口
    public String generateWxOrder( @RequestParam(value="orderId",required=true)String orderId,
                                   @RequestParam(value="quantity",required=true)int quantity,
                                   @RequestParam(value="userGame",required=true)String userGame,
                                   @RequestParam(value="phone",required=true)String phone,
                                   @RequestParam(value="ids",required=true)String ids,
                                   @RequestParam(value="serviceRange",required=true)String serviceRange,HttpSession session) throws Exception {
        String os = System.getProperty("os.name");
        File imagePath;  //封面图片存放地址
        File fileRealPath;   //文件存放地址
        if (os.toLowerCase().startsWith("win")) {  //windows系统
            String path = System.getProperty("user.dir");  //获取项目相对路径
            imagePath = new File(path+"/src//main/resources/static/images");
        }else{//linux系统
            //获取根目录
            //如果是在本地windows环境下，目录为项目的target\classes下
            //如果是linux环境下，目录为jar包同级目录
            File rootPath = new File(ResourceUtils.getURL("classpath:").getPath());
            if(!rootPath.exists()){
                rootPath = new File("");
            }
            imagePath = new File(rootPath.getAbsolutePath()+"/images");
        }

       String productId = ids;
        ProductFrom productFrom = productService.checkProduct(productId);
        if (productFrom == null) {
            throw new Exception("商品信息不存在!");
        }

        int amount =  quantity * productFrom.getAmount();

        try {
            // 订单入库
            AliPay pay = new AliPay();
            pay.setTraceNo(orderId);
            pay.setTotalAmount(amount);
            pay.setOrderNum(quantity);
            pay.setOrderPrice(productFrom.getAmount());
            pay.setProductId(productFrom.getId());
            pay.setSubject(productFrom.getName());
            pay.setUserGameId(userGame);
            pay.setUserPhone(phone);
            pay.setServiceRange(serviceRange);
            pay.setId(UUID.randomUUID().toString());
            pay.setPayType(2);
            productService.insetAlipaymentOrderInfo(pay);
        } catch (Exception e) {
            log.error("订单入库出错-->",e);
            return "0";
        }
        try {
            // 使用自动更新平台证书的RSA配置
            String osName = System.getProperty("os.name").toLowerCase();
            String path = "";
            if (osName.contains("win")) {
                path = "C:\\mywecat\\1682702563_20240806_cert\\apiclient_key.pem";
            } else {
                path = "/shangyu-application/private-key/apiclient_key.pem";
            }

            Config config =
                    new RSAAutoCertificateConfig.Builder()
                            .merchantId(mchId)
                            .privateKeyFromPath(path)
                            .merchantSerialNumber(merchantSerialNumber)
                            .apiV3Key(apiV3key)
                            .build();

            // 构建service
            // 构建service
            NativePayService service = new NativePayService.Builder().config(config).build();
            // request.setXxx(val)设置所需参数，具体参数可见Request定义
            PrepayRequest request = new PrepayRequest();
            Amount amountWx = new Amount();
            amountWx.setTotal(amount * 1);  // 微信支付用分来结算  记得 * 100
            amountWx.setCurrency("CNY");
            request.setAmount(amountWx);
            request.setAppid(appId);
            request.setMchid(mchId);
            request.setDescription(productFrom.getName());
            request.setNotifyUrl(notifyUrl);
            //String orderId = IdUtils.getQuantityOrderSerialNo(1)[0];
            request.setOutTradeNo(orderId);
            // 调用下单方法，得到应答
            PrepayResponse response = service.prepay(request);
            // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
            System.out.println("微信支付地址---"+response.getCodeUrl());
            String wxPayStr = response.getCodeUrl();
            //生成二维码
            String uuid = UUID.randomUUID().toString();
            String payCodePath = uuid+".jpg";
            //生成二维码
            //QRCodeUtil.createQrCode("23132", imagePath.getPath(), UUID.randomUUID()+".jpg");
            QRCodeUtil.createQrCode(wxPayStr, imagePath.getPath(), payCodePath);
            return payCodePath;
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }



    @GetMapping("/pays")
    public String pay(HttpSession httpSession) throws Exception {
//        // 使用自动更新平台证书的RSA配置
//        Config config = wxPayConfig.getConfig(wxPayConfig);
//        // 构建service
//        // 构建service
//        NativePayService service = new NativePayService.Builder().config(config).build();
//        // request.setXxx(val)设置所需参数，具体参数可见Request定义
//        PrepayRequest request = new PrepayRequest();
//        Amount amount = new Amount();
//        amount.setTotal(1);
//        request.setAmount(amount);
//        request.setAppid(appId);
//        request.setMchid(mchId);
//        request.setDescription("测试商品标题");
//        request.setNotifyUrl(notifyUrl);
//        String orderId = IdUtils.getQuantityOrderSerialNo(1)[0];
//        request.setOutTradeNo(orderId);
//        // 调用下单方法，得到应答
//        PrepayResponse response = service.prepay(request);
//        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
//        System.out.println(response.getCodeUrl());
//        //JSONObject jsonObject = JSON.parseObject(response.getCodeUrl());
//        //String wxPayStr = jsonObject.getString("code_url");
//
//        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
//        String pre = applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath()+
//                "\\src\\main\\webapp\\assets\\wx-code\\";
//        System.out.println(pre);
//        //生成二维码
//        String payCodePath = QRCodeUtil.createQrCode(response.getCodeUrl(), pre, UUID.randomUUID()+".jpg");
//        httpSession.setAttribute("payCodePath", payCodePath);
//
        return "index";
    }


//    @PostMapping("/notify1")  // 注意这里必须是POST接口
//    public String payNotify1(HttpServletRequest request) throws Exception {
//        System.out.println("=========temp========");
//
//        QRCodeUtil qrCodeUtil = new QRCodeUtil();
//        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
//        String pre = applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath()+
//                "\\src\\main\\webapp\\assets\\wx-code\\";
//        System.out.println(pre);
//
//        //生成高德地图二维码
//        QRCodeUtil.createQrCode("weixin://wxpay/bizpayurl?pr=Hm1R1nPz3111", pre,
//                UUID.randomUUID().toString()+".jpg");
//
//
//        return "success";
//    }


//    @PostMapping("/notify")  // 注意这里必须是POST接口
//    public String payNotify(HttpServletRequest request) throws Exception {
//        System.out.println("=========异步回调========");
//        wxPayService
//        return "success";
//    }


    @ApiOperation("微信支付的回调接收接口")
    @RequestMapping(value = "/notify", method = {org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.GET})
    public void callBack(HttpServletRequest request, HttpServletResponse response) {
        wxPayService.callBack(request, response);
    }

    /**
     * 生成v3证书
//     */
//    @ApiOperation("生成v3证书")
//    @RequestMapping("/createPlatformCert")
//    @ResponseBody
//    public String createPlatformCert() throws IOException {
//        return wxPayService.createPlatformCert();
//    }

    /**
     * 通过订单号查询支付情况
     *
     * @param orderId 订单号
     * @return String
     */
    @PostMapping("/queryOrderStatus")  // 注意这里必须是POST接口
    public String queryOrderStatus(@RequestParam(value="orderId",required=true)String orderId) throws Exception {
        return wxPayService.queryOrderStatus(orderId);
    }

    @PostMapping("/queryOrderStatusMyData")  // 注意这里必须是POST接口
    public String queryOrderStatusMyData(@RequestParam(value="orderId",required=true)String orderId) throws Exception {
        return wxPayService.queryOrderStatusMyData(orderId);
    }

//    @RequestMapping("/get")
//    @ResponseBody
//    public String v3Get() {
//        // 获取平台证书列表
//        try {
//            IJPayHttpResponse response = WxPayApi.v3(
//                    RequestMethodEnum.GET,
//                    WxDomainEnum.CHINA.toString(),
//                    CertAlgorithmTypeEnum.getCertSuffixUrl(CertAlgorithmTypeEnum.SM2.getCode()),
//                    wxPayConfig.getMchId(),
//                    getSerialNumber(),
//                    null,
//                    wxPayConfig.getPrivateKeyPath(),
//                    "",
//                    AuthTypeEnum.RSA.getCode()
//            );
//            Map<String, List<String>> headers = response.getHeaders();
//            log.info("请求头: {}", headers);
//            String timestamp = response.getHeader("Wechatpay-Timestamp");
//            String nonceStr = response.getHeader("Wechatpay-Nonce");
//            String serialNumber = response.getHeader("Wechatpay-Serial");
//            String signature = response.getHeader("Wechatpay-Signature");
//
//            String body = response.getBody();
//            int status = response.getStatus();
//
//            log.info("serialNumber: {}", serialNumber);
//            log.info("status: {}", status);
//            log.info("body: {}", body);
//            int isOk = 200;
//            if (status == isOk) {
//                cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(body);
//                JSONArray dataArray = jsonObject.getJSONArray("data");
//                // 默认认为只有一个平台证书
//                cn.hutool.json.JSONObject encryptObject = dataArray.getJSONObject(0);
//                JSONObject encryptCertificate = encryptObject.getJSONObject("encrypt_certificate");
//                String associatedData = encryptCertificate.getStr("associated_data");
//                String cipherText = encryptCertificate.getStr("ciphertext");
//                String nonce = encryptCertificate.getStr("nonce");
//                String algorithm = encryptCertificate.getStr("algorithm");
//                String serialNo = encryptObject.getStr("serial_no");
//                final String platSerialNo = savePlatformCert(associatedData, nonce, cipherText, algorithm, platformCertPath);
//                log.info("平台证书序列号: {} serialNo: {}", platSerialNo, serialNo);
//                // 根据证书序列号查询对应的证书来验证签名结果
//                boolean verifySignature = WxPayKit.verifySignature(response, platformCertPath);
//                log.info("verifySignature:{}", verifySignature);
//            }
//            return body;
//        } catch (Exception e) {
//            log.error("获取平台证书列表异常", e);
//            return null;
//        }
//    }

//    private String savePlatformCert(String associatedData, String nonce, String cipherText, String algorithm, String certPath) {
//        try {
//            String key3 = wxPayConfig.getApiV3key();
//            String publicKey;
//            if (StrUtil.equals(algorithm, AuthTypeEnum.SM2.getPlatformCertAlgorithm())) {
//                publicKey = PayKit.sm4DecryptToString(key3, cipherText, nonce, associatedData);
//            } else {
//                AesUtil aesUtil = new AesUtil(wxPayConfig.getApiV3key().getBytes(StandardCharsets.UTF_8));
//                // 平台证书密文解密
//                // encrypt_certificate 中的  associated_data nonce  ciphertext
//                publicKey = aesUtil.decryptToString(
//                        associatedData.getBytes(StandardCharsets.UTF_8),
//                        nonce.getBytes(StandardCharsets.UTF_8),
//                        cipherText
//                );
//            }
//            if (StrUtil.isNotEmpty(publicKey)) {
//                // 保存证书
//                FileWriter writer = new FileWriter(certPath);
//                writer.write(publicKey);
//                // 获取平台证书序列号
//                X509Certificate certificate = PayKit.getCertificate(new ByteArrayInputStream(publicKey.getBytes()));
//                return certificate.getSerialNumber().toString(16).toUpperCase();
//            }
//            return "";
//        } catch (Exception e) {
//            log.error("保存平台证书异常", e);
//            return e.getMessage();
//        }
//    }

//    String serialNo;
//    private String getSerialNumber() {
//        if (StrUtil.isEmpty(serialNo)) {
//            // 获取证书序列号
//            X509Certificate certificate = PayKit.getCertificate(wxPayConfig.getPrivateKeyPath());
//            if (null != certificate) {
//                serialNo = certificate.getSerialNumber().toString(16).toUpperCase();
//                // 提前两天检查证书是否有效
//                boolean isValid = PayKit.checkCertificateIsValid(certificate, wxPayConfig.getMchId(), -2);
//                log.info("证书是否可用 {} 证书有效期为 {}", isValid, DateUtil.format(certificate.getNotAfter(), DatePattern.NORM_DATETIME_PATTERN));
//            }
////            System.out.println("输出证书信息:\n" + certificate.toString());
////            // 输出关键信息，截取部分并进行标记
////            System.out.println("证书序列号:" + certificate.getSerialNumber().toString(16));
////            System.out.println("版本号:" + certificate.getVersion());
////            System.out.println("签发者：" + certificate.getIssuerDN());
////            System.out.println("有效起始日期：" + certificate.getNotBefore());
////            System.out.println("有效终止日期：" + certificate.getNotAfter());
////            System.out.println("主体名：" + certificate.getSubjectDN());
////            System.out.println("签名算法：" + certificate.getSigAlgName());
////            System.out.println("签名：" + certificate.getSignature().toString());
//        }
//        System.out.println("serialNo:" + serialNo);
//        return serialNo;
//    }

}

