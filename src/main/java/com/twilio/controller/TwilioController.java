package com.twilio.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@RestController
@RequestMapping("/sms")
public class TwilioController {
	@Value("${twilio.phoneNumber}")
	private String twilioPhoneNumber;

	@Autowired
	public TwilioController(@Value("${twilio.accountSid}") String twilio_account_sid,
			@Value("${twilio.authToken}") String twilio_auth_token) {
		Twilio.init(twilio_account_sid, twilio_auth_token);
	}

	@PostMapping("/send-message")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public String sendMessage(@RequestBody MessageDetails messageDetails) {
		Message.creator(new PhoneNumber(messageDetails.number), new PhoneNumber(twilioPhoneNumber),
				messageDetails.message).create();

		return "message is inbound!";
	}

	@PostMapping("/send-voicemessage")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public String sendVoiceCall(@RequestBody MessageDetails messageDetails) throws URISyntaxException {
		Message.creator(new PhoneNumber(messageDetails.number), new PhoneNumber(twilioPhoneNumber),
				messageDetails.message).create();
		Call.creator(new PhoneNumber(messageDetails.number), new PhoneNumber(twilioPhoneNumber),
				new URI("http://demo.twilio.com/docs/voice.xml")).create();

		return "call incoming";
	}

	public static class MessageDetails {
		public String number;
		public String message;
	}
}
