package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;

import java.util.List;

public class OrderResponse {
    private long id;
    private String orderStatus;
    private String orderedTime;
    private List<OrderLineResponse> orderLineResponses;

    public OrderResponse() {
    }

    public OrderResponse(long id, String orderStatus, String orderedTime) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public OrderResponse(long id, String orderStatus, String orderedTime, List<OrderLineResponse> orderLineResponses) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineResponses = orderLineResponses;
    }

    public long getId() {
        return id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getOrderedTime() {
        return orderedTime;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getId(), order.getOrderStatus(), order.getOrderedTime());
    }

    public static OrderResponse of(Order order, List<OrderLineResponse> orderLineResponses) {
        return new OrderResponse(order.getId(), order.getOrderStatus(), order.getOrderedTime(), orderLineResponses);
    }
}
