package my;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: jiaolong
 * @date: 2024/11/12 10:27
 **/
public class Link {
    public static void main(String[] args) {
        ListNode head = new ListNode(1, new ListNode(2, new ListNode(3 )));
        // ListNode head = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5)))));
        ListNode result = reverseList3(head);

        ListNode xx = result;
        while(xx!=null){
            System.out.println(xx.val);
            xx=xx.next;
        }

    }

    //递归
    public static ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode newHead = reverseList(head.next);

        //（当前节点）的下一个节点 的next指针指向当前节点
        (head.next).next = head;
        //当前节点的next指针指向null
        head.next = null;
        return newHead;
    }



    //方案3
    public static ListNode reverseList3(ListNode head) {
//        ListNode prevNode = null;
//        ListNode currNode = head;

        ListNode prevNode = null, currNode = head;

        while (currNode != null) {
            ListNode nexNode = currNode.next;
            currNode.next = prevNode;

            //设置下一次节点
            prevNode = currNode;
            currNode = nexNode;
        }

        return prevNode;
    }

    //方案2
    public static ListNode reverseList2(ListNode head) {
        ListNode prevNode = null;
        ListNode currNode = head;
        ListNode nexNode = head.next;

        while (nexNode != null) {
            currNode.next = prevNode;

            //设置下一次节点
            prevNode = currNode;
            currNode = nexNode;

            nexNode = nexNode.next;

            if(nexNode==null){
                currNode.next = prevNode;
            }
        }

        return currNode;
    }

    //方案1
    public static ListNode reverseList1(ListNode head) {

        List<ListNode> list = new ArrayList<>();
        list.add(head);
        ListNode prev = head.next;
        while (prev != null ) {
            list.add(prev);
            prev = prev.next;
        }

        ListNode result = null;
        ListNode tempListNode = null;
        for(int i=list.size();i>0; i--){
            if(i==list.size()){
                result = list.get(i-1);
                tempListNode = result;
            }else{
                tempListNode.next = list.get(i-1);
                tempListNode = list.get(i-1);
                if(i==1){
                    list.get(i-1).next = null;
                }
            }
        }

        return result;
    }


    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
}
