package top.naccl.alogorithm;

import java.util.Scanner;

/**
 * 求正整数的阶乘
 */
public class TheFactorialOfN {

    public static void main(String[] args) {
        System.out.println("请输入正整数N");
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        int N = num;
        long result = 1;//这里的long 是long int的缩写，是加长版的int，防止阶乘结果过大 超过int的范围而导致报错
        while(num>1) {
            result *= num*(num-1);
            num -= 2;

        }
        System.out.println(N+"的阶乘为"+result);
        sc.close();//关闭Scanner
    }

}
