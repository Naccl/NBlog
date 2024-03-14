package top.naccl.alogorithm;

/**
 * 两个数组合并
 * 1. 问题描述：
 * 	对于两个有序数组arrayM，arrayN，长度分别为m和n；将这两个数组合并成一个数组并进行排序，要求时间复杂度为O（m+n）；
 * 2. 解题思路：
 * 	定义一个数组res，长度为m+n；
 * 	定义两个index，分别为indexM、indexN，用来记录数组arrayM、arrayN的下标；
 * 	通过分别遍历数组arrayM和arrayN的方式来比较每个值的大小，并将值存放到res中；
 * 	判断indexM和indexN是否分别小于m、n，如果小于则继续执行，否则则表示有一个array遍历结束，需要单独对另一个array操作；
 */
public class AddTwoAarrays {

    public static void main(String[] args) {
        int[] a = { 1, 3, 5 };
        int[] b = { 2, 3, 4, 7 };
        mergeMethod(a, b);
    }
    public static void mergeMethod(int[] arrayM, int[] arrayN) {
        //定义一个汇总数组
        int[] result = new int[arrayM.length + arrayN.length];
        //定义两个数组index，遍历并记录index使用；
        int indexM =0;
        int indexN =0;
        //定义汇总数组的index；
        int k=0;
        // 使用while循环遍历；当indexM或者indexN中有任意一个值为M或者N时，则表示当前某一个数组遍历到尾部
        while(indexM<arrayM.length&&indexN<arrayN.length){
            if (arrayM[indexM]<=arrayN[indexN]){
                result[k]=arrayM[indexM];
                indexM++;
            }else{
                result[k]=arrayN[indexN];
                indexN++;
            }
            k++;
        }
        // 判断哪一个数组被遍历到尾部，则此处将另一个数组添加到汇总数组中即可；
        // 此时 a =indexM，并a++,将M数组中剩余值添加到res中；
        if(indexM!=arrayM.length){
            for(int a = indexM ;a<arrayM.length;a++){
                result[k]=arrayM[a];
                k++;
            }
        }else{
            // 此时 a =indexN，并a++,将N数组中剩余值添加到res中；
            for(int a = indexN ;a<arrayN.length;a++){
                result[k]=arrayN[a];
                k++;
            }
        }
        for(int a =0;a<k;a++){
            System.out.println(result[a]);
        }
    }

}
