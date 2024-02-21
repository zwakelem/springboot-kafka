package za.co.simplitate.dispatch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import za.co.simplitate.dispatch.message.OrderCreated;
import za.co.simplitate.dispatch.message.OrderDispatched;

@Slf4j
@Service
@RequiredArgsConstructor
public class DispatchService {

    private static final String ORDER_DISPATCHED_TOPIC = "order.dispatched";

    private final KafkaTemplate<String, Object> kafkaProducer;

    public void process(OrderCreated payload) throws Exception {
        OrderDispatched orderDispatched = OrderDispatched.builder()
                .orderId(payload.getOrderId()).build();
        kafkaProducer.send(ORDER_DISPATCHED_TOPIC, orderDispatched).get();
    }
}
