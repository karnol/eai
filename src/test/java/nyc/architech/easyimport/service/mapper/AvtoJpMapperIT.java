package nyc.architech.easyimport.service.mapper;


import nyc.architech.easyimport.EasyimportApp;
import nyc.architech.easyimport.domain.enums.Currency;
import nyc.architech.easyimport.domain.enums.Unit;
import nyc.architech.easyimport.service.dto.auction.AvtoJpLotDTO;
import nyc.architech.easyimport.service.dto.auction.LotDTO;
import nyc.architech.easyimport.service.util.AvtoJpUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Integration tests for {@link UserMapper}.
 */
@SpringBootTest(classes = EasyimportApp.class)
public class AvtoJpMapperIT {

    @Autowired
    private AvtoJpMapper avtoJpMapper;

    private AvtoJpLotDTO avtoJpLotDTO;

    @BeforeEach
    public void init() {
        avtoJpLotDTO = new AvtoJpLotDTO();
        avtoJpLotDTO.setAuctionDate(new Date());
        avtoJpLotDTO.setAuctionName("TEST_JAPAN_AUCTION");
        avtoJpLotDTO.setChassis("TEST_CHASSIS");
        avtoJpLotDTO.setColor("GRAY");
        avtoJpLotDTO.setDriveWheel("FULLTIME4WD");
        avtoJpLotDTO.setEngineDisplacementCC(1500);
        avtoJpLotDTO.setExternalId("EXTERNALID");
        avtoJpLotDTO.setImagesString("http://server.jp/img1.png#http://server.jp/img2.png");
        avtoJpLotDTO.setInfo("Inspection: 2020 July,Rate ext: B, Rate int: C");
        avtoJpLotDTO.setMake("MAKE");
        avtoJpLotDTO.setModel("MODEL");
        avtoJpLotDTO.setMileageKm(50000);
        avtoJpLotDTO.setModification("MDFCTN");
        avtoJpLotDTO.setNumber("3121");
        avtoJpLotDTO.setOptions("AAC");
        avtoJpLotDTO.setPower("56");
        avtoJpLotDTO.setRate("RA");
        avtoJpLotDTO.setSerialNumber("some_number");
        avtoJpLotDTO.setStatus("");
        avtoJpLotDTO.setTransmissionType((byte)0);
        avtoJpLotDTO.setYear(2000);

        avtoJpLotDTO.setPriceOriginal(50000);
        avtoJpLotDTO.setAveragePriceOriginal(200000);
        avtoJpLotDTO.setFinishPriceOriginal(500000);
        avtoJpLotDTO.setPriceOriginalHistoryAsString("123, 65, 234");
    }

    @Test
    public void correctMappingsTest() {
        LotDTO lotDTO = avtoJpMapper.avtoJpLotDTOToLotDTO(avtoJpLotDTO);
        assertTrue(lotDTO.getAuctionDate().equals(avtoJpLotDTO.getAuctionDate().toInstant()));
        assertTrue(lotDTO.getAuctionName().equals(avtoJpLotDTO.getAuctionName()));
        assertTrue(lotDTO.getChassis().equals(avtoJpLotDTO.getChassis()));
        assertTrue(lotDTO.getColor().equals("gray"));
        assertTrue(lotDTO.getExternalId().equals(avtoJpLotDTO.getExternalId()));
        assertTrue(lotDTO.getMake().equals(avtoJpLotDTO.getMake()));
        assertTrue(lotDTO.getModel().equals(avtoJpLotDTO.getModel()));
        assertTrue(lotDTO.getModification().equals(avtoJpLotDTO.getModification()));
        assertTrue(lotDTO.getNumber().equals(avtoJpLotDTO.getNumber()));
        assertTrue(lotDTO.getSerialNumber().equals(avtoJpLotDTO.getSerialNumber()));
        assertTrue(lotDTO.getStatus().equals(avtoJpLotDTO.getStatus()));
        assertTrue(lotDTO.getOptions().getHasClimateControl());
        assertTrue(lotDTO.getYear().equals(avtoJpLotDTO.getYear()));

        assertTrue(lotDTO.getImages().size() == 2);
        assertTrue(lotDTO.getImages().get(0).equals("http://server.jp/img1.png"));
        assertTrue(lotDTO.getPriceOriginal().equals(BigDecimal.valueOf(avtoJpLotDTO.getPriceOriginal())));

        assertTrue(lotDTO.getCurrency() == Currency.USD);
        assertTrue(lotDTO.getCurrencyOriginal() == Currency.JPY);

        assertTrue(lotDTO.getMileageUnit() == Unit.MI);

        assertTrue(lotDTO.getRate().equals("0"));
        assertTrue(lotDTO.getMileage().equals(AvtoJpUtil.adjustMileage(avtoJpLotDTO.getMileageKm())));

        assertTrue(lotDTO.getInspection().equals("2020 July"));
        assertTrue(lotDTO.getExteriorRate().equals("4"));
        assertTrue(lotDTO.getInteriorRate().equals("3"));

//        assertTrue(lotDTO.getPrice());

    }
    @Test
    public void cornerCasesMappingsTest() {
        LotDTO lotDTO = avtoJpMapper.avtoJpLotDTOToLotDTO(new AvtoJpLotDTO());
    }
}
