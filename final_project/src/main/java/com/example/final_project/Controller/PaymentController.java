package com.example.final_project.Controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.final_project.Service.PayPalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PayPalService payPalService;


    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<Map<String, String>> createPayment(@RequestParam Double amount, @RequestParam String description) {
        try {
            Payment payment = payPalService.createPayment(amount, description);
            String approvalUrl = payment.getLinks().stream()
                .filter(link -> "approval_url".equals(link.getRel()))
                .findFirst()
                .map(Links::getHref)
                .orElseThrow(() -> new RuntimeException("Approval URL non trovato"));

            
            return ResponseEntity.ok(Map.of("approvalUrl", approvalUrl));
        } catch (PayPalRESTException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/success")
    public String paymentSuccess(@RequestParam String paymentId, @RequestParam String PayerID) {
        try {
            Payment payment = payPalService.executePayment(paymentId, PayerID);
            if("approved".equalsIgnoreCase(payment.getState())) {
                return "redirect:/?payment=success&id=" + payment.getId();
            }
        } catch (PayPalRESTException e) {
            return "redirect:/?payment=error";
        }
        return "redirect:/?payment=error";
    }

    @GetMapping("/cancel")
    public String paymentCancel() {
        return "redirect:/?payment=cancelled";
    }
}
