package main.common;

import java.net.URI;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

/**
 * Utilsクラス
 * @author 7days
 */
public class Utils {

    /** logger */
    private static final Logger logger = Logger.getLogger(Utils.class);

    /**
     * URLエンコード
     * @param url url
     * @return エンコード後のURL
     */
    public static String encodeUrl(String url) {
        String encodeUrl = "";
        try {
            logger.debug("url before:" + url);
            encodeUrl = new URI(url).toASCIIString();
            logger.debug("url after :" + encodeUrl);
        } catch (Exception e) {
            logger.warn("url encode error", e);
        }
        return encodeUrl;
    }

    /**
     * URLデコード
     * @param url url
     * @return デコード後のURL
     */
    public static String decodeUrl(String url) {
        String decodeUrl = "";
        try {
            logger.debug("url before:" + url);
            decodeUrl = URLDecoder.decode(url, "UTF-8");
            logger.debug("url after :" + decodeUrl);
        } catch (Exception e) {
            logger.warn("url decode error", e);
        }
        return decodeUrl;
    }
}
