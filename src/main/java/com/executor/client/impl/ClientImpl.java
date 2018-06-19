package com.executor.client.impl;

import com.executor.client.Client;
import com.executor.service.WorkService;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

public class ClientImpl implements Client {

  public static final int N_THREADS = 4;

  @Autowired
  private WorkService workService;

  @PostConstruct
  private void setUp(){
    doManyTasks();
  }

  @Override
  public void doManyTasks() {

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    ArrayList<String> works = Lists.newArrayList("work1", "work2", "work3");

    List<Callable<String>> taskList = works.stream()
        .map((work) -> (Callable<String>) () -> workService.doWork(work))
        .collect(Collectors.toList());

    ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
    try {
      executorService.invokeAll(taskList).stream()
          .map(f -> {
            try {
              return f.get();
            } catch (Exception e) {
              e.printStackTrace();
            }
            return null;
          })
          .forEach(r -> System.out.println("result " + r));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    stopWatch.stop();
    long lastTime = stopWatch.getLastTaskTimeMillis();
    System.out.println("All work takes " + lastTime / 1000. + " sec.");
    executorService.shutdown();

  }
}
