package com.poweredbypace.pace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.job.Job;
import com.poweredbypace.pace.job.JobCancelRequest;
import com.poweredbypace.pace.job.JobScheduler;
import com.poweredbypace.pace.job.task.GenerateAlbumPreviewTask;
import com.poweredbypace.pace.job.task.GenerateAlbumTask;
import com.poweredbypace.pace.job.task.GenerateBinderyFormTask;
import com.poweredbypace.pace.job.task.GenerateBinderyFormTaskImpl;
import com.poweredbypace.pace.job.task.GenerateCoverTask;
import com.poweredbypace.pace.job.task.GenerateProductionSheetsTask;
import com.poweredbypace.pace.print.OutputType;
import com.poweredbypace.pace.repository.ProductRepository;
import com.poweredbypace.pace.service.UserService;

@Controller
public class TaskController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private JobScheduler jobScheduler;
	
	
	@RequestMapping(value = "/api/lowrespdf/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void lowrespdf(@PathVariable long id) {
		Product product = productRepo.getOne(id);
		User user = userService.getCurrentUser();
		Job job = new Job();
		job.setType(GenerateAlbumPreviewTask.class);
		GenerateAlbumPreviewTask.Params params = new GenerateAlbumPreviewTask.Params();
		params.productId = id;
		params.outputType = OutputType.Pdf;
		job.setParams(params);
		job.setUser(user);
		job.setDescription("Generating a low res PDF for "+product.getName());
		jobScheduler.scheduleJob(job);
	}
	
	@RequestMapping(value = "/api/job", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public String scheduleJob(@RequestBody Job job) {
		return jobScheduler.scheduleJob(job);
	}
	
	@RequestMapping(value = "/api/print/{outputType}/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void print(@PathVariable("outputType") OutputType outputType, @PathVariable("id") long id) {
		generateAlbum(id, outputType);
	}
	
	@RequestMapping(value = "/api/pdf/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void hirespdf(@PathVariable long id) {
		generateAlbum(id, OutputType.Pdf);
	}
	
	@RequestMapping(value = "/api/jpeg/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void jpegGen(@PathVariable long id) {
		generateAlbum(id, OutputType.Jpeg);
	}
	
	@RequestMapping(value = "/api/tiff/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void tiffGen(@PathVariable long id) {
		generateAlbum(id, OutputType.Tiff);
	}
	
	@RequestMapping(value = "/api/pdfcover/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void hiresPdfCover(@PathVariable long id) {
		generateCover(id, OutputType.Pdf);
	}
	
	@RequestMapping(value = "/api/jpegcover/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void jpegCover(@PathVariable long id) {
		generateCover(id, OutputType.Jpeg);
	}
	
	@RequestMapping(value = "/api/tiffcover/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void tiffCover(@PathVariable long id) {
		generateCover(id, OutputType.Tiff);
	}
	
	@RequestMapping(value = "/api/binderyform/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void binderyForm(@PathVariable long id) {
		
		User user = userService.getCurrentUser();
		Job job = new Job();
		job.setType(GenerateBinderyFormTask.class);
		GenerateBinderyFormTaskImpl.Params params = new GenerateBinderyFormTaskImpl.Params();
		params.productId = id;
		job.setParams(params);
		job.setUser(user);
		job.setDescription("Generating Bindery Form");
		jobScheduler.scheduleJob(job);
		
	}
	
	@RequestMapping(value = "/api/productionsheet/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void productionSheets(@PathVariable long id) {
		
		User user = userService.getCurrentUser();
		Job job = new Job();
		job.setType(GenerateProductionSheetsTask.class);
		GenerateProductionSheetsTask.Params params = new GenerateProductionSheetsTask.Params();
		params.batchIds = new long[] { id };
		job.setParams(params);
		job.setUser(user);
		job.setDescription("Generating Production Sheets");
		jobScheduler.scheduleJob(job);
		
	}
	
	
	
//	@RequestMapping(value = "/api/scangenerate", method = RequestMethod.GET, produces="application/json")
//	@ResponseBody
//	@ResponseStatus(value = HttpStatus.OK)
//	public void scanGen() {
//		printService.scanAndGenerate();
//	}
	
	
	@RequestMapping(value = "/api/job/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void cancelJob(@PathVariable String id) {
		JobCancelRequest req = new JobCancelRequest();
		req.setUser(userService.getCurrentUser());
		req.setId(id);
		jobScheduler.cancelJob(req);
	}

	private void generateAlbum(long id, OutputType outputType) {
		Product product = productRepo.getOne(id);
		User user = userService.getCurrentUser();
		Job job = new Job();
		job.setType(GenerateAlbumTask.class);
		GenerateAlbumTask.Params params = new GenerateAlbumTask.Params();
		params.productId = id;
		params.outputType = outputType;
		job.setParams(params);
		job.setUser(user);
		job.setDescription("Generating high res " + outputType.name().toUpperCase() + " for "+product.getName());
		jobScheduler.scheduleJob(job);
	}

	private void generateCover(long id, OutputType outputType) {
		Product product = productRepo.getOne(id);
		User user = userService.getCurrentUser();
		Job job = new Job();
		job.setType(GenerateCoverTask.class);
		GenerateCoverTask.Params params = new GenerateCoverTask.Params();
		params.productId = id;
		params.outputType = outputType;
		job.setParams(params);
		job.setUser(user);
		job.setDescription("Generating high res " + outputType.name().toUpperCase() + " cover for "+product.getName());
		jobScheduler.scheduleJob(job);
	}
	
}
