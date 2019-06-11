package com.xuecheng.auth.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.StandardSocketOptions;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther : shindou
 * @Date : 2019/4/23
 * @Description : 功能描述
 * @Version : 1.0
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestJwtToken {


    //测试生成Jwt令牌
    @Test
    public void createJwtToken(){
        //证书文件
        String key_store = "xc.keystore";
        //密钥库密码
        String keystore_password = "xuechengkeystore";
        //访问证书路径
        ClassPathResource classPathResource = new ClassPathResource(key_store);
        //密钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource,keystore_password.toCharArray());

        //密钥的密码，此密码和别名要匹配
        String keypassword = "xuecheng";
        //密钥别名
        String alias = "xckey";
        //密钥对（公钥和密钥）
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias,keypassword.toCharArray());
        //私钥
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        //定义payload信息
        Map<String,Object> tokenMap = new HashMap<>();
        tokenMap.put("id","123");
        tokenMap.put("name","mrt");
        tokenMap.put("roles","r01,r02");
        tokenMap.put("ext","1");

        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap),new RsaSigner(rsaPrivateKey));

        //取出令牌
        String token = jwt.getEncoded();
        System.out.println("jwttoken= "+token);

    }


    //验证令牌  资源服务使用公钥验证Jwt的合法性。并对jwt解码
    @Test
    public void checkJwtToken(){
        //jwt令牌：
//        String jwtString = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHQiOiIxIiwicm9sZXMiOiJyMDEscjAyIiwibmFtZSI6Im1ydCIsImlkIjoiMTIzIn0.a3pPI4RIGiI-4gYmC5aO_5iqsVaigKb_vSmjTxx-AjtujjBTENxqt6OI4K9ppx0IO3WEebBeOKQr6po-PnQvmMl7aNHPd9KWfKD_he_0Im88nXWU2JBX0ARc85rFiIB190r7kjFqlZrj7YkhwUsccC7PqfiyKg7Y6B7Ca_l98Dx3Zv6VJwYxVU159XlB6G2NGMIRmDbJhnxLUG0zZdOKBs0BsQobU-IRRCI2bEu5zTImimfogNsGciTCS-7CMN8kO4T_rSMwEe66vPMA-IjchsubfseIptYNVu9_QHbc8RNGf3wWrBcZOmTxaCIH-zAo02u1TQXyeeafLQQVzRTt8g";
//        String jwtString = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6InN0dTIiLCJzY29wZSI6WyJhcHAiXSwibmFtZSI6IuWtpueUnzIiLCJ1dHlwZSI6IjEwMTAwMSIsImlkIjoiNTEiLCJleHAiOjE1NTY3NDU3NzgsImp0aSI6ImFhZjEwYjkxLWE3ZjctNDI0ZS1iYzFmLTVjNTAxYzZkY2EyMSIsImNsaWVudF9pZCI6IlhjV2ViQXBwIn0.LL_rPXlJzLTFriDAEF0QDyooxJdiHd4q2zgGVYbh_XVBdHhz6AJ1pQ8SVWF8lPOLFG6aAZYRX6wn-ZUvjquJAqlI_864diBeXJpapwLMcoPnCg-ZuYPp-N2ueJ046MPSZkle7T3_nPs0KXq5uZnWS0zyNPYOmhxRGYbej2CPvKfL5lE2NdwfVI2m5ASfE4DJOFvnQltsyk9JxNTMlrcXKVRCeYUzxWOFEn4w1RdKByZtnsuyf19GElFPl7pTKAHvUfSZOz84vNoAKSLgqNiLpWzQOXNtv5M-6gk1ztg4d-1PYW07i1haYWtqkiadG3KWlK-umQnYYSYkKVSDpnQHkw";
        String jwtString = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOiIxIiwidXNlcnBpYyI6bnVsbCwidXNlcl9uYW1lIjoiaXRjYXN0Iiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOiJ0ZXN0MDIiLCJ1dHlwZSI6IjEwMTAwMiIsImlkIjoiNDkiLCJleHAiOjE1NTY3NDU5OTksImF1dGhvcml0aWVzIjpbInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYmFzZSIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfZGVsIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9saXN0IiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9wbGFuIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZSIsImNvdXJzZV9maW5kX2xpc3QiLCJ4Y190ZWFjaG1hbmFnZXIiLCJ4Y190ZWFjaG1hbmFnZXJfY291cnNlX21hcmtldCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfcHVibGlzaCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYWRkIl0sImp0aSI6IjEzNTVhOTBlLTMyMjEtNDhiZC05NjA0LTQzZmIxZmE1NWYxOSIsImNsaWVudF9pZCI6IlhjV2ViQXBwIn0.FrNRz8R2s7ggk-_hh11437PhuUfJvjZm54yc1kw0oGEOsam2cyMC1K9AMFZbIismWwKTnVAklxM85854YAsDCaSjs1UvXIiDLaZzfHDuC0CYQlCNhY-TK_1qNC41AnN2bdLakeNWZ9reAnVOcULf2-_fHfCzAvTKuHQD0J79N6ODtsdG_m_6Twk2qllYl23jc6tBrY43haryqkDsdZx_wYXiwr676mCNtFCN_GAnn8q2zIYbp4xIodtMowq2aH4ysc70_6spaLVq-tJ2QFGCpgD-bgKDLUV7gNT4UqL08F883-FouCCbL-n_LT3T28I0pSwTaUpf_4UUmM7n9xmG_g";
        //公钥：
        String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnASXh9oSvLRLxk901HANYM6KcYMzX8vFPnH/To2R+SrUVw1O9rEX6m1+rIaMzrEKPm12qPjVq3HMXDbRdUaJEXsB7NgGrAhepYAdJnYMizdltLdGsbfyjITUCOvzZ/QgM1M4INPMD+Ce859xse06jnOkCUzinZmasxrmgNV3Db1GtpyHIiGVUY0lSO1Frr9m5dpemylaT0BV3UwTQWVW9ljm6yR3dBncOdDENumT5tGbaDVyClV0FEB1XdSKd7VjiDCDbUAUbDTG1fm3K9sx7kO1uMGElbXLgMfboJ963HEJcU01km7BmFntqI5liyKheX+HBUCD4zbYNPw236U+7QIDAQAB-----END PUBLIC KEY-----";

        //校验jwt
        Jwt jwt = JwtHelper.decodeAndVerify(jwtString,new RsaVerifier(publicKey));

        //获取jwt的原始内容
        String claims = jwt.getClaims();
        String encode = jwt.getEncoded();
        System.out.println(encode);
    }
}
