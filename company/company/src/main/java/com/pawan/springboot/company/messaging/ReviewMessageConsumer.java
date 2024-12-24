package com.pawan.springboot.company.messaging;

import com.pawan.springboot.company.CompanyService;
import com.pawan.springboot.company.dto.ReviewMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ReviewMessageConsumer {
    private final CompanyService companyService;

    public ReviewMessageConsumer(CompanyService companyService) {
        this.companyService = companyService;
    }

    @RabbitListener(queues = "companyRatingQueue")
    public void consumeMessage(ReviewMessage reviewMessage){

        companyService.updateCompanyRating(reviewMessage);
    }
}
