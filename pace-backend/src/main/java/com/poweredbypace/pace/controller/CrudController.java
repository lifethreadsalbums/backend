package com.poweredbypace.pace.controller;

import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.service.CrudService;

public abstract class CrudController<Entity extends BaseEntity> {

	protected abstract CrudService<Entity> getService();
	
	protected abstract JpaRepository<Entity, Long> getRepository();
	
	public CrudController() { }
	
	@RequestMapping(value = "", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public Entity save(@RequestBody Entity data) {
		if (getService()!=null)
			return getService().save(data);
		
		return getRepository().save(data);
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Entity> getAll() {
		if (getService()!=null)
			return getService().findAll();
		
		return getRepository().findAll();
	}
	
	@RequestMapping(value = "", params={"pageIndex","pageSize"}, method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Entity> getAll(@RequestParam Integer pageIndex,
			@RequestParam Integer pageSize) {
		return getRepository().findAll(new PageRequest(pageIndex, pageSize)).getContent();
	}
	
	@RequestMapping(value = "/summary", method = RequestMethod.GET, produces = "application/json")
	//@ResponseView(SummaryView.class)
	@ResponseBody
	public List<Entity> getAllSummary() {
		return getAll();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Entity get(@PathVariable long id) {
		if (getService()!=null)
			return getService().findOne(id);
		
		return getRepository().findOne(id);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "", params="id", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Entity> getMultiple(@RequestParam List<Long> id) {
		if (getService()!=null)
			return getService().findAll(id);
		
		return IteratorUtils.toList(getRepository().findAll(id).iterator());
	}
	
	@RequestMapping(value = "/{id}/summary", method = RequestMethod.GET, produces = "application/json")
	//@ResponseView(SummaryView.class)
	@ResponseBody
	public Entity getSummary(@PathVariable long id) {
		return get(id);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void delete(@PathVariable long id) {
		if (getService()!=null) {
			getService().delete(id);
			return;
		}
		getRepository().delete(id);
	}
	
	public static final class IDSParam {
		private List<Long> ids;

		public List<Long> getIds() {
			return ids;
		}

		public void setIds(List<Long> ids) {
			this.ids = ids;
		}
	}
	
}
