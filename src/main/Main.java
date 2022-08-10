package main;

import model.ModelAfter;
import util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {

        String line = "";
        String splitBy = ";";
        String[] header = {"id", "Nama", "Age",	"Balanced",	"No 2b Thread-No",	"No 3 Thread-No", "Previous Balanced",
                "Average Balanced",	"No 1 Thread-No", "Free Transfer", "No 2a Thread-No"
        };
        AtomicInteger budget = new AtomicInteger(1000);
        List<ModelAfter> modelList = new ArrayList<>();
        List<String[]> datas = new ArrayList<>();
        datas.add(header);
        try{
            BufferedReader br = new BufferedReader(new FileReader("Before Eod.csv"));
            int idx = 0;
            while ((line = br.readLine()) != null)
            {
                if(idx != 0){
                    String[] cust = line.split(splitBy);
                    ModelAfter model = new ModelAfter();
                    model.setId(Integer.parseInt(cust[0]));
                    model.setNama(cust[1]);
                    model.setAge(Integer.parseInt(cust[2]));
                    model.setBalanced(Integer.parseInt(cust[3]));
                    model.setPreviousBalanced(Integer.parseInt(cust[4]));
                    model.setAverageBalanced(Integer.parseInt(cust[5]));
                    model.setFreeTransfer(Integer.parseInt(cust[6]));
                    modelList.add(model);
                }
                idx++;
            }

            modelList.parallelStream().forEach((modelAfter) -> {
                System.out.println("id : " + modelAfter.getId() + ", name : " + modelAfter.getNama());
                //avg
                RunnableAvg Rav1 = new RunnableAvg( "1", modelAfter);
                Rav1.start();

                RunnableAvg Rav2 = new RunnableAvg( "2", modelAfter);
                Rav2.start();

                RunnableAvg Rav3 = new RunnableAvg( "3", modelAfter);
                Rav3.start();

                //free transfer
                if(modelAfter.getBalanced() != null && modelAfter.getBalanced() >= 100 && modelAfter.getBalanced() <= 150){
                    RunnableFreeTrf Rft1 = new RunnableFreeTrf( "4", 5, modelAfter);
                    Rft1.start();

                    RunnableFreeTrf Rft2 = new RunnableFreeTrf( "5", 5, modelAfter);
                    Rft2.start();

                    RunnableFreeTrf Rft3 = new RunnableFreeTrf( "6", 5, modelAfter);
                    Rft3.start();
                }else if(modelAfter.getBalanced() != null && modelAfter.getBalanced() > 150){
                    RunnableFreeTrf Rft1 = new RunnableFreeTrf( "4", 25, modelAfter);
                    Rft1.start();

                    RunnableFreeTrf Rft2 = new RunnableFreeTrf( "5", 25, modelAfter);
                    Rft2.start();

                    RunnableFreeTrf Rft3 = new RunnableFreeTrf( "6", 25, modelAfter);
                    Rft3.start();
                }else {
                    RunnableFreeTrf Rft1 = new RunnableFreeTrf("4", 0, modelAfter);
                    Rft1.start();

                    RunnableFreeTrf Rft2 = new RunnableFreeTrf("5", 0, modelAfter);
                    Rft2.start();

                    RunnableFreeTrf Rft3 = new RunnableFreeTrf("6", 0, modelAfter);
                    Rft3.start();
                }

                if(budget.get() >= 10){

                    RunnableBonusBal Rbb1 = new RunnableBonusBal("7", 10, modelAfter);
                    Rbb1.start();

                    RunnableBonusBal Rbb2 = new RunnableBonusBal("8", 10, modelAfter);
                    Rbb2.start();

                    RunnableBonusBal Rbb3 = new RunnableBonusBal("9", 10, modelAfter);
                    Rbb3.start();

                    RunnableBonusBal Rbb4 = new RunnableBonusBal("10", 10, modelAfter);
                    Rbb4.start();

                    RunnableBonusBal Rbb5 = new RunnableBonusBal("11", 10, modelAfter);
                    Rbb5.start();

                    RunnableBonusBal Rbb6 = new RunnableBonusBal("12", 10, modelAfter);
                    Rbb6.start();

                    RunnableBonusBal Rbb7 = new RunnableBonusBal("13", 10, modelAfter);
                    Rbb7.start();

                    RunnableBonusBal Rbb8 = new RunnableBonusBal("14", 10, modelAfter);
                    Rbb8.start();

                    budget.addAndGet(-10);
                }

                datas.add(new String[]{String.valueOf(modelAfter.getId()),
                        modelAfter.getNama(),
                        String.valueOf(modelAfter.getAge()),
                        String.valueOf(modelAfter.getBalanced()),
                        modelAfter.getNo2bThreadNo() == null ? "x" : modelAfter.getNo2bThreadNo(),
                        modelAfter.getNo3ThreadNo() == null ? "x" : modelAfter.getNo3ThreadNo(),
                        String.valueOf(modelAfter.getPreviousBalanced()),
                        String.valueOf(modelAfter.getAverageBalanced()),
                        modelAfter.getNo1ThreadNo() == null ? "x" : modelAfter.getNo1ThreadNo(),
                        String.valueOf(modelAfter.getFreeTransfer()),
                        modelAfter.getNo2aThreadNo()  == null ? "x" : modelAfter.getNo2aThreadNo()
                });
            });

            Util writer = new Util();
            writer.writeToCsvFile(datas, new File("After Eod.csv"));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    static class RunnableAvg implements Runnable {
        private Thread t;
        private String threadName;
        private ModelAfter modelAfter;

        RunnableAvg( String name, ModelAfter mod) {
            threadName = name;
            this.modelAfter = mod;
            System.out.println("Creating " +  threadName );
        }

        public void run() {
            System.out.println("Running " +  threadName );
            try {
                OptionalDouble newAvgBal = IntStream.of(modelAfter.getBalanced()+modelAfter.getPreviousBalanced()).average();
                modelAfter.setAverageBalanced((int) newAvgBal.orElse(0D));
                modelAfter.setNo1ThreadNo(threadName);
            } catch (Exception e) {
                System.out.println("Thread " +  threadName + " error.");
                e.printStackTrace();
            }
            System.out.println("Thread " +  threadName + " exiting.");
        }

        public void start () {
            System.out.println("Starting " +  threadName );
            if (t == null) {
                t = new Thread (this, threadName);
                t.start ();
            }
        }
    }

    static class RunnableFreeTrf implements Runnable {
        private Thread t;
        private Integer i;
        private String threadName;
        private ModelAfter modelAfter;

        RunnableFreeTrf( String name, Integer count, ModelAfter mod) {
            threadName = name;
            i = count;
            this.modelAfter = mod;
            System.out.println("Creating " +  threadName );
        }

        public void run() {
            System.out.println("Running " +  threadName );
            try {
                if(i == 5){
                    modelAfter.setFreeTransfer(i);
                    modelAfter.setNo2aThreadNo(threadName);
                }else{
                    modelAfter.setBalanced(modelAfter.getBalanced()+25);
                    modelAfter.setNo2bThreadNo(threadName);
                }

            } catch (Exception e) {
                System.out.println("Thread " +  threadName + " error.");
                e.printStackTrace();
            }
            System.out.println("Thread " +  threadName + " exiting.");
        }

        public void start () {
            System.out.println("Starting " +  threadName );
            if (t == null) {
                t = new Thread (this, threadName);
                t.start ();
            }
        }
    }

    static class RunnableBonusBal implements Runnable {
        private Thread t;
        private Integer i;
        private String threadName;
        private ModelAfter modelAfter;

        RunnableBonusBal( String name, Integer count, ModelAfter mod) {
            threadName = name;
            i = count;
            this.modelAfter = mod;
            System.out.println("Creating " +  threadName );
        }

        public void run() {
            System.out.println("Running " +  threadName );
            try {
                modelAfter.setBalanced(modelAfter.getBalanced()+i);
                modelAfter.setNo3ThreadNo(threadName);
            } catch (Exception e) {
                System.out.println("Thread " +  threadName + " error.");
                e.printStackTrace();
            }
            System.out.println("Thread " +  threadName + " exiting.");
        }

        public void start () {
            System.out.println("Starting " +  threadName );
            if (t == null) {
                t = new Thread (this, threadName);
                t.start ();
            }
        }
    }
}
