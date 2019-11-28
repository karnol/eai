package nyc.architech.easyimport.service.dto.auction;

import lombok.Data;

@Data
public class AuctionSearchDTO {

    private String auction;
    private String make;
    private String model;
    private String year;
    private String priceUSD;
    private String price;
    private String mileageMi;
    private String engineDisplacementCC;
    private String transmissionType;
    private String color;
}
