package cn.ponfee.web.framework.util;

import code.ponfee.commons.jce.CryptoProvider;
import code.ponfee.commons.jce.digest.DigestUtils;
import code.ponfee.commons.jce.passwd.BCrypt;

/**
 * The common utility
 * 
 * @author Ponfee
 */
public class CommonUtils {

    // RSA加密组件，私钥已经包含公钥
    private static final CryptoProvider DECRYPTOR = CryptoProvider.rsaPrivateKeyProvider(
          "MIIEpAIBAAKCAQEAghx+O+RvGxl3Nuwpztivr26DBEgxVvSx/t6s5Y/7y51NgRil"
        + "s3O/WsAe6XKKTUzXWcByd+NXlMmwPscrlR9hfNZXq4jpfFnv7Qq7++3jP5inoUCd"
        + "wXkjQkn2SqarzOtl5d0NWovobkolSzVuWoDBXB66noqLA9fTvdIJYfYRo4jLSWuS"
        + "XO2hzY999uE/53GnQnUEy8Pghjf5sRbxWhK5YJ5sCCd2UcVNcKV0ggsrc6OTHFCJ"
        + "zPb9MasZ5tdT6hgGmgQP8svVZkigvbo5Gu/evWRIS2IU/iY96J47KWLnunCwAJ+C"
        + "fUogFtVlMiGailuXEwZJhQS9pywHWPUCoqRQcwIDAQABAoIBADg2zUddI6/Gqd4d"
        + "3NZUgls+WPbRpheWswyV8odgVP6c4O6E0pqcyLERzok4Bo7tQyGw5wPrQlyzYyVM"
        + "cgUEwN7p0Ij5Jz+lr+ZvO9bV6dgBQXOG8mGzz8sFH5cTOajYj3bnsaxMREjaWhCL"
        + "bjlF5cTCNgjnfLZVWlNp2snrQaWwh6pzEp4m8JQydS8aPS0/XKNfm9cbFj7J+4xq"
        + "+/icbsq3V8kuCIxfC8xpB2FzQTuL3x/KMWdTfBqag0yGN/+7CswI6RDWmSerl6So"
        + "WNACkdbtPwdi6LS6MsDRzzMatydvOx2iZbx2zzUyfxTg3K9KbI+kxwv3a+47Ssyt"
        + "+biko4kCgYEAvZe2pDjY7wLcifywf3MFVIDOP/FnvjBbEhJkfqGkA0jaPaNBgn9B"
        + "tbMljh0czEuq2Z6TUEfN98OXxSbINy9KiFeIKU8ZjfEC3f/Bm9pbOsHkz4uBL7Tg"
        + "5kdb7f816UZ+FbpbN3isQkCC1OpjISiwL4LX5cyxbUAQ8ikN/tkTTXcCgYEAr689"
        + "qvs1uBay1QGeUaIRJ0VcUC35GnGQGzIuMNUNlnKOaLRtAKFFRUWWpj42EiLs+Vxi"
        + "LbnOzgZpyIoyPcsqs998cUdTeUgKvL7bdhwkMCt/IOZplC5wB74BNsw4kHT+Movx"
        + "NerRPcXQ6TuQ7cac6bW53GvuvcbQNnfDcXytY+UCgYB2OjlmxxxgT4R/d9gIYg2c"
        + "2IXFUiDDdRwVdOq+HMZOVgviDAA6BGlRiVmN+ZGPxE/STRTIJz7Flm76Pmj0IMV1"
        + "qD/8opro7LZBAdAWBGCHj4nD8taojT8B2tX04AUaZx8RJZjkSpgA6IGtjEwszVax"
        + "CgLgBnlOC3mFyRw1I+DpmQKBgQCE4RXSmhjieQCgDY5SjjNNMbAF0Q/liVmGyGiZ"
        + "dLvHPDQsS7NaU6BSKbUtXkaFZzc30EFN9ojN2ZnvHV+sz1soG3RDNcCULgI5Cl5x"
        + "sg1JBc0s9DmM46bGpFbEvKhX0rVlQ+WYc1f50gIWNoCC58NNHa9LYbN8hMB9Qg5o"
        + "avW2ZQKBgQC3w2rY99eZb499Nwxqcvw3hXmXn9K5xZm5REf1+WGU4XEfCJVUR54B"
        + "FVClMz0BTDy9aFlX+AWugnp65ydeQwbL1Ail65btNR0H/g4DexU/8PidHWsupswI"
        + "gUGZKpsLRRjlDKzNpk3PpLPFYk8AtEBNZ9BBFRPXGa+eYmzXxo+7qg=="
    );

    /*private static final CryptoProvider ENCRYPTOR = CryptoProvider.rsaPublicKeyProvider(
          "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAghx+O+RvGxl3Nuwpztiv"
        + "r26DBEgxVvSx/t6s5Y/7y51NgRils3O/WsAe6XKKTUzXWcByd+NXlMmwPscrlR9h"
        + "fNZXq4jpfFnv7Qq7++3jP5inoUCdwXkjQkn2SqarzOtl5d0NWovobkolSzVuWoDB"
        + "XB66noqLA9fTvdIJYfYRo4jLSWuSXO2hzY999uE/53GnQnUEy8Pghjf5sRbxWhK5"
        + "YJ5sCCd2UcVNcKV0ggsrc6OTHFCJzPb9MasZ5tdT6hgGmgQP8svVZkigvbo5Gu/e"
        + "vWRIS2IU/iY96J47KWLnunCwAJ+CfUogFtVlMiGailuXEwZJhQS9pywHWPUCoqRQ"
        + "cwIDAQAB"
    );*/

    /**
     * Crypt password as BCrypt hashed
     * 
     * @param password the password of user input
     * @return password bcrypt hashed
     */
    public static String cryptPassword(String password) {
        // 密码sha1后再进行bcrypt
        return BCrypt.create(DigestUtils.sha1(password), 2);
    }

    /**
     * Checks the password
     * 
     * @param password the password of user input
     * @param hashed   the hashed password of t_user.password value
     * @return {@code true} check success
     */
    public static boolean checkPassword(String password, String hashed) {
        return BCrypt.check(DigestUtils.sha1(password), hashed);
    }

    /**
     * Returns the passowrd plain with RSA decrypt
     * 
     * @param password the password RSA encrypted
     * @return the plain password
     */
    public static String decryptPassword(String password) {
        try {
            return DECRYPTOR.decrypt(password);
        } catch (Exception e) {
            throw new IllegalArgumentException("密码无效");
        }
    }

    /**
     * Returns the cipher passowrd with RSA encrypt
     * 
     * @param password the password plain text
     * @return the cipher password
     */
    public static String encryptPassword(String password) {
        //return ENCRYPTOR.encrypt(password);
        return DECRYPTOR.encrypt(password);
    }

}
