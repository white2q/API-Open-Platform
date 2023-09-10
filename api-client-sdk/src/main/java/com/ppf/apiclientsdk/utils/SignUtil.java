package com.ppf.apiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * @author panpengfei
 * @date 2023/8/18
 */
public class SignUtil {
    public static String genSign(String body, String secreteKey) {
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        String content = body + "." + secreteKey;
        String sign = digester.digestHex(content);
        return sign;
    }
}
