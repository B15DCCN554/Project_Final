package common;

import bean.QrTerminalPo;

public class DivisionQueue {
    //use 4 queue
    private static final int NUMBER_SHARK = 4;

    //queue = id mod 4
    public static String getQueueName(int id){
        int sharkNumber = id % NUMBER_SHARK;
        Shark shark = Shark.getSharkByValue(sharkNumber);
        return shark.getQueueName();
    }
}
