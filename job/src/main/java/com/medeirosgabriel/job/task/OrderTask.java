package com.medeirosgabriel.job.task;

import com.medeirosgabriel.job.model.Order_;
import com.medeirosgabriel.job.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class OrderTask {

    @Autowired
    private OrderRepository orderRepository;
    private final Logger log = LoggerFactory.getLogger(OrderTask.class);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    // 5000 -> 5 seconds
    @Scheduled(fixedRate = 1000)
    public void reportCurrentTime() throws InterruptedException {
        List<Order_> orders = this.orderRepository.findAll();
        System.out.println(orders.size());
        for (Order_ order: orders) {
            int randomInt = this.randomNumber(1, 10);
            int sleepTime = randomInt * 1000;
            System.out.println(sleepTime);
            Thread.sleep(sleepTime);
        }
        //log.info("The time is now {}", dateFormat.format(new Date()));
    }

    private int randomNumber(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }
}
