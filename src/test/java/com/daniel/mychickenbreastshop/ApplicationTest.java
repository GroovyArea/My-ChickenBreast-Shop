package com.daniel.mychickenbreastshop;

import com.daniel.ddd.order.adaptor.out.persistence.OrderProductRepository;
import com.daniel.ddd.order.adaptor.out.persistence.OrderRepository;
import com.daniel.ddd.payment.adaptor.out.persistence.CardRepository;
import com.daniel.ddd.payment.adaptor.out.persistence.PaymentRepository;
import com.daniel.mychickenbreastshop.domain.product.category.model.CategoryRepository;
import com.daniel.mychickenbreastshop.domain.product.item.model.ProductRepository;
import com.daniel.mychickenbreastshop.domain.user.model.UserRepository;
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

}
