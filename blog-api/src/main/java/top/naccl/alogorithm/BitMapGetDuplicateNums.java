package top.naccl.alogorithm;

/**
 * @author willWang
 * @description BitMap获取数组中重复的数据
 * @create 2023-03-04 20:00
 * Bit-Map用byte简单实现方法:
 * 用byte数组，一个byte所占内存空间为 1字节 ，也就是 8bit，但是却可以表示8种状态。还拿上面的去重数组举例
 * 去重数组 {2, 3, 0, 1, 3, 4}，初始化一个byte，值为 0， 二进制位 0000 0000。
 * 7 6 5 4 3 2 1 0
 *-----------------
 *|0|0|0|0|0|0|0|0|
 *-----------------
 * 一个byte可以表示 0 ~ 7。对数组进行遍历，第一个元素为 2，则把第三位也就是 2 的位置置为 1。
 * 7 6 5 4 3 2 1 0
 *-----------------
 *|0|0|0|0|0|1|0|0|
 *-----------------
 * 现在byte表示有一个元素 2 。
 * 下一个元素为 3，则把第三位置为 1。
 * 7 6 5 4 3 2 1 0
 *-----------------
 *|0|0|0|0|1|1|0|0|
 *-----------------
 * 下一个为 0 。
 * 7 6 5 4 3 2 1 0
 *-----------------
 *|0|0|0|0|1|1|0|1|
 *-----------------
 * 下一个为1 。
 * 7 6 5 4 3 2 1 0
 *-----------------
 *|0|0|0|0|1|1|1|1|
 *-----------------
 * 再下一个为 3 ，但是此时 3 的位置已经被置为 1 了，所以 3 是第一个重复的元素。
 */
public class BitMapGetDuplicateNums {

    private static byte[] bitmap;

    public static void initBitMap(int capacity) {
        //如果capacity % 8 == 0，就不用 + 1
        int length = capacity % 8 == 0 ? capacity / 8 : capacity / 8 + 1;
        bitmap = new byte[length];
    }

    public static boolean setBit(int index) {
        // 放在一个bit中的第几位
        int bIndex = index % 8;
        // 放在第几个bit上
        index = index / 8;
        // 取出bIndex上的内容
        byte b = (byte) (bitmap[index] >> bIndex);
        //以下代码有很大的改进空间，留给小伙伴们了
        // 判断bIndex位置上是否为1，是1，返回false，不在放置
        if((b & 0x01) == 1) {
            return false;
        }
        // 在bIndex这个位置上放置1
        byte temp = (byte) (0x01 << bIndex);
        // 将该bit数据放到初始化的bit对应的位置
        bitmap[index] = (byte) (bitmap[index] | temp);
        return true;
    }

    public static int duplicate(int[] numbers) {
        int duplicateNums = 0;
        if(numbers == null || numbers.length == 0) return duplicateNums;
        initBitMap(numbers.length);
        for(int i = 0; i < numbers.length; ++i) {
            if(!setBit(numbers[i])){
                duplicateNums += 1;
            }
        }
        return duplicateNums;
    }

    public static void main(String[] args){
        int[] nums = {2,3,0,1,3,4};
        int bb =  duplicate(nums);
        System.out.println(bb);
    }

}
