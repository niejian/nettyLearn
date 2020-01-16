package cn.com.netty.common;

import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @desc: cn.com.netty.common.CommonInstance
 * @author: niejian9001@163.com
 * @date: 2020/1/16 11:08
 */
public interface CommonInstance {
    static StringDecoder STRINGDECODER = new StringDecoder();
    static StringEncoder STRINGENCODER = new StringEncoder();
    static String STRINGDECODER_NAME = "stringdecoder";
    static String STRINGENCODER_NAME = "stringencoder";
    static String DATE_PARTTERN = "yyyy-MM-dd HH:mm:ss:SSS";

}
