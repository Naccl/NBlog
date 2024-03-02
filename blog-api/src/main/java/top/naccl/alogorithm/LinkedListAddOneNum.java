package top.naccl.alogorithm;

/**
 * 用一个 非空 单链表来表示一个非负整数，然后将这个整数加一。
 * 你可以假设这个整数除了 0 本身，没有任何前导的 0。
 * 这个整数的各个数位按照 高位在链表头部、低位在链表尾部 的顺序排列。
 * 示例:
 * 输入: [1,2,3]
 * 输出: [1,2,4]
 */
public class LinkedListAddOneNum {

    public static void main(String[] args){
        // 初始化
        Node headNode = new Node(1);
        Node secondNode = new Node(2);
        Node threeNode = new Node(3);
        headNode.nextIndex = secondNode;
        secondNode.nextIndex = threeNode;
        // 计算
        Node resultNode = funcOne(headNode);
//        Node resultNode = funcTwo(headNode);
        while(resultNode != null){
            System.out.println(resultNode.value);
            resultNode = resultNode.nextIndex;
        }
    }
    // 方案一：先翻转再加一再反转
    public static Node funcOne(Node node){
        // 翻转
        Node headNode = reverseRecursive(node);
        // 加一
        int carry = 1;
        Node nowNode = headNode;
        while(nowNode != null){
            // 加到当前节点
            int value = nowNode.value + carry;
            nowNode.value = value % 10;
            carry = value / 10;
            // 如果后边没有数据了则跳出循环
            if(nowNode.nextIndex == null){
                break;
            }
            nowNode = nowNode.nextIndex;
        }
        // 如果有进位值，并且当前已经是最后一个节点了，增加一个新节点
        if(carry > 0){
            nowNode.nextIndex = new Node(carry);
        }
        // 翻转
        Node resultNode = reverseRecursive(headNode);
        return resultNode;
    }

    // 方案二递归加一,递归实质上就是系统帮你压栈的过程，系统在压栈的时候会保留现场。
    static int carry = 1;
    public static Node funcTwo(Node node){
        addNum(node);
        // 处理最终进位
        if(carry > 0){
            Node newNode = new Node(carry);
            newNode.nextIndex = node;
            node = newNode;
        }
        return node;
    }
    public static void addNum(Node node){
        // 如果后边还有数据，继续往后查找
        if(node != null && node.nextIndex != null){
            addNum(node.nextIndex);
        }
        // 出栈时计算每一个节点的数据，此时node的值是入栈时保存的Node节点
        int num = node.value;
        int result = num + carry;
        node.value = result % 10;
        // 记录进位值
        carry = result / 10;
    }

    // 递归实质上就是系统帮你压栈的过程，系统在压栈的时候会保留现场。
    public static Node reverseRecursive(Node headNode){
        if(headNode == null || headNode.nextIndex == null){
            return headNode;
        }
        // 暂存遍历到的节点，退栈的时候使用
        Node tempNode = headNode.nextIndex;
        // 获取最后一个节点
        Node newNode = reverseRecursive(tempNode);
        // 此时headNode是入栈时保存的上一次的node值
        tempNode.nextIndex = headNode;
        headNode.nextIndex = null;
        return newNode;
    }

    public static void reverse(Node headNode){
        Node p = headNode;
        Node preNode = null;
        // 如果还有下一个节点
        while(p.nextIndex != null){
            // 暂存下一个节点
            Node q = p.nextIndex;
            // 将前一个节点作为当前节点的下一个节点
            p.nextIndex = preNode;
            // 将前一个节点指向当前节点
            preNode = p;
            // 将当前节点指向下一个节点
            p = q;
        }
        // 如果没有下一个节点，当前即为最后一个节点，将当前节点指向前一个节点
        p.nextIndex = preNode;
        // 将head节点指向当前节点
        headNode = p;
    }

    static class Node{
        Node nextIndex;
        int value;

        public Node(int value){
            this.value = value;
        }
    }

}
