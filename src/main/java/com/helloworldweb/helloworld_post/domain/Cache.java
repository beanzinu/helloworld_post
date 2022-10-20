package com.helloworldweb.helloworld_post.domain;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cache<Long,T> extends LinkedHashMap<Long,T> {
    private int capacity;


    public Cache(int initialCapacity) {
        // accessOrder 순으로 저장
        super(initialCapacity,0.75F,true);
        this.capacity = initialCapacity;
    }

    // 해당 함수를 호출해 LRU 알고리즘으로 제거할지 판단 
    @Override
    protected boolean removeEldestEntry(Map.Entry<Long, T> eldest) {
        // 캐시의 크기가 capacity 를 넘어갈 경우
        return size() > capacity;
    }
    
    // 여러 쓰레드에 의해 캐시가 삽입되는 것을 방지
    synchronized public T syncPut(Long id,T element){
        // cache full -> LRU 알고리즘
        // LinkedHashMap removeElderEntry 실행해서 확인
        super.put(id,element);
        System.out.println("this.keySet() = " + this.keySet());
        return element;
    }

    
    synchronized public void syncPutAll(Map<? extends Long, ? extends T> m) {
        super.putAll(m);
        System.out.println("this.keySet() = " + this.keySet());
    }

}
