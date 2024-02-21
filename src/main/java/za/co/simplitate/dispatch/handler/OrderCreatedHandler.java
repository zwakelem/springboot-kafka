package za.co.simplitate.dispatch.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import za.co.simplitate.dispatch.message.OrderCreated;
import za.co.simplitate.dispatch.service.DispatchService;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderCreatedHandler {

    private final DispatchService dispatchService;

    @KafkaListener(
            id = "orderConsumerClient",
            topics = "order.created",
            groupId = "dispatch.order.created.consumer",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(OrderCreated payload) {
        log.info("Received message: {}", payload);
        try {
            dispatchService.process(payload);
        } catch (Exception e) {
            log.error("Processing failure", e);
        }
    }
}
