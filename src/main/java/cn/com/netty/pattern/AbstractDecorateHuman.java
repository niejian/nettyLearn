package cn.com.netty.pattern;

/**
 * 定义一个Human的装饰类，在这个装饰类中，需要有一个对human的引用，从而实现对Human的功能增强
 * @desc: cn.com.netty.pattern.AbstractDecorateHuman
 * @author: niejian9001@163.com
 * @date: 2020/2/15 10:22
 */
public abstract class AbstractDecorateHuman implements Human {
    /***
     * 有一个对接口类的引用
     */
    private Human human;

    AbstractDecorateHuman(Human human) {
        this.human = human;
    }

    /**
     * @return
     */
    @Override
    public String run() {
        return human.run();
    }
}
