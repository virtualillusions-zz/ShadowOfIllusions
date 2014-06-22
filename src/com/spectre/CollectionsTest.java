package com.spectre;

import java.util.*;

public class CollectionsTest {
 
 public static void main(String[] args) {
  List l = new ArrayList();
  Map m = new TreeMap();
  Set s = new TreeSet();
  
  l.add(new Integer(1));
  l.add(new Integer(4));
  l.add(new Integer(3));
  l.add(new Integer(2));
  l.add(new Integer(3));
  
  m.put(new Integer(1), "A");
  m.put(new Integer(4), "B");
  m.put(new Integer(3), "C");
  m.put(new Integer(2), "D");
  m.put(new Integer(3), "E");
  
  s.add(new Integer(1));
  s.add(new Integer(4));
  s.add(new Integer(3));
  s.add(new Integer(2));
  s.add(new Integer(3));
  
  System.out.println("List");
  Iterator i = l.iterator();
  while (i.hasNext()) System.out.println(i.next());
  
  System.out.println("Map using keys");
  i = m.keySet().iterator();
  while (i.hasNext()) System.out.println(m.get(i.next()));
  
  System.out.println("Map using entries");
  i = m.entrySet().iterator();
  while (i.hasNext()) System.out.println(i.next());
  
  System.out.println("Set");
  i = s.iterator();
  while (i.hasNext()) System.out.println(i.next());
 }
}