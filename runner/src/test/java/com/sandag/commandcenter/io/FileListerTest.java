package com.sandag.commandcenter.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sandag.commandcenter.model.Job;
import com.sandag.commandcenter.testutils.TestUtils;

public class FileListerTest
{
    // tests refer to directory/file resources in src/test/resources (copied to target dir when running)

    private FileLister lister = new FileLister();
    private Job job;
    private String baseDir;

    @BeforeClass
    public static void removeHackFiles()
    {
        TestUtils.removeHackFile("fileListTestDir/emptyScenarioNameDir");
    }

    @Before
    public void setup() throws URISyntaxException
    {
        baseDir = TestUtils.getFile("fileListTestDir").getAbsolutePath();
        job = new Job();
    }

    @Test
    public void basicControllerFunctionalityWorks()
    {
        job.setScenarioLocation("c:/Trenzalore/crack/Gallifrey");
        List<String> files = lister.listFiles(job);
        assertEquals(0, files.size());
    }

    @Test
    public void filteredFilesFound()
    {
        String scenarioNameDir = baseDir + "/modelWorkingDir/scenarioNameDir";
        job.setScenarioLocation(scenarioNameDir);
        lister.logFileNames = Arrays.asList(new String[] {"file_top", "file_0_1_a" });
        List<String> files = lister.listFiles(job);
        assertEquals(2, files.size());
        assertContained(new String[] {scenarioNameDir + "/innerDirectory_0/innerDirectory_0_1/file_0_1_a", scenarioNameDir + "/file_top", }, files);
    }

    @Test
    public void emptyDirDoesNotFail()
    {
        File emptyDir = new File(baseDir + "/emptyScenarioNameDir");
        assertTrue(emptyDir.exists());
        assertEquals(0, emptyDir.list().length);
        job.setScenarioLocation(emptyDir.getAbsolutePath());
        List<String> files = lister.listFiles(job);
        assertEquals(0, files.size());
    }

    @Test
    public void nonexistentDirDoesNotFail()
    {
        File missingDir = new File(baseDir + "/nonExistentScenarioNameDir");
        assertFalse(missingDir.exists());
        job.setScenarioLocation(missingDir.getAbsolutePath());
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
                if (e.replace(File.separatorChar, '/').equals(f.replace(File.separatorChar, '/')))
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
