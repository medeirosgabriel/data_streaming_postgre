package com.medeirosgabriel.job.task;

import com.medeirosgabriel.job.enums.OrderStatus;
import com.medeirosgabriel.job.kafka.SendMessageService;
import com.medeirosgabriel.job.postgres_async.NotifierService;
import com.medeirosgabriel.job.model.Order_;
import com.medeirosgabriel.job.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OrderTask {

    @Autowired
    NotifierService notifierService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SendMessageService sendMessageService;

    private final Logger log = LoggerFactory.getLogger(OrderTask.class);

    // 5000 -> 5 seconds
    @Scheduled(fixedRate = 10000)
    @Transactional
    public void reportCurrentTime() throws InterruptedException {
        List<Order_> orders = this.orderRepository.findByOrderStatus(OrderStatus.PENDING);
        for (Order_ order: orders) {
            //int randomInt = this.randomNumber(1, 10);
            //int sleepTime = randomInt * 1000;
//            int sleepTime = 5 * 1000;
//            Thread.sleep(sleepTime);
            log.info(String.format("Order %d COMPLETED", order.getId()));

            order.setOrderStatus(OrderStatus.COMPLETED);
            this.orderRepository.save(order);

            notifierService.notifyOrderCreated(order);

            sendMessageService.sendOrderUpdate(order);
        }
    }

    private int randomNumber(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }
}
