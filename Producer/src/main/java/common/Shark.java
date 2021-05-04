package common;

public enum Shark {
        SHARK_ZERO(0, "QUEUE_ZERO"),
        SHARK_ONE(1, "QUEUE_ONE"),
        SHARK_TWO(2, "QUEUE_TWO"),
        SHARK_THREE(3, "QUEUE_THREE");

        private final String queueName;
        private int sharkValue;

    Shark(int sharkValue, String queueName) {
        this.sharkValue = sharkValue;
        this.queueName = queueName;
    }

    public static Shark getSharkByValue(int sharkValue) {
        for (Shark shark : Shark.values()) {
            if (shark.sharkValue == sharkValue) {
                return shark;
            }
        }
        return null;
    }

    public int getSharkValue() {
        return sharkValue;
    }

    public String getQueueName() {
        return queueName;
    }
}
