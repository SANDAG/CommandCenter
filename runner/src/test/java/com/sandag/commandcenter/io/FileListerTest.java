package com.sandag.commandcenter.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.sandag.commandcenter.model.Job;

public class FileListerTest
{
    // tests refer to directory/file resources in src/test/resources (copied to target dir when running)

    private FileLister lister = new FileLister();
    private Job job;
    
    @Before
    public void setup()
    {
        URL rootUrl = ClassLoader.getSystemResource("fileListTestDir");
        lister.baseDir = rootUrl.getPath();
        lister.workingDir = "modelWorkingDir";
        lister.initialize();

        job = new Job();
    }

    @Test
    public void basicControllerFunctionalityWorks()
    {
        List<String> files = lister.listFiles(job);
        assertEquals(0, files.size());
    }

    @Test
    public void filteredFilesFound()
    {
        String scenarioNameDir = "scenarioNameDir";
        job.setScenario(scenarioNameDir);
        lister.logFileNames = Arrays.asList(new String[] {"file_top", "file_0_1_a" });
        List<String> files = lister.listFiles(job);
        assertEquals(2, files.size());
        String prefix = lister.workingDir + "/" + scenarioNameDir + "/";
        assertContained(new String[] {prefix + "file_top", prefix + "innerDirectory_0/innerDirectory_0_1/file_0_1_a" }, files);
    }

    @Test
    public void emptyDirDoesNotFail()
    {
        job.setScenario("emptyScenarioNameDir");
        List<String> files = lister.listFiles(job);
        assertEquals(0, files.size());
    }

    @Test
    public void nonexistantDirDoesNotFail()
    {
        job.setScenario("nonExistantScenarioNameDir");
        List<String> files = lister.listFiles(job);
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
                if (e.equals(f))
                {
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
