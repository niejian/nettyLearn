package cn.com.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Iterator;

/**
 * @user niejian9001@163.com
 * @date 2020/3/31 20:59
 */
public class ByteBufTest2 {

    public static void main(String[] args) {
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        ByteBuf heapBuff = Unpooled.buffer(10);
        ByteBuf directBuffer = Unpooled.directBuffer(8);
        compositeByteBuf.addComponents(heapBuff, directBuffer);
        //compositeByteBuf.removeComponent(0);
        Iterator<ByteBuf> iterator = compositeByteBuf.iterator();
        while (iterator.hasNext()) {
            //UnpooledSlicedByteBuf(ridx: 0, widx: 0, cap: 0/0, unwrapped: UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeNoCleanerDirectByteBuf(ridx: 0, widx: 0, cap: 8))
            System.out.println(iterator.next());
        }
    }
}
