package org.egc.commons.security;

import com.google.common.base.Strings;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.*;

/**
 * 基于Shiro的密码加密
 *
 * @author houzhiwei
 * @date 2016/11/3 23:16.
 */
public class EncryptionUtil
{
    /**
     * Gets encrypted.
     *
     * @param salt   the salt
     * @param hashed the hashed
     * @return the encrypted
     */
    public static EncryptedDTO getEncrypted(String salt, String hashed)
    {
        return new EncryptedDTO(salt, hashed);
    }

    /**
     * <pre/>
     * 采用可信随机数为盐
     * 16进制
     *
     * @return 盐/salt
     */
    public static String generateSalt()
    {
        return new SecureRandomNumberGenerator().nextBytes().toHex();
    }

    /**
     * Generate salt with seed string.
     *
     * @param seed the seed
     * @return the string
     */
    public static String generateSaltWithSeed(String seed)
    {
        SecureRandomNumberGenerator generator = new SecureRandomNumberGenerator();
        generator.setSeed(seed.getBytes());
        return generator.nextBytes().toHex();
    }

    /**
     * <pre/>
     * 对源进行MD5加密处理：
     * 源与可信随机数为盐，5次迭代加密并转为16进制
     *
     * @param src 需要加密的源
     * @return 加密处理之后结果
     */
    public static EncryptedDTO md5Encrypt(String src)
    {
        String salt = generateSaltWithSeed(src);
        String result = new Md5Hash(src, salt, 5).toHex();//新的加密之后的密码
        return getEncrypted(salt, result);
    }

    /**
     * <pre/>
     * Md5 encrypt string.
     * 5次迭代
     *
     * @param src     加密源
     * @param saltSrc 和随机数一起组成加密盐值
     * @return 结果
     */
    public static EncryptedDTO md5Encrypt(String src, String saltSrc)
    {
        String salt = generateSaltWithSeed(saltSrc);
        String result = new Md5Hash(src, salt, 5).toHex();//新的加密之后的密码

        return getEncrypted(salt, result);
    }

    /**
     * <pre/>
     * 获得使用email和随机盐并经md5加密之后的验证码
     * 5次迭代
     *
     * @param email
     * @return
     */
    public static String getMD5VerifyCode(String email)
    {
        String salt = generateSaltWithSeed(email);
        String code = new Md5Hash(email, salt, 5).toHex();
        return code;
    }

    /**
     * <pre/>
     * 对源进行sha256加密处理：
     * 源与可信随机数为盐，1024次迭代加密并转为 Base64
     * 注意设置：credentialsMatcher.storedCredentialsHexEncoded = false
     *
     * @param src 需要加密的源
     * @return 加密处理之后的结果
     */
    public static EncryptedDTO sha256Encrypt(String src)
    {
        String salt = generateSaltWithSeed(src);
        //Note: credentialsMatcher.storedCredentialsHexEncoded = false  //base64 encoding, not hex
        String hashedBase64 = new Sha256Hash(src, salt, 1024).toBase64();
        return getEncrypted(salt, hashedBase64);
    }

    /**
     * <pre/>
     * 对源进行sha256加密处理：
     * 源与可信随机数为盐，1024次迭代加密并转为 Base64
     * 注意设置：credentialsMatcher.storedCredentialsHexEncoded = false
     *
     * @param src     the src
     * @param saltSrc 和随机数一起组成加密盐值
     * @return the encrypted
     */
    public static EncryptedDTO sha256Encrypt(String src, String saltSrc)
    {
        String salt = generateSaltWithSeed(saltSrc);
        //Note: credentialsMatcher.storedCredentialsHexEncoded = false  //base64 encoding, not hex
        String hashedBase64 = new Sha256Hash(src, salt, 1024).toBase64();
        return getEncrypted(salt, hashedBase64);
    }


    /**
     * 源加密处理
     * toHex()
     *
     * @param src        需要加密的源
     * @param saltSrc    和随机数一起组成加密盐值,若为空，则使用src的值
     * @param algorithm  加密算法："MD2"、"SHA-512"、"MD5"、"SHA-1"、"SHA-256"、"SHA-384"
     * @param iterations 迭代次数
     * @return 加密后结果
     */
    public static EncryptedDTO encrypt(String src, String saltSrc, String algorithm, int iterations)
    {
        String salt = "";
        if (Strings.isNullOrEmpty(saltSrc))
            salt = generateSaltWithSeed(src);
        else
            salt = generateSaltWithSeed(saltSrc);
        HashService hashService = new DefaultHashService();
//        SimpleHash hash = new SimpleHash(algorithm, password, username + salt, iterations);
//        hash.toHex().toString();
        HashRequest hashRequest = new HashRequest.Builder()
                .setSource(src).setAlgorithmName(algorithm)
                .setSalt(salt).setIterations(iterations).build();
        String hashed = hashService.computeHash(hashRequest).toHex().toString();
        return getEncrypted(salt, hashed);
    }
}
