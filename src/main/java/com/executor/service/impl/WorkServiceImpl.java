package com.executor.service.impl;

import com.executor.service.WorkService;
import org.springframework.stereotype.Service;

@Service
public class WorkServiceImpl implements WorkService {

  @Override
  public String doWork(String work) {
    try {
      Thread.sleep(1000);
      String threadName = Thread.currentThread().getName();
      System.out.println(work + " " + threadName);
    } catch (InterruptedException e) {
      e.printStackTrace();
      return null;
    }
    return work.toUpperCase();
  }
}
