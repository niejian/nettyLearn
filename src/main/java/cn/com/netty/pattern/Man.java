package cn.com.netty.pattern;

/**
 * @desc: cn.com.netty.pattern.Man
 * @author: niejian9001@163.com
 * @date: 2020/2/15 10:21
 */
public class Man implements Human {
    /**
     * @return
     */
    @Override
    public String run() {
        System.out.println("人的本能-奔跑");
        return "人会奔跑";
    }
}
