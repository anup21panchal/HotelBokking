package com.hotel.hotel_booking_service.service;

import com.hotel.hotel_booking_service.domain.Booking;
import com.hotel.hotel_booking_service.domain.User;
import org.springframework.stereotype.Service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;

    @Value("${support.email.to:}")
    private String supportEmailTo;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Value("${app.frontend.confirm-url-prefix:http://localhost:8080/api/auth/confirm?token=}")
    private String confirmUrlPrefix;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends confirmation email to a newly registered user.
     *
     * @param user  user who registered
     * @param token confirmation token
     */
    @Async
    public void sendUserConfirmation(User user, String token) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(user.getEmail());
            msg.setFrom(fromEmail);
            msg.setSubject("Please confirm your account");
            String confirmLink = confirmUrlPrefix + token;
            msg.setText(String.format("Hello %s,\n\nThank you for registering. Please confirm your account by clicking:\n\n%s\n\nIf you did not sign up, ignore this email.",
                    user.getName(), confirmLink));
            mailSender.send(msg);
            System.out.println("Sent confirmation email to " + user.getEmail());
        } catch (Exception ex) {
            // log and move on (fire-and-forget)
            ex.printStackTrace();
        }
    }

    /**
     * Sends notification email to support when a booking is created.
     *
     * @param booking booking details
     */
    @Async
    public void notifySupportOfBooking(Booking booking) {
        try {
            if (supportEmailTo == null || supportEmailTo.isBlank()) {
                System.out.println("Support email not configured. Booking: " + booking.getId());
                return;
            }
            SimpleMailMessage msg = getSimpleMailMessage(booking);
            mailSender.send(msg);
            System.out.println("Support notified for booking " + booking.getId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private SimpleMailMessage getSimpleMailMessage(Booking booking) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(supportEmailTo);
        msg.setFrom(fromEmail);
        msg.setSubject("New Booking Created");
        msg.setText(String.format("A new booking was created:\nHotelId: %s\nCustomer: %s\nEmail: %s\nPhone: %s\nFrom: %s\nTo: %s",
                booking.getHotelId(), booking.getCustomerName(), booking.getCustomerEmail(),
                booking.getCustomerPhone(), booking.getStartDate(), booking.getEndDate()));
        return msg;
    }
}