package cn.com.netty.pattern;

/**
 * @desc: cn.com.netty.pattern.DecorateHuman
 * @author: niejian9001@163.com
 * @date: 2020/2/15 10:26
 */
public class DecorateHuman extends AbstractDecorateHuman {

    public DecorateHuman(Human human) {
        super(human);
    }

    /**
     * 对装饰类添加的增强的方法
     */
    public void fly() {
        System.out.println("增强的fly功能");

    }

    /**
     * @return
     */
    @Override
    public String run() {
        fly();
        return super.run();
    }
}
