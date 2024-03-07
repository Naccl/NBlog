package top.naccl.alogorithm;

/**
 * 环形数组获取数据,每查找一次，耗时一秒，返回总共消耗的时间
 */
public class CircleListGetChar_StringVersion {
    public static void main(String[] args){
        String s1 = "AZGB";
        // 0 1 7 5 = 13
        String s2 = "BJEF";
        // 1 8 5 1 = 15
        long result = getTime(s2);
        System.out.println("result:"+result);
    }

    /**
     * 使用正反数组或字符串模拟双向环形链表
     * @param s
     * @return
     */
    public static long getTime(String s){
        long theTime = 0L;
        String posNumStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String nagNumStr = "ZYXWVUTSRQPONMLKJIHGFEDCBA";

        if(s != null && s.length() > 0){
            // 正向查找字符时，从哪里开始，记录开始位置的下标
            int prePosIndex = 0;
            // 正向循环查找到该字符总共用时多少
            int posTime = 0;
            // 反向查找字符时，用哪里开始，记录开始位置的下标
            int preNagIndex = 0;
            // 反向循环查找到该字符总共用时多少
            int nagTime = 0;
            // 记录上一次的字符，即从哪个字符开始查找本次的字符
            char preChar = 0;
            for(int i = 0; i < s.length(); i++){
                char tempChar = s.charAt(i);
                // 正向查找该字符
                int posIndex = posNumStr.indexOf(tempChar,prePosIndex);
                // 如果没有找到，从头开始找，最终时长要加上没有找到时查找的时长
                if(posIndex < 0){
                    // 没找到的时候，找了几次？从新开始找了，肯定找了一次
                    int spaceTime = posNumStr.length() - prePosIndex;
                    // 从新开始找，该字符下标，从新开始找，下标就是找的次数
                    int tempIndex = posNumStr.indexOf(tempChar,0);
                    System.out.println("从新沿着A-Z查找从新查找"+tempChar +",找到了，下标是："+ tempIndex);
                    // 从新开始找的时长，加上没找到的查找时长，是本次总共的查找时长
                    posTime = tempIndex + spaceTime;
                    System.out.println("从"+preChar+"沿着A-Z查找"+tempChar +"本次用时"+ posTime);
                    // 更新坐标
                    prePosIndex = tempIndex;
                }else{
                    posTime = posIndex - prePosIndex;
                    System.out.println("从"+preChar+"沿着A-Z查找"+tempChar +"本次用时"+ posTime);
                    // 将该位置记录
                    prePosIndex = posIndex;
                }
                // 反向查找该字符
                int nagIndex = nagNumStr.indexOf(tempChar,preNagIndex);
                // 如果没有找到，从头开始找，最终时长要加上没有找到时查找的时长
                if(nagIndex < 0){
                    // 没找到的时候，找了几次？从新开始找了，肯定找了一次
                    int spaceTime = nagNumStr.length() - preNagIndex;
                    // 从新开始找，该字符下标，从新开始找，下标就是找的次数
                    int tempIndex = nagNumStr.indexOf(tempChar,0);
                    System.out.println("从新沿着Z-A查找从新查找"+tempChar +",找到了，下标是："+ tempIndex);
                    // 从新开始找的时长，加上没找到的查找时长，是本次总共的查找时长
                    nagTime = tempIndex + spaceTime;
                    System.out.println("从"+preChar+"沿着Z-A查找"+tempChar +"本次用时"+ nagTime);
                    // 更新坐标
                    preNagIndex = tempIndex;
                }else{
                    nagTime = nagIndex - preNagIndex;
                    System.out.println("从"+preChar+"沿着A-Z查找"+tempChar +"本次用时"+ nagTime);
                    // 将该位置记录
                    preNagIndex = nagIndex;
                }
                // 至此，双向查找完毕，对比时间长短，取较短的路径
                int thisTime = posTime < nagTime ? posTime : nagTime;
                System.out.println("从"+preChar+"到"+tempChar+"共用时"+thisTime);
                theTime += thisTime;
                // 记录这次查找的字符
                preChar = tempChar;
            }
        }
        return theTime;
    }
}
