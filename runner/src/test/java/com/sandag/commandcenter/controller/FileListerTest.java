package com.sandag.commandcenter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.sandag.commandcenter.model.Job;

public class FileListerTest {
	// tests refer to directory/file resources in src/test/resources (copied to target dir when running) 

	FileLister lister = new FileLister();
	Job job;
	Model model; 	
	
	@Before
	public void setup()
	{		
		URL rootUrl = ClassLoader.getSystemResource("fileListTestDir");
		lister.rootPath = rootUrl.getPath();
		lister.initialize();

		job = new Job();		
		model = new ExtendedModelMap();

	}	
	
	@Test
	public void basicControllerFunctionalityWorks()
	{
		assertEquals("files", lister.listFiles(job, model));
		assertTrue(model.containsAttribute("files"));
	}
	
	@Test
	public void filteredFilesFound() 
	{
		String scenarioNameDir = "scenarioNameDir";
		job.setScenario(scenarioNameDir);	
		lister.fileNames = Arrays.asList(new String[]{"file_0_1_a", "file_top"});
		lister.listFiles(job, model);
		@SuppressWarnings("unchecked")
		List<String> files = (List<String>)model.asMap().get("files");
		assertEquals(2, files.size());
		assertContained(new String[]{
				scenarioNameDir + "/file_top", 
				scenarioNameDir + "/innerDirectory_0/innerDirectory_0_1/file_0_1_a"
			}, files);
	}
	
	@Test
	public void emptyDirDoesNotFail()
	{
		job.setScenario("emptyScenarioNameDir");
		lister.listFiles(job, model);
		@SuppressWarnings("unchecked")
		List<String> files = (List<String>)model.asMap().get("files");		
		assertEquals(0, files.size());
	}
	
	@Test
	public void nonexistantDirDoesNotFail()
	{
		job.setScenario("nonExistantScenarioNameDir");
		lister.listFiles(job, model);
		@SuppressWarnings("unchecked")
		List<String> files = (List<String>)model.asMap().get("files");		
		assertEquals(0, files.size());
	}
	
	// support
	private void assertContained(String[] expectedPaths, List<String> foundPaths)
	{
		Set<String> notMatched = new HashSet<String>();
		notMatched.addAll(foundPaths);
		
		for (String e : expectedPaths)
		{
			for (String f : foundPaths)
			{
				if (e.equals(f)) {
					notMatched.remove(f);
				}			
			}
		}
		if (notMatched.size() > 0)
		{
			fail(String.format("'%s' not in '%s'", notMatched, Arrays.toString(expectedPaths)));
		}
						
	}
	
	
}
