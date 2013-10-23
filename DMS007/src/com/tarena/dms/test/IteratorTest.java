package com.tarena.dms.test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** 
 * @author  Viking
 * @version 创建时间：2013年10月21日 下午9:25:32 
 * 
 */
public class IteratorTest {
	
	public static void main(String[] args) {
		Set<Integer> set = new HashSet<Integer>();
		
		set.add(1);
		set.add(3);
		set.add(4);
		set.add(5);
		
		for(Iterator<Integer> it1 = set.iterator();it1.hasNext();){
			 int a = it1.next();
			Iterator<Integer> it2 = set.iterator();it2.hasNext();{
				it2.next();
				it2.remove();
			}
			System.out.println(a);
		}
		
		
	}

}
