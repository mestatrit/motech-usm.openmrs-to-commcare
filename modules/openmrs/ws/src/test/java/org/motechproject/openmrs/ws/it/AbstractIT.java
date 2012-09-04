package org.motechproject.openmrs.ws.it;

import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testApplicationContext.xml")
public class AbstractIT {

    @Autowired
    protected OpenMrsDeletableContext context;

    @After
    public void clearOpenMrsData() {
        context.deleteAllDataInContext();
    }

}
