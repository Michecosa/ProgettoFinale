package com.example.final_project.Service;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.example.final_project.Config.PayPalConfig;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayPalService {
    private final APIContext apiContext;
    private final PayPalConfig config;

    public Payment createPayment(Double total, String description) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(config.getCurrency());
        amount.setTotal(String.format(Locale.US, "%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(config.getCancelUrl());
        redirectUrls.setReturnUrl(config.getReturnUrl());

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(List.of(transaction));
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution execution = new PaymentExecution();
        execution.setPayerId(payerId);
        
        return payment.execute(apiContext, execution);
    }
}
