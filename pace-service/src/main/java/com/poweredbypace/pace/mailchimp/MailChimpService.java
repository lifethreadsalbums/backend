package com.poweredbypace.pace.mailchimp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poweredbypace.pace.domain.Address;
import com.poweredbypace.pace.domain.user.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class MailChimpService {

	private final String resturl = "https://us2.api.mailchimp.com/3.0/lists/2aa4cde51b/members";
	private final String base64Creds = "bGlmZXRocmVhZHM6YzU5OWM5MTk2MmRlMGVkYTgyMDAxNGMyNmYyY2NlZjUtdXMy";

	public String signUp(User user) {
		//RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
		//List<ClientHttpRequestInterceptor> interceptors = new ArrayList();
		//interceptors.add(new LoggingRequestInterceptor());
		//restTemplate.setInterceptors(interceptors);
		System.setProperty("https.protocols", "TLSv1.1,TLSv1.2");
		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper objectMapper = new ObjectMapper();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(objectMapper);

		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(converter);
		restTemplate.setMessageConverters(messageConverters);
		MailChimpRequest mailChimpRequest = new MailChimpRequest();
		mailChimpRequest.setEmail_address(StringUtils.defaultIfBlank(user.getEmail(),""));
		mailChimpRequest.setStatus("subscribed");
		mailChimpRequest.getMerge_fields().setFNAME(StringUtils.defaultIfBlank(user.getFirstName(),""));
		mailChimpRequest.getMerge_fields().setLNAME(StringUtils.defaultIfBlank(user.getLastName(),""));
		mailChimpRequest.getMerge_fields().setPHONE(StringUtils.defaultIfBlank(user.getPhone(),""));
		mailChimpRequest.getMerge_fields().setMMERGE5(StringUtils.defaultIfBlank(user.getCompanyName(),""));
		mailChimpRequest.getMerge_fields().setMMERGE10(StringUtils.defaultIfBlank(user.getAddress(Address.AddressType.ShippingAddress).getState().getStateCode(),""));
		mailChimpRequest.getMerge_fields().setMMERGE11(StringUtils.defaultIfBlank(user.getWebsite(),""));
		mailChimpRequest.getMerge_fields().setMMERGE6(StringUtils.defaultIfBlank(user.getAddress(Address.AddressType.ShippingAddress).getAddressLine1(),""));
		mailChimpRequest.getMerge_fields().setMMERGE7(StringUtils.defaultIfBlank(user.getAddress(Address.AddressType.ShippingAddress).getCity(),""));
		mailChimpRequest.getMerge_fields().setMMERGE8(StringUtils.defaultIfBlank(user.getAddress(Address.AddressType.ShippingAddress).getZipCode(), ""));
		mailChimpRequest.getMerge_fields().setMMERGE9(StringUtils.defaultIfBlank(user.getAddress(Address.AddressType.ShippingAddress).getCountry().getIsoCountryCode(),""));
		ResponseEntity<Object> response = restTemplate.exchange(resturl, HttpMethod.POST, getHeader(mailChimpRequest), Object.class);
		return response.getStatusCode().toString();
	}

	private HttpEntity getHeader(MailChimpRequest mailChimpRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		HttpEntity<MailChimpRequest> httpEntity = new HttpEntity(mailChimpRequest, headers);
		return httpEntity;
	}
}
