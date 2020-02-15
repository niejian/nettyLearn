package cn.com.netty.pattern;

/**
 * @desc: cn.com.netty.pattern.Client
 * @author: niejian9001@163.com
 * @date: 2020/2/15 10:28
 */
public class Client {
    public static void main(String[] args) {
        Human human = new Man();
        DecorateHuman decorateHuman = new DecorateHuman(human);
        decorateHuman.run();
    }
}
