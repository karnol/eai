package nyc.architech.easyimport.service;

import nyc.architech.easyimport.service.dto.auction.DictionaryDTO;

import java.util.Collection;

public interface DictionaryService {

    Collection<DictionaryDTO> findAllDictionaries();
}
