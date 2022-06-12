package tech.astromobile.merchant;

public class SetterOrders {

    public SetterOrders() {
    }

    private String merchantUid, clientUid, key, orderCode, deliveryAreaKey, notes, name, surname, confirmationCode, address, mobile;
    private double orderCharge, deliveryCharge, processingCharge, serviceCharge, takeawayCharge, total;
    private long timestamp, deliveryStart, deliverBefore, deliveredOn;
    private boolean isPaid, isPreparing, isClub, isDone, isDelivered, isTakeaway;

    public SetterOrders(String uid, String clientUid, String key, String orderCode, String deliveryAreaKey,
                        String notes, String name, String surname, String confirmationCode, String address, String mobile,
                        double orderCharge, double deliveryCharge, double processingCharge,
                        double serviceCharge, double takeawayCharge, double total,
                        long timestamp, long deliveryStart, long deliverBefore, long deliveredOn,
                        boolean isPaid, boolean isPreparing, boolean isClub,
                        boolean isDone, boolean isDelivered, boolean isTakeaway) {
        this.merchantUid = uid;
        this.clientUid = clientUid;
        this.key = key;
        this.orderCode = orderCode;
        this.deliveryAreaKey = deliveryAreaKey;
        this.notes = notes;
        this.name = name;
        this.surname = surname;
        this.confirmationCode = confirmationCode;
        this.address = address;
        this.mobile = mobile;
        this.orderCharge = orderCharge;
        this.deliveryCharge = deliveryCharge;
        this.processingCharge = processingCharge;
        this.serviceCharge = serviceCharge;
        this.takeawayCharge = takeawayCharge;
        this.total = total;
        this.timestamp = timestamp;
        this.deliveryStart = deliveryStart;
        this.deliverBefore = deliverBefore;
        this.deliveredOn = deliveredOn;
        this.isPaid = isPaid;
        this.isPreparing = isPreparing;
        this.isClub = isClub;
        this.isDone = isDone;
        this.isDelivered = isDelivered;
        this.isTakeaway = isTakeaway;
    }

    public String getClientUid() {
        return clientUid;
    }

    public void setClientUid(String clientUid) {
        this.clientUid = clientUid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getMerchantUid() {
        return merchantUid;
    }

    public void setMerchantUid(String merchantUid) {
        this.merchantUid = merchantUid;
    }

    public String getDeliveryAreaKey() {
        return deliveryAreaKey;
    }

    public void setDeliveryAreaKey(String deliveryAreaKey) {
        this.deliveryAreaKey = deliveryAreaKey;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getDeliveryStart() {
        return deliveryStart;
    }

    public void setDeliveryStart(long deliveryStart) {
        this.deliveryStart = deliveryStart;
    }

    public double getTakeawayCharge() {
        return takeawayCharge;
    }

    public void setTakeawayCharge(double takeawayCharge) {
        this.takeawayCharge = takeawayCharge;
    }

    public boolean isTakeaway() {
        return isTakeaway;
    }

    public void setTakeaway(boolean takeaway) {
        isTakeaway = takeaway;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getOrderCharge() {
        return orderCharge;
    }

    public void setOrderCharge(double orderCharge) {
        this.orderCharge = orderCharge;
    }

    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public double getProcessingCharge() {
        return processingCharge;
    }

    public void setProcessingCharge(double processingCharge) {
        this.processingCharge = processingCharge;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getDeliverBefore() {
        return deliverBefore;
    }

    public void setDeliverBefore(long deliverBefore) {
        this.deliverBefore = deliverBefore;
    }

    public long getDeliveredOn() {
        return deliveredOn;
    }

    public void setDeliveredOn(long deliveredOn) {
        this.deliveredOn = deliveredOn;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public boolean isPreparing() {
        return isPreparing;
    }

    public void setPreparing(boolean preparing) {
        isPreparing = preparing;
    }

    public boolean isClub() {
        return isClub;
    }

    public void setClub(boolean club) {
        isClub = club;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }
}
