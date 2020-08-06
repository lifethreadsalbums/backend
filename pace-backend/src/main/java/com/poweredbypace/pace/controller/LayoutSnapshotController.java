package com.poweredbypace.pace.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.poweredbypace.pace.domain.layout.LayoutSnapshot;
import com.poweredbypace.pace.dto.FileInfo;
import com.poweredbypace.pace.service.StorageService;

@Controller
@RequestMapping(value = "/api/layoutSnapshot")
public class LayoutSnapshotController {

	//private Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private StorageService storageService;
	
	public LayoutSnapshotController() { }

	@RequestMapping(value = "", params="layoutId", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<LayoutSnapshot> getByLayoutId(@RequestParam long layoutId) 
	
	{
		String path = String.format("snapshots/%d/", layoutId);
		
		List<LayoutSnapshot> result = new ArrayList<LayoutSnapshot>();
		List<FileInfo> files = storageService.listFiles(path);
		long i = 0;
		for(FileInfo file:files) {
			String dateString = file.getName().replaceAll(path, "").replaceAll(".json", "");
			Date date = ISO8601Utils.parse(dateString);
			LayoutSnapshot ls = new LayoutSnapshot();
			ls.setDate(date);
			ls.setLayoutId(layoutId);
			ls.setId(i++);
			result.add(ls);
		}
		
		return result;
	}
	
	@RequestMapping(value = "", params={"layoutId", "id"}, method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public LayoutSnapshot getByLayoutIdAndId(@RequestParam int id, @RequestParam long layoutId) throws IOException {
		String path = String.format("snapshots/%d/", layoutId);
		
		List<FileInfo> files = storageService.listFiles(path);
		File file = storageService.getFile(files.get(id).getName());
		
		LayoutSnapshot ls = new LayoutSnapshot();
		ls.setLayoutId(layoutId);
		ls.setId((long)id);
		ls.setLayoutJson(FileUtils.readFileToString(file));
		
		file.delete();
		return ls;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public LayoutSnapshot save(@RequestBody LayoutSnapshot data) throws IOException {
		Date date = new Date();
		String path = String.format("snapshots/%d/%s.json", data.getLayoutId(), ISO8601Utils.format(date));
		File file = File.createTempFile("layout-snapshot-", ".json");
		FileUtils.writeStringToFile(file, data.getLayoutJson());
		storageService.putFile(file, path);
		
		file.delete();
		
		LayoutSnapshot result = new LayoutSnapshot();
		result.setDate(date);
		result.setLayoutId(data.getLayoutId());
		return result;
	}

}
