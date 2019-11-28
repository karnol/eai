package nyc.architech.easyimport.service.dto.auction;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AvtoJpLotDTO {

    @JsonAlias("ID")
    private String externalId;
    @JsonAlias("LOT")
    private String number;
    @JsonAlias("AUCTION_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auctionDate;

    @JsonAlias("AUCTION")
    private String auctionName;
    @JsonAlias("MARKA_NAME")
    private String make;
    @JsonAlias("MODEL_NAME")
    private String model;
    @JsonAlias("GRADE")
    private String modification;
    @JsonAlias("YEAR")
    private Integer year;

    @JsonAlias("START")
    private Integer priceOriginal;
    @JsonAlias("AVG_PRICE")
    private Integer averagePriceOriginal;
    @JsonAlias("FINISH")
    private Integer finishPriceOriginal;
    @JsonAlias("AVG_STRING")
    private String priceOriginalHistoryAsString;
    @JsonAlias("ENG_V")
    private Integer engineDisplacementCC;
    //IMPORTANT: Avto.Jp says it's catalog value! It has not come from auctions!!
    @JsonAlias("PW")
    private String power;
    @JsonAlias("MILEAGE")
    private Integer mileageKm;
    @JsonAlias("RATE")
    private String rate;
    @JsonAlias("COLOR")
    private String color;
    @JsonAlias("KPP_TYPE")
    private Byte transmissionType;
    @JsonAlias("PRIV")
    private String driveWheel;
    @JsonAlias("KUZOV")
    private String chassis;
    @JsonAlias("EQUIP")
    private String options;
    @JsonAlias("STATUS")
    private String status;

    @JsonAlias("IMAGES")
    private String imagesString;

    @JsonAlias("SERIAL")
    private String serialNumber;
    @JsonAlias("INFO")
    private String info;
}
