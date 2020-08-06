package com.poweredbypace.pace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.layout.ImageElement;
import com.poweredbypace.pace.job.Job;
import com.poweredbypace.pace.job.JobScheduler;
import com.poweredbypace.pace.job.task.RegenerateImagesTask;
import com.poweredbypace.pace.job.task.RegenerateImagesTaskImpl;
import com.poweredbypace.pace.repository.ImageElementRepository;
import com.poweredbypace.pace.repository.ImageFileRepository;
import com.poweredbypace.pace.service.CrudService;
import com.poweredbypace.pace.service.UserService;

@Controller
@RequestMapping(value = "/api/imagefile")
public class ImageFileController extends CrudController<ImageFile> {

	@Override
	protected CrudService<ImageFile> getService() { return null; }
	
	@Override
	protected JpaRepository<ImageFile, Long> getRepository() { return repo; }

	@Autowired
	private ImageFileRepository repo;
	
	@Autowired
	private ImageElementRepository imageElementRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JobScheduler jobScheduler;

	@RequestMapping(value="/regenerate", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void regenerate(@RequestBody RegenerateImagesTaskImpl.Params params) {
		Job job = new Job();
		job.setType(RegenerateImagesTask.class);
		job.setParams(params);
		job.setUser(userService.getCurrentUser());
		job.setDescription("Regenerating CMYK images");
		jobScheduler.scheduleJob(job);
	}

	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void delete(@PathVariable long id) {
		
		ImageFile file = repo.findOne(id);
		
		if (file!=null) {
			List<ImageElement> imageElements = imageElementRepo.findByImageFile(file);
			imageElementRepo.delete(imageElements);
			repo.delete(file);
		}
	
	}
	
	
}
