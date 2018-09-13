package lgjt.common.base.constants;

/**
 * @author wuguangwei
 * @date 2018/7/19
 * @Description: 支付状态
 */
public enum PayStatus {

    CREATE_PAYMENT(0, "付款中"),
    PAY_SUCCESS(1, "付款完成"),
    PAY_FAIL(2, "付款失败"),
    PAY_CHECKING(3, "取消"),
    PAY_CLOSE(4, "未知"),
    REFUND_SUCCESS(5, "全部退款"), ;

    private int value;

    private String name;

    PayStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }


}
