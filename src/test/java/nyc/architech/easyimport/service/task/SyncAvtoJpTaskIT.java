package nyc.architech.easyimport.service.task;

import nyc.architech.easyimport.EasyimportApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = EasyimportApp.class)
public class SyncAvtoJpTaskIT {

    @Autowired
    private SyncAvtoJpTask syncAvtoJpTask;

    @Test
    @Transactional
    public void testSyncAvtoJpFilters() {
        syncAvtoJpTask
            .syncAllFilters();
    }
    
}
