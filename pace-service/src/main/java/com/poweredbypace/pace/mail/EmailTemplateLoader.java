package com.poweredbypace.pace.mail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.poweredbypace.pace.domain.mail.EmailTemplate;
import com.poweredbypace.pace.repository.EmailTemplateRepository;

import freemarker.cache.TemplateLoader;

@Repository
public class EmailTemplateLoader implements TemplateLoader  {

	@Autowired
	private EmailTemplateRepository emailTemplateRepo;
	
	@Override
	public void closeTemplateSource(Object arg0) throws IOException { }

	@Override
	public Object findTemplateSource(String name) throws IOException {
		
		Locale locale = LocaleContextHolder.getLocale();
		name = name.replaceAll(".html","");
		name = name.replace("_" + locale.getCountry(), "");
		name = name.replace("_" + locale.getLanguage(), "");
		return emailTemplateRepo.findByName(name);
	}

	@Override
	public long getLastModified(Object arg0) {
		return new Date().getTime();
	}

	@Override
	public Reader getReader(Object templateSource, String encoding) throws IOException {
		return new StringReader(((EmailTemplate) templateSource).getTranslatedBody());
	}

}
