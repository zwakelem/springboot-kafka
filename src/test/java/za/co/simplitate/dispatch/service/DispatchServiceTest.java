package za.co.simplitate.dispatch.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import za.co.simplitate.dispatch.message.OrderCreated;
import za.co.simplitate.dispatch.message.OrderDispatched;
import za.co.simplitate.dispatch.util.TestEventData;

import java.util.concurrent.CompletableFuture;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DispatchServiceTest {

    @InjectMocks
    private DispatchService service;

    @Mock
    private KafkaTemplate kafkaTemplateMock;

    @Test
    void process_sucess() throws Exception {
        when(kafkaTemplateMock.send(anyString(), any(OrderDispatched.class))).thenReturn(mock(CompletableFuture.class));

        OrderCreated testEvent = TestEventData.buildOrderCreatedEvent(randomUUID(), "item");
        service.process(testEvent);

        verify(kafkaTemplateMock,atLeastOnce()).send(eq("order.dispatched"), any(OrderDispatched.class));
    }

    @Test
    void process_producerThrowsException() throws Exception {
        OrderCreated testEvent = TestEventData.buildOrderCreatedEvent(randomUUID(), "item");
        doThrow(new RuntimeException("Producer failure")).when(kafkaTemplateMock).send(eq("order.dispatched"), any(OrderDispatched.class));

        Exception exception = assertThrows(RuntimeException.class, () -> service.process(testEvent));

        verify(kafkaTemplateMock,atLeastOnce()).send(eq("order.dispatched"), any(OrderDispatched.class));
        assertEquals(exception.getMessage(), "Producer failure");
    }
}