package nyc.architech.easyimport.service;

import lombok.RequiredArgsConstructor;
import nyc.architech.easyimport.domain.Settings;
import nyc.architech.easyimport.repository.SettingsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class SettingsService {

    private final SettingsRepository repository;

    public Settings getSettingsByName(String name) {
        List<Settings> settings = repository.findByNameEquals(name);
        Assert.isTrue(settings.size() <= 1, "There should be only one setting for every name!");
        return settings.size() == 1 ? settings.get(0) : null;
    }

    public Settings createOrUpdate(String name, String value) {
        Settings setting = getSettingsByName(name);
        if (Objects.nonNull(setting)) {
            setting.setValue(value);
            setting.setModified(Instant.now());
        } else {
            setting = new Settings();
            setting.setName(name);
            setting.setValue(value);
//            setting.setPlatformId(Platform.AVTO_JP);
        }
        return createOrUpdate(setting);
    }

    public Settings createOrUpdate(Settings settings) {
        return repository.saveAndFlush(settings);
    }
}
