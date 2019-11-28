package nyc.architech.easyimport.service.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nyc.architech.easyimport.service.avtojp.impl.AutoJpSyncService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:config/custom/task.properties")
public class SyncAvtoJpTask {

    private final AutoJpSyncService autoJpSyncService;

    @Scheduled(fixedDelayString = "${task.avto.jp.filters.update.fixedDelayString}", initialDelayString = "${task.avto.jp.filters.update.fixedDelayString}")
    public void syncAllFilters() {
        autoJpSyncService.syncFilters();
    }

    @Scheduled(fixedDelayString = "${task.avto.jp.watch.list.update.fixedDelayString}", initialDelayString = "${task.avto.jp.watch.list.update.fixedDelayString}")
    public void syncWatchList() {
        autoJpSyncService.syncWatchList();
    }
}
