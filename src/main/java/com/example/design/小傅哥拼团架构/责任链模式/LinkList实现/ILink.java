package com.example.design.小傅哥拼团架构.责任链模式.LinkList实现;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 链接口,原生的linkedList里node是私有的，无法进行移动
 * @create 2025-01-18 09:27
 */
public interface ILink<E> {

    boolean add(E e);

    boolean addFirst(E e);

    boolean addLast(E e);

    boolean remove(Object o);

    E get(int index);

    void printLinkList();

}
