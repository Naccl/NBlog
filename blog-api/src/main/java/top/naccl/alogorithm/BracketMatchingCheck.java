package top.naccl.alogorithm;

import java.util.Scanner;
import java.util.Stack;

/**
 * 括号匹配校验
 * [ ( [ ] [ ] ) ]
 * 1 2 3 4 5 6 7 8
 */
public class BracketMatchingCheck {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Stack<String> s = new Stack<String>();
        //将输入的一个字符压入栈顶
        Scanner input = new Scanner(System.in);
        String bracket = input.next();
        s.push(bracket);
        //第一个元素不是左括号则输入非法
        if(!s.isEmpty()){
            if(!s.peek().equals("(") && !s.peek().equals("[")){
                System.out.println("输入有误，第一个请输入左括号");
                return;
            }
        }

        while(!s.isEmpty()){
            //输入第二个开始匹配，根据"期待的急迫程度"
            String bracket1 = input.next();
            //如果输入非括号字符，则提示非法输入
            if(!bracket1.equals("(") && !bracket1.equals("[") && !bracket1.equals(")") && !bracket1.equals("]")){
                System.out.println("输入有误，请输入括号字符");
                return;
            }
            //获取栈顶元素
            String top = (String)s.peek();
            //如果匹配则弹出栈顶元素
            if((top.equals("[") && bracket1.equals("]"))){
                s.pop();
                System.out.println("匹配[]成功");
                continue;
            }
            if((top.equals("(") && bracket1.equals(")"))){
                s.pop();
                System.out.println("匹配()成功");
                continue;
            }
            //不匹配则继续压入栈顶
            s.push(bracket1);
        }
        input.close();
    }

}
