package devices.model;

public enum DeviceType {

    GATEWAY(1), SWITCH(2), ACCESS_POINT(3);

    private int orderPrecedence;

    public int getOrderPrecedence() {
        return orderPrecedence;
    }

    DeviceType(int orderPrecedence) {
        this.orderPrecedence = orderPrecedence;
    }


}