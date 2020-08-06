package com.poweredbypace.pace.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.poweredbypace.pace.domain.layouttemplate.LayoutTemplate;
import com.poweredbypace.pace.domain.layouttemplate.LayoutTemplates;
import com.poweredbypace.pace.domain.layouttemplate.TwoPageLayoutTemplate;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.repository.LayoutTemplateRepository;
import com.poweredbypace.pace.service.CrudService;
import com.poweredbypace.pace.service.UserService;

@Controller
@RequestMapping(value = "/api/layouttemplate")
public class LayoutTemplateController extends CrudController<LayoutTemplate> {
	
	public static final int MAX_RECENTLY_USED = 15;
	
	@Autowired
	private LayoutTemplateRepository repo;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@Override
	public List<LayoutTemplate> getAll() {
		LayoutTemplates layoutTemplates = new LayoutTemplates();
		layoutTemplates.addAll(userService.getCurrentUser().getSavedLayoutTemplates());
		layoutTemplates.addAll(repo.getAllPublic());
		return layoutTemplates;
	}
	
	@RequestMapping(value = "/public", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public LayoutTemplate[] getAllPublic() {
		Set<LayoutTemplate> templates = repo.getAllPublic();
		return templates.toArray(new LayoutTemplate[templates.size()]);
	}
	
	@RequestMapping(value = "/public/page", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public LayoutTemplate[] getAllPagePublic(@RequestParam int size) {
		Set<LayoutTemplate> templates = repo.getAllPagePublicTemplates(size);
		return templates.toArray(new LayoutTemplate[templates.size()]);
	}
	
	@RequestMapping(value = "/public/spread", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public LayoutTemplate[] getAllSpreadPublic(@RequestParam int lSize, @RequestParam int rSize) {
		if (lSize + rSize > 0) {
			Set<LayoutTemplate> twoPage = repo.getAllTwoPagePublicTemplates(lSize, rSize);
			Set<LayoutTemplate> single = repo.getAllSpreadSinglePublicTemplates(lSize + rSize);
			twoPage.addAll(single);				
			return twoPage.toArray(new LayoutTemplate[twoPage.size()]);
		} else return new LayoutTemplate[0];
	}
	
	@RequestMapping(value = "/saved", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public LayoutTemplate[] getSaved() {
		List<LayoutTemplate> templates = userService.getCurrentUser().getSavedLayoutTemplates();
		return templates.toArray(new LayoutTemplate[templates.size()]);
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public LayoutTemplate save(@RequestBody LayoutTemplate layoutTemplate) {
		layoutTemplate = saveAtFirst(layoutTemplate);
		addTemplateToSaved(layoutTemplate);

		return layoutTemplate;
	}
	
	@RequestMapping(value = "/order", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void saveOrder(@RequestBody List<LayoutTemplateOrder> order) {
		
		for(LayoutTemplateOrder o:order) {
			LayoutTemplate lt = repo.getOne(o.id);
			if (lt!=null) {
				lt.setOldId((long)o.order);
				repo.save(lt);
			}
		}
		
	}
	
	@RequestMapping(value = "/import", params={"email"}, method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public LayoutTemplate importTemplate(@RequestBody LayoutTemplate layoutTemplate, @RequestParam String email) {
		layoutTemplate = saveAtFirst(layoutTemplate);
		
		final User user = userService.getByEmail(email);
		
		List<LayoutTemplate> templates = user.getSavedLayoutTemplates();
		if(templates == null) {
			templates = new ArrayList<LayoutTemplate>();
		}
		templates.add(layoutTemplate);
		user.setSavedLayoutTemplates(templates);
		
		userService.save(user);
		
		return layoutTemplate;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@Override
	public void delete(@PathVariable long id) {
		final User user = userService.getCurrentUser();
		for(LayoutTemplate t:user.getSavedLayoutTemplates()) {
			if (t.getId()==id) {
				user.getSavedLayoutTemplates().remove(t);
				userService.save(user);
				break;
			}
		}
		super.delete(id);
	}
	
	private void addTemplateToSaved(LayoutTemplate template) {
		final User user = userService.getCurrentUser();
		
		List<LayoutTemplate> templates = user.getSavedLayoutTemplates();
		if(templates == null) {
			templates = new ArrayList<LayoutTemplate>();
		}
		boolean found = false;
		for(LayoutTemplate t:templates) {
			if (t.getId()==template.getId()) {
				found = true;
				break;
			}
		}
		if (!found)
			templates.add(template);
		user.setSavedLayoutTemplates(templates);
		
		userService.save(user);
	}
	
	private LayoutTemplate saveAtFirst(LayoutTemplate layoutTemplate) {
		if(layoutTemplate != null) {
			if(layoutTemplate instanceof TwoPageLayoutTemplate) {
				final TwoPageLayoutTemplate twoTemplate = (TwoPageLayoutTemplate)layoutTemplate;
				if(twoTemplate.getLeft() != null && twoTemplate.getLeft().getId() != null)
					twoTemplate.setLeft( repo.findOne(twoTemplate.getLeft().getId()) );
				
				if(twoTemplate.getRight() != null && twoTemplate.getRight().getId() != null)
					twoTemplate.setRight( repo.findOne(twoTemplate.getRight().getId()) );
			}
			
			return getRepository().save(layoutTemplate);
		}
		
		return layoutTemplate;
	}

	@Override
	protected CrudService<LayoutTemplate> getService() { return null; }

	@Override
	protected JpaRepository<LayoutTemplate, Long> getRepository() { return repo; }

	public static class LayoutTemplateOrder {
		public long id;
		public int order;
	}
}
