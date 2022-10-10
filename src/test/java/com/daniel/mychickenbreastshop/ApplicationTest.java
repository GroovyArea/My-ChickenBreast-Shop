package com.daniel.mychickenbreastshop;

import com.daniel.mychickenbreastshop.domain.payment.domain.order.OrderProductRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.OrderRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.CardRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.PaymentRepository;
import com.daniel.mychickenbreastshop.domain.product.domain.category.CategoryRepository;
import com.daniel.mychickenbreastshop.domain.product.domain.item.ProductRepository;
import com.daniel.mychickenbreastshop.domain.user.domain.UserRepository;
import com.daniel.mychickenbreastshop.global.redis.store.RedisStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected PaymentRepository paymentRepository;

    @Autowired
    protected OrderProductRepository orderProductRepository;

    @Autowired
    protected CardRepository cardRepository;

    @Autowired
    protected RedisStore redisStore;

}
