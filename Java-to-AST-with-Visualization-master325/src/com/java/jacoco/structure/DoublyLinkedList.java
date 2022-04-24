package com.java.jacoco.structure;


/**
 * 用Java定义一个双向链表，实现链表的基本操作： 初始化、获取头结点、添加新元素、删除链表元素、 获取链表元素、查找链表元素、更新链表中某个元素、
 * 判断链表是否为空、求链表元素个数、输出链表元素、清空链表。
 * 
 * @ProjectName: Test
 * @ClassName: DoublyLinkedList
 
 */
public class DoublyLinkedList<T> {
	
	@SuppressWarnings("hiding")
	class Node<T>
    {
		private T data;
        private Node<T> next=null;
        private Node<T> pre=null; 
        public Node(T data) {
			this.data=data;
		}
    }
	private Node<T> first=null;
    private Node<T> last=null; 
    private int size=0;
	
	/**
	 * 初始化
	 */
	public DoublyLinkedList(){
		first = null;
        last = null;
	}
	
	/**
	 * 获取头结点
	 * @MethodName: getFrist
	 * @Description: 
	 * @return Node<T> 
	 * @throws
	 */
	public Node<T> getFrist(){
		return first;
	}
	
	/**
	 * 获取尾结点
	 * @MethodName: getLast
	 * @Description: 
	 * @return Node<T> 
	 * @throws
	 */
	public Node<T> getLast(){
		return last;
	}
	
	/**
	 * 添加头新元素
	 * @MethodName: insertFirst
	 * @Description: 
	 * @param obj void
	 * @throws
	 */
	public void insertFirst(T obj){
		Node<T> node = new Node<T>(obj);
		node.data=obj;
		if(first==null){
			last=node;
		}else{
			node.next=first;
			first.pre=node;
		}
		first=node;
		size++;
	}
	
	/**
	 * 添加尾新元素
	 * @MethodName: insertLast
	 * @Description: 
	 * @param obj void 
	 * @throws
	 */
	public void insertLast(T obj){
		Node<T> node = new Node<T>(obj);
		if(first==null){
			first=node;
		}else{
			last.next=node;
			node.pre=last;
		}
		last=node;
		size++;
	}
	/**
	 * 在指定节点后面插入新节点
	 * @MethodName: insertAfter
	 * @Description: 
	 * @param target
	 * @param obj
	 * @return boolean 
	 * @throws
	 */
	public boolean insertAfter(T target,T obj){
		Node<T> node = new Node<T>(obj);
		Node<T> temp = first;
		while(temp != null){
			if(temp.data.equals(target)){
				node.next = temp.next;
				node.pre = temp;
				if(temp == last)
					last = node;
				else
					temp.next.pre = node;
				temp.next = node;
				size++;
				return true;
			}
			temp = temp.next;
		}
		
		return false;
	}

	/**
	 * 删除头节点
	 * @MethodName: deleteFirst
	 * @Description: 
	 * @return
	 * @throws Exception T 
	 * @throws
	 */
	public Node<T> deleteFirst() throws Exception{
		if(first == null)
			throw new Exception("empty!");
		Node<T> temp = first;
		if(first.next == null){
			first = null;
			last = null;
		}else{
			first.next.pre = null;
			first = first.next;
		}
		size--;
		return temp;
	}
	
	/**
	 * 删除尾节点
	 * @MethodName: deleteLast
	 * @Description: 
	 * @return
	 * @throws Exception T 
	 * @throws
	 */
	public Node<T> deleteLast() throws Exception{
		if(first == null)
			throw new Exception("empty!");
		Node<T> temp = last;
		if(first.next == null){
			first = null;
			last = null;
		}else{
			last.pre.next = null;
			last = last.pre;
		}
		size--;
		return temp;
	}

	/**
	 * 删除指定节点
	 * @MethodName: delete
	 * @Description: 
	 * @param obj
	 * @throws Exception void 
	 * @throws
	 */
	public void delete(T obj) throws Exception{
		//检查链表是否为空
		if(isEmpty())
			throw new Exception("empty!");
		Node<T> temp = first;
		while (temp != null) {
			if (temp.data.equals(obj)) {
				if (temp == last)
					last = temp.pre;
				else
					temp.next.pre = temp.pre;
				if (temp == first)
					first = temp.next;
				else
					temp.pre.next = temp.next;
			}
			temp = temp.next;
		}
		size--;
	}
	/**
	 * 获取链表的第index个位置的元素
	 * @MethodName: getElement
	 * @Description: 
	 * @param index
	 * @return Node<T> 
	 * @throws
	 */
	public Node<T> getElement(int index){
		if(index<=0 || index>size()){
            System.out.println("获取链表的位置有误！返回null，显示当前链表信息");
            return first;
        }else{
        	// 当索引的值小于该链表长度的一半时，那么从链表的头结点开始向后找是最快的
        	if(index<size/2){
        		Node<T> temp = first;
        		for(int i=1;i<index;i++){
        		      temp = temp.next;
        		}
        	    return temp;
        	}else{
        		//当索引值位于链表的后半段时，则从链表的另端开始找是最快的
            	Node<T> temp = last;
            	int newIndex = size - (index-1);
            	for(int i=1;i<newIndex;i++){
            	     temp = temp.pre;
                }
            	return temp;
        	}
        	
        }
		
	}
	
	/**
     * 查找链表元素
     * @MethodName: isContain
     * @Description: 
     * @param o
     * @return Boolean 
     * @throws
     */
    public Boolean isContain(T obj){
    	Node<T> temp = first;
		for(int i=1;i<size();i++){
			if(temp.data.equals(obj)){
				return true;
			}
			temp = temp.next;
		}
	    return false;
    }
	
    /**
     * 更新链表中某个元素
     * @MethodName: modify
     * @Description: 
     * @param o
     * @param pos
     * @return boolean 
     * @throws
     */
    public boolean modify(T obj,int pos){
		if(isEmpty()){
			System.out.println("链表为空");
			return false;
		}else{
			if(pos<1 || pos>size()){
				System.out.println("pos值不合法");
				return false;
			}
			int num =1;
			Node<T> temp = first;
			while (num<pos) {
				temp =temp.next;
				num++;
			}
			temp.data = obj;
			return true;
		}
    }
    
	/**
     * 判断链表是否为空
     * @MethodName: isEmpty
     * @Description: 
     * @return boolean 
     * @throws
     */
  	public boolean isEmpty(){
  		return size() == 0;
  	}
  	
  	/**
     * 求链表元素个数
     * @MethodName: size
     * @Description: 
     * @return int 
     * @throws
     */
    public int size(){
        return size;
    }
	
    /**
     * 打印链表
     * @MethodName: print
     * @Description:  void 
     * @throws
     */
	public void print(){
		System.out.print("first -> last : ");
		Node<T> node = first;
		while(node != null){
			System.out.print(node.data.toString() + " -> ");
			node = node.next;
		}
		System.out.print("\n");
	}
	
	/**
	 * 清空链表
	 * @MethodName: clear
	 * @Description:  void 
	 * @throws
	 */
    public void clear(){
    	first=null;
    	last=first;
	}
	
	public static void main(String[] args) throws Exception{
		//初始化
		DoublyLinkedList<Integer> doublyLinkedList = new DoublyLinkedList<Integer>();
		//插入头节点
		doublyLinkedList.insertFirst(1);
		//插入尾节点
		doublyLinkedList.insertLast(3);
		doublyLinkedList.print();
		//在1后面插入2
		doublyLinkedList.insertAfter(1, 2);
		doublyLinkedList.print();
		//在3后面插入4
		doublyLinkedList.insertAfter(3, 4);
		//在4后面插入5
		doublyLinkedList.insertAfter(4, 5);
		doublyLinkedList.print();
		
		//获取指定位置的数据
		System.out.println("第3个元素的数据为："+doublyLinkedList.getElement(3).data);
		//判断元素1是否存在
		System.out.println("元素1是否存在："+doublyLinkedList.isContain(1));
		//将第5个位置的元素替换成55
		doublyLinkedList.modify(55,5);
		doublyLinkedList.print();
		//删除头节点
		doublyLinkedList.deleteFirst();
		doublyLinkedList.print();
		//删除尾节点
		doublyLinkedList.deleteLast();
		doublyLinkedList.print();
		//删除指定节点
		doublyLinkedList.delete(3);
		doublyLinkedList.print();
		//清空链表
		doublyLinkedList.clear();
		doublyLinkedList.print();
	}
}
