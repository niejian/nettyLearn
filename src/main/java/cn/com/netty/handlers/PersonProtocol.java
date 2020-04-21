package cn.com.netty.handlers;

/**
 * 自定义协议
 * @desc: cn.com.netty.handlers.PersonProtocol
 * @author: niejian9001@163.com
 * @date: 2020/4/21 17:35
 */
public class PersonProtocol {
    private int length;
    private byte[] content;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}

