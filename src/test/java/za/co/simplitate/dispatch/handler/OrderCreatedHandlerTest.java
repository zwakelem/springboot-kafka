package za.co.simplitate.dispatch.handler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.simplitate.dispatch.message.OrderCreated;
import za.co.simplitate.dispatch.service.DispatchService;
import za.co.simplitate.dispatch.util.TestEventData;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderCreatedHandlerTest {

    @Mock
    private DispatchService dispatchService;
    @InjectMocks
    private OrderCreatedHandler orderCreatedHandler;

    @Test
    void listen_sucess() throws Exception {
        OrderCreated testEvent = TestEventData.buildOrderCreatedEvent(randomUUID(), "item");
        orderCreatedHandler.listen(testEvent);
        verify(dispatchService, times(1)).process(testEvent);
    }

    void listen_throwsException() throws Exception {
        OrderCreated testEvent = TestEventData.buildOrderCreatedEvent(randomUUID(), "item");
        doThrow(new RuntimeException("Service failure")).when(dispatchService).process(testEvent);

        orderCreatedHandler.listen(testEvent);
        verify(dispatchService, times(1)).process(testEvent);
    }
}